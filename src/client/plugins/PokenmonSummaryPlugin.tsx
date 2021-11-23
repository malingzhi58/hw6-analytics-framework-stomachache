import * as React from 'react';
import { useState, useEffect, useContext } from 'react';
import PokemonCardsPage from '../components/PokemonCardsPage';
import { PokemonClient } from "pokenode-ts";
import { Context } from "../components/Store";
import PokemonSummaryPage from "../components/PokemonSummaryPage";


const PokemonCardsPlugin = () => {

    return (
        <div>
            <PokemonSummaryPage />
        </div>
    );
};


export default PokemonCardsPlugin;
