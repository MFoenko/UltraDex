package com.mikhail.pokedex.fragments;

import android.os.*;
import android.preference.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.misc.*;
import android.content.*;

public class AppSettingsActivity extends PreferenceActivity implements DrawerItem
{
	
	public static final String TITLE = "Settings";
	public static final int ICON = R.drawable.ic_settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_settings_activity);
	}

	

	@Override
	public String getDrawerItemName()
	{
		return TITLE;
	}

	@Override
	public int getDrawerItemIconResourceId()
	{
		return ICON;
	}

	@Override
	public byte getDrawerItemType()
	{
		return DRAWER_ITEM_TYPE_CLICKABLE;
	}

	@Override
	public boolean onDrawerItemClick(Context context)
	{
		Intent intent = new Intent(context, AppSettingsActivity.class);
		context.startActivity(intent);
		return false;
	}
	

	
	
}
