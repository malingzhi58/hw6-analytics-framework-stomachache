package deadlinefighters.analyticsframework.framework.core;

import deadlinefighters.analyticsframework.framework.model.AggregatedStockQuotes;

import java.util.List;

public interface VisualizationPlugin extends Plugin {
    /**
     * Requirement of the plugin on the data
     *
     * @param symbolCount number of unique symbols in the data
     * @return boolean      indicating whether the number of symbols is
     * supported by the plugin
     */
    boolean isSupported(int symbolCount);

    /**
     * Render the corresponding char using Plotly
     *
     * @param data a list of aggregated stock quotes
     * @return HTML that will be embedded in the web-based GUI
     */
    String render(List<AggregatedStockQuotes> data);
}
