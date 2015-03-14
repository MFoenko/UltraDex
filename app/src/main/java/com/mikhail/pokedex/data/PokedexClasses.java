package com.mikhail.pokedex.data;

import android.content.*;
import android.graphics.*;
import android.util.*;
import java.text.*;
import com.mikhail.pokedex.misc.*;
import com.mikhail.pokedex.activities.*;

public abstract class PokedexClasses{

	public static final String ICON_DIR = "Icons/";
	public static final String MODELS_DIR = "Models/";

	

	public static class Pokemon implements Linkable, VarComparable<Pokemon>{

		
		public final int id;
		public final int dispId;
		public final String name;
		public final String genus;
        public final int[] stats;
        public final int[] types;
		public final String suffix;
		public final int order;
		public final boolean isDefault;
		public final boolean hasUniqueIcon;

		public Bitmap icon;

		public Pokemon(Pokemon.Builder builder){
			this.id = builder.id;
			this.dispId = builder.dispId;
			this.name = builder.name;
			this.genus = builder.genus;
			this.stats = builder.stats;
			this.types = builder.types;
			this.suffix = builder.suffix;
			this.order = builder.order;
			this.isDefault = true;
			this.hasUniqueIcon = builder.hasUniqueIcon;

		}

		public Bitmap loadBitmap(Context context){
			if (icon == null){
				icon = BitmapFactory.decodeFile(context.getExternalFilesDir(null).getAbsolutePath() + "/" + ICON_DIR + getIconFileName());
			}
			return icon;
		}

		public String getIconFileName(){
			return dispId + (hasUniqueIcon ?"-" + suffix: "") + ".png";
		}

		public String getModelFileName(){
			DecimalFormat df = new DecimalFormat("000");
			String formattedId = df.format(dispId);
			return formattedId + (suffix != null ?"-" + suffix: "") + ".gif";
		}

		@Override
		public int compareTo(Pokemon other, int compareOn){
			return id-other.id;
		}

		
		

		public static class Builder{


			public int id;
			public int dispId;
			public String name;
			public String genus;
			public int[] stats;
			public int[] types;
			public String suffix;
			public int order;
			public boolean isDefault;
			public boolean hasUniqueIcon;

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

			public Builder genus(String genus){
				this.genus = genus;
				return this;
			}

			public Builder suffix(String suf){
				this.suffix = suf;
				return this;
			}

			public Builder stats(int[] stats){
				this.stats = stats;
				return this;
			}

			public Builder types(int[] types){
				this.types = types;
				return this;
			}

			public Builder hasUniqueIcon(boolean hasIcon){
				this.hasUniqueIcon = hasIcon;
				return this;
			}

			public Pokemon build(){
				return new Pokemon(this);
			}

		}

		@Override
		public int getId(){
			return id;
		}

		@Override
		public String getName(){
			return name;
		}

		@Override
		public Class<? extends InfoActivity> getInfoActivityClass(){
			return PokemonInfoActivity.class;
		}

	}


	public static class Move implements Linkable, VarComparable<Move>{

		public final int id;
		public final String name;
		public final String description;
		public final int power;
		public final int accuracy;
		public final int pp;
		public final int priority;
		public final int type;
		public final int damageClass;

		public Move(Builder builder){
			this.id = builder.id;
			this.name = builder.name;
			this.description = builder.description;
			this.power = builder.power;
			this.accuracy = builder.accuracy;
			this.pp = builder.pp;
			this.priority = builder.priority;
			this.type = builder.type;
			this.damageClass = builder.damageClass;
			
			
		}

		@Override
		public int compareTo(Move other, int compareOn){
			return name.compareTo(other.name);
		}

		
		

		public static class Builder{

			public int id;
			public String name;
			public String description;
			public int power;
			public int accuracy;
			public int pp;
			public int priority;
			public int type;
			public int damageClass;

			
			public Builder id(int id){
				this.id = id;
				return this;
			}
			public Builder name(String name){
				this.name = name;
				return this;
			}
			public Builder description(String desc){
				this.description = desc;
				return this;
			}
			public Builder power(int pow){
				this.power = pow;
				return this;
			}
			public Builder accuracy(int acc){
				this.accuracy = acc;
				return this;
			}
			public Builder pp(int pp){
				this.pp = pp;
				return this;
			}
			public Builder priority(int pri){
				this.priority = pri;
				return this;
			}
			public Builder type(int type){
				this.type = type;
				return this;
			}
			public Builder damageClass(int dClass){
				this.damageClass = dClass;
				return this;
			}

			public Move build(){
				return new Move(this);
			}

		}
		
		@Override
		public int getId(){
			return id;
		}

		@Override
		public String getName(){
			return name;
		}

		@Override
		public Class<? extends InfoActivity> getInfoActivityClass(){
			return MoveInfoActivity.class;
		}

	}

	public interface Linkable{
		
		public int getId();
		public String getName();
		public Class<? extends InfoActivity> getInfoActivityClass();
		
	}	
	
	public interface VarComparable<T>{
		public int compareTo(T other, int compareOn);
	}
	
}
