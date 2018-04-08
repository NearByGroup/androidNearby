package com.sword.yukti.nearby;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBhelper extends SQLiteOpenHelper {

    public DBhelper(Context context) {
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String user_info=("create table User_info_details (user_id INTEGER PRIMARY KEY AUTOINCREMENT,user_email VARCHAR,user_password VARCHAR,user_address VARCHAR,alternate_number VARCHAR)");
        sqLiteDatabase.execSQL(user_info);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void addRegisterValues(Getter_setter getter_setter)
    {   SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.USER_EMAIL, getter_setter.getEmail());
        values.put(Database.USER_PASSWORD, getter_setter.getPassword());
        values.put(Database.USER_ADDRESS,getter_setter.getAddress());
        values.put(Database.ALTERNATE_NUMBER, getter_setter.getAlternate_number());

        db.insert(Database.USER_INFO_TABLE , null, values);
    }
}
