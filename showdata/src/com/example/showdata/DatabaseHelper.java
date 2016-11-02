package com.example.showdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	public DatabaseHelper(Context context) {	 
		super(context, "shujuku.db", null, 1);
	}	
	@Override
	public void onCreate(SQLiteDatabase db){
		String table="create table songtable(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "singer varchar(15),song varchar(15))";
		
		db.execSQL(table);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
}
