package deadlinefighters.analyticsframework.framework.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Objects;

public class StockQuote {
    private String symbol;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    // nullable as some data sources may not come with particular prices
    private Double open;
    private Double high;
    private Double low;
    // close should always be there
    private double close;

    /**
     * Constructor for stock quote
     *
     * @param symbol  the symbol of the stock quote
     * @param date    the date of the stock quote
     * @param date    the date of the stock quote
     * @param open    the open price of the stock quote
     * @param close   the close price of the stock quote
     * @param high    the high price of the stock quote
     * @param low     the low price of the stock quote
     */
    public StockQuote(String symbol, LocalDate date, Double open, double close, Double high, Double low) {
        this.symbol = symbol;
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    /**
     * Get the symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Set the symbol
     *
     * @param symbol the symbol of the stock quote
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Get the date
     *
     * @return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Set the date
     *
     * @param date the date of the stock quote
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Get the open price
     *
     * @return open price
     */
    public Double getOpen() {
        return open;
    }

    /**
     * Set the open price
     *
     * @param open open price
     */
    public void setOpen(Double open) {
        this.open = open;
    }

    /**
     * Get the high price
     *
     * @return  high price
     */
    public Double getHigh() {
        return high;
    }

    /**
     * Set the high price
     *
     * @param high high price
     */
    public void setHigh(Double high) {
        this.high = high;
    }

    /**
     * Get the low price
     *
     * @return low price
     */
    public Double getLow() {
        return low;
    }

    /**
     * Set the low price
     *
     * @param low low price
     */
    public void setLow(Double low) {
        this.low = low;
    }

    /**
     * Get the close price
     *
     * @return close price
     */
    public double getClose() {
        return close;
    }

    /**
     * Set the close price
     *
     * @param close close price
     */
    public void setClose(double close) {
        this.close = close;
    }

    /**
     * Only symbol and date are used to compare whether they are equal
     *
     * @param o  StockQuote object
     * @return  whether they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockQuote that = (StockQuote) o;
        return symbol.equals(that.symbol) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, date);
    }

    @Override
    public String toString() {
        return "StockQuote{" +
            "symbol='" + symbol + '\'' +
            ", date=" + date +
            '}';
    }
}
