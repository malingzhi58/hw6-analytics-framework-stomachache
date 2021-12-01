package deadlinefighters.analyticsframework.framework.core;

import deadlinefighters.analyticsframework.framework.model.AggregatedStockQuotes;
import deadlinefighters.analyticsframework.framework.model.DataPluginInput;
import deadlinefighters.analyticsframework.framework.model.ImportDataResult;
import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FrameworkImplTest {

    private FrameworkImpl framework;
    private final String symbol = "AAPL";

    @BeforeEach
    void setUp() {
        framework = new FrameworkImpl();
    }

    private static void verifyGroupByDateMap(Map<LocalDate, List<StockQuote>> ans) {
        assertEquals(ans.keySet().stream().sorted().toList(), new ArrayList<>(ans.keySet()));
        for (List<StockQuote> quotes : ans.values()) {
            List<StockQuote> sorted = new ArrayList<>(quotes);
            sorted.sort(Comparator.comparing(StockQuote::getDate));
            assertEquals(sorted, quotes);
        }
    }

    @Test
    public void testGroupByDateWeekly() {
        List<StockQuote> stockQuotes = Arrays.asList(new StockQuote(symbol,
                LocalDate.of(2021, 11, 19),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 20),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 22),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 23),
                null, 2.5, null, null));

        Map<LocalDate, List<StockQuote>> ans = framework.groupByDate(stockQuotes, FrameworkImpl.WEEKLY);
        verifyGroupByDateMap(ans);
        assertEquals(2, ans.size());
        List<LocalDate> dates = new ArrayList<>(ans.keySet());
        assertEquals(LocalDate.of(2021, 11, 15), dates.get(0));
        assertEquals(LocalDate.of(2021, 11, 22), dates.get(1));
        assertEquals(stockQuotes.get(0), ans.get(dates.get(0)).get(0));
        assertEquals(stockQuotes.get(1), ans.get(dates.get(0)).get(1));
        assertEquals(stockQuotes.get(2), ans.get(dates.get(1)).get(0));
        assertEquals(stockQuotes.get(3), ans.get(dates.get(1)).get(1));
    }

    @Test
    public void testGroupByDateMonthly() {
        List<StockQuote> stockQuotes = Arrays.asList(new StockQuote(symbol,
                LocalDate.of(2021, 12, 19),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 20),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 10, 22),
                null, 2.5, null, null));

        Map<LocalDate, List<StockQuote>> ans = framework.groupByDate(stockQuotes, FrameworkImpl.MONTHLY);
        verifyGroupByDateMap(ans);
        assertEquals(3, ans.size());
        List<LocalDate> dates = new ArrayList<>(ans.keySet());
        assertEquals(LocalDate.of(2021, 10, 1), dates.get(0));
        assertEquals(LocalDate.of(2021, 11, 1), dates.get(1));
        assertEquals(LocalDate.of(2021, 12, 1), dates.get(2));
        assertEquals(stockQuotes.get(2), ans.get(dates.get(0)).get(0));
        assertEquals(stockQuotes.get(1), ans.get(dates.get(1)).get(0));
        assertEquals(stockQuotes.get(0), ans.get(dates.get(2)).get(0));
    }

    @Test
    public void testGroupByDateQuarterly() {
        List<StockQuote> stockQuotes = Arrays.asList(new StockQuote(symbol,
                LocalDate.of(2021, 10, 1),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 20),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 9, 22),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2021, 1, 22),
                null, 2.5, null, null));

        Map<LocalDate, List<StockQuote>> ans = framework.groupByDate(stockQuotes, FrameworkImpl.QUARTERLY);
        verifyGroupByDateMap(ans);
        assertEquals(3, ans.size());
        List<LocalDate> dates = new ArrayList<>(ans.keySet());
        assertEquals(LocalDate.of(2021, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2021, 7, 1), dates.get(1));
        assertEquals(LocalDate.of(2021, 10, 1), dates.get(2));
        assertEquals(stockQuotes.get(3), ans.get(dates.get(0)).get(0));
        assertEquals(stockQuotes.get(2), ans.get(dates.get(1)).get(0));
        assertEquals(stockQuotes.get(0), ans.get(dates.get(2)).get(0));
        assertEquals(stockQuotes.get(1), ans.get(dates.get(2)).get(1));
    }


    @Test
    public void testGroupByDateYearly() {
        List<StockQuote> stockQuotes = Arrays.asList(new StockQuote(symbol,
                LocalDate.of(2021, 11, 19),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2020, 11, 20),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2019, 11, 22),
                null, 2.5, null, null),
            new StockQuote(symbol,
                LocalDate.of(2019, 1, 22),
                null, 2.5, null, null));

        Map<LocalDate, List<StockQuote>> ans = framework.groupByDate(stockQuotes, FrameworkImpl.YEARLY);
        verifyGroupByDateMap(ans);
        assertEquals(3, ans.size());
        List<LocalDate> dates = new ArrayList<>(ans.keySet());
        assertEquals(LocalDate.of(2019, 1, 1), dates.get(0));
        assertEquals(LocalDate.of(2020, 1, 1), dates.get(1));
        assertEquals(LocalDate.of(2021, 1, 1), dates.get(2));
        assertEquals(stockQuotes.get(3), ans.get(dates.get(0)).get(0));
        assertEquals(stockQuotes.get(2), ans.get(dates.get(0)).get(1));
        assertEquals(stockQuotes.get(1), ans.get(dates.get(1)).get(0));
        assertEquals(stockQuotes.get(0), ans.get(dates.get(2)).get(0));
    }

    @Test
    public void testAggregateByTimeAllFields() {
        List<StockQuote> stockQuotes = Arrays.asList(new StockQuote(symbol,
                LocalDate.of(2021, 11, 19),
                null, 2.5, 1.2, 1.1),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 20),
                null, 7.92, 2.3, 2.2),
            new StockQuote(symbol,
                LocalDate.of(2022, 11, 17),
                1.0, 2.0, 3.0, 4.0),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 16),
                6.78, 2.5, 3.4, 3.3),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 17),
                null, 2.5, 4.5, 4.4));

        List<StockQuote> ans = framework.aggregateByTime(stockQuotes, "weekly");
        assertEquals(2, ans.size());
        assertEquals(LocalDate.of(2021, 11, 15), ans.get(0).getDate());
        assertEquals(LocalDate.of(2022, 11, 14), ans.get(1).getDate());
        assertEquals(6.78, ans.get(0).getOpen());
        assertEquals(7.92, ans.get(0).getClose());
        assertEquals(4.5, ans.get(0).getHigh());
        assertEquals(1.1, ans.get(0).getLow());
        assertEquals(1.0, ans.get(1).getOpen());
        assertEquals(2.0, ans.get(1).getClose());
        assertEquals(3.0, ans.get(1).getHigh());
        assertEquals(4.0, ans.get(1).getLow());
    }

    @Test
    public void testAggregateByTimeSomeFields() {
        List<StockQuote> stockQuotes = Arrays.asList(
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 16),
                null, 2.5, 3.4, 2.0),
            new StockQuote(symbol,
                LocalDate.of(2021, 11, 17),
                null, 4.5, null, null));

        List<StockQuote> ans = framework.aggregateByTime(stockQuotes, "weekly");
        assertEquals(1, ans.size());
        assertEquals(LocalDate.of(2021, 11, 15), ans.get(0).getDate());
        assertEquals(2.5, ans.get(0).getOpen());
        assertEquals(4.5, ans.get(0).getClose());
        assertEquals(4.5, ans.get(0).getHigh());
        assertEquals(2.0, ans.get(0).getLow());
    }

    @Test
    public void testRegisterDataPlugin() {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        when(dataPlugin.getName()).thenReturn("mock");
        framework.registerDataPlugin(dataPlugin);

        verify(dataPlugin).onRegister(framework);
        assertEquals(dataPlugin, framework.getRegisteredDataPlugins().get(0));
    }

    @Test
    public void testImportData() throws IOException {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        framework.registerDataPlugin(dataPlugin);

        String arg = "arg";
        String symbol = "symbol";
        List<String> symbols = List.of(symbol);

        StockQuote quote = new StockQuote(symbol, LocalDate.of(2021, 11, 20), 1.0, 1.0, 1.0, 1.0);

        when(dataPlugin.getStockQuotes(symbols))
            .thenReturn(CompletableFuture.supplyAsync(() -> new StockQuotesResult(List.of(
                quote
            ), null)));

        ImportDataResult result = framework.importAndProcessData(List.of(new DataPluginInput(dataPlugin, arg, symbols)));

        AggregatedStockQuotes aggregated = framework.getAggregatedStockQuotesList().get(0);
        assertEquals(symbol, aggregated.getSymbol());
        assertEquals(quote, aggregated.getDaily().get(0));
        assertFalse(result.hasError());
        assertEquals(quote, result.getStockQuotesResults().get(0).getStockQuotes().get(0));

        verify(dataPlugin).openConnection(arg);
        verify(dataPlugin).getStockQuotes(symbols);
        verify(dataPlugin).closeConnection();
    }

    @Test
    public void testImportDataWithErrorMessage() throws IOException {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        framework.registerDataPlugin(dataPlugin);

        String arg = "arg";
        String symbol = "symbol";
        List<String> symbols = List.of(symbol);

        StockQuote quote = new StockQuote(symbol, LocalDate.of(2021, 11, 20), 1.0, 1.0, 1.0, 1.0);

        when(dataPlugin.getStockQuotes(symbols))
            .thenReturn(CompletableFuture.supplyAsync(() -> new StockQuotesResult(List.of(
                quote
            ), "test")));

        ImportDataResult result = framework.importAndProcessData(List.of(new DataPluginInput(dataPlugin, arg, symbols)));

        AggregatedStockQuotes aggregated = framework.getAggregatedStockQuotesList().get(0);
        assertEquals(symbol, aggregated.getSymbol());
        assertEquals(quote, aggregated.getDaily().get(0));
        assertTrue(result.hasError());
        assertEquals("test", result.getErrorMessages().get(0));

        verify(dataPlugin).openConnection(arg);
        verify(dataPlugin).getStockQuotes(symbols);
        verify(dataPlugin).closeConnection();
    }

    @Test
    public void testImportDataException() {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        framework.registerDataPlugin(dataPlugin);

        String arg = "arg";
        String symbol = "symbol";
        List<String> symbols = List.of(symbol);

        when(dataPlugin.getStockQuotes(symbols))
            .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("test")));

        IOException ex = assertThrows(IOException.class, () -> framework.importAndProcessData(List.of(new DataPluginInput(dataPlugin, arg, symbols))));
        assertEquals("test", ex.getCause().getMessage());
    }

    @Test
    public void testCloseConnectionIllegalArgumentException() throws IOException {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        framework.registerDataPlugin(dataPlugin);

        String arg = "arg";
        String symbol = "symbol";
        List<String> symbols = List.of(symbol);

        doThrow(new IllegalArgumentException()).when(dataPlugin).openConnection(arg);

        assertThrows(IllegalArgumentException.class, () -> framework.importAndProcessData(List.of(new DataPluginInput(dataPlugin, arg, symbols))));
    }

    @Test
    public void testOpenConnectionIOException() throws IOException {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        framework.registerDataPlugin(dataPlugin);

        String arg = "arg";
        String symbol = "symbol";
        List<String> symbols = List.of(symbol);

        doThrow(new IOException()).when(dataPlugin).openConnection(arg);

        assertThrows(IOException.class, () -> framework.importAndProcessData(List.of(new DataPluginInput(dataPlugin, arg, symbols))));
    }

    @Test
    public void testOpenConnectionIOExceptionDuringClose() throws IOException {
        DataPlugin dataPlugin = mock(DataPlugin.class);

        framework.registerDataPlugin(dataPlugin);

        String arg = "arg";
        String symbol = "symbol";
        List<String> symbols = List.of(symbol);

        StockQuote quote = new StockQuote(symbol, LocalDate.of(2021, 11, 20), 1.0, 1.0, 1.0, 1.0);

        when(dataPlugin.getStockQuotes(symbols))
            .thenReturn(CompletableFuture.supplyAsync(() -> new StockQuotesResult(List.of(
                quote
            ), null)));

        doThrow(new IOException()).when(dataPlugin).closeConnection();

        ImportDataResult result = framework.importAndProcessData(List.of(new DataPluginInput(dataPlugin, arg, symbols)));

        AggregatedStockQuotes aggregated = framework.getAggregatedStockQuotesList().get(0);
        assertEquals(symbol, aggregated.getSymbol());
        assertEquals(quote, aggregated.getDaily().get(0));
        assertTrue(result.hasError());

        verify(dataPlugin).openConnection(arg);
        verify(dataPlugin).getStockQuotes(symbols);
        verify(dataPlugin).closeConnection();
    }
}
