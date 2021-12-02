package deadlinefighters.analyticsframework.framework.model;

import deadlinefighters.analyticsframework.framework.core.VisualizationPlugin;

import java.util.List;

public class VisualizationPluginsSupport {
    private List<VisualizationPlugin> supported;
    private List<VisualizationPlugin> unsupported;

    /**
     * Constructor for VisualizationPluginsSupport
     *
     * @param supported list of supported VisualizationPlugins
     * @param unsupported list of unsupproted VisualizationPluginsSupport
     */
    public VisualizationPluginsSupport(List<VisualizationPlugin> supported,
                                       List<VisualizationPlugin> unsupported) {
        this.supported = supported;
        this.unsupported = unsupported;
    }

    /**
     * Get the list of supported VisualizationPlugins
     *
     * @return list of supported VisualizationPlugins
     */
    public List<VisualizationPlugin> getSupported() {
        return supported;
    }

    /**
     * Set list of supported VisualizationPlugins
     *
     * @param supported list of supported VisualizationPlugins
     */
    public void setSupported(List<VisualizationPlugin> supported) {
        this.supported = supported;
    }

    /**
     * Get list of unsupported VisualizationPlugins
     *
     * @return list of unsupported VisualizationPlugins
     */
    public List<VisualizationPlugin> getUnsupported() {
        return unsupported;
    }

    /**
     * Set list of unsupported VisualizationPlugins
     *
     * @param unsupported list of unsupported VisualizationPlugins
     */
    public void setUnsupported(List<VisualizationPlugin> unsupported) {
        this.unsupported = unsupported;
    }
}
