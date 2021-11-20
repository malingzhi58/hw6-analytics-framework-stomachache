import * as express from "express";
import {getData} from './barchart'

import {
    PokemonClient,
    TypePokemon,
    NamedAPIResource,
    APIResource,
} from "pokenode-ts";

const router = express.Router();

router.get("/api/hello", (req, res, next) => {
    let name = "";
    (async () => {
        const api = new PokemonClient();
        await api
            .getPokemonByName("luxray")
            .then((data) => {
                name = data.name;
                // console.log(name);
                res.json(name);
            }) // will output "Luxray"
            .catch((error) => console.error(error));
    })();
    tmpf()
    // (async () => {
    //     const api = new PokemonClient();
    //     await api
    //         .getTypeByName("fire")
    //         .then((data) => {
    //             for (let i = 0; i < data.pokemon.length; i++) {
    //                 let curname =data.pokemon[i].pokemon.name
    //
    //                 console.log();
    //             }
    //         }) // will output "Luxray"
    //         .catch((error) => console.error(error));
    // })();
});


router.get("/api/barchart", (req, res, next) => {
    getData().then(r => {
        const result = {xdata: r.xdata, ydata: r.ydata}
        res.json(result)
    })
});

router.get("/api/topweightfire", (req, res, next) => {
    tmpf().then(r => {
        const result = {xdata: r.xdata, ydata: r.ydata}
        res.json(result)
    })
});
async function tmpf() {
    const api = new PokemonClient()
    const typeres = await api.getTypeByName("fire")
    const x: string[] = []
    const y: number[] = []
    var array: { name: string,weight:number }[] = [];
    await Promise.all(typeres.pokemon.map(async (type) => {
        const result = await api.getPokemonByName(type.pokemon.name)
            .then(data =>{
                // console.log(type.pokemon.name)
                // x.push(data.name)
                // y.push(data.weight)
                array.push({name:data.name,weight:data.weight})
                console.log(data.name+":"+data.weight)
            })
            .catch((error) => console.error(error))
    }))
    const sortedArray: { name: string,weight:number }[] = array.sort((n1,n2) => {
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

/**
 var objectArray: { age: number; }[] = [{ age: 10}, { age: 1 }, {age: 5}];

 var sortedArray: { age: number; }[] = objectArray.sort((n1,n2) => {
        if (n1.age > n2.age) {
            return 1;
        }

        if (n1.age < n2.age) {
            return -1;
        }

        return 0;
    })
 */

export default router;

