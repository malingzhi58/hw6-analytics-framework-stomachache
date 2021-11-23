import React, { useRef } from "react";
import { Typography, Card, CardContent } from "@mui/material";
import { Pokemon } from "pokenode-ts";
import ReactECharts from "echarts-for-react";

const PokemonDetails = ({ pokemon }: { pokemon: Pokemon | undefined }) => {
  const instance = useRef(null);
  return (
    <Card variant="outlined">
      <CardContent>
        <Typography
          variant="h6"
          sx={{ width: "100%", fontWeight: "bold" }}
          textAlign="center"
        >
          Stats
        </Typography>
        <ReactECharts
          ref={instance}
          option={{
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            xAxis: {
              type: 'value'
            },
            yAxis: {
              type: 'category',
              data: ["HP", "Attack", "Defense", "Sp. Atk", "Sp. Def", "Speed"]
            },
            series: [
              {
                data: [
                {
                  value: pokemon?.stats.find((s) => s.stat.name === "hp")?.base_stat,
                  itemStyle: {color: '#d87a80'},
                },
                {
                    value: pokemon?.stats.find((s) => s.stat.name === "attack")?.base_stat,
                    itemStyle: {color: '#b6a2de'},
                },
                {
                    value: pokemon?.stats.find((s) => s.stat.name === "defense")?.base_stat,
                    itemStyle: {color: '#5ab1ef'},
                },
                {
                    value: pokemon?.stats.find((s) => s.stat.name === "special-attack")?.base_stat,
                    itemStyle: {color: '#9a7fd1'},
                },
                {
                  value: pokemon?.stats.find((s) => s.stat.name === "special-defense")?.base_stat,
                  itemStyle: {color: '#588dd5'},
                },
                {
                  value: pokemon?.stats.find((s) => s.stat.name === "speed")?.base_stat,
                  itemStyle: {color: '#e5cf0d'},
              }
                ],
                type: 'bar'
              }
            ]
          }}
        />
      </CardContent>
    </Card>
  );
};
export default PokemonDetails;
