import * as React from 'react';
import { useState,useContext, useEffect } from 'react';
import PokemonCardsPage from './components/PokemonCardsPage';
// @ts-ignore
import Radio from '@mui/material/Radio';
// @ts-ignore
import RadioGroup from '@mui/material/RadioGroup';
// @ts-ignore
import FormControlLabel from '@mui/material/FormControlLabel';
// @ts-ignore
import FormControl from '@mui/material/FormControl';
// @ts-ignore
import FormLabel from '@mui/material/FormLabel';
import PokemonCardsPlugin from './plugins/PokemonCardsPlugin';
import Store from "./components/Store";
import { PokemonClient } from "pokenode-ts";
import { Context } from "./components/Store";
import PokenmonSummary from "./plugins/PokenmonSummaryPlugin";


const App = () => {
    const [page, setPage] = useState<string>("list")

    const handleChange = (e) => {
        setPage(e.target.value)
    }

    useEffect(() => {
        async function getPokemonList() {
            const pokemonList = await (await fetch('/api/getpokemonlist')).json();
            dispatch({ type: "SET_POKEMON_LIST", payload: pokemonList });
        }
        getPokemonList();
    }, []);


	return (
        <>
            <div style={{ margin: 20 }}>
                <FormControl component="fieldset">
                    <FormLabel component="legend">Choose visualization type</FormLabel>
                    <RadioGroup
                        value={page}
                        onChange={handleChange}
                        row
                    >
                        <FormControlLabel value="list" control={<Radio />} label="Pokemon List" />
                        <FormControlLabel value="summary" control={<Radio />} label="Pokemon Summary" />
                    </RadioGroup>
                </FormControl>
            </div>
        {page === 'list'? <PokemonCardsPage /> : <PokenmonSummary/> }
		</>
	);
};


export default App;
