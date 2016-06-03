package com.floo.lenteramandiri.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.floo.lenteramandiri.utils.DataManager;

import java.util.ArrayList;

/**
 * Created by Floo on 2/3/2016.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "SurveyDatabase.db";

    //TABLE RESPONDENCE
    private static final String TABLE_NOTIFICATION = "table_notification";
    private static final String KEY_NOTIFICATION_ID = "id_notification";
    private static final String KEY_NOTIFICATION_TITLE = "title_notification";
    private static final String KEY_NOTIFICATION_CONTENT = "content_notification";
    private static final String KEY_NOTIFICATION_DATE = "date_notification";



    String SYNTAX_NOTIFICATION = "CREATE TABLE "+TABLE_NOTIFICATION+ "("+ KEY_NOTIFICATION_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NOTIFICATION_TITLE + " TEXT, "+ KEY_NOTIFICATION_CONTENT +" TEXT, "+ KEY_NOTIFICATION_DATE + " INTEGER)";
    String DROP_NOTIFICATION = "DROP TABLE IF EXISTS "+TABLE_NOTIFICATION;



    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SYNTAX_NOTIFICATION);
        //db.execSQL("INSERT INTO ");

        /*
        db.execSQL("INSERT INTO table_respondence (id_respondence, nama_respondence, status_respondence, created_at, " +
                "last_modified, latitude, longitude, id_survey) VALUES (1, 'floostudio', 'unlock', '2016-02-02 16:29:57', '2016-02-02 16:29:57', 123, 456, '1');");
                */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_NOTIFICATION);

        onCreate(db);
    }

    /*----------T A B L E  S U R V E Y----------*/

    public void addNotification(Notifi notifi) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NOTIFICATION_TITLE, notifi.getTITLE());
            values.put(KEY_NOTIFICATION_CONTENT, notifi.getCONTENT());
            values.put(KEY_NOTIFICATION_DATE, notifi.getDATE());
            db.insert(TABLE_NOTIFICATION, null, values);
            //Log.d("datanotifikasi", String.valueOf(db.insert(TABLE_NOTIFICATION, null, values)));
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }


    public ArrayList<Notifi> getAllSurvey() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Notifi> surveyList = null;
        try {
            surveyList = new ArrayList<Notifi>();
            String QUERY = "SELECT * FROM " + TABLE_NOTIFICATION+" ORDER BY "+KEY_NOTIFICATION_ID+" DESC";
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    //Log.d("today", (cursor.getInt(3)+2505600)+">" +DataManager.dateToEpoch(DataManager.getDatesNow()));
                    int expire = (cursor.getInt(3)+2505600);
                    if (expire < DataManager.dateToEpoch(DataManager.getDatesNow())){
                        deletePerson(cursor.getInt(3));
                    }else {
                        Notifi survey = new Notifi();
                        survey.setID(cursor.getString(0));
                        survey.setTITLE(cursor.getString(1));
                        survey.setCONTENT(cursor.getString(2));
                        survey.setDATE(cursor.getInt(3));

                        surveyList.add(survey);
                    }

                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return surveyList;
    }

    public Integer deletePerson(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NOTIFICATION,
                KEY_NOTIFICATION_DATE + " = ? ",
                new String[] { Integer.toString(id) });
    }
}
