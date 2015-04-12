package com.mikhail.pokedex.activities;

import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexClasses.Item;

/**
 *
 * Created by mchail on 4/4/15.
 */
public class ItemInfoActivity extends InfoActivity<Item> {


	View mLayout;
    ImageView mIconIV;
    TextView mNameTV;
    TextView mDescriptionTV;


	Palette mPallete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.item_info_activity);
		
        mIconIV = (ImageView)findViewById(R.id.icon);
        mNameTV = (TextView)findViewById(R.id.name);
        mDescriptionTV = (TextView)findViewById(R.id.description);
		mDescriptionTV.setMovementMethod(LinkMovementMethod.getInstance());

		
    }

    @Override
    public Item getData(int id) {
        Item i = mPokedexDatabase.getItem(id);
		try{
		mPallete = Palette.generate(i.loadBitmap(this));
		}catch(IllegalArgumentException e){}
        return i;
    }
    @Override
    public void displayData(final Item curentItem) {
		
		
        mIconIV.setImageBitmap(curentItem.icon);
		mNameTV.setText(curentItem.name);
		try{
		getWindow().getDecorView().setBackgroundColor(mPallete.getLightVibrantColor(0xFFFFFF));
		}catch(NullPointerException e){}
        mDescriptionTV.setText(curentItem.description);
		new Thread(new Runnable(){
				@Override
				public void run() { 
					final Spannable parsedLinks = mPokedexDatabase.parseLinks(curentItem.description);
					runOnUiThread(new Runnable(){
							@Override
							public void run(){
								mDescriptionTV.setText(parsedLinks);
							}
						});
				}
			}).start();
    }
}
