package com.mikhail.pokedex.fragments;

import android.content.*;
import android.media.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.activities.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import java.io.*;
import java.text.*;
import java.util.*;

import android.view.View.OnClickListener;

public class PokemonInfoFragment extends InfoPagerFragment<Pokemon> {

	public static final String TITLE = "Info";
	private Pokemon mPoke;
	private ArrayList<ArrayList<Evolution>> mEvolutions;
	private int[] mEvolutionsIds;
    private Pokemon[] mForms;
	int[] mFormIds;

	SoundPool mSoundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
	

	private View mLayout;

	LinearLayout treeLL;
	LinearLayout formsLL;
	TextView evolutionsHeader;
	TextView formsHeader;

	DecimalFormat mDF = new DecimalFormat("########.#");

	public static final int GENDER_BAR_TEXT_SIZE_SP = 14;
	public static final int ARROW_OPACITY = 0x40;
	StatBarView mGenderBar;

	OnClickListener mEvolutionIconClickListener = new OnClickListener(){
		@Override
		public void onClick(View p1) {
			int pos=(Integer)p1.getTag();
			Intent intent = new Intent(p1.getContext(), PokemonInfoActivity.class);
			intent.putExtra(PokemonInfoActivity.EXTRA_ID_ARRAY, mEvolutionsIds);
			intent.putExtra(PokemonInfoActivity.EXTRA_ID_INDEX, pos);
			p1.getContext().startActivity(intent);
		}
	};

	OnClickListener mFormIconClickListener = new OnClickListener(){
		@Override
		public void onClick(View p1) {
			int pos=(Integer)p1.getTag();
			Intent intent = new Intent(p1.getContext(), PokemonInfoActivity.class);
			intent.putExtra(PokemonInfoActivity.EXTRA_ID_ARRAY, mFormIds);
			intent.putExtra(PokemonInfoActivity.EXTRA_ID_INDEX, pos);
			p1.getContext().startActivity(intent);
		}
	};


	TextView dexTextTV;
	
	TextView metricHeightTV;
	TextView imperialHeightTV;
	TextView metricWeightTV;
	TextView imperialWeightTV;

	TextView expTV;
	TextView evTV;
	TextView catchTV;
	TextView happinessTV;
	TextView eggGroupTV;
	TextView stepsTV;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mLayout = inflater.inflate(R.layout.pokemon_info_fragment, container, false);

		treeLL = (LinearLayout)mLayout.findViewById(R.id.evolutions_container);
		formsLL = (LinearLayout)mLayout.findViewById(R.id.forms_container);
		evolutionsHeader = (TextView)mLayout.findViewById(R.id.evolutions_header);
		formsHeader = (TextView)mLayout.findViewById(R.id.forms_header);

		dexTextTV = (TextView)mLayout.findViewById(R.id.pokedex_text);

		metricHeightTV = (TextView)mLayout.findViewById(R.id.height_m);
		imperialHeightTV = (TextView)mLayout.findViewById(R.id.height_ft);
		metricWeightTV = (TextView)mLayout.findViewById(R.id.weight_kg);
		imperialWeightTV = (TextView)mLayout.findViewById(R.id.weight_lb);

		mGenderBar = (StatBarView)mLayout.findViewById(R.id.gender);

