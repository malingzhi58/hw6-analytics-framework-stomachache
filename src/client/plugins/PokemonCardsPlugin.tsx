import * as React from 'react';
import { useState, useEffect, useContext } from 'react';
import PokemonCardsPage from '../components/PokemonCardsPage';
import { PokemonClient } from "pokenode-ts";
import { Context } from "../components/Store";


const PokemonCardsPlugin = () => {
	const [greeting, setGreeting] = useState<string>('');
	const [state, dispatch] = useContext(Context);

	useEffect(() => {
		async function getGreeting() {
			try {
				const res = await fetch('/api/hello');
				const greeting = await res.json();
				setGreeting(greeting);
			} catch (error) {
				console.log(error);
			}
		}
		getGreeting();
	}, []);

  useEffect(() => {
    async function getPokemonList() {
		const pokemonList = await (await fetch('/api/getpokemonlist')).json();
		console.log(pokemonList)
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
