package com.mikhail.pokedex.fragments;

import android.content.*;
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

import android.view.View.OnClickListener;

public abstract class MoveListFragment<T> extends RecyclerFragment<T, Move, MoveListFragment.MoveListAdapter.MoveViewHolder> implements UsesRightDrawer{

	@Override
	public RecyclerFragment.ListItemAdapter<PokedexClasses.Move, MoveListFragment.MoveListAdapter.MoveViewHolder> getNewAdapter(){
		return new MoveListAdapter();
	}

	@Override
	public RecyclerFragment.Filter<PokedexClasses.Move, MoveListFragment.MoveListAdapter.MoveViewHolder> getNewFilter(){
		return new MoveFilter(mAdapter);
	}

	@Override
	public View getRightDrawerLayout(LayoutInflater inflater, ViewGroup container){
		View filters = inflater.inflate(R.layout.move_list_filter, container, false);

		ViewGroup typesContainer = (ViewGroup)filters.findViewById(R.id.type_filters);
        for (int i=0;i < PokedexDatabase.TYPE_NAMES[PokedexDatabase.GEN_TYPE_VERSIONS[PokedexDatabase.GEN]].length;i++){
            View typeFilter = inflater.inflate(R.layout.type_filter, typesContainer, false);
			CheckBox checkBox = (CheckBox)typeFilter.findViewById(R.id.check_box);
			checkBox.setTag(i);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton p1, boolean p2){
						int type = p1.getTag();
						Log.e("AAA", "" + mFilter);
						((MoveFilter)mFilter).types[type] = p2;
						mFilter.filter();
					}
				});

			TypeView typeView = (TypeView)typeFilter.findViewById(R.id.type);
            typeView.setType(i);
            typesContainer.addView(typeFilter);

        }

		ViewGroup statsContainer = (ViewGroup)filters.findViewById(R.id.stat_filters);
		int len = MoveFilter.STAT_LABELS.length;
		for (int i=0;i < len;i++){
			View rangeView = inflater.inflate(R.layout.range_filter, statsContainer, false);
			((TextView)rangeView.findViewById(R.id.label)).setText(MoveFilter.STAT_LABELS[i] + ":");

			ViewGroup seekBarContainer = (ViewGroup)rangeView.findViewById(R.id.seek_bar_container);
			RangeSeekBar<Integer> bar = new RangeSeekBar<Integer>(
				MoveFilter.STAT_MINS[i],
				MoveFilter.STAT_MAXES[i],
				container.getContext(),
				0xFF000000 + PokedexDatabase.STAT_TOTAL_COLOR
			);
			((TextView)rangeView.findViewById(R.id.values)).setText(bar.getSelectedMinValue() + " - " + bar.getSelectedMaxValue());

			bar.setNotifyWhileDragging(true);
			bar.setTag(i);
			bar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>(){
					@Override
					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue){
						((TextView)((ViewGroup)bar.getParent().getParent()).findViewById(R.id.values)).setText(minValue + " - " + maxValue);
						int i = bar.getTag();
						((MoveFilter)mFilter).stats[0][i] = minValue;
						((MoveFilter)mFilter).stats[1][i] = maxValue;
						mFilter.filter();
					}
				});
			seekBarContainer.addView(bar);
			statsContainer.addView(rangeView);
		}


		return filters;
	}






	protected static class MoveListAdapter extends ListItemAdapter<Move, MoveListAdapter.MoveViewHolder>{


		@Override
		public MoveListFragment.MoveListAdapter.MoveViewHolder onCreateViewHolder(ViewGroup p1, int p2){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			View moveView = inflater.inflate(R.layout.move_list_item, p1, false);
			MoveViewHolder holder = new MoveViewHolder(moveView);
			moveView.setOnClickListener(holder);
			return holder;
		}

		@Override
		public void onBindViewHolder(MoveListFragment.MoveListAdapter.MoveViewHolder p1, int p2){
			Move m = listItems.get(p2);
			p1.nameTV.setText(m.name);
			p1.typeTV.setType(m.type);
			if (m.learnMethod != -1){
				p1.learnTV.setVisibility(View.VISIBLE);
				if (m.learnMethod == 0 && m.level == 0){
					p1.learnTV.setText("Start");
				}else{
					p1.learnTV.setText(PokedexDatabase.MOVE_METHOD_LABELS[m.learnMethod] + (m.level == 0 ? "" : " " + m.level));
				}
			}
		}

		public int[] getIdArray(){


			int[] idArray = new int[listItems.size()];
			int len = idArray.length;
			for (int i=0;i < len;i++){
				idArray[i] = listItems.get(i).id;
			}
			return idArray;
		}

		public class MoveViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

			public final TextView learnTV;
			public final TextView nameTV;
			public final TypeView typeTV;
			public final ImageView classIV;


			public MoveViewHolder(View v){
				super(v);
				learnTV = (TextView)v.findViewById(R.id.learn_method);
				nameTV = (TextView)v.findViewById(R.id.name);
				typeTV = (TypeView)v.findViewById(R.id.type);
				classIV = (ImageView)v.findViewById(R.id.damage_class);
			}


			@Override
			public void onClick(View p1){
				Intent intent = new Intent(p1.getContext(), MoveInfoActivity.class);
				intent.putExtra(MoveInfoActivity.EXTRA_ID_ARRAY, getIdArray());
				intent.putExtra(MoveInfoActivity.EXTRA_ID_INDEX, getPosition());
				p1.getContext().startActivity(intent);
			}

		}

	}

	private static class MoveFilter extends Filter<Move, MoveListAdapter.MoveViewHolder>{

		public boolean[] types = new boolean[PokedexDatabase.TYPE_NAMES[PokedexDatabase.GEN_TYPE_VERSIONS[PokedexDatabase.GEN]].length];
		/* Power, Accuracy, PP, Priority*/
		public static final String[] STAT_LABELS = {"Power", "Accuracy", "PP", "Priority"};


		public static final int MIN = 0;
		public static final int MAX = 1;

		public static final int POWER = 0;
		public static final int ACCURACY = 1;
		public static final int PP = 2;
		public static final int PRIORITY = 3;


		public static final int[] STAT_MINS = {0,0,0,-7};
		public static final int[] STAT_MAXES = {200,100,40,7};

		public int[][] stats = {STAT_MINS, STAT_MAXES};

		public MoveFilter(ListItemAdapter<Move, MoveListAdapter.MoveViewHolder> adapter){
			super(adapter);
		}

		@Override
		public boolean isMatchFilter(PokedexClasses.Move item){
			return isMatchSearch(item) && isMatchType(item) && isMatchStat(item);
		}



		@Override
		public boolean isMatchSearch(PokedexClasses.Move item){
			return item.name.toLowerCase().indexOf(search.toLowerCase()) > -1 || item.description.toLowerCase().indexOf(search.toLowerCase()) > -1;
		}

		public boolean isMatchType(Move item){
			if (sumItems(types) == 0)
				return true;
			return types[item.type];

		}

		public boolean isMatchStat(Move item){
			for (int i=0;i < STAT_LABELS.length;i++){
				int stat = 0;
				switch (i){
					case POWER:
						stat = item.power;
						break;
					case ACCURACY:
						stat = item.accuracy;
						break;
					case PP:
						stat = item.pp;
						break;
					case PRIORITY:
						stat = item.priority;
						break;
				}
				if (stat > stats[MAX][i] || stat < stats[MIN][i]){
					return false;
				}

			}
			return true;
		}

		private static int sumItems(boolean[] arr){
			int sum = 0;
			for (boolean b:arr){
				sum += (b ?1: 0);
			}
			return sum;
		}

	}


}
