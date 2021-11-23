// @ts-ignore
import React, {useRef} from 'react';
import {BarChartVisualPlugin} from "../BarChartVisualPlugin";

// the first graph for summary part
const Page: React.FC = () => {
    const api = new BarChartVisualPlugin()

    return api.setData('/api/barchart')
};

export default Page;

// function init(): React.FC {
//     return Page
// }
// export { init }