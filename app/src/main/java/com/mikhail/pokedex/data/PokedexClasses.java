package com.mikhail.pokedex.data;

import android.content.*;
import android.graphics.*;

import java.text.*;

import com.mikhail.pokedex.activities.*;

public abstract class PokedexClasses {

	public static final String SHINY_GIF_URL = "http://mchail.byethost4.com/shiny/";
	
	public static final String POKEMON_ICON_DIR = "Icons/";
    public static final String ITEM_ICON_DIR = "Items/";

    public static final String MODELS_DIR = "Models/";

	public static abstract class DexObject implements Linkable {
		public final int id;
		public final String name;

		public DexObject(int id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return getClass().getName() + " " + id + " " + name;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}








	}

	public static abstract class VarComparableDexObject<T> extends DexObject implements VarComparable<T> {
		public VarComparableDexObject(int id, String name) {
			super(id, name);
		}
	}

	public static class Pokemon extends VarComparableDexObject<Pokemon> {


		public final int dispId;
		public final String genus;
        public final int[] stats;
        public final int[] types;
		public final float height;
		public final float weight;
		public final int femalesPer8Males;
		public final int baseExperience;
		public final int baseHappiness;
		public final int catchRate;
		public final int hatchCycles;
		public final int[] evYield;
		public final int[] eggGroups;
		public final String identifier;

		public final String suffix;
		public final int order;
		public final boolean isDefault;
		public final boolean hasUniqueIcon;
		public final boolean isForm;

		public Bitmap icon;

		public Pokemon(Pokemon.Builder builder) {
			super(builder.id, builder.name);
			this.dispId = builder.dispId;
			this.genus = builder.genus;
			this.stats = builder.stats;
			this.types = builder.types;
			this.height = builder.height;
			this.weight = builder.weight;
			this.femalesPer8Males = builder.femalesPer8Males;
			this.baseExperience = builder.baseExperience;			
			this.suffix = builder.suffix;
			this.order = builder.order;
			this.isDefault = true;
			this.hasUniqueIcon = builder.hasUniqueIcon;
			this.isForm = id != dispId;
			this.baseHappiness = builder.baseHappiness;
			this.catchRate = builder.catchRate;
			this.hatchCycles = builder.stepsToHatch;
			this.evYield = builder.evYield;
			this.eggGroups = builder.eggGroups;
			this.identifier = builder.identifier;

		}

		public int getStatTotal() {
			int total = 0;
			for (int s:stats) {
				total += s;
			}
			return total;
		}

		public Bitmap loadBitmap(Context context) {
			if (icon == null) {
				icon = BitmapFactory.decodeFile(context.getExternalFilesDir(null).getAbsolutePath() + "/" + POKEMON_ICON_DIR + getIconFileName());
			}
			return icon;
		}

		public String getIconFileName() {
			return dispId + (hasUniqueIcon ?"-" + suffix: "") + ".png";
		}

		public String getModelFileName() {
			DecimalFormat df = new DecimalFormat("000");
			String formattedId = df.format(dispId);
			return formattedId + (suffix != null && isForm ?"-" + suffix: "") + ".gif";
		}
		
		public String getShinyModelFileName(){
			return identifier+".gif";
		}

