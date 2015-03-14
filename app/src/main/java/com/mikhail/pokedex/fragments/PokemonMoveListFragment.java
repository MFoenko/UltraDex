package com.mikhail.pokedex.fragments;

import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.*;

public class PokemonMoveListFragment extends MoveListFragment<Pokemon>
{

	public static final String TITLE = "Moves";
	
	@Override
	public void setData(Pokemon data){
		mData = PokedexDatabase.getInstance(getActivity()).getMovesByPokemon(data.id);
	}

	@Override
	public String getTitle(){
		return TITLE;
	}


	
	
	
}
