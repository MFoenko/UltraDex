package com.mikhail.pokedex.misc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhail.pokedex.R;

import org.w3c.dom.Text;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class DrawerItemAdapter extends BaseAdapter {

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
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItem item = (DrawerItem)getItem(position);
        ViewGroup listItemView;


        if(convertView == null) {
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
