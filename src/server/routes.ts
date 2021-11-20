import * as express from "express";
import { getData } from './barchart'

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
});


router.get("/api/barchart", (req, res, next) => {
  getData().then(r => {
    const result = {xdata: r.xdata, ydata: r.ydata }
    res.json(result)
  })
  // (async () => {
  //     const api = new PokemonClient();
  //     await api
  //         .listTypes()
  //         .then((data) => {
  //             let pokenlist: NamedAPIResource[] | APIResource[] = data.results;
  //             for (let val of pokenlist) {
  //                 if (isNamedAPIResource(val)) {
  //                     let valname: string = val.name;
  //                     (async () => {
  //                         const api2 = new PokemonClient();
  //                         await api2
  //                             .getTypeByName(valname)
  //                             .then((data2) => {
  //                                 x.push(valname);
  //                                 y.push(data2.pokemon.length);
  //                             })
  //                             .catch((error) => console.error(error));
  //                     })();
  //                 }
  //             }
  //         })
  //         .then(() => {
  //             console.log(x);
  //             res.json({ xdata: x, ydata: y });
  //         })
  //         .catch((error) => console.error(error));
  // })();
});

export default router;

