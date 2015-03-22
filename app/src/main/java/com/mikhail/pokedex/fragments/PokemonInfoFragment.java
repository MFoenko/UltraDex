package com.mikhail.pokedex.fragments;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import java.util.*;

public class PokemonInfoFragment extends InfoPagerFragment<Pokemon>{

	public static final String TITLE = "Info";
	private Pokemon mPoke;
	private ArrayList<ArrayList<Evolution>> mEvolutions;
    private Pokemon[] mForms;

	private View mLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);

		return mLayout = inflater.inflate(R.layout.pokemon_info_fragment, container, false);

	}




	@Override
	public void setData(Pokemon data){
		mPoke = data;
        PokedexDatabase pokedexDatabase = PokedexDatabase.getInstance(getActivity());
		mEvolutions = pokedexDatabase.getEvolutions(data.id);
		for (ArrayList<Evolution> branch:mEvolutions){
			for (Evolution evo:branch){
				evo.evolvedPoke.loadBitmap(getActivity());
			}
		}
        mForms = pokedexDatabase.getForms(mPoke.id);
        for(Pokemon p: mForms){
            p.loadBitmap(getActivity());
        }
	}

	@Override
	public boolean displayData(){
		if (mPoke == null || mEvolutions == null || mLayout == null|| mForms == null)
			return false;

		LinearLayout treeLL = (LinearLayout)mLayout.findViewById(R.id.evolutions_container);
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
				float density = getResources().getDisplayMetrics().density;
				LayoutParams iconParams = new LayoutParams((int)(64*density), (int)(64*density));
				iconIV.setLayoutParams(iconParams);
				iconIV.setImageBitmap(evo.evolvedPoke.icon);
				branchLL.addView(iconIV);
			}
			
			treeLL.addView(branchLL);

		}

        LinearLayout formsLL = (LinearLayout)mLayout.findViewById(R.id.forms_container);

        for(Pokemon form:mForms){

            ImageView iconIV = new ImageView(formsLL.getContext());
            float density = getResources().getDisplayMetrics().density;
            LayoutParams iconParams = new LayoutParams((int)(64*density), (int)(64*density),1);
            iconIV.setLayoutParams(iconParams);
            iconIV.setImageBitmap(form.icon);
            formsLL.addView(iconIV);

        }

		return true;

	}

    @Override
    public void onStop() {
        super.onStop();
        mPoke = null;
        mEvolutions = null;
    }

    @Override
	public String getTitle(){
		return TITLE;
	}







}
