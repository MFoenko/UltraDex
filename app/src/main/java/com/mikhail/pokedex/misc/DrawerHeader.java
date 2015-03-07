package com.mikhail.pokedex.misc;

import android.content.Context;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class DrawerHeader implements DrawerItem{

    private String itemName;


    public DrawerHeader(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getDrawerItemName() {
        return itemName;
    }

    @Override
    public int getDrawerItemIconResourceId() {
        return DRAWER_ICON_NONE;
    }

    @Override
    public byte getDrawerItemType() {
        return DRAWER_ITEM_TYPE_HEADER;
    }

    @Override
    public boolean onDrawerItemClick(Context context) {
        return false;
    }
}
