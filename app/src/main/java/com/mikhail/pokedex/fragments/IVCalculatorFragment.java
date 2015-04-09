package com.mikhail.pokedex.fragments;

import android.content.*;
import android.support.v4.app.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.PokedexDatabase;
import com.mikhail.pokedex.misc.*;
import android.view.*;
import android.os.*;
import android.widget.*;

public class IVCalculatorFragment extends Fragment implements DrawerItem {

	public static final String TITLE = "IV Calculator";
	public static final int ICON = R.drawable.ic_iv_calculator;

	AutoCompleteTextView mPokemonACTV;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.iv_calculator_fragment, container, false);
		mPokemonACTV = (AutoCompleteTextView)root.findViewById(R.id.pokemon_auto_complete);
		mPokemonACTV.setAdapter(new PokemonAutoCompleteAdapter(PokedexDatabase.getInstance(getActivity()).getAllPokemon()));
		mPokemonACTV.setThreshold(1);
		return root;
	}




	@Override
	public String getDrawerItemName() {
		return TITLE;
	}

	@Override
	public int getDrawerItemIconResourceId() {
		return ICON;
	}

	@Override
	public byte getDrawerItemType() {
		return DRAWER_ITEM_TYPE_CLICKABLE;
	}

	@Override
	public boolean onDrawerItemClick(Context context) {
		return true;
	}

}
