package com.mikhail.pokedex.misc;

import android.content.Context;

/**
 * The DrawerItem interface allows the navigation drawer in MainActivity to display multiple
 * types of items that behave differently when clicked
 */
public interface DrawerItem {

    public static final int DRAWER_ICON_NONE = -1;

    /**
     * Constant that symbolizes the DrawerItems type 'headers'.
     * Headers are non-clickable items that serve to section off the drawer list
     */
    public static final byte DRAWER_ITEM_TYPE_HEADER = 1;
    /**
     * Constant that symbolizes the DrawerItems type 'clickable'.
     * Clickables are items that can be clicked on.
     */
    public static final byte DRAWER_ITEM_TYPE_CLICKABLE = 0;

    /**
     * Returns the name to display in the drawer
     * @return The name to be displayed
     */
    public String getDrawerItemName();

    /**
     * Returns the id of the drawable resource to be displayed alongside the
     * DrawerItem name in the Navigation Drawer
     * @return
     * The resource id of a drawable or -1 to not display an icon
     */
    public int getDrawerItemIconResourceId();

    /**
     * Returns the drawer item type
     * @return
     * A byte symbolizing the type of DrawerItem
     *
     */
    public byte getDrawerItemType();

    /**
     * Method invoked when a clickable item is clicked. After it is invoked, the app will attempt to
     * cast the DrawerItem to a {@link android.support.v4.app.Fragment}
     * @param context The app Context. Used by clickable DrawerItems to create a new {@Link Activity}
     * @return true if the activity should display the item as a Fragment
     *
     */
    public boolean onDrawerItemClick(Context context);



}
