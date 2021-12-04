package deadlinefighters.analyticsframework.plugin.data;

import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * created by @author Lingzhi Ma.
 * email: lingzhim@andrew.cmu.edu
 * andrewId: lingzhim
 */
public class MarketPyCSVDataPluginTest {
    private MarketPyCSVPlugin pyCSVDataPlugin;
    private static final String BASE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() {
        pyCSVDataPlugin = new MarketPyCSVPlugin();
    }

    @Test
    void testOpenConnectionNotFound() {
        String arg = BASE_PATH + "/fileNotFound.csv";

        assertThrows(IOException.class, () ->
            pyCSVDataPlugin.openConnection(arg));
    }

//    @Test
//    void testOpenConnectionExist() {
//        String arg = BASE_PATH + "/all.csv";
//
//        assertDoesNotThrow(() ->
//            csvFileDataPlugin.openConnection(arg));
//    }

    @Test
    void testGetColIndex() {
        String[] header = {"date", "open", "close", "high", "low", "symbol"};
        int[] index = pyCSVDataPlugin.getColIndex(header);

        assertEquals(6, index.length);
        assertEquals(5, index[0]);
        assertEquals(0, index[1]);
        assertEquals(1, index[2]);
        assertEquals(3, index[3]);
        assertEquals(4, index[4]);
        assertEquals(2, index[5]);
    }
    @Test
    void testGetStockQuotesPy() {
//        String arg =   "aapl";
        String arg =   "your api key";
        assertDoesNotThrow(() ->
            pyCSVDataPlugin.openConnection(arg));

//        List<String> symbols = Arrays.asList("aapl");
        List<String> symbols = Arrays.asList("aapl");
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = pyCSVDataPlugin.getStockQuotes(symbols);
        StockQuotesResult stockQuotesResult =
            stockQuotesResultCompletableFuture.join();
        List<StockQuote> stockQuotes = stockQuotesResult.getStockQuotes();

        assertFalse(stockQuotesResult.hasError());
        assertNull(stockQuotesResult.getErrorMessage());
//        assertEquals(3, stockQuotes.size());
//
//        assertEquals("AAPL", stockQuotes.get(0).getSymbol());
//        assertEquals(LocalDate.parse("2021-11-20"), stockQuotes.get(0).getDate());
//        assertEquals(2.5, stockQuotes.get(0).getClose());
        assertNotNull(stockQuotes.get(0).getLow());
    }
}
