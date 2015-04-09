package com.mikhail.pokedex.activities;

import android.content.res.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.fragments.*;
import com.mikhail.pokedex.misc.*;

import android.support.v7.app.ActionBarDrawerToggle;
import java.util.*;
import android.content.*;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class MainActivity extends ActionBarActivity implements OnItemClickListener {


    public static final DrawerItem[] DRAWER_ITEMS = new DrawerItem[]{
		new DrawerHeader("Dexes"),
		new MainPokemonListFragment(),
		new MainMoveListFragment(),
		new MainAbilityListFragment(),
		new MainItemListFragment(),
		new DrawerHeader("Tools"),
		new TypeChartFragment(),
		new NaturesFragment(),
		new IVCalculatorFragment(),
		new DrawerHeader("App"),
		new DrawerItem(){

			@Override
			public String getDrawerItemName() {
				return "Bug Report";
			}

			@Override
			public int getDrawerItemIconResourceId() {
				return R.drawable.ic_bug_report;
			}

			@Override
			public byte getDrawerItemType() {
				return DRAWER_ITEM_TYPE_CLICKABLE;
			}

			@Override
			public boolean onDrawerItemClick(Context context) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, CrashDialog.DEV_EMAIL);
				intent.putExtra(Intent.EXTRA_SUBJECT, "Ultadex 3.0b Bug");
				intent.putExtra(Intent.EXTRA_TEXT, "Describe the bug here");

				context.startActivity(Intent.createChooser(intent, "Send Email"));
				return false;
			}


		},
		new CreditsFragment()

    };
	public static final int POKEDEX_FRAGMENT = 1;
	public static final int MOVEDEX_FRAGMENT = 2;
    public static final int ABILITYDEX_FRAGMENT = 3;
    public static final int ITEMDEX_FRAGMENT = 4;

	public static final int DEFAULT_INDEX = POKEDEX_FRAGMENT;

	public static final String KEY_FRAG = "frag_selecred";

    DrawerLayout mDrawerLayout;
    ListView mLeftDrawer;
	ViewGroup mRightDrawer;
    DrawerItemAdapter mLeftDrawerAdapter;
	ActionBarDrawerToggle mLeftDrawerToggle;

	public MainActivity() {
		Thread.setDefaultUncaughtExceptionHandler(new CrashDialog(this));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (ListView) findViewById(R.id.left_drawer);
        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);
        mLeftDrawer.setAdapter(mLeftDrawerAdapter = new DrawerItemAdapter(DRAWER_ITEMS));
        mLeftDrawer.setOnItemClickListener(this);
		mLeftDrawer.setDividerHeight(0);
		mDrawerLayout.setDrawerListener(mLeftDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.left_drawer_open, R.string.left_drawer_closed){

										});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);



		onItemClick(null, null, DEFAULT_INDEX, 0);

    }

	@Override
	protected void onStart() {
		super.onStart();

        PokedexDatabase pokedexDatabase = PokedexDatabase.getInstance(this);

		((MainPokemonListFragment)DRAWER_ITEMS[POKEDEX_FRAGMENT]).loadData(pokedexDatabase.getAllPokemon());
		((MainMoveListFragment)DRAWER_ITEMS[MOVEDEX_FRAGMENT]).loadData(pokedexDatabase.getAllMoves());
		((MainAbilityListFragment)DRAWER_ITEMS[ABILITYDEX_FRAGMENT]).loadData(pokedexDatabase.getAllAbilities());
        ((MainItemListFragment)DRAWER_ITEMS[ITEMDEX_FRAGMENT]).loadData(pokedexDatabase.getAllItems());

	}

	@Override
	protected void onResume() {
		//Log.i("AAA", Arrays.toString(DRAWER_ITEMS));


		super.onResume();
	}



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_FRAG, mLeftDrawer.getSelectedItemPosition());
    }

    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		onItemClick(null, null, savedInstanceState.getInt(KEY_FRAG), 0);
	}





	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

		if (DRAWER_ITEMS[p3].onDrawerItemClick(this)) {
			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().replace(R.id.content_view, (Fragment)DRAWER_ITEMS[p3]).commit();
			getSupportActionBar().setTitle(DRAWER_ITEMS[p3].getDrawerItemName());
			mLeftDrawer.setItemChecked(p3, true);
			if (DRAWER_ITEMS[p3] instanceof UsesRightDrawer) {
				mRightDrawer.removeAllViews();
				View filters = ((UsesRightDrawer)DRAWER_ITEMS[p3]).getRightDrawerLayout(getLayoutInflater(), mRightDrawer);
				if (filters.getParent() != null) {
					((ViewGroup)filters.getParent()).removeAllViews();
				}
				mRightDrawer.addView(filters);
			}

			mDrawerLayout.setDrawerLockMode(
				(DRAWER_ITEMS[p3] instanceof UsesRightDrawer
				? DrawerLayout.LOCK_MODE_UNLOCKED
				: DrawerLayout.LOCK_MODE_LOCKED_CLOSED), mRightDrawer);

			if (DRAWER_ITEMS[p3] instanceof InfoPagerFragment)
				((InfoPagerFragment)DRAWER_ITEMS[p3]).displayData();
			

		}

		mDrawerLayout.closeDrawers();

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mLeftDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mLeftDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mLeftDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}


		return super.onOptionsItemSelected(item);

	}






	public static class DrawerItemAdapter extends BaseAdapter {

		public DrawerItem[] drawerItems;

		public DrawerItemAdapter(DrawerItem[] drawerItems) {
			this.drawerItems = drawerItems;
		}


		@Override
		public int getCount() {
			return drawerItems.length;
		}

		@Override
		public Object getItem(int position) {
			return drawerItems[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			return drawerItems[position].getDrawerItemType();
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			DrawerItem item = (DrawerItem)getItem(position);
			ViewGroup listItemView;
			switch (getItemViewType(position)) {

				case DrawerItem.DRAWER_ITEM_TYPE_CLICKABLE:
					if (convertView == null) {
						LayoutInflater inflater = LayoutInflater.from(parent.getContext());
						listItemView = (ViewGroup)inflater.inflate(R.layout.drawer_list_item, parent, false);
					} else {
						listItemView = (ViewGroup)convertView;
					}

					TextView itemName = (TextView)listItemView.findViewById(R.id.name);
					itemName.setText(item.getDrawerItemName());
					if (item.getDrawerItemIconResourceId() != DrawerItem.DRAWER_ICON_NONE) {
						ImageView iconIV = (ImageView)listItemView.findViewById(R.id.icon);

						iconIV.setImageResource(item.getDrawerItemIconResourceId());
					}
					return listItemView;

				case DrawerItem.DRAWER_ITEM_TYPE_HEADER:
					if (convertView == null) {
						LayoutInflater inflater = LayoutInflater.from(parent.getContext());
						listItemView = (ViewGroup)inflater.inflate(R.layout.drawer_header_itemi, parent, false);
					} else {
						listItemView = (ViewGroup)convertView;
					}

					TextView headerName = (TextView)listItemView.findViewById(R.id.header);
					headerName.setText(item.getDrawerItemName());
					return listItemView;

			}
			return convertView;
		}

	}

}
