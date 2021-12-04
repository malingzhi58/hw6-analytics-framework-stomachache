package deadlinefighters.analyticsframework.plugin.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import deadlinefighters.analyticsframework.framework.core.DataPlugin;
import deadlinefighters.analyticsframework.framework.core.Framework;
import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

/**
 * created by @author Lingzhi Ma.
 * email: lingzhim@andrew.cmu.edu
 * andrewId: lingzhim
 */
public class YahooPyCSVDataPlugin implements DataPlugin {
    private static final String NAME = "yahoo python CSV local file";
    private static final String[] TARGET_COL = {"symbol", "date",
        "open", "high", "low", "close"};

    private Framework framework;
    private CSVReader csvReader;
    private String path = "src/main/resources/";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onRegister(Framework f) {
        framework = f;
    }

    @Override
    public void openConnection(String arg) throws IOException {
        String filepath = path + "tmp" + arg + ".csv";
        createCSV(arg);
        // Create an object of filereader
        // class with CSV file as a parameter.
        FileReader filereader = new FileReader(filepath);

        // create csvReader object passing
        // file reader as a parameter
        csvReader = new CSVReader(filereader);
    }

    public void createCSV(String arg) {
        arg = arg.toLowerCase();
        String[] cmd = {
            "python",
            "src/main/java/deadlinefighters/analyticsframework/plugin/data/yahooapi.py",
            arg,
        };
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String strCurrentLine;
            while ((strCurrentLine = in.readLine()) != null) {
                System.out.println(strCurrentLine);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected int[] getColIndex(String[] headers) {
        int[] colIndex = new int[TARGET_COL.length];
        List<String> headerList = Arrays.asList(headers);
        for (int i = 0; i < TARGET_COL.length; i++) {
            colIndex[i] = headerList.indexOf(TARGET_COL[i]);
        }
        return colIndex;
    }

    @Override
    public CompletableFuture<StockQuotesResult> getStockQuotes(
        List<String> symbols) {
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = CompletableFuture.supplyAsync(new Supplier<StockQuotesResult>() {
            @Override
            public StockQuotesResult get() {
                List<StockQuote> stockQuotes = new ArrayList<>();
                boolean hasError = false;
                String errorMessage = null;
                Set<String> symbolsSet = new HashSet<>(symbols);
                Set<String> symbolsVisited = new HashSet<>();

                String[] nextRecord;
                int[] colIndex;
                try {
                    // header
                    nextRecord = csvReader.readNext();
                    for (int i = 0; i < nextRecord.length; i++) {
                        nextRecord[i] = nextRecord[i].toLowerCase();
                    }
                    colIndex = getColIndex(nextRecord);

                    // check symbol & date
                    for (int i = 0; i < 2; i++) {
                        if (colIndex[i] == -1) {
                            throw new CompletionException(
                                new IOException("Column " + TARGET_COL[i] +
                                    " is missing from CSV file."));
                        }
                    }

                    while ((nextRecord = csvReader.readNext()) != null) {
                        String symbol = nextRecord[colIndex[0]];
                        if (!symbolsSet.contains(symbol)) continue;
                        symbolsVisited.add(symbol);

                        LocalDate date = LocalDate.parse(nextRecord[colIndex[1]]);

                        // open, high, low
                        Double[] nullables = new Double[3];
                        for (int i = 2; i < colIndex.length - 1; i++) {
                            String nullableStr = colIndex[i] != -1 ?
                                nextRecord[colIndex[i]] : null;
                            nullables[i - 2] = StringUtils.isNotBlank(nullableStr) ?
                                Double.parseDouble(nullableStr) : null;
                        }

                        double close = Double.parseDouble(nextRecord[colIndex[5]]);

                        stockQuotes.add(new StockQuote(symbol, date, nullables[0],
                            close, nullables[1], nullables[2]));
                    }

                } catch (IOException e) {
                    throw new CompletionException(e);
                }

                // check symbols that haven't seen
                symbolsSet.removeAll(symbolsVisited);
                if (symbolsSet.size() != 0) {
                    hasError = true;
                    StringBuilder missingSymbol = new StringBuilder();
                    for (String noSymbol : symbolsSet) {
                        missingSymbol.append(" ").append(noSymbol);
                    }
                    errorMessage = "Missing symbols" + missingSymbol;
                }

                return new StockQuotesResult(stockQuotes, errorMessage);
            }
        });

        return stockQuotesResultCompletableFuture;
    }

    @Override
    public void closeConnection() throws IOException {
        csvReader.close();
    }
}
