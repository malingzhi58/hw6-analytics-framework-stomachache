package deadlinefighters.analyticsframework.framework.model;

import deadlinefighters.analyticsframework.framework.core.DataPlugin;

import java.util.List;

/**
 * For input required in a data plugin
 */
public class DataPluginInput {
    private DataPlugin dataPlugin;
    private String arg;
    private List<String> symbols;

    /**
     * constructs a new instance
     * @param dataPlugin data plugin
     * @param arg argument
     * @param symbols list of symbols
     */
    public DataPluginInput(DataPlugin dataPlugin, String arg,
                           List<String> symbols) {
        this.dataPlugin = dataPlugin;
        this.arg = arg;
        this.symbols = symbols;
    }

    /**
     * get data plugin
     * @return data plugin
     */
    public DataPlugin getDataPlugin() {
        return dataPlugin;
    }

    /**
     * set data plugin
     * @param dataPlugin data plugin
     */
    public void setDataPlugin(DataPlugin dataPlugin) {
        this.dataPlugin = dataPlugin;
    }

    /**
     * get arg
     * @return arg
     */
    public String getArg() {
        return arg;
    }

    /**
     * set arg
     * @param arg arg
     */
    public void setArg(String arg) {
        this.arg = arg;
    }

    /**
     * get symbols
     * @return list of symbols
     */
    public List<String> getSymbols() {
        return symbols;
    }

    /**
     * set symbols
     * @param symbols list of symbols
     */
    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }
}
