package deadlinefighters.analyticsframework.framework.core;

import deadlinefighters.analyticsframework.framework.model.AggregatedStockQuotes;
import deadlinefighters.analyticsframework.framework.model.DataPluginInput;
import deadlinefighters.analyticsframework.framework.model.ImportDataResult;
import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import deadlinefighters.analyticsframework.framework.model.VisualizationPluginsSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Component
public class FrameworkImpl implements Framework {

    private static final Map<String, TemporalAdjuster> ADJUSTERS = new HashMap<>();
    protected static final String WEEKLY = "weekly";
    protected static final String MONTHLY = "monthly";
    protected static final String QUARTERLY = "quarterly";
    protected static final String YEARLY = "yearly";

    private List<StockQuotesResult> stockQuotesResults;
    private int symbolCnt;
    private List<AggregatedStockQuotes> aggregatedStockQuotesList;
    private List<DataPlugin> registeredDataPlugins;
    private List<VisualizationPlugin> registeredVisualizationPlugins;

    public FrameworkImpl() {
        registeredDataPlugins = new ArrayList<>();
        registeredVisualizationPlugins = new ArrayList<>();

        ADJUSTERS.put(WEEKLY, TemporalAdjusters.previousOrSame(DayOfWeek.of(1)));
        ADJUSTERS.put(MONTHLY, TemporalAdjusters.firstDayOfMonth());
        ADJUSTERS.put(QUARTERLY, new FirstDayOfQuarter());
        ADJUSTERS.put(YEARLY, TemporalAdjusters.firstDayOfYear());
    }

    public void registerDataPlugin(DataPlugin dataPlugin) {
        dataPlugin.onRegister(this);
        registeredDataPlugins.add(dataPlugin);
    }

    public void registerVisualizationPlugin(VisualizationPlugin visualizationPlugin) {
        visualizationPlugin.onRegister(this);
        registeredVisualizationPlugins.add(visualizationPlugin);
    }

    public List<DataPlugin> getRegisteredDataPlugins() {
        return registeredDataPlugins;
    }

    public VisualizationPluginsSupport getVisualizationPluginsSupport() {
        List<VisualizationPlugin> supported = new ArrayList<>();
        List<VisualizationPlugin> unsupported = new ArrayList<>();
        for (VisualizationPlugin visualizationPlugin : registeredVisualizationPlugins) {
            if (visualizationPlugin.isSupported(symbolCnt)) {
                supported.add(visualizationPlugin);
            } else {
                unsupported.add(visualizationPlugin);
            }
        }
        return new VisualizationPluginsSupport(supported, unsupported);
    }

    public ImportDataResult importData(List<DataPluginInput> dataPluginInputs) throws IOException {
        List<String> errorMessages = new ArrayList<>();

        // open connect
        for (DataPluginInput dataPluginInput : dataPluginInputs) {
            DataPlugin dataPlugin = dataPluginInput.getDataPlugin();
            String arg = dataPluginInput.getArg();
            dataPlugin.openConnection(arg);
        }

        // parallelize get stock
        List<CompletableFuture<StockQuotesResult>> rawStockQuotesFutures = new ArrayList<>();
        for (DataPluginInput dataPluginInput : dataPluginInputs) {
            DataPlugin dataPlugin = dataPluginInput.getDataPlugin();
            List<String> symbols = dataPluginInput.getSymbols();
            CompletableFuture<StockQuotesResult> rawStockQuoteFuture =
                dataPlugin.getStockQuotes(symbols);
            rawStockQuotesFutures.add(rawStockQuoteFuture);
        }
        try {
            stockQuotesResults = rawStockQuotesFutures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        } catch (CompletionException e) {
            throw new IOException(e.getCause() != null ? e.getCause() : e);
        }

        // check error
        for (StockQuotesResult stockQuotesResult : stockQuotesResults) {
            if (stockQuotesResult.hasError()) {
                errorMessages.add(stockQuotesResult.getErrorMessage());
            }
        }

        // close connect
        for (DataPluginInput dataPluginInput : dataPluginInputs) {
            DataPlugin dataPlugin = dataPluginInput.getDataPlugin();
            try {
                dataPlugin.closeConnection();
            } catch (IOException e) { // IOException is acceptable when closing connection
                errorMessages.add(e.getMessage());
            }
        }

        return new ImportDataResult(stockQuotesResults, errorMessages);
    }

