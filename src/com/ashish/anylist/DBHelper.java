/*
 * Copyright 2014 AshishGudla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* 
 * Database helper class to manage sqlite database.
 * Source : http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/ 
*/

package com.ashish.anylist;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper 
{

	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "AnyListManager";
 
    // Table Names
    private static final String TABLE_MAINLISTS = "mainlist";
    private static final String TABLE_LISTELEMENTS = "listelements";
 
    // Common column names
    private static final String KEY_ID = "id";
 
    // MAINLISTS Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_CREATEDON = "createdon";
    private static final String KEY_MODIFIEDON = "modifiedon";
 
    // LISTELEMENTS Table - column names
    private static final String KEY_LISTID = "listid";
    private static final String KEY_ELEMENT = "element";
 
    
    // Table Create Statements
    
    // MAINLISTS table create statement
    private static final String CREATE_TABLE_MAINLISTS = "CREATE TABLE "
            + TABLE_MAINLISTS + "(" 
    		+ KEY_ID + " INTEGER PRIMARY KEY," 
            + KEY_NAME + " TEXT," 
    		+ KEY_CREATEDON + " DATETIME,"  
            + KEY_MODIFIEDON + " DATETIME" + ")" ;
 
    // LISTELEMENTS table create statement
    private static final String CREATE_TABLE_LISTELEMENTS = "CREATE TABLE " + TABLE_LISTELEMENTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LISTID + " INTEGER,"
            + KEY_ELEMENT + " TEXT" + ")";
 
    //Constructors
    public DBHelper(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	public DBHelper(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// Create tables
		db.execSQL(CREATE_TABLE_MAINLISTS);
        db.execSQL(CREATE_TABLE_LISTELEMENTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTELEMENTS);
    
        // create new tables
        onCreate(db);
		
	}
	
	//add a new mainlist record
	public boolean createMainList (MainList ml)
	{
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    values.put(KEY_NAME, ml.get_name());
	    values.put(KEY_CREATEDON, sqlDate.toString());
	    values.put(KEY_MODIFIEDON, sqlDate.toString());
	    db.insert(TABLE_MAINLISTS, null, values);
	    return true;
	}
	
	//add a new listelement record
	public boolean createListElement (ListElement le)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    values.put(KEY_LISTID, le.get_list_id());
	    values.put(KEY_ELEMENT, le.element);
	    db.insert(TABLE_LISTELEMENTS, null, values);
	    return true;
	}
	
	//get all mainlist records
	public List<MainList> fetchAllMainLists()
	{
		List<MainList> mls = new ArrayList<MainList>();
	    String selectQuery = "SELECT  * FROM " + TABLE_MAINLISTS;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) 
	    {
	        do 
	        {
	            MainList ml = new MainList();
	            ml.set_id(c.getInt((c.getColumnIndex(KEY_ID))));
	            ml.set_name((c.getString(c.getColumnIndex(KEY_NAME))));
	            ml.set_created_on(c.getString(c.getColumnIndex(KEY_CREATEDON)));
	            ml.set_modified_on(c.getString(c.getColumnIndex(KEY_MODIFIEDON)));
	            mls.add(ml);
	        } while (c.moveToNext());
	    }
	 
	    return mls;
	}
	
	//get all listelement records
	public List<ListElement> fetchAllListElements(int listid)
	{
		List<ListElement> les = new ArrayList<ListElement>();
	    String selectQuery = "SELECT  * FROM " + TABLE_LISTELEMENTS 
	             + " WHERE "
	            + KEY_LISTID + " = " + listid   ;
	    
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) 
	    {
	        do 
	        {
	            ListElement le = new ListElement();
	            le.set_id(c.getInt((c.getColumnIndex(KEY_ID))));
	            le.set_list_id(c.getInt((c.getColumnIndex(KEY_LISTID))));
	            le.set_elem((c.getString(c.getColumnIndex(KEY_ELEMENT))));
	            les.add(le);
	        } while (c.moveToNext());
	    }
	 
	    return les;
	}
	
	//remove a mainlist record
	public void deleteMainList(int id)
	{
		    SQLiteDatabase db = this.getWritableDatabase();
		    db.delete(TABLE_MAINLISTS, KEY_ID + " = ?",
		            new String[] { String.valueOf(id) });
		
	}
	
	//remove a list element record
	public void deleteListElement(int id)
	{
		    SQLiteDatabase db = this.getWritableDatabase();
		    db.delete(TABLE_LISTELEMENTS, KEY_ID + " = ?",
		            new String[] { String.valueOf(id) });
		
	}
	
	//remove list element records of a particular main list element
	public void deleteListElementofList(int listid)
	{
		    SQLiteDatabase db = this.getWritableDatabase();
		    db.delete(TABLE_LISTELEMENTS, KEY_LISTID + " = ?",
		            new String[] { String.valueOf(listid) });
		
	}
	
	//update a mainlist record
	public int updateMainList (MainList ml)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_NAME, ml.get_name());
	 
	    // updating row
	    return db.update(TABLE_MAINLISTS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(ml.get_id()) });
	}
	
	//update a list element record
	public int updateListElement (ListElement le)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_ELEMENT, le.get_element());
	 
	    // updating row
	    return db.update(TABLE_LISTELEMENTS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(le.get_id()) });
	}
	
	//close DB connection
	public void closeDB() 
	{
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
