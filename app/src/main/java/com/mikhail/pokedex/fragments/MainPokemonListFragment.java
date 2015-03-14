package com.mikhail.pokedex.fragments;
import android.content.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;

public class MainPokemonListFragment extends PokemonListFragment<Pokemon[]> implements DrawerItem
{
	
	public static final String DRAWER_ITEM_NAME = "Pokedex";
	public static final int DRAWER_ITEM_ICON = DRAWER_ICON_NONE;

	
	
	@Override
	public void setData(Pokemon[] data){
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