		public String getCryFileNameNoExtension() {
			return dispId + (suffix != null & isForm ?"-" + suffix: "");
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
		public int compareTo(Pokemon other, int compareOn) {

			switch (compareOn) {
				case SORT_BY_DISP_ID_ASC:
					return dispId - other.dispId + (dispId == other.dispId ? id - other.id: 0);
				case SORT_BY_DISP_ID_DES:
					return other.dispId - dispId + (other.dispId == dispId ? other.id - id: 0);
				case SORT_BY_NAME_ASC:
					return name.compareTo(other.name);
				case SORT_BY_NAME_DES:
					return other.name.compareTo(name);
				case SORT_BY_TYPE_ASC:
					if (types[0] == other.types[0]) {
						if (types.length == 2) {
							if (other.types.length == 2) {
								return types[1] - other.types[1];
							} else {
								return 1;
							}
						} else {
							if (other.types.length == 2) {
								return -1;
							} else {
								return 0;
							}
						}
					} else {
						return types[0] - other.types[0];
					}
				case SORT_BY_TYPE_DES:
					if (types[0] != other.types[0]) {
						if (types.length == 2) {
							if (other.types.length == 2) {
								return other.types[1] - types[1];
							} else {
								return -1;
							}
						} else {
							if (other.types.length == 2) {
								return 1;
							} else {
								return 0;
							}
						}
					} else {
						return other.types[0] - types[0];
					}
				case SORT_BY_HP_ASC:
				case SORT_BY_ATTACK_ASC:
				case SORT_BY_DEFENSE_ASC:
				case SORT_BY_SPECIAL_ATTACK_ASC:
				case SORT_BY_SPECIAL_DEFENSE_ASC:
				case SORT_BY_SPEED_ASC:
					return stats[compareOn - SORT_BY_HP_ASC] - other.stats[compareOn - SORT_BY_HP_ASC];
				case SORT_BY_HP_DES:
				case SORT_BY_ATTACK_DES:
				case SORT_BY_DEFENSE_DES:
				case SORT_BY_SPECIAL_ATTACK_DES:
				case SORT_BY_SPECIAL_DEFENSE_DES:
				case SORT_BY_SPEED_DES:
					return other.stats[Math.abs(compareOn) - SORT_BY_HP_ASC] - stats[Math.abs(compareOn) - SORT_BY_HP_ASC];
				case SORT_BY_STAT_TOTAL_ASC:
					return getStatTotal() - other.getStatTotal();
				case SORT_BY_STAT_TOTAL_DES:
					return other.getStatTotal() - getStatTotal();

			}

			return 0;
		}

		@Override
		public String toString() {
			return name;
		}




		public static class Builder {


			public int id;
			public int dispId;
			public String name;
			public String genus;
			public int[] stats;
			public int[] types;
			public String suffix;
			public int order;
			public float height;
			public float weight;
			public int femalesPer8Males;
			public int baseExperience;
			public int baseHappiness;
			public int catchRate;
			public int stepsToHatch;
			public int[] evYield;
			public int[] eggGroups;
			public String identifier;


			public boolean isDefault;
			public boolean hasUniqueIcon;

			public Builder id(int id) {
				this.id = id;
				return this;
			}

			public Builder dispId(int id) {
				this.dispId = id;
				return this;
			}

			public Builder name(String name) {
				this.name = name;
				return this;
			}

			public Builder genus(String genus) {
				this.genus = genus;
				return this;
			}

			public Builder suffix(String suf) {
				this.suffix = suf;
				return this;
			}

			public Builder stats(int[] stats) {
				this.stats = stats;
				return this;
			}

			public Builder types(int[] types) {
				this.types = types;
				return this;
			}

			public Builder hasUniqueIcon(boolean hasIcon) {
				this.hasUniqueIcon = hasIcon;
				return this;
			}

			public Builder height(float h) {
				this.height = h;
				return this;
			}
			public Builder weight(float w) {
				this.weight = w;
				return this;
			}
			public Builder genderRate(int rate) {
				this.femalesPer8Males = rate;
				return this;
			}
			public Builder baseExp(int baseXP) {
				this.baseExperience = baseXP;
				return this;
			}
			public Builder baseHappiness(int baseHappiness) {
				this.baseHappiness = baseHappiness;
				return this;
			}
			public Builder catchRate(int catchRate) {
				this.catchRate = catchRate;
				return this;
			}
			public Builder stepsToHatch(int stepsToHatch) {
				this.stepsToHatch = stepsToHatch;
				return this;
			}
			public Builder evYield(int[] evYield) {
				this.evYield = evYield;
				return this;
			}
			public Builder eggGroups(int[] eggGroups) {
				this.eggGroups = eggGroups;
				return this;
			}

			public Builder identifier(String iden) {
				this.identifier = iden;
				return this;
			}

			public Pokemon build() {
				return new Pokemon(this);
			}

		}

		@Override
		public Class<? extends InfoActivity> getInfoActivityClass() {
			return PokemonInfoActivity.class;
		}

	}

	public static class Evolution {

		public Pokemon evolvedPoke;
		public String evolutionMethod;

