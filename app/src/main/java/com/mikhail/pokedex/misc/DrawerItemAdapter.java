package com.mikhail.pokedex.misc;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class DrawerItemAdapter extends BaseAdapter {

    public DrawerItem[] drawerItems;

    public DrawerItemAdapter(DrawerItem[] drawerItems) {
        this.drawerItems = drawerItems;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
