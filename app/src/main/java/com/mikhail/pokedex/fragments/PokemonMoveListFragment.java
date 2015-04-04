package com.mikhail.pokedex.fragments;

import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import android.widget.AdapterView.*;

public class PokemonMoveListFragment extends MoveListFragment<Pokemon>
{

	public static final String TITLE = "Moves";
	
	public int versionGroup = PokedexDatabase.VERSION_GROUP;
	
	private Pokemon mPoke;

	private final OnItemSelectedListener GAMES_SPINNER_LISTENER = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
			mData = PokedexDatabase.getInstance(getActivity()).getMovesByPokemon(mPoke.id, PokedexDatabase.VERSION_GROUP_VERSION[p3]);
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
		
		Spinner gameSpinner = (Spinner)view.findViewById(R.id.item_list_spinner);
		gameSpinner.setVisibility(View.VISIBLE);
		gameSpinner.setAdapter(new GamesAdapter());
		gameSpinner.setOnItemSelectedListener(GAMES_SPINNER_LISTENER);
		gameSpinner.setSelection(PokedexDatabase.VERSION_GROUP);
		return view;
	}
	
	
	
	@Override
	public void setData(Pokemon data){
		mPoke = data;
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


	private static class GamesAdapter extends BaseAdapter {

		public static String[] GAMES = PokedexDatabase.VERSION_GROUP_NAMES;
		
		@Override
		public int getCount() {
			return GAMES.length;
		}

		@Override
		public Object getItem(int p1) {
			return GAMES[p1];
		}

		@Override
		public long getItemId(int p1) {
			return p1;
		}

		@Override
		public View getView(int p1, View p2, ViewGroup p3) {
			TextView gameTV;
			if(p2 == null){
				gameTV = new TextView(p3.getContext());
				gameTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			}else{
				gameTV = (TextView)p2;
			}
			gameTV.setText(GAMES[p1]);
			return gameTV;
		}
		
		
		
		
	}
	

}
