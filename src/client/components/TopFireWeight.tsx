// @ts-ignore
import React, {useEffect, useRef, useState} from 'react';
import {BarChartVisualPlugin} from "../BarChartVisualPlugin";

// the 2nd graph for summary part
const Page2: React.FC = () => {
    const api = new BarChartVisualPlugin()
    return api.setData('/api/topweightfire')
};

export default Page2;