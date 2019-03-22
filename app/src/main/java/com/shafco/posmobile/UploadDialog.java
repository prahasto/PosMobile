package com.shafco.posmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.shafco.posmobile.database.SourceOpname;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class UploadDialog extends Activity {
    ProgressBar pbar;
    TextView txtTitle, txtPercent;
    SourceOpname so;
    int max,proc;
    float percent;
    String kodetoko, nik, kodelokator, tglopname, ip, team;
    Button btnStart;
   String url = "http://www.shafco.com/mob_stockopname/upload.php";
   //String url = "http://www.shafco.com/pdt_tes/upload.php";
    private RequestQueue queue;
    String res;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upload);

        this.setFinishOnTouchOutside(false);
        pbar = (ProgressBar) findViewById(R.id.progressBar);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPercent = (TextView) findViewById(R.id.txtPercent);

        so = new SourceOpname(this);
        so.open();

        pbar.setMax(100);
        pbar.setIndeterminate(true);

        SharedPreferences pref1 = getSharedPreferences("LoginPref",MODE_PRIVATE);
        nik = pref1.getString("nik","-");

        SharedPreferences pref2 = getSharedPreferences("OpnameSetting",MODE_PRIVATE);
        kodetoko = pref2.getString("Kode_toko","-");
        //kodelokator = pref2.getString("Kode_Lokator","-");
        tglopname = pref2.getString("Tanggal_Opname","-");
        team = pref2.getString("Team","-");
        ip = pref2.getString("IpAddress","-");

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!ip.equals("")) {
                                                startUpload();
                                            }
                                        }
                                    });
        //txtTitle.setText(so.sendJsonToServer(nik,kodetoko,tglopname,ip).toString() + " \n \n "+so.getCountOpname());
        //cekUpload();
        startUpload();
    }

    public void startUpload(){
        max = so.getCountOpname(kodetoko, team);
        txtTitle.setText(String.valueOf(max));
        proc = 0;
        percent = 0;
       // Toast.makeText(this, "PROSES ", Toast.LENGTH_SHORT).show();
        //txtTitle.setText(so.sendJsonToServer(nik,kodetoko,tglopname,kodelokator,ip).toString());
        if(max > 0) {
            Toast.makeText(this, "UPLOAD", Toast.LENGTH_SHORT).show();
            cekUpload();
        } else {
            Toast.makeText(this, "Tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void cekUpload(){
        if(percent == 100){
            SharedPreferences.Editor pref = getSharedPreferences("OpnameSetting",MODE_PRIVATE).edit();
            pref.remove("Tanggal_Opname");
            if(pref.commit()) {
                //if (so.clearData()){
                   Toast.makeText(UploadDialog.this, "Selesai", Toast.LENGTH_SHORT).show();
                //  so.close();
                    finish();
             //  }
            }
        }else {
          //  so.sendJsonToServer(nik,kodetoko,tglopname,ip,team )
            sendData(so.sendJsonToServer(nik,kodetoko,tglopname,ip,team ));
        }
    }

    public void sendData(JSONObject soObj) {
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, soObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    //    Toast.makeText(UploadDialog.this, "senddata..", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, response.toString());
                        pbar.setIndeterminate(false);
                        try {
                            //SourceOpname so = new SourceOpname(UploadDialog.this);
                            //so.open();
                            //txtTitle.setText(response.toString());

                            JSONObject result = response.getJSONObject("response");
                            res = result.getString("result");
                           // Toast.makeText(UploadDialog.this, response.toString(), Toast.LENGTH_SHORT).show();
                            if(res.equals("DONE")){
                                long id = result.getLong("id");
                             //   Toast.makeText(UploadDialog.this, "Long ID", Toast.LENGTH_SHORT).show();
                                if(so.changeStatus(id)){
                                    proc++;
                                    txtTitle.setText(proc+" / "+max);
                                    percent = (proc * 100f) / max;
                                    txtPercent.setText(Math.round(percent)+"%");
                                    pbar.setProgress((int) percent);
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        } finally {
                            cekUpload();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        finish();
                        pbar.setIndeterminate(false);
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof ServerError) {
                            message = "IP ADDRESS Tidak ditemukan!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof ParseError) {
                            message = "IP ADDRESS Tidak ditemukan!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(UploadDialog.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
