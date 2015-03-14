package com.mikhail.pokedex.fragments;

import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.*;

public class PokemonTypingFragment extends TypingFragment<Pokemon>{

	public static final String TITLE = "Typing";
	public static final String[] EFFECTIVENESS_HEADERS = {"Immune to:", "Resistant to:", "Indifferent to:", "Weak to:"};
	
	
	@Override
	public String getTitle(){
		return TITLE;
	}

	@Override
	protected String getHeaderLabel(int section){
		return EFFECTIVENESS_HEADERS[section];
	}

	@Override
	protected float getEffectiveness(int mType, int otherType){
		return PokedexDatabase.TYPE_EFFICIENCY[mTypeVersion][otherType][mType];
	}

	@Override
	public void setData(PokedexClasses.Pokemon data){
		mTypes = data.types;
	}
	
	
}
