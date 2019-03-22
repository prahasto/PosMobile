package com.shafco.posmobile.settingOpname;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.shafco.posmobile.R;
import com.shafco.posmobile.database.ModelToko;
import com.shafco.posmobile.database.SourceLokator;
import com.shafco.posmobile.database.SourceProduk;
import com.shafco.posmobile.database.SourceToko;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.v;

/**
 * Created by BoTaXs on 12-06-2017.
 */

public class LokatorDialog extends Activity {
    private ListView lv;
    ArrayAdapter<String> adapter;
    SourceLokator sl;
    String kdtoko;
    ProgressBar pgBar;
    EditText InputSearch;

    ArrayList<String> kdlokator = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_toko);

        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        pgBar.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.list_view);
        lv.setVisibility(View.GONE);
       // getDataToko();
        InputSearch = (EditText)findViewById(R.id.inputSearch);

        Intent i = getIntent();
        sl = new SourceLokator(this);
        sl.open();

        pgBar.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        String[] valueLokator = sl.getAllLokator().split(",,");

        adapter = new ArrayAdapter<String>(LokatorDialog.this, R.layout.list_toko, R.id.txtToko, valueLokator);
        lv.setAdapter(adapter);
       /* InputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                LokatorDialog.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        //adapter = new ArrayAdapter<String>(TokoDialog.this,R.layout.list_toko,R.id.txtToko,valueToko);
        //lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                String a = lv.getItemAtPosition(position).toString();
                i.putExtra("result",a);
                setResult(04, i);
                //Toast.makeText(LokatorDialog.this, ""+a, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void getDataToko() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.shafco.com/mob_stockopname/datalokator.php";
        //Toast.makeText(LokatorDialog.this, "konek intenet", Toast.LENGTH_SHORT).show();
        HashMap<String, String> obj = new HashMap<String, String>();
        SharedPreferences pref = getSharedPreferences("OpnameSetting", MODE_PRIVATE);
        obj.put("ip", pref.getString("IpAddress", null));
        obj.put("kdtoko", pref.getString("Kode_toko", null));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(obj),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pgBar.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        Log.d(TAG, response.toString());
                        JSONObject obj = new JSONObject();
                        try {
                            JSONArray arr = response.getJSONArray("response");
                            if (arr != null) {
                                int len = arr.length();
                                if(len > 0) {
                                    for (int i = 0; i < len; i++) {
                                        obj = arr.getJSONObject(i);
                                        kdlokator.add(obj.getString("kdlokator"));
                                    }
                                    adapter = new ArrayAdapter<String>(LokatorDialog.this, R.layout.list_toko, R.id.txtToko, kdlokator);
                                    lv.setAdapter(adapter);
                                } else {
                                    finish();
                                    Toast.makeText(LokatorDialog.this, "Tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //Toast.makeText(TokoDialog.this, "--"+namaToko.toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //prDialog.hide();
                        finish();
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof ServerError) {
                            message = "Mohon ulangi beberapa saat lagi";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Sedang dalam perawatan";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Tidak dapat terhubung ke internet, mohon periksa koneksi anda!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Koneksi internet terlalu lama. Mohon ulangi lagi";
                        }
                        Toast.makeText(LokatorDialog.this, message, Toast.LENGTH_SHORT).show();
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