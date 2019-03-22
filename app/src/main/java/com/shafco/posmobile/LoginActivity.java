package com.shafco.posmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.shafco.posmobile.database.SourceToko;
import com.shafco.posmobile.settingOpname.TokoDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText edt_nik, edt_password;
    private TextView txtToko;
    String kdtoko,iptoko,namatoko;
  
    ProgressDialog prDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        edt_nik = (EditText) findViewById(R.id.edt_nik);
        edt_password = (EditText) findViewById(R.id.edt_password);
        txtToko = (TextView) findViewById(R.id.txtToko);

        txtToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(LoginActivity.this, TokoDialog.class);
                startActivityForResult(i3,03);
            }
        });
        prDialog = new ProgressDialog(this);
        prDialog.setMessage("Mohon Tunggu ...");
        prDialog.setCancelable(false);

    }

    @Override
    public   void onActivityResult(int requestCode, int resultCode, Intent data){
        SharedPreferences.Editor edit = getSharedPreferences("OpnameSetting", Context.MODE_PRIVATE).edit();
        if (data != null){
            String res = data.getStringExtra("result");
            switch (requestCode){
                case 03 :
                    namatoko = data.getStringExtra("namatoko");
                    iptoko = data.getStringExtra("iptoko");
                    kdtoko = data.getStringExtra("kdtoko");

                    txtToko.setText(namatoko);
                    //SourceToko st = new SourceToko(LoginActivity.this);
                    //st.open();
                    //String[] set = st.getKodeToko(res).split("@@");
                    //edit.putString("Kode_toko", set[0]);
                    //edit.putString("Toko",set[1]);

                    break;
            }
        }
    }


    //--------------------------------------------------------------
    public void login(View v) {


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.shafco.com/mob_stockopname/login.php";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("nik", edt_nik.getText().toString());
        postParam.put("password", edt_password.getText().toString());
        postParam.put("ip", iptoko);

        prDialog.show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try{
                            JSONObject results = response.getJSONObject("response");
                            String res = results.getString("result");
                            if(res.equals("succes")) {
                                prDialog.hide();
                                String nik = results.getString("nik");
                                String nama = results.getString("nama");
                                SharedPreferences.Editor editor = getSharedPreferences("LoginPref",MODE_PRIVATE).edit();
                                editor.putString("nik",nik);
                                editor.putString("nama",nama);

                                SharedPreferences.Editor edit = getSharedPreferences("OpnameSetting",Context.MODE_PRIVATE).edit();
                                edit.putString("IpAddress", iptoko);
                                edit.putString("Kode_toko", kdtoko);
                                edit.putString("Toko", namatoko);

                                if(editor.commit() && edit.commit()){
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                                }

                            } else if(res.equals("fail")) {
                                prDialog.hide();
                                Toast.makeText(LoginActivity.this, "Nik atau password tidak terdaftar", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        prDialog.hide();
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }
}

