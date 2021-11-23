import {BarChartDataPlugin} from "../src/server/dataplugins/barChartDataPlugin";
import {TopBarChartDataPlugin} from "../src/server/dataplugins/topBarChartDataPlugin";

test('test for the sort function in TopBarChartDataPlugin', () => {
    let barchart = new TopBarChartDataPlugin()
    var array: { name: string, weight: number }[] = [];
    array.push({name: "a", weight: 1})
    array.push({name: "b", weight: 3})
    array.push({name: "c", weight: 2})
    var res =barchart.parseData(array)
    expect(res.xdata.length).toEqual(3)
    expect(res.ydata[0]).toEqual(3)
    // barchart.prepareData().then(r => {
    //     console.log(r.xdata)
    //     console.log(r.ydata)
    //     expect(r.xdata.length).toEqual(r.ydata.length)
    // })

    // async function f1() {
    //     var x = await barchart.prepareData().then(
    //         r => {
    //             console.log(r.xdata)
    //             console.log(r.ydata)
    //             expect(r.xdata.length).toEqual(r.ydata.length)
    //         }
    //     )
    //     return 100
    // }
    //
    // f1().then(() => {
    //     console.log("hell")
    // });
    // console.log("2")

})

function a() {
    return 1
}