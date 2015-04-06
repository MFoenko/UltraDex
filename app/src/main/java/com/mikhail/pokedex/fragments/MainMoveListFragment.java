package com.mikhail.pokedex.fragments;

import android.content.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;

public class MainMoveListFragment extends MoveListFragment<Move[]> implements DrawerItem
{
	
	public static final String DRAWER_ITEM_NAME = "Movedex";
	public static final int DRAWER_ITEM_ICON = R.drawable.ic_movedex;
	

	@Override
	public void setData(Move[] data){
		mData = data;
	}

	@Override
	public String getDrawerItemName(){
		return DRAWER_ITEM_NAME;
	}

	@Override
	public int getDrawerItemIconResourceId(){
		return DRAWER_ITEM_ICON;
	}

	@Override
	public byte getDrawerItemType(){
		return DRAWER_ITEM_TYPE_CLICKABLE;
	}

	@Override
	public boolean onDrawerItemClick(Context context){
		return true;
	}

	@Override
	public String getTitle(){
		return DRAWER_ITEM_NAME;
	}

	
	
	
	
}
