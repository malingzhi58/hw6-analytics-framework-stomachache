// @ts-ignore
import React from "react";
import Page from "./SimpleChart";
import Page2 from "./TopFireWeight";

const PokemonSummaryPage = () => {

    return (
        <>
            <h1>Number of Pokemons in each type</h1>
            <Page/>
            <h1>Top 10 heaviest fire pokemon</h1>
            <Page2/>
        </>
    );
};

export default PokemonSummaryPage;
