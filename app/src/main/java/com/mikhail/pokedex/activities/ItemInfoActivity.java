package com.mikhail.pokedex.activities;

import android.os.*;
import android.support.v7.graphics.*;
import android.text.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.PokedexClasses.*;

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

		mPallete = Palette.generate(i.loadBitmap(this));
		
        return i;
    }
    @Override
    public void displayData(final Item curentItem) {
		
		
        mIconIV.setImageBitmap(curentItem.icon);
		mNameTV.setText(curentItem.name);

		getWindow().getDecorView().setBackgroundColor(mPallete.getLightVibrantColor(0xFFFFFF));
		
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
