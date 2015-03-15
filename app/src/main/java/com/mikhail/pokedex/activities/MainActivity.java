package com.mikhail.pokedex.activities;

import android.os.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.fragments.*;
import com.mikhail.pokedex.misc.*;

import android.support.v7.app.ActionBarDrawerToggle;
import android.content.res.*;
import com.mikhail.pokedex.data.*;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class MainActivity extends ActionBarActivity implements OnItemClickListener{


    public static final DrawerItem[] DRAWER_ITEMS = new DrawerItem[]{new MainPokemonListFragment(), new MainMoveListFragment()};
	public static final int DEFAULT_INDEX = 0;
	public static final int POKEDEX_FRAGMENT = 0;
	public static final int MOVEDEX_FRAGMENT = 1;


    DrawerLayout mDrawerLayout;
    ListView mLeftDrawer;
	ViewGroup mRightDrawer;
    DrawerItemAdapter mLeftDrawerAdapter;
	ActionBarDrawerToggle mLeftDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

		((MainPokemonListFragment)DRAWER_ITEMS[POKEDEX_FRAGMENT]).loadData(PokedexDatabase.getInstance(this).getAllPokemon());
		((MainMoveListFragment)DRAWER_ITEMS[MOVEDEX_FRAGMENT]).loadData(PokedexDatabase.getInstance(this).getAllMoves());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (ListView) findViewById(R.id.left_drawer);
        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);
        mLeftDrawer.setAdapter(mLeftDrawerAdapter = new DrawerItemAdapter(DRAWER_ITEMS));
        mLeftDrawer.setOnItemClickListener(this);
		mDrawerLayout.setDrawerListener(mLeftDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.left_drawer_open, R.string.left_drawer_closed){

										});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		onItemClick(null, null, DEFAULT_INDEX, 0);

    }

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){

		if (DRAWER_ITEMS[p3].onDrawerItemClick(this)){
			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().replace(R.id.content_view, (Fragment)DRAWER_ITEMS[p3]).commit();
			getSupportActionBar().setTitle(DRAWER_ITEMS[p3].getDrawerItemName());
			mLeftDrawer.setItemChecked(p3, true);
			if (DRAWER_ITEMS[p3] instanceof UsesRightDrawer){

				mRightDrawer.addView(((UsesRightDrawer)DRAWER_ITEMS[p3]).getRightDrawerLayout(getLayoutInflater()));
			}
			mDrawerLayout.setDrawerLockMode(
				DRAWER_ITEMS[p3] instanceof UsesRightDrawer
				? DrawerLayout.LOCK_MODE_UNLOCKED
				: DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		

	}

	mDrawerLayout.closeDrawers();

}

@Override
protected void onPostCreate(Bundle savedInstanceState){
super.onPostCreate(savedInstanceState);
mLeftDrawerToggle.syncState();
}

@Override
public void onConfigurationChanged(Configuration newConfig){
super.onConfigurationChanged(newConfig);
mLeftDrawerToggle.onConfigurationChanged(newConfig);
}

@Override
public boolean onOptionsItemSelected(MenuItem item){
if(mLeftDrawerToggle.onOptionsItemSelected(item)){
return true;
}


return super.onOptionsItemSelected(item);

}






public static class DrawerItemAdapter extends BaseAdapter{

	public DrawerItem[] drawerItems;

	public DrawerItemAdapter(DrawerItem[] drawerItems){
		this.drawerItems = drawerItems;
	}


	@Override
	public int getCount(){
		return drawerItems.length;
	}

	@Override
	public Object getItem(int position){
		return drawerItems[position];
	}

	@Override
	public long getItemId(int position){
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		DrawerItem item = (DrawerItem)getItem(position);
		ViewGroup listItemView;


		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			listItemView = (ViewGroup)inflater.inflate(R.layout.drawer_list_item, parent, false);
		}else{
			listItemView = (ViewGroup)convertView;
		}

		TextView itemName = (TextView)listItemView.findViewById(R.id.name);
		itemName.setText(item.getDrawerItemName());

		return listItemView;
	}

	}

}
