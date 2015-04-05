package com.mikhail.pokedex.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.data.PokedexDatabase;

/**
 * Created by mchail on 4/4/15.
 */
public class ItemInfoActivity extends InfoActivity<Item> {

    ImageView mIconIV;
    TextView mNameTV;
    TextView mDescriptionTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_info_activity);
        mIconIV = (ImageView)findViewById(R.id.icon);
        mNameTV = (TextView)findViewById(R.id.name);
        mDescriptionTV = (TextView)findViewById(R.id.description);
    }

    @Override
    public Item getData(int id) {
        Item i = mPokedexDatabase.getItem(id);
        i.loadBitmap(this);
        return i;
    }
    @Override
    public void displayData(Item curentItem) {
        mIconIV.setImageBitmap(curentItem.icon);
        mNameTV.setText(curentItem.name);
        mDescriptionTV.setText(curentItem.effect);
    }
}
