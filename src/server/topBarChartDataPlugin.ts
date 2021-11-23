import { DataPluginInterface } from "./dataPluginInterface";
import { PokemonClient } from "pokenode-ts";

export class TopBarChartDataPlugin implements DataPluginInterface {
    async getData(): Promise<any> {
        const api = new PokemonClient()
        const typeres = await api.getTypeByName("fire")
        const x: string[] = []
        const y: number[] = []
        var array: { name: string, weight: number }[] = [];
        await Promise.all(typeres.pokemon.map(async (type) => {
            const result = await api.getPokemonByName(type.pokemon.name)
                .then(data => {
                    // console.log(type.pokemon.name)
                    // x.push(data.name)
                    // y.push(data.weight)
                    array.push({name: data.name, weight: data.weight})
                    console.log(data.name + ":" + data.weight)
                })
                .catch((error) => console.error(error))
        }))
        return array
    }

    parseData(p: { name: string, weight: number }[]): any {
        const x: string[] = []
        const y: number[] = []
        const sortedArray: { name: string, weight: number }[] = p.sort((n1, n2) => {
            if (n1.weight > n2.weight) {
                return -1;
            }
            if (n1.weight < n2.weight) {
                return 1;
            }
            return 0;
        })
        for (let i = 0; i < 10; i++) {
            x.push(sortedArray[i].name)
            y.push(sortedArray[i].weight)
        }
        return {xdata: x, ydata: y}
    }

    prepareData(): Promise<any> {
        // const res =this.getData()
        // return this.parseData(res)
        return this.getData().then((r) => this.parseData(r))
    }

}

/**
 *
 async function tmpf() {
    const api = new PokemonClient()
    const typeres = await api.getTypeByName("fire")
    const x: string[] = []
    const y: number[] = []
    var array: { name: string, weight: number }[] = [];
    await Promise.all(typeres.pokemon.map(async (type) => {
        const result = await api.getPokemonByName(type.pokemon.name)
            .then(data => {
                // console.log(type.pokemon.name)
                // x.push(data.name)
                // y.push(data.weight)
                array.push({name: data.name, weight: data.weight})
                console.log(data.name + ":" + data.weight)
            })
            .catch((error) => console.error(error))
    }))
    const sortedArray: { name: string, weight: number }[] = array.sort((n1, n2) => {
        if (n1.weight > n2.weight) {
            return 1;
        }
        if (n1.weight < n2.weight) {
            return -1;
        }
        return 0;
    })
    for (let i = 0; i < 10; i++) {
        x.push(sortedArray[i].name)
        y.push(sortedArray[i].weight)
    }
    return {xdata: x, ydata: y}
}
 */