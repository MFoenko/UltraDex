package com.mikhail.pokedex.fragments;

import android.app.*;
import android.content.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.activities.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;

public abstract class AbilityListFragment<T> extends RecyclerFragment<T, Ability, AbilityListFragment.AbilityListAdapter.AbilityViewHolder>{

	public static final String TITLE = "Abilities";
	
	@Override
	public RecyclerFragment.ListItemAdapter<Ability, AbilityListFragment.AbilityListAdapter.AbilityViewHolder> getNewAdapter(){
		return new AbilityListAdapter();
	}

	@Override
	public RecyclerFragment.Filter<Ability, AbilityListFragment.AbilityListAdapter.AbilityViewHolder> getNewFilter(Activity a){
		return new AbilityFilter(mAdapter, a);
	}

	@Override
	public Pair<String, Integer>[] getSortOptions(){
		return new Pair[]{
			new Pair<String, Integer>("Name \u25B2", Pokemon.SORT_BY_NAME_ASC),
			new Pair<String, Integer>("Name \u25BC", Pokemon.SORT_BY_NAME_DES),
		};
	}


	public static class AbilityFilter extends RecyclerFragment.Filter<Ability, AbilityListFragment.AbilityListAdapter.AbilityViewHolder>{

		public AbilityFilter(ListItemAdapter<Ability, AbilityListAdapter.AbilityViewHolder> adapter, Activity a){
			super(adapter, a);
		}
		
		@Override
		public boolean isMatchSearch(PokedexClasses.Ability item){
			return true;
		}

		
	}
	

	
	
	public static class AbilityListAdapter extends RecyclerFragment.ListItemAdapter<Ability, AbilityListAdapter.AbilityViewHolder>{

		@Override
		public void onBindViewHolder(AbilityListFragment.AbilityListAdapter.AbilityViewHolder p1, int p2){
			Ability a = listItems.get(p2);
			p1.mNameTV.setText(a.name);
		}

		@Override
		public AbilityListFragment.AbilityListAdapter.AbilityViewHolder onCreateViewHolder(ViewGroup p1, int p2){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			View listItemView = inflater.inflate(R.layout.ability_list_item, p1, false);
			AbilityViewHolder viewHolder = new AbilityViewHolder(listItemView);
			
			return viewHolder;			
		}




		

		public class AbilityViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

			public final TextView mNameTV;
			
			public AbilityViewHolder(View v){
				super(v);
				v.setOnClickListener(this);
				mNameTV = (TextView)v.findViewById(R.id.name);
			}
			
			@Override
			public void onClick(View p1){
				Intent intent = new Intent(p1.getContext(), AbilityInfoActivity.class);
				intent.putExtra(PokemonInfoActivity.EXTRA_ID_ARRAY, getIdArray());
				intent.putExtra(PokemonInfoActivity.EXTRA_ID_INDEX, this.getPosition());
				p1.getContext().startActivity(intent);
			}

		}
	}

	@Override
	public String getTitle(){
		return TITLE;
	}


	
	
}
