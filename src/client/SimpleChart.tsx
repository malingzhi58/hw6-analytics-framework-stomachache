import React, { useRef } from 'react';
import ReactECharts from 'echarts-for-react';
import { useState, useEffect  } from 'react';


const Page: React.FC = () => {
  const [xdata, setXdata] = useState<String[]>([]);
  const [ydata, setYdata] = useState<number[]>([]);

	useEffect(() => {
    async function getGreeting() {
      try {
        const res = await fetch('/api/barchart')
        const mapdata = await res.json()
        console.log("xxxxxx")
        console.log(mapdata)
        setXdata(mapdata.xdata)
        setYdata(mapdata.ydata)
      } catch (error) {
        console.log(error);
      }
    }
    getGreeting();
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
        style={{ height: 400 }}
      />
    </>
  );
};

export default Page;