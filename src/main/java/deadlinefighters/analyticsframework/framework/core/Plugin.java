package deadlinefighters.analyticsframework.framework.core;

public interface Plugin {
    /**
     * Get the name of the plugin to show on the GUI
     *
     * @return name of the plugin
     */
    String getName();

    /**
     * Called (only once) when the plugin is first registered with the
     * framework, giving the plugin a chance to perform any set-up during
     * plugin registration.
     *
     * @param framework The {@link Framework} instance with which the plugin
     *                  was registered.
     */
    void onRegister(Framework framework);
}
