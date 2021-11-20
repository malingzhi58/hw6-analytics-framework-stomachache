import * as express from "express";

import {PokemonClient,} from "pokenode-ts";
import {BarChartDataPlugin} from "./barChartDataPlugin";
import {TopBarChartDataPlugin} from "./topBarChartDataPlugin";

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
    const barchart = new BarChartDataPlugin()
    barchart.prepareData().then(r => {
        const result = {xdata: r.xdata, ydata: r.ydata}
        res.json(result)
    })

});

router.get("/api/topweightfire", (req, res, next) => {
    const topweight = new TopBarChartDataPlugin()
    topweight.prepareData()
        .then(r => {
            const result = {xdata: r.xdata, ydata: r.ydata}
            res.json(result)
        })
});




export default router;

