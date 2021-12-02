package deadlinefighters.analyticsframework.framework.model;

import java.util.List;

/**
 * A model for aggregated stock quotes so that visualization plugins can
 * choose which one to use
 */
public class AggregatedStockQuotes {
    private String symbol;
    private List<StockQuote> daily;
    private List<StockQuote> weekly;
    private List<StockQuote> monthly;
    private List<StockQuote> quarterly;
    private List<StockQuote> yearly;

    /**
     * Construct a new instance
     * @param symbol symbol
     * @param daily daily data
     * @param weekly weekly data
     * @param monthly monthly data
     * @param quarterly quarterly data
     * @param yearly yearly data
     */
    public AggregatedStockQuotes(String symbol, List<StockQuote> daily,
                                 List<StockQuote> weekly,
                                 List<StockQuote> monthly,
                                 List<StockQuote> quarterly,
                                 List<StockQuote> yearly) {
        this.symbol = symbol;
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
        this.quarterly = quarterly;
        this.yearly = yearly;
    }

    /**
     * Get symbol
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Set symbol
     * @param symbol symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Get daily data
     * @return daily data
     */
    public List<StockQuote> getDaily() {
        return daily;
    }

    /**
     * Set daily data
     * @param daily daily data
     */
    public void setDaily(List<StockQuote> daily) {
        this.daily = daily;
    }

    /**
     * Get weekly data
     * @return weekly data
     */
    public List<StockQuote> getWeekly() {
        return weekly;
    }

    /**
     * Set weekly data
     * @param weekly weekly data
     */
    public void setWeekly(List<StockQuote> weekly) {
        this.weekly = weekly;
    }

    /**
     * Get monthly data
     * @return monthly data
     */
    public List<StockQuote> getMonthly() {
        return monthly;
    }

    /**
     * Set monthly data
     * @param monthly monthly data
     */
    public void setMonthly(List<StockQuote> monthly) {
        this.monthly = monthly;
    }

    /**
     * Get quarterly data
     * @return quarterly data
     */
    public List<StockQuote> getQuarterly() {
        return quarterly;
    }

    /**
     * Set quarterly data
     * @param quarterly quarterly data
     */
    public void setQuarterly(List<StockQuote> quarterly) {
        this.quarterly = quarterly;
    }

    /**
     * Get yearly data
     * @return yearly data
     */
    public List<StockQuote> getYearly() {
        return yearly;
    }

    /**
     * Set yearly data
     * @param yearly quarterly data
     */
    public void setYearly(List<StockQuote> yearly) {
        this.yearly = yearly;
    }
}
