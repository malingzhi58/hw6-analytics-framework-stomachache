import * as express from "express";

import { PokemonClient, } from "pokenode-ts";
import { BarChartDataPlugin } from "./dataplugins/barChartDataPlugin";
import { TopBarChartDataPlugin } from "./dataplugins/topBarChartDataPlugin";
import { getPokemonListPlugin } from "./dataplugins/getPokemonListPlugin";


const router = express.Router();

router.get("/api/barchart", (req, res, next) => {
    const barchart = new BarChartDataPlugin()
    barchart.prepareData().then(r => {
        const result = { xdata: r.xdata, ydata: r.ydata }
        res.json(result)
    })

});

router.get("/api/topweightfire", (req, res, next) => {
    const topweight = new TopBarChartDataPlugin()
    topweight.prepareData().then(r => {
        const result = { xdata: r.xdata, ydata: r.ydata }
        res.json(result)
    })
});

router.get("/api/getpokemonlist", (req, res, next) => {
    const pokemonList = new getPokemonListPlugin()
    pokemonList.prepareData().then(r => {
        res.json(r)
    })
});


export default router;
