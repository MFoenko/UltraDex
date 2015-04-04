package com.mikhail.pokedex.fragments;

import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.*;

public class AbilityPokemonListFragment extends PokemonListFragment<Ability>
{

	public static final String TITLE = "Pokemon";
	
	@Override
	public void setData(Ability data){
		mData = PokedexDatabase.getInstance(getActivity()).getPokemonByCommonAbility(data.id);
	}

	@Override
	public String getTitle(){
		return TITLE;
	}
	
}
