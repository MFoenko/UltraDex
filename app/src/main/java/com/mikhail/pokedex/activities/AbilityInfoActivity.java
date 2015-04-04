package com.mikhail.pokedex.activities;
import android.support.v4.app.*;
import android.text.*;
import android.text.method.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.fragments.*;
import com.mikhail.pokedex.misc.*;

public class AbilityInfoActivity extends PagerInfoActivity<Ability>
{

	TextView mNameTV;
	TextView mDescriptionTV;
	
	
	@Override
	public Ability getData(int id){
		return mPokedexDatabase.getAbility(id);
	}
	
	@Override
	public View getContentView(LayoutInflater inflater, ViewGroup container){
		View mLayout = inflater.inflate(R.layout.ability_info_activity, container, false);
		mNameTV = (TextView)mLayout.findViewById(R.id.name);
		mDescriptionTV = (TextView)mLayout.findViewById(R.id.description);
		
		mDescriptionTV.setMovementMethod(LinkMovementMethod.getInstance());
		
		
		return mLayout;
	}
	

	@Override
	public void displayData(final Ability curentItem){
		super.displayData(curentItem);
		mNameTV.setText(curentItem.name);
		mDescriptionTV.setText(curentItem.effect);
		new Thread(new Runnable(){
				@Override
				public void run(){
					final Spannable parsedLinks = mPokedexDatabase.parseLinks(curentItem.effect);
					runOnUiThread(new Runnable(){
							@Override
							public void run(){
								mDescriptionTV.setText(parsedLinks);
							}
						});
				}
			}).start();
	}

	
	
	@Override
	public int getDefaultPage(){
		return 0;
	}

	@Override
	public InfoFragmentPagerAdapter getNewAdapter(){
		return new AbilityInfoViewPagerAdapter(getSupportFragmentManager());
	}

	
	public static class AbilityInfoViewPagerAdapter extends InfoFragmentPagerAdapter<Ability>{

		
		public final InfoPagerFragment<Ability>[] FRAGMENTS = new InfoPagerFragment[]{new AbilityPokemonListFragment()};
		
		
		public AbilityInfoViewPagerAdapter(FragmentManager fm){
			super(fm);
		}
		
		@Override
		public int getNumFrags(){
			return 1;
		}

		@Override
		public InfoPagerFragment<Ability> getFragment(int position){
			// TODO: Implement this method
			return FRAGMENTS[position];
		}

		
	}
	
	
	
}
