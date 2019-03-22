package com.shafco.posmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.shafco.posmobile.database.SourceLokator;
import com.shafco.posmobile.database.SourceProduk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by BoTaXs on 19-10-2017.
 */

public class UpdateLokator extends Activity {

    private Button btnCek;
    SourceLokator sl;
    private String lastId, url;
    int proc,max;
    private RequestQueue queue;
    double percent;
    TextView txtMax, txtProc, txtPercen;
    ProgressBar pbar;
     ProgressDialog pDialog;
    String proses, ip,idoutlet;
    //DecimalFormat decimalFormat ;
    AlertDialog alertDialog;
    private Context context = this;
    ProgressDialog prDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updater);
        SharedPreferences myPref = getSharedPreferences("OpnameSetting", MODE_PRIVATE);
        ip = myPref.getString("IpAddress", null);
        idoutlet = myPref.getString("Toko", null);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        sl = new SourceLokator(context);
        sl.open();
        // decimalFormat =  new DecimalFormat("##.##");
        pbar = (ProgressBar) findViewById(R.id.progressBar);
        pbar.setMax(100);
        txtProc = (TextView) findViewById(R.id.txtProc);
        txtPercen = (TextView) findViewById(R.id.txtPercen);
        txtMax = (TextView) findViewById(R.id.txtMax);
        //Toast.makeText(context, ""+ip, Toast.LENGTH_SHORT).show();
        proses = txtProc.getText().toString();
        btnCek = (Button) findViewById(R.id.btnCek);
        txtProc.setText("Sync Lokasi");
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCek.setVisibility(View.INVISIBLE);
                UpdateLokasi();
            }
        });
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void UpdateLokasi(){
        queue = Volley.newRequestQueue(this);
        proc = 0;
        percent = 0 ;
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("kdtoko", idoutlet);
        postParam.put("ip", ip);
        Log.d("Lokator","datalokator");
        url = "http://www.shafco.com/mob_stockopname/getdatalokator.php";
        Log.d("Lokator", "max"+idoutlet);
        showpDialog();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Lokator", response.toString());
                        proc = 0;

                        int n;
                        JSONObject obj = new JSONObject();
                       // JSONObject results = response.getJSONObject("response");

                        try{
                            JSONArray arr = response.getJSONArray("response");

                            Log.d("Lokator", "array "+ response.getJSONArray("response"));
                           // getProc = response.getJSONArray("response").length();
                            max = Integer.parseInt(response.getString("max"));
                            Log.d("Lokator", "max"+response.getString("max"));


                          //  JSONArray resultsDetail = results.getJSONArray("detail");
                           // getProc = resultsDetail.length();


                            if (arr != null) {
                               // int len = arr.length();


                                for (int i = 0; i < max; i++) {

                                    obj = arr.getJSONObject(i);
                                    sl.justCreate(obj.getString("kdlokator"));

                                        proc = proc+1;
                                       // txtMax.setText(proc+" / "+max);
                                       // txtProc.setText("Sync Lokasi");
                                        txtMax.setText(obj.getString("kdlokator"));
                                        percent = (proc * 100f) / max;
                                        txtPercen.setText(new DecimalFormat("##.##").format(percent) + "%");
                                        pbar.setProgress((int)percent);
                                    }
                                }



                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                        }finally {
                            hidepDialog();
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

    public void syncdata( JSONObject response ){
        txtMax.setText(String.valueOf(max));
        txtProc.setText("Sync Lokasi");
        txtPercen.setText("0%");
        pbar.setIndeterminate(false);
        pbar.setProgress(0);



        Log.d("Lokator", "array "+ response.toString());
        // getProc = response.getJSONArray("response").length();



        //  JSONArray resultsDetail = results.getJSONArray("detail");
        // getProc = resultsDetail.length();



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
        hidepDialog();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        //auto
        //cekUpdater();
        btnCek.setVisibility(View.GONE);
    }
}
