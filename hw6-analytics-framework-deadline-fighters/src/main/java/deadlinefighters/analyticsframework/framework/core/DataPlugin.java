package deadlinefighters.analyticsframework.framework.core;

import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DataPlugin extends Plugin {
    /**
     * Establish a connection with the data source, this is called by the
     * framework before getStockQuotes is called.
     *
     * @param arg argument required to initialize the data plugin
     *            (e.g. local file path, web url or database url)
     * @throws IllegalArgumentException when fail to parse the arg
     *                                  or IOException when fail to establish a connection
     */
    void openConnection(String arg) throws IOException;

    /**
     * Read from the data source with only selected symbols
     * and parse the data into stock object.
     * <p>
     * If exception was thrown during the execution, the framework should catch
     * ExecutionException and handle it.
     * <p>
     * If symbols are missing in the data source, error message will be written
     * in the result.
     *
     * @param symbols a list of selected symbols
     * @return a promise of stock quotes result
     */
    CompletableFuture<StockQuotesResult> getStockQuotes(List<String> symbols);

    /**
     * Close the connection with the data source, this is called by the
     * framework after getStockQuotes is called.
     *
     * @throws IOException when fail to close a connection
     */
    void closeConnection() throws IOException;
}
