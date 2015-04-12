package com.mikhail.pokedex.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexDatabase;
import com.mikhail.pokedex.fragments.AdmobBannerAd;
import com.mikhail.pokedex.fragments.CreditsFragment;
import com.mikhail.pokedex.fragments.IVCalculatorFragment;
import com.mikhail.pokedex.fragments.InfoPagerFragment;
import com.mikhail.pokedex.fragments.MainAbilityListFragment;
import com.mikhail.pokedex.fragments.MainItemListFragment;
import com.mikhail.pokedex.fragments.MainMoveListFragment;
import com.mikhail.pokedex.fragments.MainPokemonListFragment;
import com.mikhail.pokedex.fragments.NaturesFragment;
import com.mikhail.pokedex.fragments.TypeChartFragment;
import com.mikhail.pokedex.misc.CrashDialog;
import com.mikhail.pokedex.misc.DrawerHeader;
import com.mikhail.pokedex.misc.DrawerItem;
import com.mikhail.pokedex.misc.UsesRightDrawer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class MainActivity extends ActionBarActivity implements OnItemClickListener {


    public final DrawerItem[] DRAWER_ITEMS = new DrawerItem[]{
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
		new SettingsActivity(),
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
		new CreditsFragment(),
		new DrawerItem(){

			@Override
			public String getDrawerItemName()
			{
				return "Remove Ads";
			}

			@Override
			public int getDrawerItemIconResourceId()
			{
				return DRAWER_ICON_NONE;
			}

			@Override
			public byte getDrawerItemType()
			{
				// TODO: Implement this method
				return DRAWER_ITEM_TYPE_CLICKABLE;
			}

			@Override
			public boolean onDrawerItemClick(Context context)
			{

				try
				{
					Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
																   "remove_ads", "inapp", "");
				
				
				PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
				
					((Activity)context).startIntentSenderForResult(pendingIntent.getIntentSender(),
																   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
																   Integer.valueOf(0));
				}
				catch (IntentSender.SendIntentException e)
				{}
				catch (RemoteException e){
					
				}

				
				return false;
			}
			
			
			
		}

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
	IInAppBillingService mService;

	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, 
									   IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
		}
	};
	
	

	public MainActivity() {
		//Thread.setDefaultUncaughtExceptionHandler(new CrashDialog(this));
	}
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

		
		
		

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		if (requestCode == 1001) {    	
			int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
			Log.i("AAA", ""+responseCode);
			String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
			String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

			if (resultCode == RESULT_OK) {
				try {
					JSONObject jo = new JSONObject(purchaseData);
					String sku = jo.getString("productId");
					PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(AdmobBannerAd.PREF_SHOW_ADS, false).apply();
					AdmobBannerAd.showAds = false;
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
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
