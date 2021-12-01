package deadlinefighters.analyticsframework.plugin.data;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import deadlinefighters.analyticsframework.framework.core.DataPlugin;
import deadlinefighters.analyticsframework.framework.core.Framework;
import deadlinefighters.analyticsframework.framework.model.StockQuote;
import deadlinefighters.analyticsframework.framework.model.StockQuotesResult;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * The API key should be registered from the AlphaVantage website,
 * and passed into the arg field
 */
public class WebAPIAlphaVantageDataPlugin implements DataPlugin {

    private static final String NAME = "Web API - Alpha Vantage";
    protected static final String NULL_API_KEY_MESSAGE = "API key is empty";

    private Framework framework;

    protected OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .build();
    }

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
        if (StringUtils.isBlank(arg)) {
            throw new IOException(NULL_API_KEY_MESSAGE);
        }

        Config cfg = Config.builder()
            .key(arg)// arg is the api key
            .timeOut(10)
            .httpClient(getHttpClient())
            .build();
        AlphaVantage.api().init(cfg);
    }

    @Override
    public CompletableFuture<StockQuotesResult> getStockQuotes(
        List<String> symbols) {
        CompletableFuture<StockQuotesResult> stockQuotesResultCompletableFuture
            = CompletableFuture.supplyAsync(new Supplier<StockQuotesResult>() {
            @Override
            public StockQuotesResult get() {
                List<StockQuote> stockQuotes = new ArrayList<>();
                String errorMessage = null;

                for (String symbol : symbols) {
                    TimeSeriesResponse response = AlphaVantage.api()
                        .timeSeries()
                        .daily()
                        .forSymbol(symbol)
                        .outputSize(OutputSize.FULL)
                        .fetchSync();

                    if (response.getErrorMessage() != null) {
                        if (errorMessage == null) {
                            errorMessage = symbol + ": " + response.getErrorMessage();
                        } else {
                            errorMessage =
                                errorMessage + "\n" + symbol + ": " + response.getErrorMessage();
                        }
                        continue;
                    }

                    List<StockUnit> stockUnits = response.getStockUnits();
                    for (StockUnit stockUnit : stockUnits) {
                        stockQuotes.add(new StockQuote(symbol, LocalDate.parse(stockUnit.getDate()), stockUnit.getOpen(),
                            stockUnit.getClose(), stockUnit.getHigh(), stockUnit.getLow()));
                    }
                }
                return new StockQuotesResult(stockQuotes, errorMessage);
            }
        });
        return stockQuotesResultCompletableFuture;
    }

    @Override
    public void closeConnection() throws IOException {
        AlphaVantage.api().init(null);
    }
}
