package com.mikhail.pokedex.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.misc.*;

public class CreditsFragment extends Fragment implements DrawerItem
{
	public static final String TITLE = "Credits";
	public static final int ICON = R.drawable.ic_credits;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.credits, container, false);
	}

	
	@Override
	public String getDrawerItemName() {
		return TITLE;
	}

	@Override
	public int getDrawerItemIconResourceId() {
		return ICON;
	}

	@Override
	public byte getDrawerItemType() {
		return DRAWER_ITEM_TYPE_CLICKABLE;
	}

	@Override
	public boolean onDrawerItemClick(Context context) {
		return true;
	}


	
	
}
