package deadlinefighters.analyticsframework.framework.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Result for import data
 */
public class ImportDataResult {
    private List<StockQuotesResult> stockQuotesResults;
    private List<String> errorMessages;

    /**
     * construct a new instance
     * @param stockQuotesResults stock quotes results
     * @param errorMessages list of errorMessages, if any
     */
    public ImportDataResult(List<StockQuotesResult> stockQuotesResults, List<String> errorMessages) {
        this.stockQuotesResults = stockQuotesResults;
        this.errorMessages = errorMessages;
    }

    /**
     * Check if result has error
     * @return a boolean indicating whether it has any error messages
     */
    @JsonProperty
    public boolean hasError() {
        return errorMessages != null && !errorMessages.isEmpty();
    }

    /**
     * Get stock quote results
     * @return a list of stock quote results
     */
    public List<StockQuotesResult> getStockQuotesResults() {
        return stockQuotesResults;
    }

    /**
     * Set stock quote results
     * @param stockQuotesResults a list of stock quote results
     */
    public void setStockQuotesResults(List<StockQuotesResult> stockQuotesResults) {
        this.stockQuotesResults = stockQuotesResults;
    }

    /**
     * Get error messages
     * @return a list of error messages
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Set error messages
     * @param errorMessages a list of error messages
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
