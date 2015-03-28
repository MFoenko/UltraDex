package com.mikhail.pokedex.fragments;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.activities.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import java.text.*;
import java.util.*;

import android.view.View.OnClickListener;
import com.mikhail.pokedex.fragments.RecyclerFragment.*;

public abstract class PokemonListFragment<T> extends RecyclerFragment<T, Pokemon, PokemonListFragment.PokemonListAdapter.PokemonViewHolder> implements UsesRightDrawer{


	public LoadIconsTask task;

	@Override
	public RecyclerFragment.ListItemAdapter<Pokemon, PokemonListFragment.PokemonListAdapter.PokemonViewHolder> getNewAdapter(){
		return new PokemonListAdapter();

	}

	@Override
	public Pair<String, Integer>[] getSortOptions(){
		return new Pair[]{
			new Pair<String, Integer>("ID \u25B2", Pokemon.SORT_BY_DISP_ID_ASC),
			new Pair<String, Integer>("ID \u25BC", Pokemon.SORT_BY_DISP_ID_DES),
			new Pair<String, Integer>("Name \u25B2", Pokemon.SORT_BY_NAME_ASC),
			new Pair<String, Integer>("Name \u25BC", Pokemon.SORT_BY_NAME_DES),
			new Pair<String, Integer>("Type \u25B2", Pokemon.SORT_BY_TYPE_ASC),
			new Pair<String, Integer>("Type \u25BC", Pokemon.SORT_BY_TYPE_DES),
			new Pair<String, Integer>("HP \u25B2", Pokemon.SORT_BY_HP_ASC),
			new Pair<String, Integer>("HP \u25BC", Pokemon.SORT_BY_HP_DES),
			new Pair<String, Integer>("Attack \u25B2", Pokemon.SORT_BY_ATTACK_ASC),
			new Pair<String, Integer>("Attack \u25BC", Pokemon.SORT_BY_ATTACK_DES),
			new Pair<String, Integer>("Defense \u25B2", Pokemon.SORT_BY_DEFENSE_ASC),
			new Pair<String, Integer>("Defense \u25BC", Pokemon.SORT_BY_DEFENSE_DES),
			new Pair<String, Integer>("Sp. Attack \u25B2", Pokemon.SORT_BY_SPECIAL_ATTACK_ASC),
			new Pair<String, Integer>("Sp. Attack \u25BC", Pokemon.SORT_BY_SPECIAL_ATTACK_DES),
			new Pair<String, Integer>("Sp. Defense \u25B2", Pokemon.SORT_BY_SPECIAL_DEFENSE_ASC),
			new Pair<String, Integer>("Sp. Defense \u25BC", Pokemon.SORT_BY_SPECIAL_DEFENSE_DES),
			new Pair<String, Integer>("Speed \u25B2", Pokemon.SORT_BY_SPEED_ASC),
			new Pair<String, Integer>("Speed \u25BC", Pokemon.SORT_BY_SPEED_DES),
			new Pair<String, Integer>("Total \u25B2", Pokemon.SORT_BY_STAT_TOTAL_ASC),
			new Pair<String, Integer>("Total \u25BC", Pokemon.SORT_BY_STAT_TOTAL_DES),
			

		};

	}








	@Override
	public boolean displayData(){
		if (!super.displayData()){
			return false;
		}
		task = new LoadIconsTask(getActivity());
		task.execute(mData);
		return true;
	}

	@Override
	public RecyclerFragment.Filter<PokedexClasses.Pokemon, PokemonListFragment.PokemonListAdapter.PokemonViewHolder> getNewFilter(){
		return new PokemonFilter(mAdapter);
	}

