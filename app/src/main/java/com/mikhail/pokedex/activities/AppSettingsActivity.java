package com.mikhail.pokedex.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.misc.DrawerItem;

public class AppSettingsActivity extends PreferenceActivity implements DrawerItem
{
	
	public static final String TITLE = "Settings";
	public static final int ICON = R.drawable.ic_settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.app_settings_activity);
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
