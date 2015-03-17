package com.mikhail.pokedex.fragments;

import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import java.util.*;

public class PokemonStatsFragment extends InfoPagerFragment<Pokemon>{

	private static final String TITLE = "Stats";
	private int[] mStats = null; 
	private int mTotal= 0;

	private StatBarView[] mStatBarViews;
	private StatBarView mTotalStatBarView;
	




	private static final int TEXT_SIZE_SP = 14;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout layout = new LinearLayout(container.getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		int statVersion = PokedexDatabase.GEN_STAT_VERSIONS[PokedexDatabase.GEN];
		int len = PokedexDatabase.STAT_LABELS[statVersion].length;
		mStatBarViews = new StatBarView[len];
		float textSize = container.getContext().getResources().getDisplayMetrics().scaledDensity * TEXT_SIZE_SP;
		for (int s=0;s < len;s++){
			mStatBarViews[s] = new StatBarView(layout.getContext());
			mStatBarViews[s].setLabel(PokedexDatabase.STAT_LABELS[statVersion][s] + ": ");
			mStatBarViews[s].setMax(PokedexDatabase.STAT_MAXES[statVersion][s]);
			mStatBarViews[s].setColor(PokedexDatabase.STAT_COLORS[statVersion][s] - 0xBB000000);
			mStatBarViews[s].setTextSize(textSize);
			mStatBarViews[s].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
			layout.addView(mStatBarViews[s]);
		}
		mTotalStatBarView = new StatBarView(layout.getContext());
		mTotalStatBarView.setLabel(PokedexDatabase.STAT_TOTAL_LABEL + ": ");
		mTotalStatBarView.setMax(PokedexDatabase.STAT_TOTAL_MAX);
		mTotalStatBarView.setColor(PokedexDatabase.STAT_TOTAL_COLOR - 0xBB000000);
		mTotalStatBarView.setTextSize(mTotalStatBarView.getContext().getResources().getDisplayMetrics().scaledDensity * TEXT_SIZE_SP);
		mTotalStatBarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		layout.addView(mTotalStatBarView);

		return layout;
	}


	@Override
	public void setData(Pokemon data){
		this.mStats = data.stats;
		mTotal = 0;
		for(int s:mStats){
			mTotal += s;
		}
	}

	public boolean displayData(){
		if (mStats == null || mStatBarViews == null){
			return false;
		}
		int len = mStatBarViews.length;
		for (int s=0;s < len;s++){
			mStatBarViews[s].setStat(mStats[s]);
		}
		mTotalStatBarView.setStat(mTotal);
		Log.i("AAA", "success");
		return true;
	}

	@Override
	public String getTitle(){
		return TITLE;
	}




}