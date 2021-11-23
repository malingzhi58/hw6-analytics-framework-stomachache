import { useContext, useEffect, useState } from "react";
import { PokemonClient, Pokemon, NamedAPIResource } from "pokenode-ts";
import LoadingScreen from "./LoadingScreen";

import {
  Box,
  Dialog,
  DialogContent,
  DialogTitle,
  Grid,
  Slide,
} from "@mui/material";
import Paging from "./Paging";
// import PokemonDetails from "./PokemonDetails/PokemonDetails";
import PokeCard from "./PokeCard";
// @ts-ignore
import React from "react";
import { Context } from "./Store";

const apiClient = new PokemonClient();


const PokemonCardsPage = () => {
  const [currentPageNumber, setCurrentPageNumber] = useState<number>(1);
  const [totalPageNumber, setTotalPageNumber] = useState<number>(1);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [open, setOpen] = useState<boolean>(false);
  const [selectedPokemon, setSelectedPokemon] = useState<Pokemon>();
  const countPerPage: number = 32;
  const [state, dispatch] = useContext(Context);

  const getPokemon = async (offset: number = 0) => {
    console.log(state.pokemonList);
    if (state.pokemonList && state.pokemonList.results) {
      const filteredPokemonList = state.pokemonList.results.slice(
        offset,
        offset + countPerPage
      );

      let pokemon: Pokemon[] = [];
      for (const poke of filteredPokemonList) {
        pokemon.push(await apiClient.getPokemonByName(poke.name));
    //     console.log(poke.name)
    //     pokemon.push(await (await fetch('/api/getpokebyname', {
    //         method: 'POST',
    //         body: poke.name,
    //     })).json());

      }

      dispatch({ type: "SET_POKEMON", payload: pokemon });
    }
  };

  const handleClickOpen = (p: Pokemon) => {
    setOpen(true);
    setSelectedPokemon(p);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const getNext = async () => {
    if (currentPageNumber < totalPageNumber) {
      setIsLoading(true);
      await getPokemon(currentPageNumber * countPerPage);
      setCurrentPageNumber(currentPageNumber + 1);
    }
  };

  const getPrevious = async () => {
    if (currentPageNumber > 1) {
      setIsLoading(true);
      await getPokemon((currentPageNumber - 2) * countPerPage);
      setCurrentPageNumber(currentPageNumber - 1);
    }
  };


  useEffect(() => {
    setIsLoading(!state.pokemon);
  }, [state.pokemon]);

  useEffect(() => {
    getPokemon();
    setTotalPageNumber(Math.ceil(state.pokemonList?.count / countPerPage));
  }, [state.pokemonList]);

  useEffect(() => {
    setIsLoading(!state.pokemon);
  }, [state.pokemon]);

  if (isLoading) {
    return <LoadingScreen />;
  }


  return (
    <Box
      sx={{
        display: "flex",
        flexFlow: "column",
        flex: 1,
        marginX: 2,
      }}
      alignItems="center"
      justifyContent="center"
    >
      <Grid container spacing={2}>
        {state.pokemon.map((p: Pokemon) => (
          <Grid item md={3} xs={6} key={p.id}>
            <Box
              sx={{
                display: "flex",
                flexFlow: "row",
                flexWrap: "wrap",
              }}
              alignItems="center"
              justifyContent="space-between"
            >
              <PokeCard pokemon={p} onClick={() => handleClickOpen(p)} />
            </Box>
          </Grid>
        ))}
      </Grid>
      <Grid container spacing={2}>
        <Grid item md={4} xs={12} />
        <Grid item md={4} xs={12}>
          <Box
            sx={{
              display: "flex",
              flexFlow: "row",
              flexWrap: "wrap",
              marginBottom: 2,
            }}
            alignItems="center"
            justifyContent="center"
          >
            <Paging
              totalPageNumber={totalPageNumber}
              countPerPage={countPerPage}
              currentPageNumber={currentPageNumber}
              onPrevious={getPrevious}
              onNext={getNext}
            />
          </Box>
        </Grid>
      </Grid>
      <Dialog
        open={open}
        keepMounted
        onClose={handleClose}
        aria-describedby="alert-dialog-slide-description"
        maxWidth="md"
        fullWidth
      >
        <DialogTitle sx={{ textTransform: "capitalize" }}>
          {`${
            (selectedPokemon?.id.toString().length || 0) < 3
              ? selectedPokemon?.id.toString().padStart(3, "0")
              : selectedPokemon?.id.toString()
          } - ${selectedPokemon?.name}`}
        </DialogTitle>
        {/* <DialogContent>
          <PokemonDetails pokemon={selectedPokemon} />
        </DialogContent> */}
      </Dialog>
    </Box>
  );
};

export default PokemonCardsPage;
