package com.mikhail.pokedex.fragments;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.activities.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import java.util.*;

import android.view.View.OnClickListener;

public class PokemonInfoFragment extends InfoPagerFragment<Pokemon>{

	public static final String TITLE = "Info";
	private Pokemon mPoke;
	private ArrayList<ArrayList<Evolution>> mEvolutions;
	private int[] mEvolutionsIds;
    private Pokemon[] mForms;

	private View mLayout;

	LinearLayout treeLL;
	LinearLayout formsLL;
	
	OnClickListener mEvolutionIconClickListener = new OnClickListener(){
		@Override
		public void onClick(View p1){
			int pos=(Integer)p1.getTag();
			Intent intent = new Intent(p1.getContext(), PokemonInfoActivity.class);
			intent.putExtra(PokemonInfoActivity.EXTRA_ID_ARRAY, mEvolutionsIds);
			intent.putExtra(PokemonInfoActivity.EXTRA_ID_INDEX, pos);
			p1.getContext().startActivity(intent);
		}
	};
	
	
	
	TextView metricHeightTV;
	TextView imperialHeightTV;
	TextView metricWeightTV;
	TextView imperialWeightTV;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);

		mLayout = inflater.inflate(R.layout.pokemon_info_fragment, container, false);

		treeLL = (LinearLayout)mLayout.findViewById(R.id.evolutions_container);
		formsLL = (LinearLayout)mLayout.findViewById(R.id.forms_container);
		
		metricHeightTV = (TextView)mLayout.findViewById(R.id.height_m);
		imperialHeightTV = (TextView)mLayout.findViewById(R.id.height_ft);
		metricWeightTV = (TextView)mLayout.findViewById(R.id.weight_kg);
		imperialWeightTV = (TextView)mLayout.findViewById(R.id.weight_lb);
		
		
		return mLayout;

	}




	@Override
	public void setData(Pokemon data){
		mPoke = data;
        PokedexDatabase pokedexDatabase = PokedexDatabase.getInstance(getActivity());
		mEvolutions = pokedexDatabase.getEvolutions(data.id);
		/*for (ArrayList<Evolution> branch:mEvolutions){
			for (Evolution evo:branch){
				evo.evolvedPoke.loadBitmap(getActivity());
			}
		}*/
		int c=0;
		for(int i=0;i<mEvolutions.size();i++){
			for(int j=0;j<mEvolutions.get(i).size();j++){
				c++;
			}
		}
		mEvolutionsIds = new int[c];
		c=0;
		
		for(int i=0;i<mEvolutions.size();i++){
			for(int j=0;j<mEvolutions.get(i).size();j++){
				Evolution evo = mEvolutions.get(i).get(j);
				 mEvolutionsIds[c++] = evo.evolvedPoke.id;
				 evo.evolvedPoke.loadBitmap(getActivity());
			}
		}
		
		
		
        mForms = pokedexDatabase.getForms(mPoke.id);
        for (Pokemon p: mForms){
            p.loadBitmap(getActivity());
        }
	}

	@Override
	public boolean displayData(){
		if (mPoke == null || mEvolutions == null || mLayout == null || mForms == null || isDetached())
			return false;


		float density;
		try{

			density = getResources().getDisplayMetrics().density;
		}catch (IllegalStateException e){
			return false;
		}

		int iconSize =(int)(64*density);


		int c =0;
		treeLL.removeAllViews();
        for (ArrayList<Evolution> branch:mEvolutions){

			LinearLayout branchLL = new LinearLayout(treeLL.getContext());
			branchLL.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams branchParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			branchLL.setLayoutParams(branchParams);

			for (Evolution evo:branch){

				if (!evo.isBaseEvo()){
					TextView methodTV = new TextView(branchLL.getContext());
					LayoutParams methodParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
					methodTV.setLayoutParams(methodParams);
					methodTV.setText(evo.evolutionMethod);
					branchLL.addView(methodTV);
				}
				ImageView iconIV = new ImageView(branchLL.getContext());
				LayoutParams iconParams = new LayoutParams(iconSize,iconSize);
				iconIV.setLayoutParams(iconParams);
				iconIV.setImageBitmap(evo.evolvedPoke.icon);
				iconIV.setTag(c++);
				iconIV.setClickable(true);
				iconIV.setOnClickListener(mEvolutionIconClickListener);
				branchLL.addView(iconIV);
			}

			treeLL.addView(branchLL);



		}


        for (Pokemon form:mForms){

            ImageView iconIV = new ImageView(formsLL.getContext());
            LayoutParams iconParams = new LayoutParams(iconSize, iconSize, 1);
            iconIV.setLayoutParams(iconParams);
            iconIV.setImageBitmap(form.icon);
            formsLL.addView(iconIV);

        }
		
		
		metricHeightTV.setText(mPoke.height + "m");
		int heightIn = (int)(mPoke.height*100/2.54);
		imperialHeightTV.setText((heightIn/12)+"'"+(heightIn%12)+"\"");
		metricWeightTV.setText(mPoke.weight+"kg");
		imperialWeightTV.setText(mPoke.weight*2.204+"lbs");

		return true;
	}

    @Override
    public void onStop(){
        super.onStop();
        mPoke = null;
        mEvolutions = null;
		mForms = null;
    }

    @Override
	public String getTitle(){
		return TITLE;
	}

	
	
}
