package com.shafco.posmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class DBDataSource {

    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] allColumnsToko = { DBHelper.COLUMN_ID,
            DBHelper.COLUMN_KODE_TOKO,
            DBHelper.COLUMN_NAMA_TOKO};

    private String[] allColumnsAlokator = { DBHelper.COLUMN_ID,
            DBHelper.COLUMN_KODE_LOKATOR,
            DBHelper.COLUMN_NAMA_LOKATOR};

    private String[] allColumnsProduk = {DBHelper.COLUMN_ID,
            DBHelper.COLUMN_KODE_PRODUK,
            DBHelper.COLUMN_NAMA_PRODUK,
            DBHelper.COLUMN_HPJ,
            DBHelper.COLUMN_R_PRODUK_ID};

    private String[] allColumnsOpname = {DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TEAM,
            DBHelper.COLUMN_TANGGAL,
            DBHelper.COLUMN_JAM,
            DBHelper.COLUMN_KODE_TOKO,
            DBHelper.COLUMN_KODE_LOKATOR,
            DBHelper.COLUMN_KODE_PRODUK,
            DBHelper.COLUMN_QTY,
            DBHelper.COLUMN_STATUS};

    public DBDataSource(Context context){dbHelper = new DBHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){ dbHelper.close(); }

    public ModelOpname createModelOpname(   int team,
                                            String tanggal,
                                            String jam,
                                            String kode_toko,
                                            String kode_lokator,
                                            String kode_produk,
                                            int qty,
                                            String status){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TEAM, team);
        values.put(DBHelper.COLUMN_TANGGAL, tanggal);
        values.put(DBHelper.COLUMN_JAM, jam);
        values.put(DBHelper.COLUMN_KODE_TOKO, kode_toko);
        values.put(DBHelper.COLUMN_KODE_LOKATOR, kode_lokator);
        values.put(DBHelper.COLUMN_KODE_PRODUK, kode_produk);
        values.put(DBHelper.COLUMN_QTY, qty);
        values.put(DBHelper.COLUMN_STATUS, status);

        long insertId = database.insert(DBHelper.TABLE_NAME_4, null, values);

        Cursor cursor = database.query(DBHelper.TABLE_NAME_4, allColumnsOpname,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null ,null);

        cursor.moveToFirst();
        ModelOpname newModelOpname = cursorToModelOpname(cursor);
        cursor.close();
        return newModelOpname;
    }

    private ModelOpname cursorToModelOpname(Cursor cursor){
        ModelOpname modelOpname = new ModelOpname();
        modelOpname.setId(cursor.getLong(0));
        modelOpname.setTeam(cursor.getString(1));
        modelOpname.setTanggal(cursor.getString(2));
        modelOpname.setJam(cursor.getString(3));
        modelOpname.setKode_toko(cursor.getString(4));
        modelOpname.setKode_alokator(cursor.getString(5));
        modelOpname.setKode_produk(cursor.getString(6));
        modelOpname.setQty(cursor.getInt(7));
        modelOpname.setStatus(cursor.getString(8));

        return modelOpname;
    }

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

    public ModelLokator createModelLokator(String kode_lokator,
                                           String nama_loaktor){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_KODE_LOKATOR, kode_lokator);
        values.put(DBHelper.COLUMN_NAMA_LOKATOR, nama_loaktor);

        long insertId = database.insert(DBHelper.TABLE_NAME_2, null, values);

        Cursor cursor = database.query(DBHelper.TABLE_NAME_2, allColumnsAlokator,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        ModelLokator newModelLokator = cursorToModelLokator(cursor);
        cursor.close();
        return newModelLokator;
    }

    private ModelLokator cursorToModelLokator(Cursor cursor){
        ModelLokator modelLokator = new ModelLokator();
        modelLokator.setId(cursor.getLong(0));
        modelLokator.setKode_lokator(cursor.getString(1));
        modelLokator.setNama_lokator(cursor.getString(2));

        return modelLokator;
    }

}
