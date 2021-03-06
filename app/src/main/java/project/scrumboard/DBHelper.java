package project.scrumboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import project.scrumboard.DataContract.DataEntry;

/**
 * Created by Spencer on 3/29/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "Data.db";
    public String TABLE_NAME;
    public static int number = 0;

    private static final String TEXT_TYPE = " TEXT";
    private String SQL_CREATE_ENTRIES;
    private String SQL_DELETE_ENTRIES;

    public DBHelper(final Context context, final String name)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        TABLE_NAME = name;
        Log.d("table name", TABLE_NAME);
        if(TABLE_NAME.equals("posts")){
            SQL_CREATE_ENTRIES =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                            DataEntry._ID + " INTEGER PRIMARY KEY," +
                            "title " + "text," +
                            "description " + "text," +
                            "members " + "text," +
                            "priority " + "INTEGER," +
                            "column " + "text," +
                            "row " + "text )";
        }
        else {
            SQL_CREATE_ENTRIES =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                            DataEntry._ID + " INTEGER PRIMARY KEY," +
                            DataEntry.COLUMN_NAME_VALUE + TEXT_TYPE +
                            " )";
        }
        SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public void onCreate(final SQLiteDatabase db)
    {
        Log.d("We are in oncreate", " yes");
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(String insertVal){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("table name on insert", TABLE_NAME);
        ContentValues values = new ContentValues();
        values.put("value", insertVal);
        db.insert(TABLE_NAME, null, values);
        db.close();
        //db.execSQL("INSERT or REPLACE into data (value) values('anything here')");
    }

    public void insertPost(String title,
                           String description,
                           String members,
                           int priority,
                           String columns,
                           String rows){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("table name on insert", TABLE_NAME);
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("members", members);
        values.put("priority", priority);
        values.put("column", columns);
        values.put("row", rows);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> select(){
        number = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Log.d("xxxxxX", TABLE_NAME);
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        //String[] data      =  new String[1];
        ArrayList<String> data = new ArrayList<String>();
        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(1));
                number++;
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public String selectPostTitle(String col, String row){
        String selectQuery = "SELECT  * FROM " + TABLE_NAME +
                " WHERE column = '"+col+"' and row = '"+row+"'";
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String data = "FAILED";

        if (cursor.moveToFirst()) {
            data = cursor.getString(1);
        }
        cursor.close();
        return data;
    }

    public void deleteSinglePost(String title, String col, String row){
        SQLiteDatabase db  = this.getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME +
                " WHERE title = '"+title+"' and column = '"+col+"' and row = '"+row+"'";
        db.execSQL(query);
    }
    public void deleteRow(String row){
        SQLiteDatabase db  = this.getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME +
                " WHERE row = '"+row+"'";
        db.execSQL(query);
    }
    public void deleteCol(String col){
        SQLiteDatabase db  = this.getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME +
                " WHERE column = '"+col+"'";
        db.execSQL(query);
    }
    public void updatePost(String title,
                           String col,
                           String row,
                           String newTitle,
                           String newCol,
                           String newRow,
                           String newDesc,
                           String newMem,
                           int newPriority){
        SQLiteDatabase db  = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_NAME +
                " set column = '"+newCol+"', row = '"+newRow+"', description = '"+newDesc+
                "', members = '"+newMem+"', priority = '"+newPriority+"', title = '"+newTitle+"'"+
                " WHERE title = '"+title+"' and column = '"+col+"' and row = '"+row+"'";
        db.execSQL(query);
    }
    public String[] getValues(String title, String col, String row){
        Log.d("title is ", title);
        Log.d("col is ", col);
        Log.d("row is ", row);
        String[] values = new String[6];
        SQLiteDatabase db  = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE title = '"+title+"' and column = '"+col+"' and row = '"+row+"'";
        //db.execSQL(query);
        Cursor cursor      = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                values[0] = cursor.getString(1);
                values[1] = cursor.getString(2);
                values[2] = cursor.getString(3);
                values[3] = String.valueOf(cursor.getInt(4));
                values[4] = cursor.getString(5);
                values[5] = cursor.getString(6);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return values;
    }


    public void delete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void setup(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_CREATE_ENTRIES);
    }
}