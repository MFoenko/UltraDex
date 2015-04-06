package com.mikhail.pokedex.misc;

import android.content.*;
import android.graphics.drawable.*;
import android.util.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;

public class TypeView extends TextView{
	
	private int mTypeVersion = PokedexDatabase.GEN_TYPE_VERSIONS[PokedexDatabase.GEN];
	private int mTypeId = 0;

	public TypeView(Context context){
		super(context);
		setLines(1);
	}

	public TypeView(Context context, AttributeSet attr){
		super(context, attr);
		setLines(1);
	}

	public void setType(int typeId){
		mTypeId = typeId;
		drawableStateChanged();
	}

	public void setGen(int gen){
		mTypeVersion = PokedexDatabase.GEN_TYPE_VERSIONS[gen];
		drawableStateChanged();
	}

	public void settypeVersion(int vType){
		mTypeVersion = vType;
		drawableStateChanged();
	}

	@Override
	protected void drawableStateChanged(){
		GradientDrawable bg = (GradientDrawable)getBackground();
		if (bg == null){
			bg = (GradientDrawable)getContext().getResources().getDrawable(R.drawable.type_background);
			bg.setAlpha(0xCC);
		}
		bg.setColor(PokedexDatabase.TYPE_COLORS[mTypeVersion][mTypeId]);
		setBackgroundDrawable(bg);
		setText(PokedexDatabase.TYPE_NAMES[mTypeVersion][mTypeId]);
		super.drawableStateChanged();
	}



}
