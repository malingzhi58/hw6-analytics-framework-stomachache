// @ts-ignore
import React, {useEffect, useRef, useState} from "react";
import ReactECharts from "echarts-for-react";
import {VisualizationPlugin} from "./VisualPluginInterface"
class BarChartVisualPlugin implements VisualizationPlugin {
    setData(route: string): any {
        const [xdata, setXdata] = useState<String[]>([]);
        const [ydata, setYdata] = useState<number[]>([]);

        useEffect(() => {
            async function setData() {
                try {
                    const res = await fetch(route)
                    const mapdata = await res.json()
                    setXdata(mapdata.xdata)
                    setYdata(mapdata.ydata)
                } catch (error) {
                    console.log(error);
                }
            }

            setData();
        }, []);

        const option = {
            xAxis: {
                type: 'category',
                data: xdata
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: ydata,
                    type: 'bar'
                }
            ]
        };

        const instance = useRef(null);

        return (
            <>
                <ReactECharts
                    ref={instance}
                    option={option}
                    style={{height: 400}}
                />
            </>
        );
    }
}

export {BarChartVisualPlugin};