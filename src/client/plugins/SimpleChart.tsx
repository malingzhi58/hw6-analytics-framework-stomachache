// @ts-ignore
import React, {useRef} from 'react';
import {BarChartVisualPlugin} from "../VisualPlugin";

const Page: React.FC = () => {
    const api = new BarChartVisualPlugin()
    return api.setData('/api/barchart')
};

export default Page;