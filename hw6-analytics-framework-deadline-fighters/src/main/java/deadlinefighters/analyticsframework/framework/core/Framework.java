package deadlinefighters.analyticsframework.framework.core;

import deadlinefighters.analyticsframework.framework.model.DataPluginInput;
import deadlinefighters.analyticsframework.framework.model.ImportDataResult;
import deadlinefighters.analyticsframework.framework.model.VisualizationPluginsSupport;

import java.io.IOException;
import java.util.List;

public interface Framework {

    /**
     * Register data plugin to the framework
     *
     * @param dataPlugin
     */
    void registerDataPlugin(DataPlugin dataPlugin);

    /**
     * Register visualization plugin to the framework
     *
     * @param visualizationPlugin
     */
    void registerVisualizationPlugin(VisualizationPlugin visualizationPlugin);

    /**
     * Get the registered data plugins
     *
     * @return registered data plugins
     */
    List<DataPlugin> getRegisteredDataPlugins();

    /**
     * Get the supported and unsupported visualization plugins
     *
     * @return supported and unsupported visualization plugins
     */
    VisualizationPluginsSupport getVisualizationPluginsSupport();

    /**
     * Import Data give the inputs
     * <p>
     * REST controller should handler the IOExceptions
     *
     * @param dataPluginInputs
     * @return
     * @throws IOException I/O failure during open/close connection
     */
    ImportDataResult importData(List<DataPluginInput> dataPluginInputs) throws
        IOException;

    /**
     * Framework process the data
     */
    void processData();

    /**
     * Wrap function for import and process data dor REST controller
     *
     * @param dataPluginInputs data plugin inputs from the frontend
     * @return import data result
     * @throws IOException I/O failure during open/close connection
     */
    ImportDataResult importAndProcessData(List<DataPluginInput> dataPluginInputs) throws IOException;

    /**
     * Render the frontend page
     *
     * @param visualizationPlugin selected visualization plugin
     * @return HTML string
     */
    String renderVisualization(VisualizationPlugin visualizationPlugin);
}
