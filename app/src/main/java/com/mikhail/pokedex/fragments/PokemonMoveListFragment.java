package com.mikhail.pokedex.fragments;

import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;

public class PokemonMoveListFragment extends MoveListFragment<Pokemon> {

	public static final String TITLE = "Moves";

	public int versionGroup = PokedexDatabase.VERSION_GROUP;

	private int mPokeId;

	private final OnItemSelectedListener GAMES_SPINNER_LISTENER = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
			mData = PokedexDatabase.getInstance(getActivity()).getMovesByPokemon(mPokeId, PokedexDatabase.VERSION_GROUP_VERSION[p3]);
			displayData();
		}

		@Override
		public void onNothingSelected(AdapterView<?> p1) {
			p1.setSelection(PokedexDatabase.VERSION_GROUP);
		}


	};

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		setUsesVersionSpinner(GAMES_SPINNER_LISTENER);
		return view;
	}



	@Override
	public void setData(Pokemon data) {
		mData = PokedexDatabase.getInstance(getActivity()).getMovesByPokemon(mPokeId = data.id);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

    @Override
    public Pair<String, Integer>[] getSortOptions() {
        return new Pair[]{
			new Pair<String, Integer>("Learn \u25B2", Move.SORT_BY_LEARN_ASC),
			new Pair<String, Integer>("Learn \u25BC", Move.SORT_BY_LEARN_DES),
			new Pair<String, Integer>("Name \u25B2", Move.SORT_BY_NAME_ASC),
			new Pair<String, Integer>("Name \u25BC", Move.SORT_BY_NAME_DES),
			new Pair<String, Integer>("Type \u25B2", Move.SORT_BY_TYPE_ASC),
			new Pair<String, Integer>("Type \u25BC", Move.SORT_BY_TYPE_DES),
			new Pair<String, Integer>("Power \u25B2", Move.SORT_BY_POWER_ASC),
			new Pair<String, Integer>("Power \u25BC", Move.SORT_BY_POWER_DES),
			new Pair<String, Integer>("Accuracy \u25B2", Move.SORT_BY_ACCURACY_ASC),
			new Pair<String, Integer>("Accuracy \u25BC", Move.SORT_BY_ACCURACY_DES),
			new Pair<String, Integer>("PP \u25B2", Move.SORT_BY_PP_ASC),
			new Pair<String, Integer>("PP \u25BC", Move.SORT_BY_PP_DES),
			new Pair<String, Integer>("Priority \u25B2", Move.SORT_BY_PRIORITY_ASC),
			new Pair<String, Integer>("Priority \u25BC", Move.SORT_BY_PRIORITY_DES)

        };

    }


	
}
