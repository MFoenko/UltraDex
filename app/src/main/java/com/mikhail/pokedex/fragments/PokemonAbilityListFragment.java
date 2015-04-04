package com.mikhail.pokedex.fragments;

import android.view.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.fragments.RecyclerFragment.*;
import com.mikhail.pokedex.fragments.AbilityListFragment.AbilityListAdapter.*;
import android.os.*;

public class PokemonAbilityListFragment extends AbilityListFragment<Pokemon>
{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	
	
	@Override
	public void setData(Pokemon data){
		mData = PokedexDatabase.getInstance(getActivity()).getAbilitiesByPokemon(data.id);
	}

	@Override
	public RecyclerFragment.ListItemAdapter<PokedexClasses.Ability, AbilityListFragment.AbilityListAdapter.AbilityViewHolder> getNewAdapter(){
		return new PokemonAbilityListAdapter();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		mScrollerView.setVisibility(View.GONE);
	}

	
	
	
	private static class PokemonAbilityListAdapter extends AbilityListAdapter{

		@Override
		public AbilityListFragment.AbilityListAdapter.AbilityViewHolder onCreateViewHolder(ViewGroup p1, int p2){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			return new AbilityViewHolder(inflater.inflate(R.layout.ability_list_item_small, p1, false));
			
			
		}
		
		
		
	}
}
