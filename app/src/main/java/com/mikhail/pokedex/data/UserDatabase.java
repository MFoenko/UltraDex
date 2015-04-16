package com.mikhail.pokedex.data;

import android.content.*;
import android.database.sqlite.*;

public class UserDatabase extends SQLiteOpenHelper
{
	public static final String DB_NAME = "user";
	public static final int DB_VERSION = 5;
	
	public static final String DB_NAME = "user";
	
	
	private static UserDatabase instance;
	private SQLiteDatabase db;
	
	public static UserDatabase getInstance(Context c){
		if(instance == null){
			instance = new UserDatabase(c);
		}
		return instance;
	}
	
	public UserDatabase(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		db = getReadableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase p1)
	{
		p1.execSQL(
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int p2, int p3)
	{
		// TODO: Implement this method
	}


	
	
	
	
}
