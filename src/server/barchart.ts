import {DataPlugin, ChartObj} from './dataplugin'
import {
    PokemonClient,
    TypePokemon,
    NamedAPIResource,
    APIResource,
} from "pokenode-ts";


async function getData() {
    const api = new PokemonClient()
    const types = await api.listTypes()
    const x: string[] = []
    const y: number[] = []
    await Promise.all(types.results.map(async (type) => {
        if (isNamedAPIResource(type)) {
            const result = await api.getTypeByName(type.name)
            x.push(type.name)
            y.push(result.pokemon.length)
        }
    }))
    return {xdata: x, ydata: y}
}

class BarChartPlugin implements DataPlugin{
    getData(): NamedAPIResource[] | APIResource[] {
        // const api = new PokemonClient()
        // const types = await api.listTypes()
        return undefined;
    }

    onRegister(): void {
    }

    parseData(): ChartObj {
        return undefined;
    }

}


function isNamedAPIResource(object: any): object is NamedAPIResource {
    return "name" in object;
}

export {getData};