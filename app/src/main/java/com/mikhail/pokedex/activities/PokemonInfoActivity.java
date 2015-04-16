package com.mikhail.pokedex.activities;


import android.graphics.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.fragments.*;
import com.mikhail.pokedex.misc.*;
import java.io.*;

public class PokemonInfoActivity extends PagerInfoActivity<Pokemon> {

	
	View mContent;
	View mAbilities;

	TextView mNameTV;
	TextView mGenusTV;

	WebView mModelWV;
	TypeView mType1TV;
	TypeView mType2TV;
	
	ModelTouchListener mModelListener;

	PokemonAbilityListFragment abilitiesListFragment;

	public static final int DEFAULT_PAGE = 2;
	public static final int OPACITY = 0x77000000;

	@Override
	public View getContentView(LayoutInflater inflater, ViewGroup container) {

		mContent = inflater.inflate(R.layout.pokemon_info_activity, container, false);
		mModelWV = (WebView)mContent.findViewById(R.id.model);
		mNameTV = (TextView)mContent.findViewById(R.id.name);
		mGenusTV = (TextView)mContent.findViewById(R.id.genus);
		mType1TV = (TypeView)mContent.findViewById(R.id.type_1);
		mType2TV = (TypeView)mContent.findViewById(R.id.type_2);

		abilitiesListFragment = new PokemonAbilityListFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.abilities, abilitiesListFragment).commit();
		mAbilities = mContent.findViewById(R.id.abilities);
		mAbilities.setBackgroundColor(Color.TRANSPARENT);
		mModelWV.setBackgroundColor(Color.TRANSPARENT);
		mModelWV.setHorizontalScrollBarEnabled(false);
		mModelWV.setVerticalScrollBarEnabled(false);
		mModelWV.setOnTouchListener(mModelListener = new ModelTouchListener());

				
			
			
		return mContent;
	}


	@Override
	public InfoFragmentPagerAdapter getNewAdapter() {
		return new PokemonInfoViewPagerAdapter(getSupportFragmentManager());
	}

	@Override
	public int getDefaultPage() {
		return DEFAULT_PAGE;
	}

	@Override
	public Pokemon getData(int id) {

		return mPokedexDatabase.getPokemon(id);
	}

	@Override
	public void displayData(Pokemon currentItem) {

		super.displayData(currentItem);
		/*int color = 0;
		 TypedValue a = new TypedValue();
		 getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
		 if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT){
		 // windowBackground is a color
		 color = a.data;
		 }*/
		int color = OPACITY + PokedexDatabase.TYPE_COLORS[PokedexDatabase.getTypeVersion()][currentItem.types[0]];

		mDrawerLayout.setBackgroundColor(color);

		mModelListener.setCurentItem(currentItem);
		mModelListener.displayModel();

		mNameTV.setText(currentItem.name);
		mGenusTV.setText(currentItem.genus + " Pokemon");
		mType1TV.setType(currentItem.types[0]);
		if (currentItem.types.length > 1) {
			mType2TV.setVisibility(View.VISIBLE);
			mType2TV.setType(currentItem.types[1]);
		} else {
			mType2TV.setVisibility(View.INVISIBLE);
		}

		abilitiesListFragment.setData(currentItem);
		abilitiesListFragment.displayData();

	}


	private static class PokemonInfoViewPagerAdapter extends InfoFragmentPagerAdapter<Move> {

		public final InfoPagerFragment<Pokemon>[] pages = new InfoPagerFragment[]{new PokemonTypingFragment(), new PokemonStatsFragment(), new PokemonInfoFragment() ,new PokemonMoveListFragment()};

		public PokemonInfoViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getNumFrags() {
			return 4;
		}

		@Override
		public InfoPagerFragment getFragment(int position) {
			return pages[position];
		}



	}

	private class ModelTouchListener implements OnTouchListener{
		boolean isShiny = false;
		long timeDown;

		Pokemon currentItem;

		private static final long LONG_TOUCH_DURATION = 750;

		public void setCurentItem(Pokemon curentItem) {
			this.currentItem = curentItem;
		}

		@Override
		public boolean onTouch(View p1, MotionEvent p2) {
			switch (p2.getAction()) {
				case MotionEvent.ACTION_DOWN:
					timeDown = System.currentTimeMillis();
					return true;
				case MotionEvent.ACTION_UP:
					if (System.currentTimeMillis() - timeDown >= LONG_TOUCH_DURATION) {

						
						
					} else {
						isShiny = !isShiny;
						displayModel();
					}

			}
			return false;
		}

		public void displayModel(){
			String gifUrl;
			if (isShiny) {
				gifUrl = PokedexClasses.SHINY_GIF_URL + currentItem.getShinyModelFileName();
				//Log.d("AAA",gifUrl);
			} else {
				gifUrl = "file://" + getExternalFilesDir(null) + "/" + PokedexClasses.MODELS_DIR + currentItem.getModelFileName();
				File f = new File(getExternalFilesDir(null) + "/" + PokedexClasses.MODELS_DIR + currentItem.getModelFileName());
				/*if(!f.exists()){
					Log.e("AAA", f.getAbsolutePath());
				}else{
					Log.i("AAA", f.getAbsolutePath());
				}*/
			}
			
			mModelWV.loadDataWithBaseURL("", "<body style='text-align:center'><img src='" + gifUrl + "'/></body>", "text/html", "UTF-8", null);

		}

		
	}


}
