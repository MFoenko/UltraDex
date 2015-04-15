package com.mikhail.pokedex.misc;

import android.util.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.data.*;

public class GamesAdapter extends BaseAdapter
{

	public String[] mGames;

	public GamesAdapter()
	{
		mGames = new String[PokedexDatabase.VERSION_GROUP + 1];
		for (int i=0;i < mGames.length;i++)
		{
			mGames[i] = PokedexDatabase.VERSION_GROUP_NAMES[i];
		}

	}

	@Override
	public int getCount()
	{
		return mGames.length;
	}

	@Override
	public Object getItem(int p1)
	{
		return mGames[p1];
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		TextView gameTV;
		if (p2 == null)
		{
			gameTV = new TextView(p3.getContext());
			gameTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		}
		else
		{
			gameTV = (TextView)p2;
		}
		gameTV.setText(mGames[p1]);
		return gameTV;
	}

}
