import * as React from 'react';
import { Card, CardContent, Box, Typography } from "@mui/material";
import { Pokemon } from "pokenode-ts";
import { useContext } from "react";
import { Context } from "./Store";

const PokeCard = ({
  pokemon,
  onClick,
}: {
  pokemon: Pokemon;
  onClick: () => void;
}) => {
  const [state] = useContext(Context);

  return (
    <Card
      variant="elevation"
      elevation={2}
      sx={{ cursor: "pointer", marginBottom: 2, flex: 1 }}
      onClick={onClick}
    >
      <CardContent>
        <Box
          sx={{ display: "flex", flexFlow: "column" }}
          alignItems="center"
          justifyContent="center"
        >
          <Typography variant="body1" sx={{ textTransform: "capitalize" }}>
            {`${
              pokemon.id.toString().length < 3
                ? pokemon.id.toString().padStart(3, "0")
                : pokemon.id.toString()
            } - ${pokemon.name}`}
          </Typography>
          <img
            src={
              state.useDreamWorldSprites
                ? pokemon.sprites.other.dream_world.front_default
                : pokemon.sprites.front_default
            }
            alt={pokemon.name}
            style={{ maxHeight: 75 }}
          />
        </Box>
      </CardContent>
    </Card>
  );
};

export default PokeCard;
