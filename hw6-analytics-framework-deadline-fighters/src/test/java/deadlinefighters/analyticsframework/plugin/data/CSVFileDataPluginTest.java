package deadlinefighters.analyticsframework.plugin.data;

import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVFileDataPluginTest {

    private CSVFileDataPlugin csvFileDataPlugin;
    private static final String BASE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() {
        csvFileDataPlugin = new CSVFileDataPlugin();
    }

    @Test
    void testOpenConnectionNotFound() {
        String arg = BASE_PATH + "/fileNotFound.csv";

        assertThrows(IOException.class, () ->
            csvFileDataPlugin.openConnection(arg));
    }

    @Test
    void testOpenConnectionExist() {
        String arg = BASE_PATH + "/all.csv";

        assertDoesNotThrow(() ->
            csvFileDataPlugin.openConnection(arg));
    }

    @Test
    void testGetColIndex() {
        String[] header = {"date", "open", "close", "high", "low", "symbol"};
        int[] index = csvFileDataPlugin.getColIndex(header);

        assertEquals(6, index.length);
        assertEquals(5, index[0]);
        assertEquals(0, index[1]);
        assertEquals(1, index[2]);
        assertEquals(3, index[3]);
        assertEquals(4, index[4]);
        assertEquals(2, index[5]);
    }

    @Test
    void testGetStockQuotesAll() {
        String arg = BASE_PATH + "/all.csv";
        assertDoesNotThrow(() ->
            csvFileDataPlugin.openConnection(arg));

        List<String> symbols = Arrays.asList("AAPL", "GOOG");
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = csvFileDataPlugin.getStockQuotes(symbols);
        StockQuotesResult stockQuotesResult =
            stockQuotesResultCompletableFuture.join();
        List<StockQuote> stockQuotes = stockQuotesResult.getStockQuotes();

        assertFalse(stockQuotesResult.hasError());
        assertNull(stockQuotesResult.getErrorMessage());
        assertEquals(3, stockQuotes.size());

        assertEquals("AAPL", stockQuotes.get(0).getSymbol());
        assertEquals(LocalDate.parse("2021-11-20"), stockQuotes.get(0).getDate());
        assertEquals(2.5, stockQuotes.get(0).getClose());
        assertNull(stockQuotes.get(0).getLow());
        assertNull(stockQuotes.get(0).getHigh());
        assertNull(stockQuotes.get(0).getOpen());
    }

    @Test
    void testGetStockQuotesAllError() {
        String arg = BASE_PATH + "/all.csv";
        assertDoesNotThrow(() ->
            csvFileDataPlugin.openConnection(arg));

        List<String> symbols = Arrays.asList("AAPL", "GOOG", "CLDR");
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = csvFileDataPlugin.getStockQuotes(symbols);
        StockQuotesResult stockQuotesResult =
            stockQuotesResultCompletableFuture.join();
        List<StockQuote> stockQuotes = stockQuotesResult.getStockQuotes();

        String expectedErrorMessage = "Missing symbols CLDR";
        assertTrue(stockQuotesResult.hasError());
        assertNotNull(stockQuotesResult.getErrorMessage());
        assertEquals(expectedErrorMessage, stockQuotesResult.getErrorMessage());
    }

    @Test
    void testGetStockQuoteNoSymbol() {
        String arg = BASE_PATH + "/noSymbol.csv";
        assertDoesNotThrow(() ->
            csvFileDataPlugin.openConnection(arg));

        List<String> symbols = Arrays.asList("AAPL", "GOOG");
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = csvFileDataPlugin.getStockQuotes(symbols);

        Exception exception = assertThrows(CompletionException.class,
            () -> stockQuotesResultCompletableFuture.join());

        String expectedMessage = "java.io.IOException: Column symbol is missing from CSV file.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testGetStockQuoteDateFormat() {
        String arg = BASE_PATH + "/wrongDate.csv";
        assertDoesNotThrow(() ->
            csvFileDataPlugin.openConnection(arg));

        List<String> symbols = Arrays.asList("AAPL", "GOOG");
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = csvFileDataPlugin.getStockQuotes(symbols);

        Exception exception = assertThrows(CompletionException.class,
            () -> stockQuotesResultCompletableFuture.join());
        assertEquals(DateTimeParseException.class, exception.getCause().getClass());
    }

    @Test
    void testGetStockQuoteDouble() {
        String arg = BASE_PATH + "/wrongDouble.csv";
        assertDoesNotThrow(() ->
            csvFileDataPlugin.openConnection(arg));

        List<String> symbols = Arrays.asList("AAPL", "GOOG");
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = csvFileDataPlugin.getStockQuotes(symbols);

        Exception exception = assertThrows(CompletionException.class,
            () -> stockQuotesResultCompletableFuture.join());
        assertEquals(NumberFormatException.class, exception.getCause().getClass());
    }



}
