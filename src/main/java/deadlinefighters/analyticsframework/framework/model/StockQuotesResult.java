package deadlinefighters.analyticsframework.framework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockQuotesResult {
    private List<StockQuote> stockQuotes;
    private String errorMessage;

    /**
     * Constructor for stock quotes result
     *
     * @param stockQuotes  list of stock quote
     * @param errorMessage error message, null indicated no error
     */
    public StockQuotesResult(List<StockQuote> stockQuotes,
                             String errorMessage) {
        this.stockQuotes = stockQuotes;
        this.errorMessage = errorMessage;
    }

    /**
     * Get the list of stock quotes
     * Will be ignored when write to JSON
     *
     * @return list of stock quotes
     */
    @JsonIgnore
    public List<StockQuote> getStockQuotes() {
        return stockQuotes;
    }

    /**
     * Set the list of stock quotes
     *
     * @param stockQuotes list of stock quotes
     */
    public void setStockQuotes(List<StockQuote> stockQuotes) {
        this.stockQuotes = stockQuotes;
    }

    /**
     * Get the unique symbols and counts in stock quotes
     *
     * @return the unique symbols and counts
     */
    public Map<String, Integer> getStockQuotesCountBySymbol() {
        Map<String, Integer> counts = new HashMap<>();
        for (StockQuote q : stockQuotes) {
            counts.put(q.getSymbol(), counts.getOrDefault(q.getSymbol(), 0) + 1);
        }
        return counts;
    }

    /**
     * Get whether result has error
     *
     * @return Whether result has error
     */
    @JsonProperty
    public boolean hasError() {
        return errorMessage != null;
    }

    /**
     * Get the error message
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the error message
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
