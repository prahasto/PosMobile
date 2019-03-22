package com.shafco.posmobile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
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
import com.shafco.posmobile.database.DBHelper;
import com.shafco.posmobile.database.SourceProduk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.shafco.posmobile.R.id.btnCek;
import static com.shafco.posmobile.R.id.txtMax;
import static com.shafco.posmobile.R.id.txtProc;

/**
 * Created by BoTaXs on 24-10-2017.
 */

public class GetProduk extends Activity {
    //--aji
    private Context context = this;
    private RequestQueue queue;
    private String url;
    int max;
    public int lbool = 0;
    private int lint;
    String  ip;
    SourceProduk sp;

    public GetProduk(Context context){
        this.context = context;
        setBool(0);
        Log.d("getproduk", "inisialisasi");

    }

    public int getBool()
    {
        return lbool;
    }
    public void setBool(int lbool)
    {
        this.lbool = lbool;
    }



    public void getUpdateProduk(String kode_produk,String ip){

        queue = Volley.newRequestQueue(context);
        // SharedPreferences myPref = getSharedPreferences("OpnameSetting", MODE_PRIVATE);
        //ip = myPref.getString("IpAddress", null);
        Log.d("getproduk", "after volley request");
        sp = new SourceProduk(context);
        sp.open();
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("kodeproduk", kode_produk);
        postParam.put("ip", ip);
        url = "http://www.shafco.com/mob_stockopname/getproduk.php";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getproduk", response.toString());


                        // JSONObject obj = new JSONObject();
                        // JSONObject results = response.getJSONObject("response");

                        try {
                            JSONObject respon = response.getJSONObject("response");
                            Log.d("getproduk", "after respon");
                            // JSONArray arr = response.getJSONArray("response");
                            //Log.d("getproduk", "array " + results.getJSONObject("response"));
                            if (respon != null) {
                                Log.d("getproduk", "jika respon tidak kosong");
                                String res = respon.getString("result");
                                max = Integer.parseInt(respon.getString("max"));
                                // String res = response.getString("result");
                                if (res.equals("DONE")) {
                                    JSONArray resultsDetail = respon.getJSONArray("detail");
                                    JSONObject detail = (JSONObject) resultsDetail.get(0);

                                    String kdproduk = detail.getString("kdproduk");
                                    String nmproduk = detail.getString("nmproduk");
                                    int hpj = detail.getInt("hpj");
                                    int r_produk_id = detail.getInt("r_produk_id");

                                    lint = sp.insertproduk(kdproduk,nmproduk,hpj,r_produk_id);
                                    Log.d("getproduk", "insert "+String.valueOf(lint));
                                    setBool(1);
                                }

                            }
                            else {
                                setBool(0);
                            }
                        }
                        catch (JSONException e) {

                            e.printStackTrace();
                            //lbool = false;
                            setBool(0);
                            Log.d("getproduk", "error");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
      //  Log.d("getproduk", String.valueOf(lbool));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);



     // return lbool;

    }

    //--

}
