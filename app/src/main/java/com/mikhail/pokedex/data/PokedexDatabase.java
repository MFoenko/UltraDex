package com.mikhail.pokedex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by MFoenko on 3/7/2015.
 */
public class PokedexDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "veekun-pokedex.sqlite";
    public static final int DATABASE_VERSION = 1;

    private static PokedexDatabase instance;
    private Context mContext;
    private SQLiteDatabase dex;

    public PokedexDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        dex = getReadableDatabase();
    }

    public static PokedexDatabase getInstance(Context context){
        if(instance == null){
            instance = new PokedexDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        copyDatabaseFromAssets(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        copyDatabaseFromAssets(db);
    }

    public void copyDatabaseFromAssets(SQLiteDatabase db){

        String oldDbPath = db.getPath();
        try {
            mContext.getAssets();
            InputStream input = mContext.getAssets().open(DATABASE_NAME);
            OutputStream output = new FileOutputStream(oldDbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            input.close();
            output.close();
        } catch (IOException e) {
            Log.e("AAA", "IOEXCEPTION", e);
        }
    }
}
