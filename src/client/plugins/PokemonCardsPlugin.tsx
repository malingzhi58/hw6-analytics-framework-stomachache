import * as React from 'react';
import { useState, useEffect, useContext } from 'react';
import PokemonCardsPage from '../components/PokemonCardsPage';
import { Context } from "../components/Store";


const PokemonCardsPlugin = () => {
	const [state, dispatch] = useContext(Context);

	useEffect(() => {
		async function getPokemonList() {
			const pokemonList = await (await fetch('/api/getpokemonlist')).json();
			dispatch({ type: "SET_POKEMON_LIST", payload: pokemonList });
		};
		getPokemonList()
	}, []);


	return (
		<div>
			<PokemonCardsPage />
		</div>
	);
};


export default PokemonCardsPlugin;
