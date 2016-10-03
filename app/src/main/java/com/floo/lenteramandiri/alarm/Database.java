/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.floo.lenteramandiri.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/* 
 * usage:  
 * DatabaseSetup.init(egActivityOrContext); 
 * DatabaseSetup.createEntry() or DatabaseSetup.getContactNames() or DatabaseSetup.getDb() 
 * DatabaseSetup.deactivate() then job done 
 */

public class Database extends SQLiteOpenHelper {
	static Database instance = null;
	static SQLiteDatabase database = null;
	
	static final String DATABASE_NAME = "DB";
	static final int DATABASE_VERSION = 1;
	
	public static final String ALARM_TABLE = "alarm";
	public static final String COLUMN_ALARM_ID = "_id";
	public static final String COLUMN_ALARM_TITLE = "alarm_title";
	public static final String COLUMN_ALARM_DATE = "alarm_date";
	public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
	
	public static void init(Context context) {
		if (null == instance) {
			instance = new Database(context);
		}
	}

	public static SQLiteDatabase getDatabase() {
		if (null == database) {
			database = instance.getWritableDatabase();
		}
		return database;
	}

	public static void deactivate() {
		if (null != database && database.isOpen()) {
			database.close();
		}
		database = null;
		instance = null;
	}

	public static long create(Call call) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ALARM_ID, call.getId());
		cv.put(COLUMN_ALARM_TITLE, call.getTitle());
		cv.put(COLUMN_ALARM_DATE, call.getDate());
		cv.put(COLUMN_ALARM_ACTIVE, call.getActive());


		return getDatabase().insert(ALARM_TABLE, null, cv);
	}

	public static int deleteAll(){
		return getDatabase().delete(ALARM_TABLE, null, null);
	}

	public static int update(Call alarm) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ALARM_ACTIVE, alarm.getActive());
					
		return getDatabase().update(ALARM_TABLE, cv, "alarm_date=" + alarm.getDate(), null);
	}

	public static Cursor getCursor() {
		// TODO Auto-generated method stub
		String[] columns = new String[] {
				COLUMN_ALARM_ID,
				COLUMN_ALARM_TITLE,
				COLUMN_ALARM_DATE,
				COLUMN_ALARM_ACTIVE
				};
		return getDatabase().query(ALARM_TABLE, columns, null, null, null, null,
				null);
	}

	Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( " 
				+ COLUMN_ALARM_ID + " INTEGER, "
				+ COLUMN_ALARM_TITLE + " TEXT NOT NULL, "
				+ COLUMN_ALARM_DATE + " LONG NOT NULL, "
				+ COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
		onCreate(db);
	}

	public static List<Call> getAll() {
		List<Call> alarms = new ArrayList<Call>();
		Cursor cursor = Database.getCursor();
		if (cursor.moveToFirst()) {

			do {
				Call call = new Call();
				call.setId(cursor.getInt(0));
				call.setTitle(cursor.getString(1));
				call.setDate(cursor.getLong(2));
				call.setActive(cursor.getInt(3)==1);

				alarms.add(call);

			} while (cursor.moveToNext());			
		}
		cursor.close();
		return alarms;
	}
}