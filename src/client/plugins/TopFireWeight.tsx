import React, {useEffect, useRef, useState} from 'react';
import {BarChartVisualPlugin} from "./VisualPlugin";
import ReactECharts from "echarts-for-react";

const Page2: React.FC = () => {
    const api = new BarChartVisualPlugin()
    return api.setData('/api/topweightfire')
};

export default Page2;