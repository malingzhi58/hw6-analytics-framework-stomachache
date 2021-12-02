package deadlinefighters.analyticsframework.controller;

import deadlinefighters.analyticsframework.framework.core.DataPlugin;
import deadlinefighters.analyticsframework.framework.core.Framework;
import deadlinefighters.analyticsframework.framework.core.FrameworkImpl;
import deadlinefighters.analyticsframework.framework.core.Plugin;
import deadlinefighters.analyticsframework.framework.core.VisualizationPlugin;
import deadlinefighters.analyticsframework.framework.model.DataPluginInput;
import deadlinefighters.analyticsframework.framework.model.ImportDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiRestController {

    public static final String PLUGIN_KEY = "key";
    public static final String PLUGIN_NAME = "name";
    public static final String PLUGIN_ARG = "arg";
    public static final String PLUGIN_SYMBOLS = "symbols";

    private final Framework framework;

    public ApiRestController() {
        this.framework = new FrameworkImpl();
        for (DataPlugin dataPlugin : ServiceLoader.load(DataPlugin.class)) {
            this.framework.registerDataPlugin(dataPlugin);
        }
        for (VisualizationPlugin visualizationPlugin : ServiceLoader.load(VisualizationPlugin.class)) {
            this.framework.registerVisualizationPlugin(visualizationPlugin);
        }
    }

    private static List<Map<Object, Object>> toPluginMaps(List<? extends Plugin> plugins) {
        return plugins.stream().map(plugin -> {
            Map<Object, Object> map = new HashMap<>();
            map.put(PLUGIN_KEY, plugin.getClass().getName());
            map.put(PLUGIN_NAME, plugin.getName());
            return map;
        }).toList();
    }

    @GetMapping("/plugins/data")
    public List<Map<Object, Object>> getDataPlugins() {
        return toPluginMaps(framework.getRegisteredDataPlugins());
    }

    @GetMapping("/plugins/visualization")
    public Map<String, List<Map<Object, Object>>> getVisualizationPlugins() {
        Map<String, List<Map<Object, Object>>> visualizationPlugins = new HashMap<>();
        visualizationPlugins.put("supported", toPluginMaps(framework.getVisualizationPluginsSupport().getSupported()));
        visualizationPlugins.put("unsupported", toPluginMaps(framework.getVisualizationPluginsSupport().getUnsupported()));
        return visualizationPlugins;
    }

    @PostMapping("/plugins/data/import")
    public ImportDataResult importData(@RequestBody List<Map<String, String>> inputMaps) throws IOException {
        Map<String, DataPlugin> dataPlugins = framework
            .getRegisteredDataPlugins()
            .stream()
            .collect(Collectors.toMap(
                p -> p.getClass().getName(),
                p -> p
            ));
        List<DataPluginInput> inputs = new ArrayList<>();
        if (inputMaps.isEmpty()) {
            throw new IllegalArgumentException("Empty input");
        }

        for (Map<String, String> inputMap : inputMaps) {
            String key = inputMap.get(PLUGIN_KEY);
            DataPlugin dataPlugin = dataPlugins.get(key);
            if (dataPlugin == null) {
                throw new IllegalArgumentException("Plugin not found");
            }
            String arg = inputMap.get(PLUGIN_ARG);
            String symbolsString = inputMap.get(PLUGIN_SYMBOLS);
            if (symbolsString == null || symbolsString.isEmpty()) {
                throw new IllegalArgumentException("Symbols are empty");
            }
            List<String> symbols = Arrays.stream(symbolsString.split(",")).map(String::trim).toList();
            if (symbols.stream().anyMatch(String::isEmpty)) {
                throw new IllegalArgumentException("Some symbols are empty");
            }
            inputs.add(new DataPluginInput(dataPlugin, arg, symbols));
        }
        return framework.importAndProcessData(inputs);
    }

    @PostMapping("/plugins/visualization/render")
    public String render(@RequestBody Map<String, String> map) {
        Map<String, VisualizationPlugin> visualizationPlugins = framework
            .getVisualizationPluginsSupport().getSupported()
            .stream()
            .collect(Collectors.toMap(
                p -> p.getClass().getName(),
                p -> p
            ));
        String key = map.get(PLUGIN_KEY);
        VisualizationPlugin visualizationPlugin = visualizationPlugins.get(key);
        if (visualizationPlugin == null) {
            throw new IllegalArgumentException("Plugin not found");
        }
        return framework.renderVisualization(visualizationPlugin);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String illegalArgumentExceptionHandler(IOException e) {
        return e.getMessage();
    }
}