    /**
     * @param stockQuotes has to share the same symbol, no duplication
     * @param timePeriod  supported: weekly, monthly, yearly
     * @return Map that both key and value are sorted by date
     */
    protected Map<LocalDate, List<StockQuote>> groupByDate(List<StockQuote> stockQuotes,
                                                           String timePeriod) {
        Map<LocalDate, List<StockQuote>> groupResult = stockQuotes.stream()
            .sorted(Comparator.comparing(StockQuote::getDate))
            .collect(Collectors.groupingBy(stockQuote -> stockQuote.getDate()
                    .with(ADJUSTERS.get(timePeriod)),
                LinkedHashMap::new,
                Collectors.toList()));
        return groupResult;
    }

    protected List<StockQuote> aggregateByTime(List<StockQuote> stockQuotes,
                                               String timePeriod) {
        Map<LocalDate, List<StockQuote>> groupResult = groupByDate(stockQuotes,
            timePeriod);

        List<StockQuote> period = new ArrayList<>();
        for (LocalDate groupDate : groupResult.keySet()) {
            List<StockQuote> sortByTime = groupResult.get(groupDate);
            if (sortByTime.size() == 0) continue;

            String symbol = sortByTime.get(0).getSymbol();
            Double open = sortByTime.get(0).getOpen() != null ?
                sortByTime.get(0).getOpen() : sortByTime.get(0).getClose();
            double close = sortByTime.get(sortByTime.size() - 1).getClose();
            Double high = Double.MIN_VALUE;
            Double low = Double.MAX_VALUE;
            for (StockQuote stockQuote : sortByTime) {
                if (stockQuote.getHigh() != null)
                    high = Double.max(high, stockQuote.getHigh());
                else
                    high = Double.max(high, stockQuote.getClose());

                if (stockQuote.getLow() != null)
                    low = Double.min(low, stockQuote.getLow());
                else
                    low = Double.min(low, stockQuote.getClose());
            }

            period.add(new StockQuote(symbol, groupDate,
                open, close, high, low));
        }
        return period;
    }


    public void processData() {
        Map<String, Set<StockQuote>> bySymbol = new HashMap<>();
        aggregatedStockQuotesList = new ArrayList<>();

        // dedup
        for (StockQuotesResult stockQuotesResult : stockQuotesResults) {
            for (StockQuote stockQuote : stockQuotesResult.getStockQuotes()) {
                String symbol = stockQuote.getSymbol();
                bySymbol.computeIfAbsent(symbol, k -> new HashSet<>()).add(stockQuote);
            }
        }


        for (String symbol : bySymbol.keySet()) {
            List<StockQuote> daily = new ArrayList<>(bySymbol.get(symbol));
            daily.sort(Comparator.comparing(StockQuote::getDate));

            List<StockQuote> weekly = aggregateByTime(daily, WEEKLY);
            List<StockQuote> monthly = aggregateByTime(daily, MONTHLY);
            List<StockQuote> quarterly = aggregateByTime(daily, QUARTERLY);
            List<StockQuote> yearly = aggregateByTime(daily, YEARLY);

            aggregatedStockQuotesList.add(new AggregatedStockQuotes(symbol,
                daily, weekly, monthly, quarterly, yearly));
        }

        this.symbolCnt = bySymbol.size();
    }

    @Override
    public ImportDataResult importAndProcessData(List<DataPluginInput> dataPluginInputs) throws IOException {
        ImportDataResult result = importData(dataPluginInputs);
        processData();
        return result;
    }

    public String renderVisualization(VisualizationPlugin visualizationPlugin) {
        return visualizationPlugin.render(aggregatedStockQuotesList);
    }

    protected List<AggregatedStockQuotes> getAggregatedStockQuotesList() {
        return aggregatedStockQuotesList;
    }
}
