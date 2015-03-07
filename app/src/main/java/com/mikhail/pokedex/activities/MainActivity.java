package com.mikhail.pokedex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;


import com.mikhail.pokedex.R;
import com.mikhail.pokedex.misc.DrawerHeader;
import com.mikhail.pokedex.misc.DrawerItem;
import com.mikhail.pokedex.misc.DrawerItemAdapter;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class MainActivity extends Activity {


    public static final DrawerItem[] DRAWER_ITEMS = new DrawerItem[]{new DrawerHeader("swag"), new DrawerHeader("entry 0")};

    DrawerLayout mDrawerLayout;
    ListView mLeftDrawer;
    DrawerItemAdapter mLeftDrawerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (ListView) findViewById(R.id.left_drawer);
        mLeftDrawer.setAdapter(mLeftDrawerAdapter = new DrawerItemAdapter(DRAWER_ITEMS));
        
    }
}
