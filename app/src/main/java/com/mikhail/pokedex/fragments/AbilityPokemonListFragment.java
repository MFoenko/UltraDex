package com.mikhail.pokedex.fragments;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;

public class AbilityPokemonListFragment extends PokemonListFragment<Ability>
{

	public static final String TITLE = "Pokemon";
	private int mAbilityId;

	private final OnItemSelectedListener GAMES_SPINNER_LISTENER = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
			mData = PokedexDatabase.getInstance(getActivity()).getPokemonByCommonAbility(mAbilityId, PokedexDatabase.VERSION_GROUP_VERSION[p3]);
			displayData();
		}

		@Override
		public void onNothingSelected(AdapterView<?> p1) {
			p1.setSelection(PokedexDatabase.VERSION_GROUP);
		}


	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = super.onCreateView(inflater, container, savedInstanceState);
		//setUsesVersionSpinner(GAMES_SPINNER_LISTENER);
		return v;
	}
	
	@Override
	public void setData(Ability data){
		mData = PokedexDatabase.getInstance(getActivity()).getPokemonByCommonAbility(data.id);
	}

	@Override
	public String getTitle(){
		return TITLE;
	}
	
}
