import { number } from "echarts";
import {
    PokemonClient,
    TypePokemon,
    NamedAPIResource,
    APIResource,
} from "pokenode-ts";

interface ChartObj {
    x: number[],
    y: string[]
}

interface DataPlugin {
    getData: () => NamedAPIResource[] | APIResource[];
    parseData: () => ChartObj;
    onRegister: () => void;
}
export { ChartObj }
export { DataPlugin }