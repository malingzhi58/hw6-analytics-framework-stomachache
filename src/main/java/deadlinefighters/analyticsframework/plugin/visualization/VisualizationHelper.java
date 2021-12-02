package deadlinefighters.analyticsframework.plugin.visualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import deadlinefighters.analyticsframework.framework.model.AggregatedStockQuotes;
import deadlinefighters.analyticsframework.helper.ObjectMapperInstance;

import java.util.List;

public class VisualizationHelper {
    private static final ObjectMapper objectMapper = ObjectMapperInstance.getInstance();

    /**
     * Converts aggregated stock quotes to JSON and adding "var aggregated = "
     * to convert it to a javascript object to be embedded into scripts
     *
     * @param data aggregated stock quotes
     * @return the string containing the javascript code for the quotes passed
     * @throws JsonProcessingException if there are errors generating the js
     */
    public static String quotesToJavascript(List<AggregatedStockQuotes> data) throws JsonProcessingException {
        return "var aggregated = " + objectMapper.writeValueAsString(data) + ";\n";
    }
}
