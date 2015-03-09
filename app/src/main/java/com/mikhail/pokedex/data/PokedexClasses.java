package com.mikhail.pokedex.data;

import android.content.*;
import android.graphics.*;
import android.util.*;
import java.text.*;

public class PokedexClasses{

	public static final String ICON_DIR = "Icons/";
	public static final String MODELS_DIR = "Models/";


	public static class Pokemon{

		public final int id;
		public final int dispId;
		public final String name;
        public final int[] stats;
        public final int[] types;
		public final String suffix;
		public final int order;
		public final boolean isDefault;
		public final boolean hasIcon;

		public Bitmap icon;

		public Pokemon(Pokemon.Builder builder){
			this.id = builder.id;
			this.dispId = builder.dispId;
			this.name = builder.name;
			this.stats = builder.stats;
			this.types = builder.types;
			this.suffix = builder.suffix;
			this.order = builder.order;
			this.isDefault = true;
			this.hasIcon = suffix != null;

		}

		public Pokemon(int id, int dispId, String name, int[] stats, int[] type, String suffix, int order){
			this.id = id;
			this.dispId = dispId;
			this.name = name;
			this.stats = stats;
			this.types = type;
			this.suffix = suffix;
			this.order = order;
			this.isDefault = true;
			this.hasIcon = suffix != null;
		}

		public Bitmap loadBitmap(Context context){
			if (icon == null){
				icon = BitmapFactory.decodeFile(context.getExternalFilesDir(null).getAbsolutePath() + "/" + ICON_DIR + getIconFileName());
			}
			return icon;
		}

		public String getIconFileName(){
			return dispId + (hasIcon ?"-" + suffix: "") + ".png";
		}

		public String getModelFileName(){
			DecimalFormat df = new DecimalFormat("000");
			String formattedId = df.format(dispId);
			return formattedId + (suffix != null ?"-" + suffix: "") + ".gif";
		}

		public static class Builder{


			public int id;
			public int dispId;
			public String name;
			public int[] stats;
			public int[] types;
			public String suffix;
			public int order;
			public boolean isDefault;
			public boolean hasIcon;

			public Builder id(int id){
				this.id = id;
				return this;
			}

			public Builder dispId(int id){
				this.dispId = id;
				return this;
			}

			public Builder name(String name){
				this.name = name;
				return this;
			}
			public Builder suffix(String suf){
				this.suffix = suf;
				return this;
			}

			public Pokemon build(){
				return new Pokemon(this);
			}




		}

	}

}
