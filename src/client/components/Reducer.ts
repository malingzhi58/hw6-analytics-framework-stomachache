const Reducer = (state: any, action: any): any => {
  switch (action.type) {
    case "SET_POKEMON_LIST":
      return {
        ...state,
        pokemonList: action.payload,
      };
    case "SET_POKEMON":
      return {
        ...state,
        pokemon: action.payload,
      };
    default:
      return state;
  }
};

export default Reducer;