	@Override
	public View getRightDrawerLayout(LayoutInflater inflater, ViewGroup container){
		View filters = inflater.inflate(R.layout.pokemon_list_filter, container, false);
		ViewGroup typesContainer = (ViewGroup)filters.findViewById(R.id.type_filters);
		for (int i=0;i < PokedexDatabase.TYPE_NAMES[PokedexDatabase.GEN_TYPE_VERSIONS[PokedexDatabase.GEN]].length;i++){
			View typeFilter = inflater.inflate(R.layout.type_filter, typesContainer, false);
			CheckBox checkBox = (CheckBox)typeFilter.findViewById(R.id.check_box);
			checkBox.setTag(i);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton p1, boolean p2){
						int type = (Integer)p1.getTag();
						Log.e("AAA", "" + mFilter);
						((PokemonFilter)mFilter).types[type] = p2;
						mFilter.filter();
					}
				});

			TypeView typeView = (TypeView)typeFilter.findViewById(R.id.type);
			typeView.setType(i);
			typesContainer.addView(typeFilter);
		}

		ViewGroup statsContainer = (ViewGroup)filters.findViewById(R.id.stat_filters);
		int statVersion = PokedexDatabase.GEN_STAT_VERSIONS[PokedexDatabase.GEN];
		for (int i=0;i < PokedexDatabase.STAT_LABELS[statVersion].length;i++){
			View rangeView = inflater.inflate(R.layout.range_filter, statsContainer, false);
			((TextView)rangeView.findViewById(R.id.label)).setText(PokedexDatabase.STAT_LABELS[statVersion][i] + ":");

			ViewGroup seekBarContainer = (ViewGroup)rangeView.findViewById(R.id.seek_bar_container);
			RangeSeekBar<Integer> bar = new RangeSeekBar<Integer>(
				PokedexDatabase.STAT_MINS[statVersion][i],
				PokedexDatabase.STAT_MAXES[statVersion][i],
				container.getContext(),
				0xFF000000 + PokedexDatabase.STAT_COLORS[statVersion][i]
			);
			((TextView)rangeView.findViewById(R.id.values)).setText(bar.getSelectedMinValue() + " - " + bar.getSelectedMaxValue());

			bar.setTag(i);
			bar.setNotifyWhileDragging(true);
			bar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>(){
					@Override
					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue){
						((TextView)((ViewGroup)bar.getParent().getParent()).findViewById(R.id.values)).setText(minValue + " - " + maxValue);
						int i = (Integer)bar.getTag();
						((PokemonFilter)mFilter).stats[0][i] = minValue;
						((PokemonFilter)mFilter).stats[1][i] = maxValue;
						mFilter.filter();
					}
				});
			seekBarContainer.addView(bar);
			statsContainer.addView(rangeView);

		}






		View rangeView = inflater.inflate(R.layout.range_filter, statsContainer, false);
		((TextView)rangeView.findViewById(R.id.label)).setText(PokedexDatabase.STAT_TOTAL_LABEL + ":");

		ViewGroup seekBarContainer = (ViewGroup)rangeView.findViewById(R.id.seek_bar_container);
		RangeSeekBar<Integer> bar = new RangeSeekBar<Integer>(
			PokedexDatabase.STAT_TOTAL_MIN,
			PokedexDatabase.STAT_TOTAL_MAX,
			container.getContext(),
			0xFF000000 + PokedexDatabase.STAT_TOTAL_COLOR
		);
		((TextView)rangeView.findViewById(R.id.values)).setText(bar.getSelectedMinValue() + " - " + bar.getSelectedMaxValue());

		bar.setNotifyWhileDragging(true);
		bar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>(){
				@Override
				public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue){
					((TextView)((ViewGroup)bar.getParent().getParent()).findViewById(R.id.values)).setText(minValue + " - " + maxValue);
					((PokemonFilter)mFilter).total[0] = minValue;
					((PokemonFilter)mFilter).total[1] = maxValue;
					mFilter.filter();
				}
			});
		seekBarContainer.addView(bar);
		statsContainer.addView(rangeView);





		return filters;
	}






	protected static class PokemonListAdapter extends RecyclerFragment.ListItemAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder>{

		DecimalFormat df = new DecimalFormat("000");


		@Override
		public void onBindViewHolder(PokemonListAdapter.PokemonViewHolder p1, int p2){
			Pokemon p = listItems.get(p2);
			p1.name.setText(p.name);
			p1.id.setText(df.format(p.dispId));
			p1.icon.setImageBitmap(p.icon);
			addExtras(p1.extra, p);
		}

		private void addExtras(ViewGroup extrasView, Pokemon p){

			boolean startFresh = ((Integer)extrasView.getTag()) != sortBy && (Math.abs(((Integer)extrasView.getTag())) < 10 || Math.abs(sortBy) < 10);
			if (startFresh){
				extrasView.removeAllViews();
				if(Math.abs(sortBy) >= 10){
					TextView tv = new TextView(extrasView.getContext());
					tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
					tv.setGravity(Gravity.CENTER);
					extrasView.addView(tv);
				}
			}
			
			
			switch (Math.abs(sortBy)){
				case Pokemon.SORT_BY_TYPE_ASC:
					if (startFresh){
						for (int i=0;i < 2;i++){
							TypeView typeView = new TypeView(extrasView.getContext());
							
							extrasView.addView(typeView);
							
						}
					}
					for (int i=0;i < 2;i++){

						TypeView typeView = (TypeView)extrasView.getChildAt(i);
						if (i == 0 || (i == 1 && p.types.length > 1)){
							typeView.setType(p.types[i]);
							typeView.setVisibility(View.VISIBLE);
						}else{
							typeView.setVisibility(View.GONE);
						}
					}
					break;
				case Pokemon.SORT_BY_HP_ASC:
				case Pokemon.SORT_BY_ATTACK_ASC:
				case Pokemon.SORT_BY_DEFENSE_ASC:
				case Pokemon.SORT_BY_SPECIAL_ATTACK_ASC:
				case Pokemon.SORT_BY_SPECIAL_DEFENSE_ASC:
				case Pokemon.SORT_BY_SPEED_ASC:
					TextView tv = (TextView)extrasView.getChildAt(0);
					tv.setBackgroundColor(0xBB000000 + PokedexDatabase.STAT_COLORS[1][Math.abs(sortBy)-Pokemon.SORT_BY_HP_ASC]);
					tv.setText(String.valueOf(p.stats[Math.abs(sortBy)-Pokemon.SORT_BY_HP_ASC]));
					break;
				case Pokemon.SORT_BY_STAT_TOTAL_ASC:
					TextView totalTv = (TextView)extrasView.getChildAt(0);
					totalTv.setBackgroundColor(0xBB000000 + PokedexDatabase.STAT_TOTAL_COLOR);
					totalTv.setText(String.valueOf(p.getStatTotal()));
					
										
			}			
			extrasView.setTag(sortBy);

		}

		@Override
		public PokemonViewHolder onCreateViewHolder(ViewGroup p1, int p2){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			View view = inflater.inflate(R.layout.pokemon_list_item, p1, false);
			view.findViewById(R.id.extra).setTag(0);
			PokemonViewHolder holder = new PokemonViewHolder(view);
			view.setOnClickListener(holder);
			return holder;
		}

		public int[] getIdArray(){
			int[] idArray = new int[listItems.size()];
			int len = idArray.length;
			for (int i=0;i < len;i++){
				idArray[i] = listItems.get(i).id;
			}
			return idArray;
		}
		

		public class PokemonViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

			public final ImageView icon;
			public final TextView id;
			public final TextView name;
			public final ViewGroup extra;

			public PokemonViewHolder(View view){
				super(view);
				this.icon = (ImageView)view.findViewById(R.id.icon);
				this.id = (TextView)view.findViewById(R.id.id);
				this.name = (TextView)view.findViewById(R.id.name);
				this.extra = (ViewGroup)view.findViewById(R.id.extra);
			}

			@Override
			public void onClick(View p1){
				Intent intent = new Intent(p1.getContext(), PokemonInfoActivity.class);
				intent.putExtra(PokemonInfoActivity.EXTRA_ID_ARRAY, getIdArray());
				intent.putExtra(PokemonInfoActivity.EXTRA_ID_INDEX, this.getPosition());
				p1.getContext().startActivity(intent);
			}
		}

	}
	private static class PokemonFilter extends Filter<Pokemon, PokemonListAdapter.PokemonViewHolder>{

		public boolean[] types = new boolean[PokedexDatabase.TYPE_NAMES[PokedexDatabase.GEN_TYPE_VERSIONS[PokedexDatabase.GEN]].length];
		private int len = PokedexDatabase.STAT_MINS[PokedexDatabase.GEN_STAT_VERSIONS[PokedexDatabase.GEN]].length;
		public int[][] stats = {Arrays.copyOf(PokedexDatabase.STAT_MINS[PokedexDatabase.GEN_STAT_VERSIONS[PokedexDatabase.GEN]], len), Arrays.copyOf(PokedexDatabase.STAT_MAXES[PokedexDatabase.GEN_STAT_VERSIONS[PokedexDatabase.GEN]], len)};
		public int[] total = {PokedexDatabase.STAT_TOTAL_MIN, PokedexDatabase.STAT_TOTAL_MAX};
		public final static int MIN = 0;
		public final static int MAX = 1;


		public PokemonFilter(ListItemAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder> adapter){
			super(adapter);
		}

		@Override
		public boolean isMatchFilter(PokedexClasses.Pokemon item){
			return isMatchSearch(item) && isMatchType(item) && isMatchStat(item);
		}

		public boolean isMatchStat(Pokemon item){
			for (int s=0;s < len;s++){
				int stat = item.stats[s];
				if (stat > stats[MAX][s] || stat < stats[MIN][s]){
					return false;
				}
			}
			int sumStat = sumItems(item.stats);
			if (sumStat > total[MAX] || sumStat < total[MIN])
				return false;

			return true;
		}


		@Override
		public boolean isMatchSearch(PokedexClasses.Pokemon item){
			return item.getName().toLowerCase().indexOf(search.toString().toLowerCase()) > -1 || String.valueOf(item.getId()).indexOf(search.toString()) > -1; 
		}

		public boolean isMatchType(Pokemon item){
			int sum = sumItems(types);
			if (sum == 0) return true;

			if (sum == 2){
				if (item.types.length != 2){
					return false;
				}
				for (int t:item.types){
					if (!types[t]){
						return false;
					}
				}
				return true;
			}else{
				for (int t:item.types){
					if (types[t]){
						return true;
					}
				}
				return false;
			}
		}



		private static int sumItems(int[] arr){
			int total=0;
			for (int i:arr){
				total += i;
			}
			return total;
		}


		private static int sumItems(boolean[] arr){
			int sum = 0;
			for (boolean b:arr){
				sum += (b ?1: 0);
			}
			return sum;
		}

	}



	private class LoadIconsTask extends AsyncTask<Pokemon ,Integer, Void>{

		Context con;

		public LoadIconsTask(Context con){
			this.con = con;
		}

		@Override
		protected Void doInBackground(Pokemon[] p1){

			int len = p1.length;
			for (int i=0;i < len;i++){
				Pokemon p = p1[i];
				p.loadBitmap(con);
				publishProgress(i);
				if (isCancelled()){
					break;
				}

			}

			return null;

		}

		@Override
		protected void onProgressUpdate(Integer[] values){
			super.onProgressUpdate(values);
			mAdapter.notifyItemChanged(values[0]);
		}
	}


}
