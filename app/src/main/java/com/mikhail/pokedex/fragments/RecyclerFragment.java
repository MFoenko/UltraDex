package com.mikhail.pokedex.fragments;

import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.support.v7.widget.*;

public class RecyclerFragment extends Fragment
{

	protected RecyclerView  mRecyclerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		mRecyclerView = new RecyclerView(inflater.getContext());
		mRecyclerView.setVerticalScrollBarEnabled(true);
		return mRecyclerView;
		
	}
	
}
