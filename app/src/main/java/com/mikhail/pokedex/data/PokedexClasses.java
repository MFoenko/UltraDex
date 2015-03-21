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
		
		public int getStatTotal(){
			int total = 0;
			for(int s:stats){
				total += s;
			}
			return total;
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


		public static final int SORT_BY_DISP_ID_ASC = 1;
		public static final int SORT_BY_DISP_ID_DES = -1;
		public static final int SORT_BY_NAME_ASC = 2;
		public static final int SORT_BY_NAME_DES = -2;
		public static final int SORT_BY_TYPE_ASC = 3;
		public static final int SORT_BY_TYPE_DES = -3;
		public static final int SORT_BY_HP_ASC = 10;
		public static final int SORT_BY_HP_DES = -10;
		public static final int SORT_BY_ATTACK_ASC = 11;
		public static final int SORT_BY_ATTACK_DES = -11;
		public static final int SORT_BY_DEFENSE_ASC = 12;
		public static final int SORT_BY_DEFENSE_DES = -12;
		public static final int SORT_BY_SPECIAL_ATTACK_ASC = 13;
		public static final int SORT_BY_SPECIAL_ATTACK_DES = -13;
		public static final int SORT_BY_SPECIAL_DEFENSE_ASC = 14;
		public static final int SORT_BY_SPECIAL_DEFENSE_DES = -14;
		public static final int SORT_BY_SPEED_ASC = 15;
		public static final int SORT_BY_SPEED_DES = -15;
		public static final int SORT_BY_STAT_TOTAL_ASC = 16;
		public static final int SORT_BY_STAT_TOTAL_DES = -16;
		
		
		
		@Override
		public int compareTo(Pokemon other, int compareOn){
			
			switch(compareOn){
				case SORT_BY_DISP_ID_ASC:
					return dispId-other.dispId + (dispId==other.dispId ? id-other.id:0);
				case SORT_BY_DISP_ID_DES:
					return other.dispId-dispId + (other.dispId == dispId ? other.id-id:0);
				case SORT_BY_NAME_ASC:
					return name.compareTo(other.name);
				case SORT_BY_NAME_DES:
					return other.name.compareTo(name);
				case SORT_BY_TYPE_ASC:
					if(types[0] == other.types[0]){
						if(types.length == 2){
							if(other.types.length == 2){
								return types[1] - other.types[1];
							}else{
								return 1;
							}
						}else{
							if(other.types.length == 2){
								return -1;
							}else{
								return 0;
							}
						}
					}else{
						return types[0] - other.types[0];
					}
				case SORT_BY_TYPE_DES:
					if(types[0] != other.types[0]){
						if(types.length == 2){
							if(other.types.length == 2){
								return other.types[1] - types[1];
							}else{
								return -1;
							}
						}else{
							if(other.types.length == 2){
								return 1;
							}else{
								return 0;
							}
						}
					}else{
						return other.types[0] - types[0];
					}
				case SORT_BY_HP_ASC:
				case SORT_BY_ATTACK_ASC:
				case SORT_BY_DEFENSE_ASC:
				case SORT_BY_SPECIAL_ATTACK_ASC:
				case SORT_BY_SPECIAL_DEFENSE_ASC:
				case SORT_BY_SPEED_ASC:
					return stats[compareOn - SORT_BY_HP_ASC] - other.stats[compareOn-SORT_BY_HP_ASC];
				case SORT_BY_HP_DES:
				case SORT_BY_ATTACK_DES:
				case SORT_BY_DEFENSE_DES:
				case SORT_BY_SPECIAL_ATTACK_DES:
				case SORT_BY_SPECIAL_DEFENSE_DES:
				case SORT_BY_SPEED_DES:
					return other.stats[Math.abs(compareOn) - SORT_BY_HP_ASC] - stats[Math.abs(compareOn)-SORT_BY_HP_ASC];
				case SORT_BY_STAT_TOTAL_ASC:
					return getStatTotal() - other.getStatTotal();
				case SORT_BY_STAT_TOTAL_DES:
					return other.getStatTotal()-getStatTotal();
					
			}
			
			return 0;
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

	public static class Evolution{
		
		public Pokemon evolvedPoke;
		public String evolutionMethod;

		public Evolution(Pokemon evolvedPoke){
			this.evolvedPoke = evolvedPoke;
		}

		public Evolution(Pokemon evolvedPoke, String evolutionMethod){
			this.evolvedPoke = evolvedPoke;
			this.evolutionMethod = evolutionMethod;
		}

		public boolean isBaseEvo(){
			return evolutionMethod == null;
		}
		
		@Override
		public String toString(){
			return ">"+evolutionMethod+">"+evolvedPoke.id;
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
		
		public final int learnMethod;
		public final int level;
		

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
			this.learnMethod = builder.learnMethod;
			this.level = builder.level;
			
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
			
			public int learnMethod = -1;
			public int level = -1;
			

			
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

			public Builder learnMethod(int lMethod){
				learnMethod = lMethod;
				return this;
			}
			
			public Builder level(int lvl){
				level = lvl;
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
