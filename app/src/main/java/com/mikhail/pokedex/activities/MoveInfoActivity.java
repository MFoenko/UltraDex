package com.mikhail.pokedex.activities;

import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexClasses;
import com.mikhail.pokedex.data.PokedexClasses.Move;
import com.mikhail.pokedex.data.PokedexDatabase;
import com.mikhail.pokedex.fragments.InfoPagerFragment;
import com.mikhail.pokedex.fragments.MovePokemonListFragment;
import com.mikhail.pokedex.fragments.MoveTypingFragment;
import com.mikhail.pokedex.misc.InfoFragmentPagerAdapter;
import com.mikhail.pokedex.misc.TypeView;

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
		mTypeTV = (TypeView)mLayout.findViewById(R.id.type);
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

		mTypeTV.setType(data.type);
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
