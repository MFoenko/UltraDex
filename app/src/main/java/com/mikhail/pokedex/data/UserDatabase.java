package com.mikhail.pokedex.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

import com.mikhail.pokedex.activities.InfoActivity;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper
{
	public static final String DB_NAME = "user";
	public static final int DB_VERSION = 5;

    public static final String COLLECTION_CREATE_STRING = "CREATE TABLE collections (id INTEGER PRIMARY KEY, name VARCHAR(30), order INTEGER, icon_shape INTEGER(2), icon_border_thickness INTEGER(3), icon_border_color INTEGER(10), icon_solid_color INTEGER(10), icon_rotate INTEGER(3))";
    public static final String FAVORITES_CREATE_STRING = "CREATE TABLE favorites (_id INTEGER PRIMARY KEY, favorites_list_id INTEGER, type INTEGER, item_id INTEGER, order INTEGER) ";
    public static final String FAVORITES_ADD_LIST_ID = "ALTER TABLE favorites ADD COLUMN collection_id INTEGER";
    public static final String FAVORITES_ADD_ORDER = "ALTER TABLE favorites ADD COLUMN order INTEGER";

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
        p1.execSQL(COLLECTION_CREATE_STRING);
        p1.execSQL(FAVORITES_CREATE_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int oldV, int newV)
	{
		switch(oldV){
            case 4:
                p1.execSQL(FAVORITES_ADD_LIST_ID);
                p1.execSQL(FAVORITES_ADD_ORDER);
        }
	}


    public static class Collection extends PokedexClasses.VarComparableDexObject<Collection>{

        public ArrayList<PokedexClasses.DexObject> listItems;

        public Collection(int id, String name) {
            super(id, name);
        }



        @Override
        public Class<? extends InfoActivity> getInfoActivityClass() {
            return null;
        }

        @Override
        public int compareTo(Collection other, int compareOn) {
            return 0;
        }

    }

    private final static String COLLECTION_ROWS = "id, name, order, icon_shape, icon_border_thickness, icon_border_color, icon_solid_color, icon_rotate";

    private static Collection getCollection(Cursor c){
        Collection collection = new Collection(c.getInt(0), c.getString(1));
        return collection;
    }

	public Collection[] getAllCollections(){
        Cursor c = db.rawQuery("SELECT "+COLLECTION_ROWS+" FROM collections;", null);

        Collection[] collections = new Collection[c.getCount()];
        for(int i=0;i<collections.length;i++){
            c.moveToNext();
            collections[i] = getCollection(c);
        }

        return collections;
    }



    public Collection getCollection(int id){
        Cursor c = db.rawQuery("SELECT "+COLLECTION_ROWS+" FROM collections WHERE id=?;", new String[]{String.valueOf(id)});
        if(c.moveToFirst()){
            return getCollection(c);
        }else{
            return null;
        }
    }

    public int insertCollection(Collection collection){
        db.execSQL("INSERT INTO collections (name) VALUES(?,?)", new Object[]{collection.name});
        Cursor c = db.rawQuery("SELECT last_insert_rowid();", null);
        c.moveToFirst();
        return c.getInt(0);
    }

    public void deleteCollection(Collection collection){
        db.execSQL("DELETE FROM collections WHERE id = ?", new Object[]{collection.id});
    }

    public void updateCollection(Collection collection){
        db.execSQL("UPDATE collections SET VALUES name = ? WHERE id = ?", new Object[]{collection.name, collection.id});
    }




}
