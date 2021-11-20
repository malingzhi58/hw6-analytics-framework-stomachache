export interface DataPlugin {
    getData: () => any;
    parseData: (p: any) => any;
    prepareData: () => any;
    // onRegister: () => void;
}