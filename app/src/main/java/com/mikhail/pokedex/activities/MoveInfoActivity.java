package com.mikhail.pokedex.activities;

import android.support.v4.app.*;
import android.text.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.fragments.*;
import com.mikhail.pokedex.misc.*;

public class MoveInfoActivity extends PagerInfoActivity<Move>{


	View mLayout;
	TextView mNameTV;
	TextView mDescriptionTV;
	TypeView mTypeTV;
	TextView mDamageClassTV;
	TextView mPowerTV;
	TextView mAccuracyTV;
	TextView mPPTV;
	TextView mPriorityTV;

	private static int ICON_OPACITY = 40;
	public static final int OPACITY = 0x77000000;
	

	@Override
	public View getContentView(LayoutInflater inflater, ViewGroup container){

		mLayout = inflater.inflate(R.layout.move_info_activity_material, container, false);

		mNameTV = (TextView)mLayout.findViewById(R.id.name);
		mDescriptionTV = (TextView)mLayout.findViewById(R.id.description);
		//mTypeTV = (TypeView)content.findViewById(R.id.type);
		mDamageClassTV = (TextView)mLayout.findViewById(R.id.damage_class);
		mPowerTV = (TextView)mLayout.findViewById(R.id.power);
		mPowerTV.getBackground().setAlpha(ICON_OPACITY);
		mAccuracyTV = (TextView)mLayout.findViewById(R.id.accuracy);
		mAccuracyTV.getBackground().setAlpha(ICON_OPACITY);
		mPPTV = (TextView)mLayout.findViewById(R.id.pp);
		mPPTV.getBackground().setAlpha(ICON_OPACITY);
		mPriorityTV = (TextView)mLayout.findViewById(R.id.priority);

		mDescriptionTV.setMovementMethod(LinkMovementMethod.getInstance());
	
		
		
		return mLayout;
		
		

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

	private static int[] DAMAGE_CLASS_ICONS = new int[]{R.drawable.status, R.drawable.physical, R.drawable.special};

	@Override
	public void displayData(final Move data){
		super.displayData(data);
		
		mDrawerLayout.setBackgroundColor(OPACITY+ PokedexDatabase.TYPE_COLORS[PokedexDatabase.getTypeVersion()][data.type]);
		
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

		//mTypeTV.setType(data.type);
		mDamageClassTV.setText(PokedexDatabase.DAMAGE_CLASS_NAMES[data.damageClass]);
		mDamageClassTV.setBackgroundResource(DAMAGE_CLASS_ICONS[data.damageClass]);
		mDamageClassTV.getBackground().setAlpha(ICON_OPACITY);
		mPowerTV.setText(data.power == 0 ?"---": String.valueOf(data.power));
		mAccuracyTV.setText(data.accuracy == 0 ?"---": String.valueOf(data.accuracy));
		mPPTV.setText(data.pp == 0 ?"---": String.valueOf(data.pp));
		mPowerTV.setText(data.power == 0 ?"---": String.valueOf(data.power));
		mPriorityTV.setText(data.priority > 0 ?"+" + data.priority: String.valueOf(data.priority));

		
	}

	private static class MoveInfoPagerAdapter extends InfoFragmentPagerAdapter<Move>{

		//public static final Class<? extends InfoPagerFragment<Move>>[] FRAGMENTS = {MovePokemonListFragment.class, MoveTypingFragment.class};
		public final InfoPagerFragment<Move>[] FRAGMENTS = new InfoPagerFragment[]{new MovePokemonListFragment(), new MoveTypingFragment()};
	
		public MoveInfoPagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public int getNumFrags(){
			return 2;
		}

		@Override
		public InfoPagerFragment<Move> getFragment(int position){
			/*try{
			 return FRAGMENTS[position].newInstance();
			 }catch (InstantiationException e){}catch (IllegalAccessException e){
			 e.printStackTrace();				
			 }
			 return null;*/
				return FRAGMENTS[position];
		}


	}


}
