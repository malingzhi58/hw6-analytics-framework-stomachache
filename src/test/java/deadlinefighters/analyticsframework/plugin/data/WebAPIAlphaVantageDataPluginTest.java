package deadlinefighters.analyticsframework.plugin.data;

import com.crazzyghost.alphavantage.AlphaVantageException;
import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class WebAPIAlphaVantageDataPluginTest {

    private WebAPIAlphaVantageDataPlugin
        webAPIDataPlugin = new WebAPIAlphaVantageDataPlugin();

    @AfterEach
    void close() {
        assertDoesNotThrow(() ->webAPIDataPlugin.closeConnection());
    }

    @Test
    void testOpenConnectionNull() {
        IOException exception = assertThrows(IOException.class,
            () ->webAPIDataPlugin.openConnection(null));
        assertEquals(WebAPIAlphaVantageDataPlugin.NULL_API_KEY_MESSAGE,
            exception.getMessage());
    }

    @Test
    void testOpenConnectionWhiteSpace() {
        IOException exception = assertThrows(IOException.class,
            () ->webAPIDataPlugin.openConnection("  "));
        assertEquals(WebAPIAlphaVantageDataPlugin.NULL_API_KEY_MESSAGE,
            exception.getMessage());
    }

    private static OkHttpClient getMockOkHttpClient(String... paths)
        throws IOException {
        String[] strs = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            strs[i] = Files.readString(Path.of(paths[i]));
        }

        OkHttpClient okHttpClient = mock(OkHttpClient.class);
        Call call = mock(Call.class);
        Request request = new Request.Builder().url(new URL("https://url")).build();
        Response[] responses = new Response[paths.length];

        for (int i = 0; i < paths.length; i++) {
            responses[i] = new Response.Builder().request(request).protocol(
                    Protocol.HTTP_2)
                .body(ResponseBody.create(
                    MediaType.get("application/json; charset=utf-8")
                    , strs[i]))
                .code(200)
                .message("")
                .build();
        }

        when(okHttpClient.newCall(any())).thenReturn(call);
        if (responses.length > 0) {
            when(call.execute()).thenReturn(responses[0],
                Arrays.copyOfRange(responses, 1, responses.length));
        }

        return okHttpClient;
    }

    @Test
    void testGetStockQuotes() throws IOException {
        webAPIDataPlugin = spy(webAPIDataPlugin);
        OkHttpClient client = getMockOkHttpClient("src/test/resources/ibm.json");
        when(webAPIDataPlugin.getHttpClient()).thenReturn(client);
        assertDoesNotThrow(() -> webAPIDataPlugin.openConnection("demo"));

        String symbol = "IBM";
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = webAPIDataPlugin.getStockQuotes(List.of(symbol));

        StockQuotesResult result = stockQuotesResultCompletableFuture.join();
        List<StockQuote> quotes = result.getStockQuotes();
        assertNull(result.getErrorMessage());
        assertNotNull(quotes);
        assertNotEquals(0, quotes.size());
        assertEquals(symbol, quotes.get(0).getSymbol());
    }

    @Test
    void testGetStockQuotesWrongSymbol() throws IOException {
        webAPIDataPlugin = spy(webAPIDataPlugin);
        OkHttpClient client = getMockOkHttpClient("src/test/resources/error.json");
        when(webAPIDataPlugin.getHttpClient()).thenReturn(client);
        assertDoesNotThrow(() -> webAPIDataPlugin.openConnection("demo"));

        String symbol = "AAAAAA";
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = webAPIDataPlugin.getStockQuotes(List.of(symbol));

        StockQuotesResult result = stockQuotesResultCompletableFuture.join();
        List<StockQuote> quotes = result.getStockQuotes();
        assertNotNull(result.getErrorMessage());
        assertEquals(0, quotes.size());
    }

    @Test
    void testGetStockQuotesWrongSymbols() throws IOException {
        webAPIDataPlugin = spy(webAPIDataPlugin);
        OkHttpClient client = getMockOkHttpClient("src/test/resources/ibm.json",
            "src/test/resources/error.json" );
        when(webAPIDataPlugin.getHttpClient()).thenReturn(client);
        assertDoesNotThrow(() -> webAPIDataPlugin.openConnection("demo"));

        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = webAPIDataPlugin.getStockQuotes(List.of("IBM", "AAAAAA"));

        StockQuotesResult result = stockQuotesResultCompletableFuture.join();
        List<StockQuote> quotes = result.getStockQuotes();
        assertNotNull(result.getErrorMessage());
        assertNotEquals(0, quotes.size());
    }

    @Test
    void testGetStockQuotesNoOpen() {
        String symbol = "AAPL";
        List<String> symbols = Arrays.asList(symbol);
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = webAPIDataPlugin.getStockQuotes(symbols);
        Exception exception = assertThrows(CompletionException.class,
            () -> stockQuotesResultCompletableFuture.join());

        String expectedErrorMessage = "Config not set";
        assertEquals(AlphaVantageException.class, exception.getCause().getClass());
        assertEquals(expectedErrorMessage, exception.getCause().getMessage());
    }

}
