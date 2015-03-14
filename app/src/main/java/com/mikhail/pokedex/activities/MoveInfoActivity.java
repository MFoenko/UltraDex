package com.mikhail.pokedex.activities;

import android.os.*;
import android.support.v4.view.*;
import android.text.*;
import android.text.method.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import com.mikhail.pokedex.fragments.*;
import android.support.v4.app.*;
import android.view.*;

public class MoveInfoActivity extends PagerInfoActivity<Move>{


	TextView mNameTV;
	TextView mDescriptionTV;
	TypeView mTypeTV;
	TextView mDamageClassTV;
	TextView mPowerTV;
	TextView mAccuracyTV;
	TextView mPPTV;
	TextView mProrityTV;

	@Override
	public View getContentView(LayoutInflater inflater, ViewGroup container){
		
		View content = inflater.inflate(R.layout.move_info_activity,container, false);
		
		mNameTV = (TextView)content.findViewById(R.id.name);
		mDescriptionTV = (TextView)content.findViewById(R.id.description);
		mTypeTV = (TypeView)content.findViewById(R.id.type);
		mDamageClassTV = (TextView)content.findViewById(R.id.damage_class);
		mPowerTV = (TextView)content.findViewById(R.id.power);
		mAccuracyTV = (TextView)content.findViewById(R.id.accuracy);
		mPPTV = (TextView)content.findViewById(R.id.pp);
		mProrityTV = (TextView)content.findViewById(R.id.priority);

		mDescriptionTV.setMovementMethod(LinkMovementMethod.getInstance());
		return content;

	}

	@Override
	public InfoFragmentPagerAdapter<PokedexClasses.Move> getNewAdapter(){
		return new MoveInfoPagerAdapter(getSupportFragmentManager());
	}

	@Override
	public int getDefaultPage(){
		return 0;
	}


	@Override
	public Move getData(int id){
		return mPokedexDatabase.getMove(id);
		
	}

	@Override
	public void displayData(final Move data){
		super.displayData(data);
		mNameTV.setText(data.name);
		mDescriptionTV.setText(data.description);

		new Thread(new Runnable(){
				@Override
				public void run(){
					final Spannable parsedLinks = mPokedexDatabase.parseLinks(data.description);
					runOnUiThread(new Runnable(){
							@Override
							public void run(){
								mDescriptionTV.setText(parsedLinks);
							}
						});
				}
			}).start();

		mTypeTV.setType(data.type);
		mDamageClassTV.setText(PokedexDatabase.DAMAGE_CLASS_NAMES[data.damageClass]);
		mPowerTV.setText(data.power == 0 ?"---": String.valueOf(data.power));
		mAccuracyTV.setText(data.accuracy == 0 ?"---": String.valueOf(data.accuracy));
		mPPTV.setText(data.pp == 0 ?"---": String.valueOf(data.pp));
		mPowerTV.setText(data.power == 0 ?"---": String.valueOf(data.power));
		mProrityTV.setText(data.priority > 0 ?"+" + data.priority: String.valueOf(data.priority));
	}

	private static class MoveInfoPagerAdapter extends InfoFragmentPagerAdapter<Move>{

	public static final Class<? extends InfoPagerFragment<Move>>[] FRAGMENTS = {MovePokemonListFragment.class, MoveTypingFragment.class};

		public MoveInfoPagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public int getNumFrags(){
			return FRAGMENTS.length;
		}

		@Override
		public InfoPagerFragment<Move> getFragment(int position){
			try{
				return FRAGMENTS[position].newInstance();
			}catch (InstantiationException e){}catch (IllegalAccessException e){
				e.printStackTrace();				
			}
			return null;
		}
		

	}


}
