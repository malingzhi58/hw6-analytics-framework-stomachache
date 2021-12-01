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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

/**
 * Import data from CSV(comma-separated values) file
 *
 * It should have the following columns:
 * symbol,date,open,high,low,close
 *
 * Symbol, date and close are required columns
 *
 * Date format: yyyy-MM-dd
 *
 * Example: src/main/resources/daily_IBM.csv (note: volume is ignored)
 */
public class CSVFileDataPlugin implements DataPlugin {

    private static final String NAME = "CSV local file";
    private static final String[] TARGET_COL = {"symbol", "date",
        "open", "high", "low", "close"};

    private Framework framework;
    private CSVReader csvReader;


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
        File file = new File(arg);
        InputStream targetStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(new BOMInputStream(targetStream), "UTF-8");
        csvReader = new CSVReaderBuilder(reader)
            // both two separators, two quotes will be treated as null
            .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
            .build();
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
