package com.mikhail.pokedex.activities;

import android.os.*;
import android.webkit.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.*;

public class PokemonInfoActivity extends InfoActivity<Pokemon>
{
	
	
	
	TextView mNameTV;
	WebView mModelWV;
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pokemon_info_activity);
		
		mModelWV = (WebView)findViewById(R.id.model);
		mNameTV = (TextView)findViewById(R.id.name);
		
	}

	@Override
	public Pokemon getData(int id){
		return mPokedexDatabase.getPokemon(id);
	}

	@Override
	public void displayData(Pokemon curentItem){
		
		/*
		
			<img src=gifurl/>
		
		*/
		String gifUrl = "file://"+getExternalFilesDir(null)+"/"+PokedexClasses.MODELS_DIR+curentItem.getModelFileName();
		//mModelWV.loadDataWithBaseURL(null, "<img src="+gifUrl+"/>","text/html",null, null);
		mModelWV.loadDataWithBaseURL(null, gifUrl,"text/html",null, null);
		mModelWV.loadUrl(gifUrl);
		mNameTV.setText(curentItem.name);
		
	}


	
	
	
	
}
