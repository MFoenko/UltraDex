package com.mikhail.pokedex.data;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

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








	public static final int GEN = 6;
	public static final int LANG = 9;





	public Pokemon[] getAllPokemon(){
		return getAllPokemon(GEN, LANG);
	}


	public Pokemon[] getAllPokemon(int gen, int lang){
		/*

		 SELECT p._id, p.species_id, IFNULL(fn.pokemon_name, sn.name)
		 FROM pokemon AS p JOIN pokemon_forms AS f ON (p._id = f.pokemon_id) 
		 JOIN pokemon_species_names AS sn ON (p.species_id = sn.pokemon_species_id)
		 JOIN pokemon_form_names AS fn ON (f.id = fn.pokemon_form_id AND sn.local_language_id = fn.local_language_id )
		 WHERE fn.local_language_id = ?; 

		 */
		String query = 
			"SELECT p._id, p.species_id, IFNULL(fn.pokemon_name, sn.name), f.form_identifier FROM pokemon AS p JOIN pokemon_forms AS f ON (p._id = f.pokemon_id) JOIN pokemon_species_names AS sn ON (p.species_id = sn.pokemon_species_id) JOIN pokemon_form_names AS fn ON (f.id = fn.pokemon_form_id AND sn.local_language_id = fn.local_language_id ) WHERE fn.local_language_id = ? ORDER BY p.species_id"; 

		Cursor c = this.dex.rawQuery(query, new String[]{String.valueOf(lang)});
		int length;
		Pokemon[] pokemon = new Pokemon[length = c.getCount()];
		Pokemon.Builder builder = new Pokemon.Builder();
		for (int i=0;i < length;i++){
			c.moveToNext();
			builder.id(c.getInt(0))
				.dispId(c.getInt(1))
				.name(c.getString(2))
				.suffix(c.getString(3));
			pokemon[i] = builder.build();
		}
		c.close();
		return pokemon;
	}

	public Pokemon getPokemon(int id){
		return getPokemon(id, GEN, LANG);
	}

	public Pokemon getPokemon(int id, int gen, int lang){
		/*

		 SELECT p._id, p.species_id, IFNULL(fn.pokemon_name, sn.name)
		 FROM pokemon AS p JOIN pokemon_forms AS f ON (p._id = f.pokemon_id) 
		 JOIN pokemon_species_names AS sn ON (p.species_id = sn.pokemon_species_id)
		 JOIN pokemon_form_names AS fn ON (f.id = fn.pokemon_form_id AND sn.local_language_id = fn.local_language_id )
		 WHERE fn.local_language_id = ?; 

		 */
		String query = 
			"SELECT p._id, p.species_id, IFNULL(fn.pokemon_name, sn.name), f.form_identifier FROM pokemon AS p JOIN pokemon_forms AS f ON (p._id = f.pokemon_id) JOIN pokemon_species_names AS sn ON (p.species_id = sn.pokemon_species_id) JOIN pokemon_form_names AS fn ON (f.id = fn.pokemon_form_id AND sn.local_language_id = fn.local_language_id ) WHERE p._id = ? AND fn.local_language_id = ? ORDER BY p.species_id"; 

		Cursor c = this.dex.rawQuery(query, new String[]{String.valueOf(id), String.valueOf(lang)});
		int length;
		Pokemon.Builder builder = new Pokemon.Builder();
		c.moveToFirst();
		builder.id(c.getInt(0))
			.dispId(c.getInt(1))
			.name(c.getString(2))
			.suffix(c.getString(3));

		c.close();
		return builder.build();
	}



}