		public Evolution(Pokemon evolvedPoke) {
			this.evolvedPoke = evolvedPoke;
		}

		public Evolution(Pokemon evolvedPoke, String evolutionMethod) {
			this.evolvedPoke = evolvedPoke;
			this.evolutionMethod = evolutionMethod;
		}

		public boolean isBaseEvo() {
			return evolutionMethod == null;
		}

		@Override
		public String toString() {
			return ">" + evolutionMethod + ">" + evolvedPoke.id;
		}


	}


	public static class Move extends VarComparableDexObject<Move> {

		public final String description;
		public final int power;
		public final int accuracy;
		public final int pp;
		public final int priority;
		public final int type;
		public final int damageClass;

		public final int learnMethod;
		public final int level;

		public Move(Builder builder) {
			super(builder.id, builder.name);
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


        public static final int SORT_BY_ID_ASC = 1;
        public static final int SORT_BY_ID_DES = -1;
        public static final int SORT_BY_NAME_ASC = 2;
        public static final int SORT_BY_NAME_DES = -2;
        public static final int SORT_BY_TYPE_ASC = 3;
        public static final int SORT_BY_TYPE_DES = -3;
        public static final int SORT_BY_LEARN_ASC = 4;
        public static final int SORT_BY_LEARN_DES = -4;
        public static final int SORT_BY_POWER_ASC = 5;
        public static final int SORT_BY_POWER_DES = -5;
        public static final int SORT_BY_ACCURACY_ASC = 6;
        public static final int SORT_BY_ACCURACY_DES = -6;
        public static final int SORT_BY_PP_ASC = 7;
        public static final int SORT_BY_PP_DES = -7;
        public static final int SORT_BY_PRIORITY_ASC = 8;
        public static final int SORT_BY_PRIORITY_DES = -8;
		
		
		@Override
        public int compareTo(Move other, int sortBy) {
            switch (sortBy) {
                case SORT_BY_ID_ASC:
					return id - other.id;
                case SORT_BY_ID_DES:
					return other.id - id;
                case SORT_BY_NAME_ASC:
					return name.compareTo(other.name);
                case SORT_BY_NAME_DES:
					return other.name.compareTo(name);
                case SORT_BY_TYPE_ASC:
					return  type - other.type;
                case SORT_BY_TYPE_DES:
					return other.type - this.type;
                case SORT_BY_LEARN_ASC:
					return (learnMethod != other.learnMethod ? learnMethod - other.learnMethod: (level != other.level ?level - other.level: name.compareTo(other.name)));
                case SORT_BY_LEARN_DES:
                    return (learnMethod != other.learnMethod ? other.learnMethod - learnMethod: (level != other.level ?other.level - level: other.name.compareTo(name)));
             	case SORT_BY_POWER_ASC:
					return power-other.power;
				case SORT_BY_POWER_DES:
					return other.power-power;
				case SORT_BY_ACCURACY_ASC:
					return accuracy-other.accuracy;
				case SORT_BY_ACCURACY_DES:
					return other.accuracy-accuracy;
				case SORT_BY_PP_ASC:
					return pp-other.pp;
				case SORT_BY_PP_DES:
					return other.pp-pp;
				case SORT_BY_PRIORITY_ASC:
					return priority-other.priority;
				case SORT_BY_PRIORITY_DES:
					return other.priority-priority;
					
					
					
					
				default:
                    return 0;
            }
        }



		public static class Builder {

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



			public Builder id(int id) {
				this.id = id;
				return this;
			}
			public Builder name(String name) {
				this.name = name;
				return this;
			}
			public Builder description(String desc) {
				this.description = desc;
				return this;
			}
			public Builder power(int pow) {
				this.power = pow;
				return this;
			}
			public Builder accuracy(int acc) {
				this.accuracy = acc;
				return this;
			}
			public Builder pp(int pp) {
				this.pp = pp;
				return this;
			}
			public Builder priority(int pri) {
				this.priority = pri;
				return this;
			}
			public Builder type(int type) {
				this.type = type;
				return this;
			}
			public Builder damageClass(int dClass) {
				this.damageClass = dClass;
				return this;
			}

			public Builder learnMethod(int lMethod) {
				learnMethod = lMethod;
				return this;
			}

			public Builder level(int lvl) {
				level = lvl;
				return this;
			}

			public Move build() {
				return new Move(this);
			}



		}

		@Override
		public Class<? extends InfoActivity> getInfoActivityClass() {
			return MoveInfoActivity.class;
		}

	}


