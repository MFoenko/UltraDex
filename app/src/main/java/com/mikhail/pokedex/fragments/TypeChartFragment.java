package com.mikhail.pokedex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.misc.*;
import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TypeChartFragment extends Fragment implements DrawerItem 
{

    public static final String TITLE = "Type Chart";
    public static final int ICON = R.drawable.ic_type_chart;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mLayout = inflater.inflate(R.layout.type_chart_fragment, container, false);
        return mLayout;
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
