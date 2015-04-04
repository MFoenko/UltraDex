package com.mikhail.pokedex.fragments;

import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import android.content.*;

public class MainAbilityListFragment extends AbilityListFragment<Ability[]> implements DrawerItem
{
	
	public static final String TITLE = "AbilityDex";
	public static final int DRAWER_ITEM_ICON = DRAWER_ICON_NONE;
	
	@Override
	public void setData(Ability[] data){
		mData = data;
	}

	@Override
	public String getTitle(){
		return TITLE;
	}
	
	@Override
	public String getDrawerItemName(){
		return TITLE;
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
	
	
}
