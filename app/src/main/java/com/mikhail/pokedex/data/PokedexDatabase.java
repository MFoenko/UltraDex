package com.mikhail.pokedex.data;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import com.mikhail.pokedex.activities.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import java.io.*;
import java.util.*;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class PokedexDatabase extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "veekun-pokedex.sqlite";
    public static final int DATABASE_VERSION = 14;

    private static PokedexDatabase instance;
    private Context mContext;
    private SQLiteDatabase dex;

    public PokedexDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        dex = getReadableDatabase();
    }

    public static PokedexDatabase getInstance(Context context){
        if (instance == null){
            instance = new PokedexDatabase(context);
        }
        return instance;
    }

	@Override
    public void onCreate(SQLiteDatabase db){
        copyDatabaseFromAssets(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        copyDatabaseFromAssets(db);
    }

    public void copyDatabaseFromAssets(SQLiteDatabase db){

        String oldDbPath = db.getPath();
        try{
            mContext.getAssets();
            InputStream input = mContext.getAssets().open(DATABASE_NAME);
			db.deleteDatabase(new File(oldDbPath));
            OutputStream output = new FileOutputStream(oldDbPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0){
                output.write(buffer, 0, length);
            }
            output.flush();
            input.close();
            output.close();
        }catch (IOException e){
			e.printStackTrace();
        }
    }


	public static final int[] GEN_STAT_VERSIONS = {0,1,1,1,1,1};
	public static final String[][] STAT_LABELS = {
		{"HP", "Attack", "Defense", "Special", "Speed"},
		{"HP", "Attack", "Defense", "Sp. Attack", "Sp. Defence", "Speed"}
	};
	public static final int[][] STAT_COLORS = {
		{0xFF0000, 0xFFA500, 0xFFFF00, 0x1E90FF, 0xFFC0CB },
		{0xFF0000, 0xFFA500, 0xFFFF00, 0x1E90FF, 0x008000, 0xFFC0CB },
	};
    public static final int[][] STAT_MINS = {
		{0,0,0,0,0},
		{0,0,0,0,0,0}
    };
	public static final int[][] STAT_MAXES = {
		{250, 150, 200, 150, 150},
		{250, 200, 250, 200, 250, 200}
	};
	public static final int STAT_TOTAL_COLOR = 0xDDDDDD;
	public static final String STAT_TOTAL_LABEL = "Total";
	public static final int STAT_TOTAL_MIN = 0;
	public static final int STAT_TOTAL_MAX = 800;


	public static final int[] GEN_TYPE_VERSIONS = {0,1,1,1,1,2};
	public static final String[][] TYPE_NAMES = {
		{"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","","Fire","Water","Grass","Electric","Psychic","Ice","Dragon"},
		{"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark"},
		{"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"}
	};
	public static final int[][] TYPE_COLORS ={
		{0xFFA8A77A, 0xFFC22E28, 0xFFA98FF3, 0xFFA33EA1, 0xFFE2BF65, 0xFFB6A136, 0xFFA6B91A, 0xFF735797, 0 , 0xFFEE8130, 0xFF6390F0, 0xFF7AC74C, 0xFFF7D02C, 0xFFF95587, 0xFF96D9D6, 0xFF6F35FC},
		{0xFFA8A77A, 0xFFC22E28, 0xFFA98FF3, 0xFFA33EA1, 0xFFE2BF65, 0xFFB6A136, 0xFFA6B91A, 0xFF735797, 0xFFB7B7CE, 0xFFEE8130, 0xFF6390F0, 0xFF7AC74C, 0xFFF7D02C, 0xFFF95587, 0xFF96D9D6, 0xFF6F35FC, 0xFF705746},
		{0xFFA8A77A, 0xFFC22E28, 0xFFA98FF3, 0xFFA33EA1, 0xFFE2BF65, 0xFFB6A136, 0xFFA6B91A, 0xFF735797, 0xFFB7B7CE, 0xFFEE8130, 0xFF6390F0, 0xFF7AC74C, 0xFFF7D02C, 0xFFF95587, 0xFF96D9D6, 0xFF6F35FC, 0xFF705746, 0xFFFF00FF}
	};
	//TYPE_EFFICIENCY[TYPE_VERSION][ATTACK_TYPE][DEFEND_TYPE];
	public static final float[][][] TYPE_EFFICIENCY=
	{ 
		{ 
			{1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,0.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{2.0f,1.0f,0.5f,0.5f,1.0f,2.0f,0.5f,0.0f,2.0f,1.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,2.0f,0.5f}, 
			{1.0f,2.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,0.5f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,2.0f,0.5f,0.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f}, 
			{1.0f,1.0f,0.0f,2.0f,1.0f,2.0f,0.5f,1.0f,2.0f,2.0f,1.0f,0.5f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{1.0f,0.5f,2.0f,1.0f,0.5f,1.0f,2.0f,1.0f,0.5f,2.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f}, 
			{1.0f,0.5f,0.5f,2.0f,1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,2.0f,0.5f}, 
			{0.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,0.0f,1.0f,1.0f,0.5f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,0.5f,1.0f,2.0f,1.0f,1.0f,2.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,2.0f,0.5f,0.5f,2.0f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,1.0f,1.0f,1.0f,2.0f,0.5f,0.5f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,0.5f,0.5f,2.0f,2.0f,0.5f,1.0f,0.5f,0.5f,2.0f,0.5f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,2.0f,1.0f,0.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,0.5f,0.5f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,0.0f,1.0f}, 
			{1.0f,1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,1.0f,0.5f,1.0f,0.5f,2.0f,1.0f,1.0f,0.5f,2.0f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,0.0f}, 
			{1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,0.5f}, 
			{1.0f,2.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,0.5f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,1.0f} 
		},
		{ 
			{1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,0.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{2.0f,1.0f,0.5f,0.5f,1.0f,2.0f,0.5f,0.0f,2.0f,1.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,2.0f,0.5f}, 
			{1.0f,2.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,0.5f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,0.5f,0.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f}, 
			{1.0f,1.0f,0.0f,2.0f,1.0f,2.0f,0.5f,1.0f,2.0f,2.0f,1.0f,0.5f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{1.0f,0.5f,2.0f,1.0f,0.5f,1.0f,2.0f,1.0f,0.5f,2.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f}, 
			{1.0f,0.5f,0.5f,0.5f,1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,2.0f,0.5f}, 
			{0.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,0.5f,1.0f,2.0f,1.0f,1.0f,2.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,2.0f,0.5f,0.5f,2.0f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,1.0f,1.0f,1.0f,2.0f,0.5f,0.5f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,0.5f,0.5f,2.0f,2.0f,0.5f,1.0f,0.5f,0.5f,2.0f,0.5f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,2.0f,1.0f,0.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,0.5f,0.5f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,0.0f,1.0f}, 
			{1.0f,1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,2.0f,1.0f,1.0f,0.5f,2.0f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,0.0f}, 
			{1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,0.5f}, 
			{1.0f,2.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,0.5f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,1.0f} 
		},
		{ 
			{1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,0.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{2.0f,1.0f,0.5f,0.5f,1.0f,2.0f,0.5f,0.0f,2.0f,1.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,2.0f,0.5f}, 
			{1.0f,2.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,0.5f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,0.5f,0.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f}, 
			{1.0f,1.0f,0.0f,2.0f,1.0f,2.0f,0.5f,1.0f,2.0f,2.0f,1.0f,0.5f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f}, 
			{1.0f,0.5f,2.0f,1.0f,0.5f,1.0f,2.0f,1.0f,0.5f,2.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f}, 
			{1.0f,0.5f,0.5f,0.5f,1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,2.0f,0.5f}, 
			{0.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,0.5f,0.5f,1.0f,0.5f,1.0f,2.0f,1.0f,1.0f,2.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,2.0f,1.0f,2.0f,0.5f,0.5f,2.0f,1.0f,1.0f,2.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,1.0f,1.0f,1.0f,2.0f,0.5f,0.5f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,0.5f,0.5f,2.0f,2.0f,0.5f,1.0f,0.5f,0.5f,2.0f,0.5f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,1.0f,2.0f,1.0f,0.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,0.5f,0.5f,1.0f,1.0f,0.5f,1.0f,1.0f}, 
			{1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,0.0f,1.0f}, 
			{1.0f,1.0f,2.0f,1.0f,2.0f,1.0f,1.0f,1.0f,0.5f,0.5f,0.5f,2.0f,1.0f,1.0f,0.5f,2.0f,1.0f,1.0f}, 
			{1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,0.0f}, 
			{1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,1.0f,1.0f,0.5f,0.5f}, 
			{1.0f,2.0f,1.0f,0.5f,1.0f,1.0f,1.0f,1.0f,0.5f,0.5f,1.0f,1.0f,1.0f,1.0f,1.0f,2.0f,2.0f,1.0f} 
		}
	};

	public static final String[] DAMAGE_CLASS_NAMES = {"Physical", "Special", "Status"};

	public static final String[] EGG_GROUP_NAMES = {"Monster", "Water 1", "Bug", "Flying", "Field", "Fairy", "Grass", "Human-Like", "Water 3", "Mineral", "Amorphous", "Water 2", "Ditto", "Dragon", "Undiscovered"};
	
	public static final String[] GENDER_NAMES = {"Female","Male","Genderless"};
	public static final int[] GENDER_COLORS = {0xFFB6C1, 0xADD8E6, 0xDDDDDD};
	
	
	public static final int[] VERSION_VERSION_GROUP = {0,0,1,2,2,3,4,4,5,6,6,7,7,8,9,9,10,10,11,12,13,13,14,14, 15, 15};
	public static final int[] VERSION_GROUP_GENERATION = {0,0,1,1,2,2,2,3,3,3,4,2,2,4,5,5,5,5};


	public static final int VERSION = 24;
	public static final int LANG = 9;

	public static final int VERSION_GROUP = VERSION_VERSION_GROUP[VERSION];
	public static final int GEN = VERSION_GROUP_GENERATION[VERSION_GROUP];


	public static int getTypeVersion(){
		return GEN_TYPE_VERSIONS[GEN];
	}

	public static int getStatVersion(){
		return GEN_STAT_VERSIONS[GEN];
	}



	private static Pokemon[] preloadedPokemon;


	private static final String BASE_POKEMON_QUERY_W_O_SELECT = "p._id, p.species_id, CASE f.form_order WHEN 1 THEN sn.name ELSE fn.pokemon_name END, f.form_identifier, (SELECT GROUP_CONCAT(t.type_id-1,',') FROM pokemon_types AS t WHERE t.pokemon_id=p._id AND generation_id-1 <= ? GROUP BY pokemon_id,generation_id ORDER BY slot, generation_id DESC LIMIT 1), (SELECT GROUP_CONCAT(s.base_stat,',') FROM pokemon_stats AS s WHERE s.pokemon_id = p._id AND stat_id != (5*(?=='1')) AND generation_id-1 <= ? GROUP BY s.generation_id ORDER BY stat_id, generation_id DESC LIMIT 1), sn.genus, p.height/10.0, p.weight/10.0, s.gender_rate, p.base_experience, s.base_happiness, s.capture_rate, s.hatch_counter, (SELECT group_concat(effort) FROM pokemon_stats AS stats WHERE stats.pokemon_id = p._id GROUP BY stats.generation_id ORDER BY stat_id LIMIT 1), (SELECT egg_group_id-1 FROM pokemon_egg_groups WHERE species_id = s.id) FROM pokemon AS p JOIN pokemon_species AS s ON (p.species_id = s.id) JOIN pokemon_forms AS f ON (p._id = f.pokemon_id) JOIN pokemon_species_names AS sn ON (p.species_id = sn.pokemon_species_id) JOIN pokemon_form_names AS fn ON (f.id = fn.pokemon_form_id AND sn.local_language_id = fn.local_language_id ) WHERE fn.local_language_id = ?";
	private static final String BASE_POKEMON_QUERY = "SELECT " + BASE_POKEMON_QUERY_W_O_SELECT;
	private static final String ALL_POKEMON_QUERY = BASE_POKEMON_QUERY + "AND f.introduced_in_version_group_id+1 <= ? AND f.is_default = 1 ORDER BY p._id;";
	private static final String SINGLE_POKEMON_QUERY = BASE_POKEMON_QUERY + " AND p._id = ? ORDER BY RANDOM();";
	private static final String MOVE_POKEMON_QUERY = "SELECT DISTINCT " + BASE_POKEMON_QUERY_W_O_SELECT + " AND p._id IN (SELECT pokemon_id FROM pokemon_moves WHERE version_group_id+1 <= ? AND move_id = ?) ;";



	public Pokemon getPokemon(int id){
		return getPokemon(id, VERSION, LANG);
	}

	public Pokemon getPokemon(int id, int ver, int lang){

        if (preloadedPokemon != null){
            return getPreloadedPokemon(id);
        }

		int gen = VERSION_GROUP_GENERATION[VERSION_VERSION_GROUP[ver]];


		Cursor c = this.dex.rawQuery(SINGLE_POKEMON_QUERY, new String[]{String.valueOf(gen), String.valueOf(gen), String.valueOf(gen), String.valueOf(lang), String.valueOf(id)});

		Pokemon.Builder builder = new Pokemon.Builder();
		c.moveToFirst();
		builder.id(c.getInt(0))
			.dispId(c.getInt(1))
			.name(c.getString(2))
			.suffix(c.getString(3))
			.types(stringArrToIntArr(c.getString(4).split(",")))
			.stats(stringArrToIntArr(c.getString(5).split(",")))
			.hasUniqueIcon(builder.id != builder.dispId)
			.genus(c.getString(6))
			.height(c.getFloat(7))
			.weight(c.getFloat(8))
			.genderRate(c.getInt(9))
			.baseExp(c.getInt(10))
			;

		c.close();
		return builder.build();
	}

    public static Pokemon getPreloadedPokemon(int id){
        return getPreloadedPokemon(id, preloadedPokemon.length / 2, 0 , preloadedPokemon.length);
    }
    public static Pokemon getPreloadedPokemon(int id, int pos, int low, int high){


        if (pos < 0 || pos > preloadedPokemon.length || high < low){
            return null;
        }
        int foundId= preloadedPokemon[pos].id;

        if (foundId < id){
			return getPreloadedPokemon(id, pos + (high - pos) / 2, pos , high);
        }else if (foundId > id){
            return getPreloadedPokemon(id, (pos - low) / 2, low, pos);
        }

        return preloadedPokemon[pos];




    }



	public Pokemon[] getPokemonArrayFromCursor(Cursor c){
		int length;
		Pokemon[] pokemon = new Pokemon[length = c.getCount()];
		Pokemon.Builder builder = new Pokemon.Builder();
		for (int i=0;i < length;i++){
			c.moveToNext();
			builder.id(c.getInt(0))
				.dispId(c.getInt(1))
				.name(c.getString(2))
				.suffix(c.getString(3))
				.types(stringArrToIntArr(c.getString(4).split(",")))
				.stats(stringArrToIntArr(c.getString(5).split(",")))
				.hasUniqueIcon(builder.id != builder.dispId)
				.genus(c.getString(6))
				.height(c.getFloat(7))
				.weight(c.getFloat(8))
				.genderRate(c.getInt(9))
				.baseExp(c.getInt(10))
				.baseHappiness(c.getInt(11))
				.catchRate(c.getInt(12))
				.stepsToHatch(c.getInt(13))
				.evYield(stringArrToIntArr(c.getString(14).split(",")))
				.eggGroups(stringArrToIntArr(c.getString(15).split(",")))
				;
			pokemon[i] = builder.build();
		}
		c.close();
		return pokemon;
	}

	public Pokemon[] getAllPokemon(){
		return getAllPokemon(VERSION, LANG);
	}

	public Pokemon[] getAllPokemon(int ver, int lang){


		if (preloadedPokemon != null){
			return preloadedPokemon;
		}

		int gen = VERSION_GROUP_GENERATION[VERSION_VERSION_GROUP[ver]];
		int vgr = VERSION_VERSION_GROUP[ver];

		Cursor c = this.dex.rawQuery(ALL_POKEMON_QUERY, new String[]{String.valueOf(gen), String.valueOf(gen), String.valueOf(gen), String.valueOf(lang),String.valueOf(vgr)});

		return preloadedPokemon = getPokemonArrayFromCursor(c);
	}


	public Pokemon[] getPokemonByCommonMove(int id){
		return getPokemonByCommonMove(id, VERSION, LANG);
	}

	public Pokemon[] getPokemonByCommonMove(int id, int ver, int lang){
		int gen = VERSION_GROUP_GENERATION[VERSION_VERSION_GROUP[ver]];
		int vgr = VERSION_VERSION_GROUP[ver];

		Cursor c = this.dex.rawQuery(MOVE_POKEMON_QUERY, new String[]{String.valueOf(gen), String.valueOf(gen), String.valueOf(gen), String.valueOf(lang),String.valueOf(vgr), String.valueOf(id)});
		return getPokemonArrayFromCursor(c);
	}

	private int[] stringArrToIntArr(String[] sArr){
		int len = sArr.length;
		int[] iArr = new int[len];
		for (int i=0;i < len;i++){
			iArr[i] = Integer.parseInt(sArr[i]);
		}
		return iArr;
	}


	private static final String EVO_QUERY = "SELECT s.id, s.evolves_from_species_id, evolution_trigger_id, trigger_item_id, minimum_level, gender_id, location_id, held_item_id, time_of_day, known_move_id, known_move_type_id, minimum_happiness, minimum_beauty, minimum_affection, relative_physical_stats + 1, party_species_id, party_type_id, trade_species_id, needs_overworld_rain, turn_upside_down FROM pokemon_species AS s  LEFT OUTER JOIN pokemon_evolution AS e ON  s.id = e.evolved_species_id WHERE evolution_chain_id = (SELECT evolution_chain_id FROM pokemon_species WHERE id =?) ORDER BY e.id;";

	public ArrayList<ArrayList<Evolution>> getEvolutions(int id){
		Cursor c = dex.rawQuery(EVO_QUERY, new String[]{String.valueOf(id)});
		ArrayList<ArrayList<Evolution>> tree = new ArrayList<ArrayList<Evolution>>();



		if (c.moveToFirst()){
            tree.add(new ArrayList<Evolution>());
            tree.get(0).add(getEvolution(c));

            ArrayList<Evolution> branch = tree.get(0);
            while (c.moveToNext()){
                Evolution evo = getEvolution(c);
                int prevolutionId = c.getInt(1);
                if (prevolutionId != branch.get(branch.size() - 1).evolvedPoke.id){
                    ArrayList<Evolution> newBranch = new ArrayList<Evolution>();
                    for (Evolution e : branch){
                        newBranch.add(e);
                        if (e.evolvedPoke.id == prevolutionId){
                            break;
                        }
                    }
                    tree.add(newBranch);
                    branch = newBranch;
                }
                branch.add(evo);
            }
        }
		return tree;

	}

	private static final String [] EVOLUTION_METHODS = new String[]{null, "Level Up", "Trade", "Use", "Shed"};

    private static final String [] RELATIVE_PHYSICAL_STATS_COMPARATORS = new String[]{"<", "=", ">"};


    private Evolution getEvolution(Cursor c){

        String method = EVOLUTION_METHODS[c.getInt(2)];

		if (method != null){
			StringBuilder evolutionReqs = new StringBuilder(method).append("\n");
			if (c.getInt(4) != 0)
				evolutionReqs.append("At level ").append(c.getInt(4)).append("\n");
            if (c.getInt(3) != 0)
                evolutionReqs.append("<item>").append(c.getInt(3)).append("</item>").append("\n");
            if (c.getInt(5) != 0)
                evolutionReqs.append(c.getInt(5) == 1 ? "Female" : "Male").append("\n");
            if (c.getInt(6) != 0)
                evolutionReqs.append("at ").append("<location>").append(c.getInt(6)).append("</location>").append("\n");
            if (c.getInt(7) != 0)
                evolutionReqs.append("holding").append("<item>").append(c.getInt(7)).append("</item>").append("\n");
            if (c.getString(8) != null)
                evolutionReqs.append("during the ").append(c.getString(8)).append("\n");
            if (c.getInt(9) != 0)
                evolutionReqs.append("knowing ").append("<move>").append(c.getInt(9)).append("</move>").append("\n");
            if (c.getInt(10) != 0)
                evolutionReqs.append("knowing a  ").append("<type>").append(c.getInt(10)).append("</type>").append(" type move \n");
            if (c.getInt(11) != 0)
                evolutionReqs.append("with ").append(c.getInt(11)).append(" happiness\n");
            if (c.getInt(12) != 0)
                evolutionReqs.append("with ").append(c.getInt(12)).append(" beauty\n");
            if (c.getInt(13) != 0)
                evolutionReqs.append("with ").append(c.getInt(13)).append(" affection\n");
            if (c.getString(14) != null)
                evolutionReqs.append("with Attack").append(RELATIVE_PHYSICAL_STATS_COMPARATORS[c.getInt(14)]).append("Defense\n");
            if (c.getInt(15) != 0)
                evolutionReqs.append("with ").append("<pokemon>").append(c.getInt(15)).append("</pokemon>").append(" in the party\n");
            if (c.getInt(16) != 0)
                evolutionReqs.append("with a ").append("<type>").append(c.getInt(16)).append("</type>").append(" type in the party \n");
            if (c.getInt(17) != 0)
                evolutionReqs.append("for a ").append("<pokemon>").append(c.getInt(17)).append("</pokemon>").append("\n");
            if (c.getInt(18) == 1)
                evolutionReqs.append("while raining\n");
            if (c.getInt(19) == 1)
                evolutionReqs.append("while 3DS is upside-down");

			return new Evolution(getPokemon(c.getInt(0)), evolutionReqs.toString());

		}


		return new Evolution(getPokemon(c.getInt(0)));


	}

    private static final String FORMS_QUERY = BASE_POKEMON_QUERY + " AND f.introduced_in_version_group_id-1 <= ? AND p.species_id = ? and p._id != ?;";

    public Pokemon[] getForms(int id){
        return getForms(id, VERSION, LANG);
    }

    public Pokemon[] getForms(int id, int ver, int lang){
        int vgr = VERSION_VERSION_GROUP[ver];
        final int generation = VERSION_GROUP_GENERATION[vgr];

		if (preloadedPokemon != null){
			Cursor c = this.dex.rawQuery("SELECT p._id FROM pokemon AS p JOIN pokemon_forms AS f ON (f.pokemon_id = p._id) WHERE f.introduced_in_version_group_id-1 <= ? AND p.species_id = ? and p._id != ?;", new String[]{String.valueOf(vgr), String.valueOf(id), String.valueOf(id)});
			int len;
			Pokemon[] forms = new Pokemon[len = c.getCount()];
			for(int i=0;i<len;i++){
				c.moveToNext();
				forms[i] = getPokemon(c.getInt(0));
			}
			return forms;
		}else{
			Cursor c = this.dex.rawQuery(FORMS_QUERY, new String[]{String.valueOf(generation), String.valueOf(generation), String.valueOf(generation), String.valueOf(lang),String.valueOf(vgr), String.valueOf(id), String.valueOf(id)});
			return getPokemonArrayFromCursor(c);
		}

    }











	public static final String EFFECT_CHANCE_SYMBOL = "$effect_chance";

	private static Move[] preloadedMoves;

	public static final String BASE_MOVE_SELECT = "SELECT m.id, mn.name, m.type_id-1,m.damage_class_id-1, m.power, m.accuracy, m.pp, m.priority , mp.effect, m.effect_chance";
	public static final String BASE_MOVE_FROM = " FROM moves AS m JOIN move_names AS mn ON (m.id = mn.move_id) JOIN move_effect_prose AS mp ON (m.effect_id = mp.move_effect_id AND mp.local_language_id=mn.local_language_id)";
	public static final String BASE_MOVE_WHERE = " WHERE mp.local_language_id = ?";
	public static final String BASE_MOVE_QUERY = BASE_MOVE_SELECT + BASE_MOVE_FROM + BASE_MOVE_WHERE;
	public static final String ALL_MOVE_QUERY = BASE_MOVE_QUERY + " AND m.generation_id <= ? AND m.id < 10000 ORDER BY mn.name;";
	public static final String SINGLE_MOVE_QUERY = BASE_MOVE_QUERY + " AND m.id = ?;";
	public static final String POKEMON_MOVE_QUERY = BASE_MOVE_SELECT + ", pm.pokemon_move_method_id-1, pm.level" + BASE_MOVE_FROM + " JOIN pokemon_moves AS pm ON (pm.move_id=m.id)" + BASE_MOVE_WHERE + " AND pm.version_group_id = ? AND pm.pokemon_id = ? ;";

	public static final String[] MOVE_METHOD_LABELS = {"Level", "Egg", "Tutor", "TM/HM", "Stadium Surfing Pikachu", "Light Ball Egg", "Colosseum Purification", "XD Shadow", "XD Purification", "Form Change"};




	public Move getMove(int id){
		return getMove(id, VERSION, LANG);
	}

	public Move getMove(int id, int ver, int lang){
		//int gen = VERSION_GROUP_GENERATION[VERSION_VERSION_GROUP[ver]] + 1;

		Cursor c=dex.rawQuery(SINGLE_MOVE_QUERY, new String[]{String.valueOf(lang), String.valueOf(id)});

		Move move = null;
		if (c.moveToFirst()){
			Move.Builder builder = new Move.Builder();
			builder
				.id(c.getInt(0))
				.name(c.getString(1))
				.type(c.getInt(2))
				.damageClass(c.getInt(3))
				.power(c.getInt(4))
				.accuracy(c.getInt(5))
				.pp(c.getInt(6))
				.priority(c.getInt(7));
			StringBuilder effect = new StringBuilder(c.getString(8));
			int effectIndex = effect.indexOf(EFFECT_CHANCE_SYMBOL);
			if (effectIndex != -1){
				effect.replace(effectIndex, effectIndex + EFFECT_CHANCE_SYMBOL.length(), c.getString(9));
			}
			builder
				.description(parseEffectString(effect));
			move = builder.build();
		}

		c.close();
		return move;
	}

	public Move getMove(CharSequence id){
		return getMove(id, VERSION, LANG);
	}

	public Move getMove(CharSequence id, int ver, int lang){
		int gen = VERSION_GROUP_GENERATION[VERSION_VERSION_GROUP[ver]] + 1;

		String query = "SELECT m.id, mn.name, m.type_id-1,m.damage_class_id-1, m.power, m.accuracy, m.pp, m.priority , mp.effect, m.effect_chance FROM moves AS m JOIN move_names AS mn ON (m.id = mn.move_id) JOIN move_effect_prose AS mp ON (m.effect_id = mp.move_effect_id AND mp.local_language_id=mn.local_language_id) WHERE m.identifier = ? AND m.generation_id <= ? AND mp.local_language_id = ?";
		Cursor c=dex.rawQuery(query, new String[]{id.toString(), String.valueOf(gen), String.valueOf(lang)});

		Move move = null;
		if (c.moveToFirst()){
			Move.Builder builder = new Move.Builder();
			builder
				.id(c.getInt(0))
				.name(c.getString(1))
				.type(c.getInt(2))
				.damageClass(c.getInt(3))
				.power(c.getInt(4))
				.accuracy(c.getInt(5))
				.pp(c.getInt(6))
				.priority(c.getInt(7));
			StringBuilder effect = new StringBuilder(c.getString(8));
			int effectIndex = effect.indexOf(EFFECT_CHANCE_SYMBOL);
			if (effectIndex != -1){
				effect.replace(effectIndex, effectIndex + EFFECT_CHANCE_SYMBOL.length(), c.getString(9));
			}
			builder
				.description(parseEffectString(effect));
			move = builder.build();
		}

		c.close();
		return move;
	}
	public Move[] getMovesFromCursor(Cursor c){
		int len = c.getCount();
		Move[] moves = new Move[len];
		Move.Builder builder = new Move.Builder();
		for (int i=0;i < len;i++){
			c.moveToNext();
			builder
				.id(c.getInt(0))
				.name(c.getString(1))
				.type(c.getInt(2))
				.damageClass(c.getInt(3))
				.power(c.getInt(4))
				.accuracy(c.getInt(5))
				.pp(c.getInt(6))
				.priority(c.getInt(7));
			StringBuilder effect = new StringBuilder(c.getString(8));
			int effectIndex = effect.indexOf(EFFECT_CHANCE_SYMBOL);
			if (effectIndex != -1){
				effect.replace(effectIndex, effectIndex + EFFECT_CHANCE_SYMBOL.length(), c.getString(9));
			}
			builder
				.description(parseEffectString(effect));
			moves[i] = builder.build();
		}

		c.close();
		return moves;
	}

	public Move[] getAllMoves(){
		return getAllMoves(VERSION, LANG);
	}

	public Move[] getAllMoves(int ver, int lang){
		if (preloadedMoves != null)
			return preloadedMoves;

		int gen = VERSION_GROUP_GENERATION[VERSION_VERSION_GROUP[ver]] + 1;

		Cursor c=dex.rawQuery(ALL_MOVE_QUERY, new String[]{String.valueOf(lang), String.valueOf(gen)});


		return preloadedMoves = getMovesFromCursor(c);
	}

	public Move[] getMovesByPokemon(int id){
		return getMovesByPokemon(id, VERSION, LANG);
	}

	public Move[] getMovesByPokemon(int id, int ver, int lang){
		int vergrp = VERSION_VERSION_GROUP[ver];
		Cursor c = dex.rawQuery(POKEMON_MOVE_QUERY, new String[]{String.valueOf(lang), String.valueOf(vergrp), String.valueOf(id)});
		int len = c.getCount();
		Move[] moves = new Move[len];
		Move.Builder builder = new Move.Builder();
		for (int i=0;i < len;i++){
			c.moveToNext();
			builder
				.id(c.getInt(0))
				.name(c.getString(1))
				.type(c.getInt(2))
				.damageClass(c.getInt(3))
				.power(c.getInt(4))
				.accuracy(c.getInt(5))
				.pp(c.getInt(6))
				.priority(c.getInt(7))
				.learnMethod(c.getInt(10))
				.level(c.getInt(11));
			StringBuilder effect = new StringBuilder(c.getString(8));
			int effectIndex = effect.indexOf(EFFECT_CHANCE_SYMBOL);
			if (effectIndex != -1){
				effect.replace(effectIndex, effectIndex + EFFECT_CHANCE_SYMBOL.length(), c.getString(9));
			}
			builder
				.description(parseEffectString(effect));
			moves[i] = builder.build();
		}

		c.close();
		return moves;

	}

	public static final String[] LINK_TYPES = {"ability", "item", "move", "pokemon"};
	public static final String LINK_MOVE =  "move";
	public static final String LINK_POKEMON =  "pokemon";
	public static final String LINK_ABILITY =  "ability";
	public static final String LINK_ITEM =  "item";



	private static String parseEffectString(String effect){
		return parseEffectString(new StringBuilder(effect));
	}
	private static String parseEffectString(StringBuilder effect){
		int start;
		String type;
		String item;
		int end;
		while ((start = effect.indexOf("[")) != -1){

			type = effect.substring(effect.indexOf("]{") + "]{".length(), effect.indexOf(":", start));
			item = effect.substring(effect.indexOf(":", start) + 1, end = effect.indexOf("}", start));
			String replacementString;
			if (isInArray(LINK_TYPES, type)){
				replacementString = new StringBuilder()
					.append("<").append(type).append(">")
					.append(item)
					.append("</").append(type).append(">").toString();
			}else{
				replacementString = item.replaceAll("-", " ");
			}
			effect.replace(start, end + 1, replacementString);
		}

		return effect.toString();		
	}

	public Linkable getLinkableData(CharSequence tag, CharSequence data){
		if (LINK_MOVE.equals(tag.toString())){
			return getMove(data);
		}
		if (LINK_POKEMON.equals(tag)){

		}
		if (LINK_MOVE.equals(tag)){

		}
		if (LINK_ITEM.equals(tag)){

		}
		return null;
	}


	public Spannable parseLinks(String unparsedString){
		ArrayList<SpanHolder> spans = new ArrayList<SpanHolder>();

		SpannableStringBuilder builder = new SpannableStringBuilder(unparsedString);
		int length = builder.length();
		CharSequence tag = null;
		CharSequence data = null;
		int tagStartIndex = -1;
		int tagOpenerEndIndex = -1;
		for (int i=0;i < length;i++){
			switch (builder.charAt(i)){
				case '<':
					if (tagStartIndex == -1){
						tagStartIndex = i;
					}else{
						data = builder.subSequence(tagOpenerEndIndex + 1, i);

						Linkable target = getLinkableData(tag, data);
						if (target != null){
							int tagEndIndex = i + tag.length() + 3;
							builder.replace(tagStartIndex, tagEndIndex, target.getName());

							spans.add(new SpanHolder(target, tagStartIndex));

							length -= tagEndIndex - tagStartIndex + 1;
							length += target.getName().length();
							i -= tagEndIndex - tagStartIndex + 1;
							i += target.getName().length();			
						}


						tagStartIndex = -1;
						tagOpenerEndIndex = -1;
						tag = null;
						data = null;
					}
					break;
				case '>':
					tag = builder.subSequence(tagStartIndex + 1, i);
					tagOpenerEndIndex = i;

			}

		}

		int[] ids = new int[length = spans.size()];
		for (int i=0;i < length;i++){
			SpanHolder span = spans.get(i);
			Linkable target = span.target;
			ids[i] = target.getId();
			builder.setSpan(new LinkSpan(target, ids, i), span.start, span.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}


		return builder;

	}

	private static class SpanHolder{

		public Linkable target;
		public int start;
		public int end;

		public SpanHolder(Linkable target, int start){
			this.target = target;
			this.start = start;
			end = start + target.getName().length();
		}

		public SpanHolder(Linkable target, int start, int end){
			this.target = target;
			this.start = start;
			this.end = end;
		}

	}

	public static class LinkSpan extends ClickableSpan{

		public Linkable mLinkable;
		public final int[] ids;
		public final int index;

		public LinkSpan(Linkable mLinkable, int[] ids, int index){
			this.mLinkable = mLinkable;
			this.ids = ids;
			this.index = index;
		}


		@Override
		public void onClick(View p1){
			Intent intent = new Intent(p1.getContext(), mLinkable.getInfoActivityClass());
			intent.putExtra(InfoActivity.EXTRA_ID_ARRAY, ids);
			intent.putExtra(InfoActivity.EXTRA_ID_INDEX, index);
			p1.getContext().startActivity(intent);

		}



	}

	private static boolean isInArray(String[] array, String string){
		for (String s:array){
			if (s.equals(string))
				return true;
		}

		return false;
	}
}

/*
	 public void getTypeChart(){

	 try{
	 File f = new File("/sdcard/f.txt");
	 f.createNewFile();
	 FileWriter w = new FileWriter(f);




	 int[] gens = {1,2,6};
	 String q1 = "SELECT group_concat((damage_factor/100.0) ||'f') FROM type_efficacy WHERE generation_id = ? AND damage_type_id = ? GROUP BY damage_type_id ORDER BY target_type_id;";
	 w.write( "{ \n");
	 for(int gen = 0; gen<3;gen++){
	 w.write(" { \n");
	 for(int t=1;t<19;t++){
	 Cursor c = dex.rawQuery(q1, new String[]{String.valueOf(gens[gen]),String.valueOf(t)});
	 c.moveToFirst();
	 w.write("{");
	 w.write(c.getString(0));
	 w.write("}, \n");		
	 c.close();
	 }
	 w.write(" },\n");
	 }
	 w.write("}");
	 w.flush();
	 w.close();
	 }catch (Exception e){

	 e.printStackTrace();
	 }
	 }
	 */

