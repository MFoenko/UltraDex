package com.mikhail.pokedex.fragments;

import android.util.Pair;

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

    @Override
    public Pair<String, Integer>[] getSortOptions(){
        return new Pair[]{
                new Pair<String, Integer>("Learn \u25B2", Move.SORT_BY_LEARN_ASC),
                new Pair<String, Integer>("Learn \u25BC", Move.SORT_BY_LEARN_DES),
                new Pair<String, Integer>("Name \u25B2", Move.SORT_BY_NAME_ASC),
                new Pair<String, Integer>("Name \u25BC", Move.SORT_BY_NAME_DES),
                new Pair<String, Integer>("Type \u25B2", Move.SORT_BY_TYPE_ASC),
                new Pair<String, Integer>("Type \u25BC", Move.SORT_BY_TYPE_DES)
        };

    }



}
