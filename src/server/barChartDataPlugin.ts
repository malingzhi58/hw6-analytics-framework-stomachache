import {DataPlugin} from "./DataPlugin";
import {APIResource, NamedAPIResource, PokemonClient} from "pokenode-ts";

export class BarChartDataPlugin implements DataPlugin {
    async getData(): Promise<any> {
        const api = new PokemonClient()
        const types = await api.listTypes()
        const x: string[] = []
        const y: number[] = []

        function isNamedAPIResource(type: NamedAPIResource | APIResource) {
            return "name" in type;
        }

        await Promise.all(types.results.map(async (type) => {
            if (isNamedAPIResource(type)) {
                const result = await api.getTypeByName(type.name)
                x.push(type.name)
                y.push(result.pokemon.length)
            }
        }))
        return {xdata: x, ydata: y};
    }


    parseData(p: any): any {
        return p
    }

    prepareData(): any {
        return this.parseData(this.getData())
    }

}