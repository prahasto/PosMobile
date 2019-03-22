package com.shafco.posmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

import com.opencsv.CSVWriter;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Date;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Build.ID;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class SourceOpname {
    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] allColumnsOpname = {DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TEAM,
            DBHelper.COLUMN_TANGGAL,
            DBHelper.COLUMN_JAM,
            DBHelper.COLUMN_KODE_TOKO,
            DBHelper.COLUMN_KODE_LOKATOR,
            DBHelper.COLUMN_KODE_PRODUK,
            DBHelper.COLUMN_QTY,
            DBHelper.COLUMN_STATUS};


    public SourceOpname(Context context){dbHelper = new DBHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public boolean deleteOpname() {
        boolean res = false;
         int o = database.delete(DBHelper.TABLE_NAME_4,null,null);
          if(o > 0){
             res = true;
         }
        return res;
    }

    public boolean exportDatabase(String tim,String kodetoko) {
        //  WriteBtn();
        Log.d("csv", "data export");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        /**First of all we check if the external storage of the device is available for writing.
         * Remember that the external storage is not necessarily the sd card. Very often it is
         * the device storage.
         */
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
              return false;
        } else {
            //We use the Download directory for saving our .csv file.
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file;
            PrintWriter printWriter = null;
            try {
                file = new File(exportDir, kodetoko+"_"+tim+"_"+"_opname.csv");
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));

                /**This is our database connector class that reads the data from the database.
                 * The code of this class is omitted for brevity.
                 */
                ////    DBCOurDatabaseConnector dbcOurDatabaseConnector = new DBCOurDatabaseConnector(myContext);
                //dbcOurDatabaseConnector.openForReading(); //open the database for reading

                /**Let's read the first table of the database.
                 * getFirstTable() is a method in our DBCOurDatabaseConnector class which retrieves a Cursor
                 * containing all records of the table (all fields).
                 * The code of this class is omitted for brevity.
                 */
                //    Cursor curCSV = dbcOurDatabaseConnector.getFirstTable();
                //Write the name of the table and the name of the columns (comma separated values) in the .csv file.
                printWriter.println("TABLE Opname");
                printWriter.println("Team,Tanggal,Jam,Kode_Toko,Kode_Lokator,Kode_Produk,Qty,Status");

               // Cursor cursor = database.query(DBHelper.TABLE_NAME_4,allColumnsOpname,null,null,null,null,null);
                Cursor cursor = database.query(DBHelper.TABLE_NAME_4, allColumnsOpname,
                                DBHelper.COLUMN_TEAM + " = '"+tim+"' and "+ DBHelper.COLUMN_KODE_TOKO + " = '"+kodetoko+"'",null
                        ,null,null,DBHelper.COLUMN_ID + " ASC");

                if(cursor.getCount()>0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        String team = cursor.getString(1);
                        String toko = cursor.getString(4);
                        String lokator = cursor.getString(5);
                        String produk = cursor.getString(6);
                        String tanggal = cursor.getString(2);
                        String jam = cursor.getString(3);
                        String qty = cursor.getString(7);
                        String status = cursor.getString(8);



                       // String record = team;
                        String record =team + "," + tanggal + "," + jam + "," + toko + "," + lokator+ "," + produk+ "," + qty+ "," + status;
                        printWriter.println(record); //write the record in the .csv file
                        cursor.moveToNext();
                    }
                }
                ///  while (curCSV.moveToNext()) {
                //Long date = curCSV.getLong(curCSV.getColumnIndex("date"));
                //String item = curCSV.getString(curCSV.getColumnIndex("item"));
                //Double amount = curCSV.getDouble(curCSV.getColumnIndex("amount"));
                //String currency = curCSV.getString(curCSV.getColumnIndex("currency"));

                /**Create the line to write in the .csv file.
                 * We need a String where values are comma separated.
                 * The field date (Long) is formatted in a readable text. The amount field
                 * is converted into String.
                 */
                //  String record = df.format(new Date(date)) + "," + item + "," + importo.toString() + "," + currency;
                //   printWriter.println(record); //write the record in the .csv file
                //}

                //curCSV.close();
                //dbcOurDatabaseConnector.close();
            } catch (Exception exc) {
                //if there are any exceptions, return false
                  return false;
            } finally {
                if (printWriter != null) printWriter.close();
            }

            //If there are no errors, return true.
            return true;

        }
    }




    public void close(){ dbHelper.close(); }

    public ModelOpname createModelOpname(   String team,
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

    public String[] getLastRecord(String kode_lokator, String kode_toko, String team, String tanggal){
        String[] result = new String[6];
        /*Cursor filter1 = database.rawQuery("Select id,kode_produk from opname order by id desc limit 1",null);
        if(filter1.getCount() > 0){
            filter1.moveToFirst();
            String kode_produk = filter1.getString(1);
            filter1.close();*/
            Cursor cursor = database.rawQuery("SELECT \n" +
                    "a.kode_produk,\n" +
                    "a.tanggal,\n" +
                    "a.qty,\n" +
                    "b.hpj,\n" +
                    "(SELECT sum(qty) from opname where status = '1') as qty_total,\n" +
                    "(SELECT count(*) from opname where status = '1') as row\n" +
                    "FROM Opname as a join m_produk as b ON a.kode_produk  = b.kode_produk\n" +
                     "where status = '1' and a.kode_lokator = '"+kode_lokator+"' and a.kode_toko = '"+kode_toko+"' " +
                    //     "where  a.kode_lokator = '"+kode_lokator+"' and a.kode_toko = '"+kode_toko+"' " +
                    "and a.team = '"+team+"' and a.tanggal = '"+tanggal+"'  order by a.id desc limit 1\n",null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result[0] = cursor.getString(0);
                result[1] = cursor.getString(1);
                result[2] = cursor.getString(2);
                result[3] = cursor.getString(3);
                /*int qty_tot = 0;
                while(!cursor.isAfterLast()){
                    qty_tot += cursor.getInt(2);
                    cursor.moveToNext();
                }
                result[4] = String.valueOf(qty_tot);
                result[5] = String.valueOf(cursor.getCount());*/
                result[4] = cursor.getString(4);
                result[5] = cursor.getString(5);
            }
        //}
        return result;
    }






    public String[] listView(String kode_toko, String kode_lokator){
        //ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>;
        //ArrayList<ArrayList<String>>
        String[] res;
        Cursor cursor = database.rawQuery("SELECT   a.kode_produk, " +
                                                    "b.nama_produk," +
                                                    "a.qty, " +
                                                    "a.team," +
                                                    "a.tanggal," +
                                                    "a.jam, a.kode_lokator, a.id " +
                "FROM opname a join " +
                "m_produk b " +
                "ON a.kode_produk = b.kode_produk " +
             //  "where a.status = '1' and a.kode_toko = '"+kode_toko+"' and a.kode_lokator = '"+kode_lokator+"' group by a.id" +
                "where a.kode_toko = '"+kode_toko+"' and a.kode_lokator = '"+kode_lokator+"' group by a.id " +
             //   "where  a.kode_toko = '"+kode_toko+"' group by a.id" +

                "",null);
        String get = "";
        if(cursor != null ){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                get += cursor.getString(0) + "@@" +
                        cursor.getString(1) + "@@" +
                        cursor.getString(2) + "@@" +
                        cursor.getString(3) + "@@" +
                        cursor.getString(4) + " " + cursor.getString(5) + "@@" + cursor.getString(6) + ",,,";
                cursor.moveToNext();
            }
        }else {
            get = "@@";
        }

        res = get.split(",,,");


        return res;
    }

    public JSONArray sendData(){
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();

          Cursor cursor = database.query(DBHelper.TABLE_NAME_4,allColumnsOpname,DBHelper.COLUMN_STATUS+" = '1'",null,null,null,null,null);
                  //- aji                  Cursor cursor = database.query(DBHelper.TABLE_NAME_4,allColumnsOpname,null,null,null,null,null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                try {
                    obj.put("team", cursor.getString(1));
                    obj.put("tanggal", cursor.getString(2));
                    obj.put("jam", cursor.getString(3));
                    obj.put("kode_toko",cursor.getString(4));
                    obj.put("kode_lokator", cursor.getString(5));
                    obj.put("kode_produk", cursor.getString(6));
                    obj.put("qty", cursor.getString(7));
                    //obj.put("kode_produk", cursor.getString(6));
                } catch (JSONException e){
                    e.printStackTrace();
                }
                ContentValues values = new ContentValues();
                values.put(DBHelper.COLUMN_STATUS, "0");
                database.update(DBHelper.TABLE_NAME_4,values,DBHelper.COLUMN_ID+" = "+cursor.getString(0),null);
                cursor.moveToNext();
            }
            arr.put(obj);
        }
    return arr;
    }

    public int getCountOpname(String kode_toko, String team){
        int count = 0;
        Cursor cursor = database.rawQuery("Select count(*) from opname where status = '1' and kode_toko = '"+kode_toko+"' and team = '"+team+"'", null);
        // aji  Cursor cursor = database.rawQuery("Select count(*) from opname where kode_toko = '"+kode_toko+"' and team = '"+team+"'", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);

        return count;
    }

    public boolean changeStatus(long id){
        boolean res = false;
        ContentValues val = new ContentValues();
        val.put(DBHelper.COLUMN_STATUS, "0");

        int cursor = database.update(DBHelper.TABLE_NAME_4,val,DBHelper.COLUMN_ID+" = "+id, null);
        if(cursor>0){
            res = true;
        }
        return res;
    }

    public JSONObject sendJsonToServer(String nik,String kodetoko, String tglopname, String ip,String team){
        JSONObject sendObj = new JSONObject();

           Cursor cursor = database.query(DBHelper.TABLE_NAME_4, allColumnsOpname, DBHelper.COLUMN_STATUS + " = '1' and "+
            DBHelper.COLUMN_TEAM + " = '"+team+"' and "+ DBHelper.COLUMN_KODE_TOKO + " = '"+kodetoko+"'",null
        ,null,null,DBHelper.COLUMN_ID + " ASC", " 1");
                           // -aji    Cursor cursor = database.query(DBHelper.TABLE_NAME_4, allColumnsOpname,
                   //      DBHelper.COLUMN_TEAM + " = '"+team+"' and "+ DBHelper.COLUMN_KODE_TOKO + " = '"+kodetoko+"'",null
        //  ,null,null,DBHelper.COLUMN_ID + " ASC", " 1");
        Log.d("upload", "createjson");
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            try {
                sendObj.put("team", cursor.getString(1));
                sendObj.put("tanggal", cursor.getString(2) + " " + cursor.getString(3));
                sendObj.put("kode_produk", cursor.getString(6));
                sendObj.put("qty", cursor.getString(7));
                sendObj.put("id", cursor.getLong(0));
                sendObj.put("lokator", cursor.getString(5));
                sendObj.put("nik", nik);
                sendObj.put("kode_toko", kodetoko);
                sendObj.put("tgl_opname", tglopname);
                //.put("lokator", lokator);
                sendObj.put("ip", ip);
            } catch (Exception a) {
                a.printStackTrace();
            }
            cursor.close();
        }
        Log.d("upload", sendObj.toString());
        return sendObj;
    }

    public boolean clearData(){
        //aji boolean res = false;
        boolean res = true;
       // int o = database.delete(DBHelper.TABLE_NAME_4,DBHelper.COLUMN_STATUS + " = '0'",null);
      //  if(o > 0){
       //     res = true;
       // }
        return res;
    }
}
