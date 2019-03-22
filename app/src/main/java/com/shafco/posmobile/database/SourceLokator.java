package com.shafco.posmobile.database;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.util.Log;
/**
 * Created by BoTaXs on 08-06-2017.
 */

public class SourceLokator {
    private SQLiteDatabase database;

    private DBHelper dbHelper;


    private String[] allColumnsLokator = { DBHelper.COLUMN_ID,
            DBHelper.COLUMN_KODE_LOKATOR,
            DBHelper.COLUMN_NAMA_LOKATOR};


    public SourceLokator(Context context){dbHelper = new DBHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){ dbHelper.close(); }

    public ModelLokator createModelLokator(String kode_lokator,
                                           String nama_loaktor){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_KODE_LOKATOR, kode_lokator);
        values.put(DBHelper.COLUMN_NAMA_LOKATOR, nama_loaktor);

        long insertId = database.insert(DBHelper.TABLE_NAME_2, null, values);

        Cursor cursor = database.query(DBHelper.TABLE_NAME_2, allColumnsLokator,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        ModelLokator newModelLokator = cursorToModelLokator(cursor);
        cursor.close();
        return newModelLokator;
    }

    public void justCreate(String a){

        ContentValues contentValues = new
                ContentValues();
        contentValues.put(DBHelper.COLUMN_KODE_LOKATOR, a);
        contentValues.put(DBHelper.COLUMN_NAMA_LOKATOR, a);
        long insertId = database.insertWithOnConflict(DBHelper.TABLE_NAME_2, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("Lokator",  a);
    }

    private ModelLokator cursorToModelLokator(Cursor cursor){
        ModelLokator modelLokator = new ModelLokator();
        modelLokator.setId(cursor.getLong(0));
        modelLokator.setKode_lokator(cursor.getString(1));
        modelLokator.setNama_lokator(cursor.getString(2));

        return modelLokator;
    }

    public String getAllLokator() {
        String daftar = "";
        Cursor cursor = database.query(DBHelper.TABLE_NAME_2,
                allColumnsLokator,null,null,null,null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                daftar += cursor.getString(2) + ",,";
                cursor.moveToNext();
            }
        } else {
            daftar = "-";
        }
        cursor.close();
        return daftar;
    }

    public void delete(){
        int cursor = database.delete(DBHelper.TABLE_NAME_2,null,null);
    }

    public void create(){
        for(int z = 1; z <= 100; z++){
            justCreate("A"+z);
        }
        for(int z = 1; z <= 100; z++){
            justCreate("B"+z);
        }
    }
}
