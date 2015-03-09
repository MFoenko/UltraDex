package com.mikhail.pokedex.activities;

import android.os.*;
import android.support.v7.app.*;
import com.mikhail.pokedex.data.*;

public abstract class InfoActivity<T> extends ActionBarActivity
{
	
	protected int[] mIdArray;
	protected int mCurrentIndex;
	protected PokedexDatabase mPokedexDatabase;

	public static final String EXTRA_ID_ARRAY = "id_arr";
	public static final String EXTRA_ID_INDEX = "id_index";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			mIdArray = extras.getIntArray(EXTRA_ID_ARRAY);
			mCurrentIndex = extras.getInt(EXTRA_ID_INDEX);
		}
		
		
		mPokedexDatabase = PokedexDatabase.getInstance(this);
		loadData();
		
	}
	
	private void loadData(){
		new Thread(new Runnable(){
				@Override
				public void run(){
					final T data = getData(mIdArray[mCurrentIndex]);
					runOnUiThread(new Runnable(){
							@Override
							public void run(){
								displayData(data);
							}
					});
				}
			}).start();
		
	}
	
	public abstract T getData(int id);
	
	public abstract void displayData(T curentItem);
	
	
	
}
