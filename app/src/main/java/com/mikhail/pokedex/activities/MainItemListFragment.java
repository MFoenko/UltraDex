package com.mikhail.pokedex.activities;

import android.content.Context;

import com.mikhail.pokedex.data.PokedexClasses;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.fragments.ItemListFragment;
import com.mikhail.pokedex.misc.DrawerItem;

/**
 * Created by mchail on 4/4/15.
 */
public class MainItemListFragment extends ItemListFragment<Item[]> implements DrawerItem {

    public static final String TITLE = "Itemdex";
    public static final int DRAWER_ITEM_ICON = DRAWER_ICON_NONE;

    @Override
    public void setData(Item[] data) {
        mData = data;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getDrawerItemName() {
        return TITLE;
    }

    @Override
    public int getDrawerItemIconResourceId() {
        return DRAWER_ITEM_ICON;
    }

    @Override
    public byte getDrawerItemType() {
        return DRAWER_ITEM_TYPE_CLICKABLE;
    }

    @Override
    public boolean onDrawerItemClick(Context context) {
        return true;
    }
}
