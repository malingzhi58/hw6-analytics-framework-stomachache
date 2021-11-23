import * as React from 'react';
import { useState,useContext, useEffect } from 'react';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import PokemonCardsPlugin from './plugins/PokemonCardsPlugin';

const App = () => {
    const [page, setPage] = useState<string>("list")

    const handleChange = (e) => {
        setPage(e.target.value)
    }


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
            {page === 'list'? <PokemonCardsPlugin /> : null }
        </>
	);
};


export default App;
