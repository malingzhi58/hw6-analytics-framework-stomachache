import * as React from 'react';
import { useState, useContext, useEffect } from 'react';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import PokemonListPlugin from './visualizationplugins/PokemonListPlugin';
import { Context } from "./components/Store";
import PokenmonSummary from "./visualizationplugins/PokenmonSummaryPlugin";


const App = () => {
    const [page, setPage] = useState<string>("list")
    const [state, dispatch] = useContext(Context);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPage(e.target.value)
    }

    useEffect(() => {
        async function getPokemonList() {
            const pokemonList: string = await (await fetch('/api/getpokemonlist')).json();
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
            {page === 'list' ? <PokemonListPlugin /> : <PokenmonSummary />}
        </>
    );
};


export default App;
