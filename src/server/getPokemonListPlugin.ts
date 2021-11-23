import { DataPluginInterface } from "./dataPluginInterface";
import {APIResource, NamedAPIResource, PokemonClient} from "pokenode-ts";

export class getPokemonListPlugin implements DataPluginInterface {
    async getData(): Promise<any> {
        const api = new PokemonClient()
        const pokemonList = await api.listPokemons(0, 3000);
        return pokemonList;
    }

    parseData(p: any): any {
        return p
    }

    prepareData(): Promise<any> {
        return this.parseData(this.getData())
    }

}