		expTV = (TextView)mLayout.findViewById(R.id.exp);
		evTV = (TextView)mLayout.findViewById(R.id.evs);
		catchTV = (TextView)mLayout.findViewById(R.id.catch_rate);
		happinessTV = (TextView)mLayout.findViewById(R.id.happiness);
		eggGroupTV = (TextView)mLayout.findViewById(R.id.egg);
		stepsTV = (TextView)mLayout.findViewById(R.id.steps);

		
		return mLayout;

	}




	@Override
	public void setData(Pokemon data) {
		mPoke = data;
        PokedexDatabase pokedexDatabase = PokedexDatabase.getInstance(getActivity());
		mEvolutions = pokedexDatabase.getEvolutions(data.id);
		
		int c=0;
		for (int i=0;i < mEvolutions.size();i++) {
			for (int j=0;j < mEvolutions.get(i).size();j++) {
				c++;
			}
		}

		mEvolutionsIds = new int[c];
		c = 0;

		for (int i=0;i < mEvolutions.size();i++) {
			for (int j=0;j < mEvolutions.get(i).size();j++) {
				Evolution evo = mEvolutions.get(i).get(j);
				mEvolutionsIds[c++] = evo.evolvedPoke.id;
				
			}
		}



        mForms = pokedexDatabase.getForms(mPoke.id);
        
		mFormIds = new int[mForms.length];
		for (int i=0;i < mForms.length;i++) {
			mFormIds[i] = mForms[i].id;
		}

		File sound = new File(pokedexDatabase.mContext.getExternalFilesDir(""), data.getCryFileNameNoExtension()+".ogg");
		if(!sound.exists()){
			sound = new File(pokedexDatabase.mContext.getExternalFilesDir(""), data.getCryFileNameNoExtension()+".ogg");
		}


		mSoundPool.load(sound.getAbsolutePath(), 1);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.play_cry, menu);
		menu.findItem(R.id.play_cry).setVisible(true);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.play_cry:
				mSoundPool.play(0, 1.0f, 1.0f, 1, -1, 1.0f);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}



	@Override
	public boolean displayData() {
		if (mPoke == null || mEvolutions == null || mLayout == null || mForms == null || isDetached())
			return false;

			
		PokedexDatabase pokedexDatabase = PokedexDatabase.getInstance(getActivity());
		
		new Thread(new Runnable(){

				@Override
				public void run() {
					for (ArrayList<Evolution> branch:mEvolutions) {
						for (Evolution evo:branch) {
							evo.evolvedPoke.loadBitmap(getActivity());
						}
					}
					for (Pokemon p: mForms) {
						p.loadBitmap(getActivity());
					}
					
					
				}
				
			
		}).start();
			
			
		float density;
		try {

			density = getResources().getDisplayMetrics().density;
		} catch (IllegalStateException e) {
			return false;
		}

		int iconSize =(int)(64 * density);


		int c =0;
		treeLL.removeAllViews();
		if (mEvolutions.size() == 0) {
			treeLL.setVisibility(View.GONE);
			evolutionsHeader.setVisibility(View.GONE);
		} else {
			treeLL.setVisibility(View.VISIBLE);
			evolutionsHeader.setVisibility(View.VISIBLE);
		}


		int padding = (int)(2 * density);
        for (ArrayList<Evolution> branch:mEvolutions) {

			LinearLayout branchLL = new LinearLayout(treeLL.getContext());
			branchLL.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams branchParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			branchLL.setLayoutParams(branchParams);
			branchLL.setGravity(Gravity.CENTER);

			for (Evolution evo:branch) {

				if (!evo.isBaseEvo()) {
					TextView methodTV = new TextView(branchLL.getContext());
					LayoutParams methodParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
					methodTV.setLayoutParams(methodParams);
					methodTV.setPadding(padding, padding, padding, padding);
					methodTV.setBackgroundResource(R.drawable.arrow);
					methodTV.getBackground().setAlpha(ARROW_OPACITY);
					methodTV.setGravity(Gravity.CENTER_VERTICAL);
					methodTV.setMovementMethod(LinkMovementMethod.getInstance());

					
					
					methodTV.setText(pokedexDatabase.parseLinks(evo.evolutionMethod));
					
					
					branchLL.addView(methodTV);
				}
				ImageView iconIV = new ImageView(branchLL.getContext());
				LayoutParams iconParams = new LayoutParams(iconSize, iconSize);
				iconParams.setMargins(padding, padding, padding, padding);

				iconIV.setLayoutParams(iconParams);
				iconIV.setImageBitmap(evo.evolvedPoke.icon);

				if (evo.evolvedPoke.id == mPoke.id) {
					iconIV.setBackgroundResource(R.drawable.blue_rounded_background);
				}

				iconIV.setTag(c++);
				iconIV.setClickable(true);
				iconIV.setOnClickListener(mEvolutionIconClickListener);
				branchLL.addView(iconIV);
			}

			treeLL.addView(branchLL);



		}

		formsLL.removeAllViews();
		if (mForms.length == 0) {
			formsLL.setVisibility(View.GONE);
			formsHeader.setVisibility(View.GONE);
		} else {
			formsLL.setVisibility(View.VISIBLE);
			formsHeader.setVisibility(View.VISIBLE);
		}
        for (int i=0;i < mForms.length;i++) {

			Pokemon form = mForms[i];

            ImageView iconIV = new ImageView(formsLL.getContext());
            LayoutParams iconParams = new LayoutParams(iconSize, iconSize, 1);
            iconIV.setLayoutParams(iconParams);
            iconIV.setImageBitmap(form.icon);
			iconIV.setTag(i);
			iconIV.setClickable(true);
			iconIV.setOnClickListener(mFormIconClickListener);
            formsLL.addView(iconIV);

        }

		dexTextTV.setText(pokedexDatabase.getPokemonPokedexText(mPoke.id));

		metricHeightTV.setText(mPoke.height + "m");
		int heightIn = (int)(mPoke.height * 100 / 2.54);
		imperialHeightTV.setText((heightIn / 12) + "'" + (heightIn % 12) + "\"");
		metricWeightTV.setText(mPoke.weight + "kg");
		imperialWeightTV.setText(mDF.format(mPoke.weight * 2.204) + "lbs");

		mGenderBar.resetText();
		if (mPoke.femalesPer8Males == -1) {
			mGenderBar.setColor(0xFF000000 + PokedexDatabase.GENDER_COLORS[2]);
			mGenderBar.setMax(8);
			mGenderBar.setStat(8);
			mGenderBar.setCenterText(PokedexDatabase.GENDER_NAMES[2]);
			mGenderBar.setTextSize(getResources().getDisplayMetrics().scaledDensity * GENDER_BAR_TEXT_SIZE_SP);
		} else {
			mGenderBar.setColor(0xFF000000 + PokedexDatabase.GENDER_COLORS[0], 0xFF000000 + PokedexDatabase.GENDER_COLORS[1]);
			mGenderBar.setMax(8);
			mGenderBar.setStat(mPoke.femalesPer8Males);
			float femalePercent = 100f * mPoke.femalesPer8Males / 8;
			mGenderBar.setLeftText(femalePercent + "%");
			mGenderBar.setRightText(100 - femalePercent + "%");
			mGenderBar.setTextSize(getResources().getDisplayMetrics().scaledDensity * GENDER_BAR_TEXT_SIZE_SP);
		}		

		expTV.setText(String.valueOf(mPoke.baseExperience));
		happinessTV.setText(String.valueOf(mPoke.baseHappiness));
		catchTV.setText(String.valueOf(mPoke.catchRate));
		stepsTV.setText(255 * (1 + mPoke.hatchCycles) + "-" + (255 * (1 + mPoke.hatchCycles) + 254));
		StringBuilder builder = new StringBuilder();
		for (int eGroup:mPoke.eggGroups) {
			builder.append(PokedexDatabase.EGG_GROUP_NAMES[eGroup])
				.append(" ");
		}
		eggGroupTV.setText(builder.toString());
		builder.delete(0, builder.length());
		for (int i=0;i < mPoke.evYield.length;i++) {
			if (mPoke.evYield[i] != 0) {
				builder.append(mPoke.evYield[i])
					.append(" ");
				builder.append(PokedexDatabase.STAT_LABELS[PokedexDatabase.getStatVersion()][i]);
				builder.append(", ");
			}
		}
		evTV.setText(builder.delete(builder.length() - 2, builder.length()).toString());

		return true;


	}

    @Override
    public void onStop() {
        super.onStop();
        mPoke = null;
        mEvolutions = null;
		mForms = null;
    }

    @Override
	public String getTitle() {
		return TITLE;
	}



}
