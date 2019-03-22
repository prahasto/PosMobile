package com.shafco.posmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class SourceProduk {
    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] allColumnsProduk = {DBHelper.COLUMN_ID,
            DBHelper.COLUMN_KODE_PRODUK,
            DBHelper.COLUMN_NAMA_PRODUK,
            DBHelper.COLUMN_HPJ,
            DBHelper.COLUMN_R_PRODUK_ID};

    public SourceProduk(Context context){dbHelper = new DBHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){ dbHelper.close(); }


    public ModelProduk createModelProduk(String kode_produk,
                                         String nama_produk,
                                         int hpj,
                                         int r_produk_id){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_KODE_PRODUK, kode_produk);
        values.put(DBHelper.COLUMN_NAMA_PRODUK, nama_produk);
        values.put(DBHelper.COLUMN_HPJ, hpj);
        values.put(DBHelper.COLUMN_R_PRODUK_ID, r_produk_id);

        long insertId = database.insert(DBHelper.TABLE_NAME_3, null, values);

        Cursor cursor = database.query(DBHelper.TABLE_NAME_3, allColumnsProduk,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        ModelProduk newModelProduk = cursorTOModelProduk(cursor);
        cursor.close();
        return newModelProduk;
    }

    private ModelProduk cursorTOModelProduk(Cursor cursor){
        ModelProduk modelProduk = new ModelProduk();
        modelProduk.setId(cursor.getLong(0));
        modelProduk.setKode_poduk(cursor.getString(1));
        modelProduk.setNama_produk(cursor.getString(2));
        modelProduk.setHpj(cursor.getInt(3));
        modelProduk.setR_produk_id(cursor.getInt(4));

        return modelProduk;
    }

    public String getLastid(){
        Cursor cursor = database.query(DBHelper.TABLE_NAME_3,allColumnsProduk,
                null,null,null,null,DBHelper.COLUMN_R_PRODUK_ID+" DESC");
        String newProdukId = "0";
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            newProdukId = cursor.getString(4);
        }

        return newProdukId;
    }

    public String kodeChecker(String r_produk_id){
        String res = null;
        Cursor cursor = database.query(DBHelper.TABLE_NAME_3,allColumnsProduk,
                DBHelper.COLUMN_R_PRODUK_ID+" = '"+r_produk_id+"'",null,null,null,null);
        if(cursor.getCount() > 0){
            res = "DONE";
            cursor.moveToFirst();
        } else{
            res = "FAIL";
        }
        return res;
    }

    public int insertproduk(String kode_produk,
                            String nama_produk,
                            int hpj,
                            int r_produk_id) {
        int llint;
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_KODE_PRODUK, kode_produk);
        values.put(DBHelper.COLUMN_NAMA_PRODUK, nama_produk);
        values.put(DBHelper.COLUMN_HPJ, hpj);
        values.put(DBHelper.COLUMN_R_PRODUK_ID, r_produk_id);

        long insertId = database.insert(DBHelper.TABLE_NAME_3, null, values);


       return (int) insertId ;
    }

    public String scanChecker(String kode_produk){
        String res = null;
        Cursor cursor = database.query(DBHelper.TABLE_NAME_3,allColumnsProduk,
                DBHelper.COLUMN_KODE_PRODUK+" = '"+kode_produk+"'",null,null,null,null);
        if (cursor.getCount() > 0) {
            res = "EXIST";
            cursor.moveToFirst();
        } else {
            res = "UNKNOW";
        }
        Log.d("getproduk", res);
        return res;
    }
}
