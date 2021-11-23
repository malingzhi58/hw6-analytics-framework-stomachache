import { DataPluginInterface } from "../dataPluginInterface";
import { APIResource, NamedAPIResource, PokemonClient } from "pokenode-ts";

export class getPokemonByNamePlugin implements DataPluginInterface {
    async getData(name): Promise<any> {
        const api = new PokemonClient()
        const poke = await api.getPokemonByName(name);
        return poke;
    }

    parseData(p: any): any {
        return p
    }

    prepareData(name): Promise<any> {
        return this.parseData(this.getData(name))
    }

}