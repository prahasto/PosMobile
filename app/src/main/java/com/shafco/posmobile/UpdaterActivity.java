package com.shafco.posmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shafco.posmobile.database.ModelOpname;
import com.shafco.posmobile.database.ModelProduk;
import com.shafco.posmobile.database.SourceProduk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by BoTaXs on 14-06-2017.
 */

public class UpdaterActivity extends Activity {
    private Button btnCek;
    SourceProduk sp;
    private String lastId, url;
    int proc,max;
    private RequestQueue queue;
    double percent;
    TextView txtMax, txtProc, txtPercen;
    ProgressBar pbar;
    String proses, ip;
    //DecimalFormat decimalFormat ;
    AlertDialog alertDialog;
    private Context context = this;
    ProgressDialog prDialog;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updater);
        SharedPreferences myPref = getSharedPreferences("OpnameSetting", MODE_PRIVATE);
        ip = myPref.getString("IpAddress", null);

        sp = new SourceProduk(context);
        sp.open();
       // decimalFormat =  new DecimalFormat("##.##");
        pbar = (ProgressBar) findViewById(R.id.progressBar);
        pbar.setMax(100);
        txtProc = (TextView) findViewById(R.id.txtProc);
        txtPercen = (TextView) findViewById(R.id.txtPercen);
        txtMax = (TextView) findViewById(R.id.txtMax);
        //Toast.makeText(context, ""+ip, Toast.LENGTH_SHORT).show();
        proses = txtProc.getText().toString();
        btnCek = (Button) findViewById(R.id.btnCek);
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCek.setVisibility(View.INVISIBLE);
                cekUpdater();
            }
        });
    }

    public void cekUpdater(){
        //btnCek.setVisibility(View.INVISIBLE);
        //proses = txtProc.getText().toString();
        if(proses.equals("-")){
            url = "http://www.shafco.com/mob_stockopname/check_update_produk.php";
            cekUpdateProduk(sp.getLastid());
        } else if (proses.equals("Sync Produk")){
            if(percent >= 100){
                Toast.makeText(context, "Selesai", Toast.LENGTH_LONG).show();
                finish();
            } else {
                syncProduk(lastId);
            }
        } else if (proses.equals("Cek Produk")){
            if(percent >= 100){
                Toast.makeText(context, "Selesai", Toast.LENGTH_LONG).show();
                finish();
            } else {
                url = "http://www.shafco.com/mob_stockopname/check_update_produk.php";
                cekUpdateProduk(sp.getLastid());
            }
        }
    }

    public void cekUpdateProduk(final String lastId2){
        queue = Volley.newRequestQueue(this);
        pbar.setIndeterminate(true);
        txtProc.setText("Cek Produk");
        proc = 0;
        percent = 0 ;
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("lastId", lastId2);
        postParam.put("ip", ip);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        //Toast.makeText(context, ""+response.toString(), Toast.LENGTH_SHORT).show();
                        try{
                            btnCek.setVisibility(View.INVISIBLE);
                            JSONObject results = response.getJSONObject("response");
                            String res = results.getString("result");
                            //Toast.makeText(context, ""+res, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, ""+response.toString(), Toast.LENGTH_SHORT).show();
                            url = "http://www.shafco.com/mob_stockopname/update_master_produk.php";
                            if(res.equals("DONE")){
                                max = Integer.parseInt(results.getString("max"));

                                txtMax.setText(String.valueOf(max));
                                txtProc.setText("Sync Produk");
                                txtPercen.setText("0%");
                                proses = "Sync Produk";
                                pbar.setIndeterminate(false);
                                pbar.setProgress(0);
                                lastId = lastId2;
                            } else if(res.equals("ZERO")){
                                txtProc.setText("Sync Produk");
                                proses = "Sync Produk";

                                pbar.setIndeterminate(false);
                                percent = 100;
                                txtMax.setText("-");
                                txtPercen.setText("-");
                                pbar.setProgress(100);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            cekUpdater();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        error(volleyError);
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void syncProduk(final String lastId2) {
        //queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("lastId", lastId2);
        postParam.put("ip", ip);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        int getProc = 0;
                        try{
                            JSONObject results = response.getJSONObject("response");
                            String res = results.getString("result");
                            if(res.equals("DONE")){
                                JSONArray resultsDetail = results.getJSONArray("detail");
                                getProc = resultsDetail.length();

                                    JSONObject detail = (JSONObject) resultsDetail.get(0);

                                    String kdproduk = detail.getString("kdproduk");
                                    String nmproduk = detail.getString("nmproduk");
                                    int hpj = detail.getInt("hpj");
                                    int r_produk_id = detail.getInt("r_produk_id");

                                    sp.createModelProduk(kdproduk,nmproduk,hpj,r_produk_id);
                                    lastId = String.valueOf(r_produk_id);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            proc += getProc;
                            txtMax.setText(proc+" / "+max);
                            percent = (proc * 100f) / max;
                            //txtPercen.setText(Math.round(percent) + "%");
                            txtPercen.setText(new DecimalFormat("##.##").format(percent) + "%");
                            pbar.setProgress((int)percent);
                            //queue = null;
                            cekUpdater();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        error(volleyError);
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void error (VolleyError volleyError){

        btnCek.setVisibility(View.VISIBLE);
        btnCek.setText("Coba Lagi");
        pbar.setIndeterminate(false);
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
        } else if (volleyError instanceof ServerError) {
            message = "Server tidak ditemukan";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
        } else if (volleyError instanceof ParseError) {
            message = "Gagal, Sedang dalam perawatan";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Tidak dapat terhubung ke internet, mohon per                                                                                                                                                        iksa koneksi anda!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Koneksi terputus, Mohon periksa koneksi anda.";
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        //auto
        cekUpdater();
        btnCek.setVisibility(View.GONE);
    }
}
