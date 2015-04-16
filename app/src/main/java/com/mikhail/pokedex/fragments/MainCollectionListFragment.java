package com.mikhail.pokedex.fragments;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

import com.mikhail.pokedex.data.UserDatabase;
import com.mikhail.pokedex.misc.DrawerItem;

/**
 * Created by mchail on 4/16/15.
 */
public class MainCollectionListFragment extends CollectionListFragment<UserDatabase.Collection[]> implements DrawerItem {

    public static final int ICON = DRAWER_ICON_NONE;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setData(UserDatabase.Collection[] data) {
        mData = data;
    }

    @Override
    public String getDrawerItemName() {
        return TITLE;
    }

    @Override
    public int getDrawerItemIconResourceId() {
        return ICON;
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
