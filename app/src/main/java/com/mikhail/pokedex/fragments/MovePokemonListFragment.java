package com.mikhail.pokedex.fragments;

import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.*;
import android.view.*;
import android.os.*;

public class MovePokemonListFragment extends PokemonListFragment<Move>	
{
	
	public final static String TITLE = "Learned by";

	
	@Override
	public void setData(Move data){
		mData = PokedexDatabase.getInstance(getActivity()).getPokemonByCommonMove(data.getId());
	}

	@Override
	public String getTitle(){
		return TITLE;
	}

	
	
	
	
}
