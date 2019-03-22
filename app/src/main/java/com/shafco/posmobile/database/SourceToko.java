package com.shafco.posmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class SourceToko {
    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] allColumnsToko = { DBHelper.COLUMN_ID,
            DBHelper.COLUMN_KODE_TOKO,
            DBHelper.COLUMN_NAMA_TOKO};

    public SourceToko(Context context){dbHelper = new DBHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){ dbHelper.close(); }


    public ModelToko createModelToko(String kode_toko,
                                     String nama_toko){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_KODE_TOKO, kode_toko);
        values.put(DBHelper.COLUMN_NAMA_TOKO, nama_toko);

        long insertId = database.insert(DBHelper.TABLE_NAME_1, null, values);

        Cursor cursor = database.query(DBHelper.TABLE_NAME_1, allColumnsToko,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        ModelToko newModelToko = cursorToModelToko(cursor);
        cursor.close();
        return newModelToko;
    }

    private ModelToko cursorToModelToko(Cursor cursor){
        ModelToko modelToko = new ModelToko();
        modelToko.setId(cursor.getLong(0));
        modelToko.setKode_toko(cursor.getString(1));
        modelToko.setNama_toko(cursor.getString(2));

        return modelToko;
    }

    public void detleteAllToko(){
        database.delete(DBHelper.TABLE_NAME_1,null,null);
        database.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'm_toko'");
    }

    public String getKodeToko(String nama){
        Cursor cursor = database.query(DBHelper.TABLE_NAME_1,allColumnsToko,DBHelper.COLUMN_NAMA_TOKO + " = '" + nama +"'",
                null,null,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(1)+"@@"+cursor.getString(2);

    }

    public String getAllToko() {
        String daftar = "";
        Cursor cursor = database.query(DBHelper.TABLE_NAME_1,
                allColumnsToko,null,null,null,null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                daftar +=  cursor.getString(2) + ",,";
                cursor.moveToNext();
            }
        } else {
            daftar = "-";
        }
        cursor.close();
        return daftar;
    }

}
