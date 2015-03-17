package com.mikhail.pokedex.activities;

import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.fragments.*;
import com.mikhail.pokedex.misc.*;

public class PokemonInfoActivity extends PagerInfoActivity<Pokemon>{

	TextView mNameTV;
	TextView mGenusTV;

	WebView mModelWV;
	TypeView mType1TV;
	TypeView mType2TV;

	public static final int DEFAULT_PAGE = 1;


	@Override
	public View getContentView(LayoutInflater inflater, ViewGroup container){

		View content = inflater.inflate(R.layout.pokemon_info_activity, container, false);
		mModelWV = (WebView)content.findViewById(R.id.model);
		mNameTV = (TextView)content.findViewById(R.id.name);
		mGenusTV = (TextView)content.findViewById(R.id.genus);
		mType1TV = (TypeView)content.findViewById(R.id.type_1);
		mType2TV = (TypeView)content.findViewById(R.id.type_2);
		return content;
	}





	@Override
	public InfoFragmentPagerAdapter getNewAdapter(){
		return new PokemonInfoViewPagerAdapter(getSupportFragmentManager());
	}

	@Override
	public int getDefaultPage(){
		return DEFAULT_PAGE;
	}

	@Override
	public Pokemon getData(int id){
		return mPokedexDatabase.getPokemon(id);
	}

	@Override
	public void displayData(Pokemon currentItem){

		super.displayData(currentItem);
		String gifUrl = "file://" + getExternalFilesDir(null) + "/" + PokedexClasses.MODELS_DIR + currentItem.getModelFileName();

		int color = 0;
		TypedValue a = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
		if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT){
			// windowBackground is a color
			color = a.data;
		}
		color -= 0xff000000;
		mModelWV.loadDataWithBaseURL("", "<body style='background-color:#" + Integer.toHexString(color) + ";text-align:center'><img src='" + gifUrl + "'/></body>", "text/html", "UTF-8", null);
		mModelWV.setHorizontalScrollBarEnabled(false);
		mModelWV.setVerticalScrollBarEnabled(false);
		mNameTV.setText(currentItem.name);
		mGenusTV.setText(currentItem.genus + " Pokemon");
		mType1TV.setType(currentItem.types[0]);
		if (currentItem.types.length > 1){
			mType2TV.setVisibility(View.VISIBLE);
			mType2TV.setType(currentItem.types[1]);
		}else{
			mType2TV.setVisibility(View.INVISIBLE);
		}

	}


	private static class PokemonInfoViewPagerAdapter extends InfoFragmentPagerAdapter<Move>{

		public static final InfoPagerFragment<Pokemon>[] pages = new InfoPagerFragment[]{new PokemonMoveListFragment(), new PokemonStatsFragment(), new PokemonTypingFragment()};

		public PokemonInfoViewPagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public int getNumFrags(){
			return 3;
		}

		@Override
		public InfoPagerFragment getFragment(int position){
				return pages[position];
		}

	}



}