	public static class Ability extends VarComparableDexObject<Ability> {

		public final String effect;

		public Ability(int id, String name, String effect) {
			super(id, name);
			this.effect = effect;
		}

		public static final int SORT_BY_ID_ASC = 1;
        public static final int SORT_BY_ID_DES = -1;
        public static final int SORT_BY_NAME_ASC = 2;
        public static final int SORT_BY_NAME_DES = -2;



		@Override
        public int compareTo(Ability other, int sortBy) {
            switch (sortBy) {
                case SORT_BY_ID_ASC:
					return id - other.id;
                case SORT_BY_ID_DES:
					return other.id - id;
                case SORT_BY_NAME_ASC:
					return name.compareTo(other.name);
                case SORT_BY_NAME_DES:
					return other.name.compareTo(name);
			}
			return 0;
		}


		@Override
		public Class<? extends InfoActivity> getInfoActivityClass() {
			return AbilityInfoActivity.class;
		}

	}

    public static class Item extends VarComparableDexObject<Item> {

        public final String description;
        public final String identifier;
        public Bitmap icon;


        public Item(int id, String name, String effect, String identifier) {
            super(id, name);
            this.description = effect;
            this.identifier = identifier;
        }

        public Bitmap loadBitmap(Context context) {
            if (icon == null) {
                icon = BitmapFactory.decodeFile(context.getExternalFilesDir(null).getAbsolutePath() + "/" +
												ITEM_ICON_DIR + getIconFileName());
            }
            return icon;
        }

        public String getIconFileName() {
            return identifier + ".png";
        }



        public static final int SORT_BY_ID_ASC = 1;
        public static final int SORT_BY_ID_DES = -1;
        public static final int SORT_BY_NAME_ASC = 2;
        public static final int SORT_BY_NAME_DES = -2;


        @Override
        public int compareTo(Item other, int sortBy) {
            switch (sortBy) {
                case SORT_BY_ID_ASC:
                    return id - other.id;
                case SORT_BY_ID_DES:
                    return other.id - id;
                case SORT_BY_NAME_ASC:
                    return name.compareTo(other.name);
                case SORT_BY_NAME_DES:
                    return other.name.compareTo(name);
            }
            return 0;
        }

		@Override
		public Class<? extends InfoActivity> getInfoActivityClass() {
			return ItemInfoActivity.class;
		}

    }

    public static final class Nature implements VarComparable<Nature>{


        public final String name;
        public final int statUp;
        public final int statDown;


        public Nature(String name, int statIdUp, int statIdDown) {
            this.name = name;
            statUp = statIdUp;
            statDown = statIdDown;
        }

        public static final int SORT_BY_NAME_ASC = 2;
        public static final int SORT_BY_NAME_DES = -2;
        public static final int SORT_BY_STAT_UP_ASC = 3;
        public static final int SORT_BY_STAT_UP_DES = -3;
        public static final int SORT_BY_STAT_DOWN_ASC = 4;
        public static final int SORT_BY_STAT_DOWN_DES = -4;

        @Override
        public int compareTo(Nature other, int sortBy) {
            switch (sortBy) {
                case SORT_BY_NAME_ASC:
                    return name.compareTo(other.name);
                case SORT_BY_NAME_DES:
                    return other.name.compareTo(name);
                case SORT_BY_STAT_UP_ASC:
                    return statUp - other.statUp;
                case SORT_BY_STAT_UP_DES:
                    return other.statUp - statUp;
                case SORT_BY_STAT_DOWN_ASC:
                    return statDown - other.statDown;
                case SORT_BY_STAT_DOWN_DES:
                    return other.statDown - statDown;
            }
            return 0;
        }

    }

	public interface Linkable {

		public int getId();
		public String getName();
		public Class<? extends InfoActivity> getInfoActivityClass();

	}	

	public interface VarComparable<T> {
		public int compareTo(T other, int compareOn);
	}

}
