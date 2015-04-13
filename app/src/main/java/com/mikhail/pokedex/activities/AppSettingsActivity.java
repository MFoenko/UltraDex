package com.mikhail.pokedex.activities;

import android.content.*;
import android.os.*;
import android.preference.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.misc.*;
import java.util.prefs.*;
import com.mikhail.pokedex.data.*;

public class AppSettingsActivity extends PreferenceActivity implements DrawerItem
{

	public static final String TITLE = "Settings";
	public static final int ICON = R.drawable.ic_settings;

	private Preference.OnPreferenceChangeListener mTimeMachinePreferenceChangeListener = new Preference.OnPreferenceChangeListener(){

		@Override
		public boolean onPreferenceChange(Preference p1, Object p2)
		{
			PokedexDatabase.deleteInstance();
			final PokedexDatabase dex = PokedexDatabase.getInstance(AppSettingsActivity.this);
			new Thread(new Runnable(){

					public void run()
					{
						try
						{
							dex.getAllPokemon();
							dex.getAllMoves();
							dex.getAllAbilities();
							dex.getAllItems();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
				return true;
		}



	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.app_settings_activity);

		findPreference("use_time_machine").setOnPreferenceChangeListener(mTimeMachinePreferenceChangeListener);

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
