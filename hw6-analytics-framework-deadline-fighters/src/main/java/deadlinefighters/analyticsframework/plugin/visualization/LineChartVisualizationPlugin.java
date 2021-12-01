package deadlinefighters.analyticsframework.plugin.visualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import deadlinefighters.analyticsframework.framework.core.Framework;
import deadlinefighters.analyticsframework.framework.core.VisualizationPlugin;
import deadlinefighters.analyticsframework.framework.model.AggregatedStockQuotes;

import java.util.List;

/**
 * This is a line chart that supports 1-2 stocks
 */
public class LineChartVisualizationPlugin implements VisualizationPlugin {

    private Framework framework;
    private static final String NAME = "Line chart (1-2 stocks)";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onRegister(Framework framework) {
        this.framework = framework;
    }

    @Override
    public boolean isSupported(int symbolCount) {
        return symbolCount >= 1 && symbolCount <= 2;
    }

    @Override
    public String render(List<AggregatedStockQuotes> data) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<script>\n");
            sb.append(VisualizationHelper.quotesToJavascript(data));
            sb.append("""
                var data = aggregated.map((s, i) => {
                  let trace = {
                    x: [],
                    y: [],
                    name: s.symbol,
                    type: 'scatter',
                  };
                  if (i == 1) {
                    trace['yaxis'] = 'y2';
                  }
                  s.weekly.forEach(q => {
                    trace.x.push(q.date);
                    trace.y.push(q.close);
                  });
                  return trace;
                });

                var layout = {
                  dragmode: 'zoom',
                  title: 'Weekly stock prices - ' + aggregated[0].symbol + (aggregated.length == 2 ? ` vs ${aggregated[1].symbol}` : ''),
                  xaxis: {
                    autorange: true,
                    title: 'Date',
                    type: 'date'
                  },
                  yaxis: {title: aggregated[0].symbol, autorange: true},
                };
                if (aggregated.length == 2) {
                  layout['yaxis2'] = {
                    title: aggregated[1].symbol,
                    autorange: true,
                    titlefont: {color: 'rgb(148, 103, 189)'},
                    tickfont: {color: 'rgb(148, 103, 189)'},
                    overlaying: 'y',
                    side: 'right'
                  }
                }
                Plotly.newPlot('myDiv', data, layout);

                """);
            sb.append("</script>\n");
            sb.append("<div id=\"myDiv\" />");
            return sb.toString();
        } catch (JsonProcessingException e) {
            return e.toString();
        }
    }
}
