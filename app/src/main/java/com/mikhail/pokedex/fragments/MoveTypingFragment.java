package com.mikhail.pokedex.fragments;

import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.*;

public class MoveTypingFragment extends TypingFragment<Move>{

	public static final String TITLE = "Typing";
	public static final String[] HEADERS = {"Useless against", "Not very effective against", "Regularly effective against", "Super effective against"};
	
	@Override
	public void setData(Move data){
		mTypes = new int[]{data.type};
	}

	@Override
	public String getTitle(){
		return TITLE;
	}

	@Override
	protected String getHeaderLabel(int section){
		return HEADERS[section];
	}

	@Override
	protected float getEffectiveness(int mType, int otherType){
		return PokedexDatabase.TYPE_EFFICIENCY[mTypeVersion][mType][otherType];
	}
	
}
