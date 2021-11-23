export interface DataPluginInterface {
    getData: (p?) => any;
    parseData: (p: any) => any;
    prepareData: (p?) => any;
    // onRegister: () => void;
}