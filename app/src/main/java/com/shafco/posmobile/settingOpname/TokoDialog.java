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
import com.shafco.posmobile.database.SourceProduk;
import com.shafco.posmobile.database.SourceToko;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.v;

/**
 * Created by BoTaXs on 12-06-2017.
 */

public class TokoDialog extends Activity {
    private ListView lv;
    ArrayAdapter<String> adapter;
    SourceToko st;
    String kdtoko,iptoko,namatoko;
    ProgressBar pgBar;

    ArrayList<String> namaToko = new ArrayList<String>();
    ArrayList<String> kodeToko = new ArrayList<String>();
    ArrayList<String> ipToko = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_toko);

        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        pgBar.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.list_view);
        lv.setVisibility(View.GONE);
        getDataToko();

        Intent i = getIntent();
        st = new SourceToko(this);
        st.open();

        String[] valueToko  = st.getAllToko().split(",,");

        //adapter = new ArrayAdapter<String>(TokoDialog.this,R.layout.list_toko,R.id.txtToko,valueToko);
        //lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kdtoko = kodeToko.get(position);
                iptoko = ipToko.get(position);
                namatoko = namaToko.get(position);
                //Toast.makeText(TokoDialog.this, ""+kdtoko, Toast.LENGTH_SHORT).show();

                Intent i = new Intent();
                i.putExtra("namatoko",namatoko);
                i.putExtra("kdtoko", kdtoko);
                i.putExtra("iptoko", iptoko);
                //Toast.makeText(TokoDialog.this, ""+a.substring(0, a.lastIndexOf(".")), Toast.LENGTH_SHORT).show();
                setResult(03, i);
                finish();
            }
        });
    }

    public void getDataToko() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.shafco.com/mob_stockopname/dataToko.php";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pgBar.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        Log.d(TAG, response.toString());
                        JSONObject obj = new JSONObject();
                        try{
                            JSONArray arr = response.getJSONArray("response");
                            if(arr != null){

                                int len = arr.length();
                                for(int i = 0;i<len;i++){
                                    obj = arr.getJSONObject(i);
                                    namaToko.add(obj.getString("namatoko"));
                                    kodeToko.add(obj.getString("kdtoko"));
                                    ipToko.add(obj.getString("ipserver"));
                                }
                            }
                            adapter = new ArrayAdapter<String>(TokoDialog.this,R.layout.list_toko,R.id.txtToko,namaToko);
                            lv.setAdapter(adapter);
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
                        Toast.makeText(TokoDialog.this, message, Toast.LENGTH_SHORT).show();
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
