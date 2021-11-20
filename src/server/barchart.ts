import { DataPlugin, ChartObj } from './dataplug'
import {
    PokemonClient,
    TypePokemon,
    NamedAPIResource,
    APIResource,
} from "pokenode-ts";



async function getData() {
    const api = new PokemonClient()
    const types = await api.listTypes()
    // let pokemon = []
    // for (let val of types.results) {
    //     if (isNamedAPIResource(val)) {
    //         pokemon.push(await api.getTypeByName(val.name))
    //     }
    // }
    const x: string[] = []
    const y: number[] = []
    await Promise.all(types.results.map(async (type) => {
      if (isNamedAPIResource(type)) {
        const result = await api.getTypeByName(type.name)
        x.push(type.name)
        y.push(result.pokemon.length)
      }
    }))
    return {xdata: x, ydata: y }
}

// const BarChartPlugin = {

//     // getData(): Promise<Object> {
//     //     const api = new PokemonClient();
//     //       async function request() {
//     //             try {
//     //                 const type = await api.listTypes()
//     //                 const res = await api.getTypeByName(type)
//     //             } catch (error) {
//     //               console.log(error)
//     //             }
//     //           }
//     //     const types =  
//     //     return await api.listTypes;
//     // }


// }


    // parseData(): ChartObj {
    //     return;
    // }
    // async function loadPlugins (targetDir: string): Promise<GamePlugin[]> {
    //     const dir = path.join(__dirname, targetDir)
    //     const filesPr = readdir(dir, { withFileTypes: true })
    //     const jsFilesPr = filesPr.then(files =>
    //       files.filter(f => f.isFile() && f.name.endsWith('.js')))
    //     const modulesPr = jsFilesPr.then(jsFiles =>
    //       jsFiles.map(f => require(path.join(dir, f.name))))
    //     return await modulesPr.then(modules =>
    //       modules.filter(m => typeof m.init === 'function').map(m => m.init() as GamePlugin))
    //   }

    // async sendData2(): Promise<Object> {
    //     try {
    //         const api = new PokemonClient();
    //         const result = await (() => {
    //             api.listTypes()
    //                 .then((data) => {
    //                     let res: string[] = []
    //                     let pokenlist: NamedAPIResource[] | APIResource[] = data.results;
    //                     for (let val of pokenlist) {
    //                         if (isNamedAPIResource(val)) {
    //                             let valname: string = val.name;
    //                             res.push(valname);
    //                         }
    //                     }
    //                     return res;
    //                 })

    //         });
    //     } catch (error) {
    //         console.log(error);
    //     }
    // }

    // async sendData(): Promise<Object> {
    //     let x: String[] = [];
    //     let y: number[] = [];
    //     const api = new PokemonClient();

    //     // const getTypes = () => new Promise((resolve) => setTimeout(() => resolve(), ))
    //     const getTypes =
    //         () => {
    //             api.listTypes()
    //                 .then((data) => {
    //                     let res: string[] = []
    //                     let pokenlist: NamedAPIResource[] | APIResource[] = data.results;
    //                     for (let val of pokenlist) {
    //                         if (isNamedAPIResource(val)) {
    //                             let valname: string = val.name;
    //                             res.push(valname);
    //                         }
    //                     }
    //                     return res;
    //                 })

    //         }

    //     await Promise.all([getTypes()])
    //         .then(console.log).catch(console.log).then();
    //     return

    //     // return await api
    //     //     .listTypes()
    //     //     .then((data) => {
    //     //         let pokenlist: NamedAPIResource[] | APIResource[] = data.results;
    //     //         for (let val of pokenlist) {
    //     //             if (isNamedAPIResource(val)) {
    //     //                 let valname: string = val.name;
    //     //                 // (async () => {
    //                         // const api2 = new PokemonClient();
    //                         // await api2
    //                         //     .getTypeByName(valname)
    //                         //     .then((data2) => {
    //                         //         x.push(valname);
    //                         //         y.push(data2.pokemon.length);
    //                         //         console.log("1" + x);
    //                         //     })
    //     //                 //         .catch((error) => console.error(error));
    //     //                 // })();
    //     //                 const api2 = new PokemonClient();
    //     //                 api2
    //     //                     .getTypeByName(valname)
    //     //                     .then((data2) => {
    //     //                         x.push(valname);
    //     //                         y.push(data2.pokemon.length);
    //     //                         console.log("1" + x);
    //     //                     })
    //     //             }
    //     //             console.log("2" + x);


    //     //         }
    //     //     })
    //     //     .then(() => {
    //     //         console.log(x);
    //     //         console.log("why");
    //     //         return { xdata: x, ydata: y };
    //     //     })
    //     //     .catch((error) => {
    //     //         console.error(error)
    //     //         return null;
    //     //     });

    // }
    // onRegister(): void {

    // }

function isNamedAPIResource(object: any): object is NamedAPIResource {
    return "name" in object;
}
export { getData };