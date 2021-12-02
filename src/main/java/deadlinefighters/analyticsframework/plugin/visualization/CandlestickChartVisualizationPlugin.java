package deadlinefighters.analyticsframework.plugin.visualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import deadlinefighters.analyticsframework.framework.core.Framework;
import deadlinefighters.analyticsframework.framework.core.VisualizationPlugin;
import deadlinefighters.analyticsframework.framework.model.AggregatedStockQuotes;

import java.util.List;

/**
 * This is a candlestick chart that supports 1 stock only
 */
public class CandlestickChartVisualizationPlugin implements VisualizationPlugin {

    private Framework framework;
    private static final String NAME = "Candlestick chart";

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
        return symbolCount == 1;
    }

    @Override
    public String render(List<AggregatedStockQuotes> data) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<script>\n");
            sb.append(VisualizationHelper.quotesToJavascript(data));
            sb.append("""

                var trace = {
                  decreasing: {line: {color: '#7F7F7F'}},
                  increasing: {line: {color: '#17BECF'}},

                  line: {color: 'rgba(31,119,180,1)'},
                  type: 'candlestick',
                  xaxis: 'x',
                  yaxis: 'y',
                  x: [],
                  close: [],
                  high: [],
                  low: [],
                  open: [],
                };

                aggregated[0].daily.forEach(q => {
                  if (q.date <= "2017-01-01") {
                    return;
                  }
                  trace.x.push(q.date);
                  trace.close.push(q.close);
                  trace.high.push(q.high);
                  trace.low.push(q.low);
                  trace.open.push(q.open);
                });
                var data = [trace];

                var layout = {
                  dragmode: 'zoom',
                  margin: {
                    r: 10,
                    t: 25,
                    b: 40,
                    l: 60
                  },
                  showlegend: false,
                  xaxis: {
                    autorange: true,
                    title: 'Date',
                    type: 'date'
                  },
                  yaxis: {
                    autorange: true,
                    type: 'linear'
                  }
                };
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
