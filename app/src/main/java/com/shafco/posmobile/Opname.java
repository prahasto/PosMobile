package com.shafco.posmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.shafco.posmobile.database.DBHelper;
import com.shafco.posmobile.database.ModelOpname;
import com.shafco.posmobile.database.SourceOpname;
import com.shafco.posmobile.database.SourceProduk;
import com.shafco.posmobile.GetProduk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.shafco.posmobile.R.id.btnCek;
import static com.shafco.posmobile.R.id.txtMax;
import static com.shafco.posmobile.R.id.txtProc;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Opname.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Opname#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Opname extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue queue;
    private Boolean isProduk = false;
    private String url;
    private int max,lint;
    TextView txtToko, txtLokator, txtTanggal, txtTeam, txtShow, txtQtyScan;

    SourceOpname SO;
    GetProduk getproduk;
    TextView txtKode,txtNama,txtQty,txtHarga,txtQtyTotal,txtCountRow;
    private EditText edtKode;
    private ImageButton btnOk, btnInputQty;
    private SourceProduk sp;
    ProgressDialog prDialog;
    // TODO: Rename and change types of parameters
    Calendar c = Calendar.getInstance();

    private String mParam1;
    private String mParam2;
    public int istrue =0;
    String team,tanggal,jam,kode_toko,kode_lokator,kode_produk,status;
    int qty;

    private OnFragmentInteractionListener mListener;

    public Opname() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Opname.
     */
    // TODO: Rename and change types and number of parameters
    public static Opname newInstance(String param1, String param2) {
        Opname fragment = new Opname();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_opname, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Opname");
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("OpnameSetting",MODE_PRIVATE);
        kode_lokator = prefs.getString("Kode_Lokator",null);
        kode_toko = prefs.getString("Kode_toko",null);
        tanggal = prefs.getString("Tanggal_Opname",null);
        team = prefs.getString("Team",null);

        prDialog = new ProgressDialog(getContext());
        prDialog.setMessage("Menyimpan");
        prDialog.setCancelable(false);

        sp = new SourceProduk(getActivity());
        sp.open();

        SO = new SourceOpname(getActivity());
        SO.open();



        //txtShow = (TextView) view.findViewById(R.id.txtShow);
        //txtShow.setText(SO.sendJsonToServer("asd","asd","asd").toString());
        String[] lastRecord = SO.getLastRecord(kode_lokator,kode_toko,team, tanggal);

        txtKode = (TextView) view.findViewById(R.id.txtKode);
        txtNama = (TextView) view.findViewById(R.id.txtNama);
        txtQty = (TextView) view.findViewById(R.id.txtQty);
        txtHarga = (TextView) view.findViewById(R.id.txtHarga);
        txtQtyTotal = (TextView) view.findViewById(R.id.txtQtyTotal);
        txtCountRow = (TextView) view.findViewById(R.id.txtCountRow);
        txtQtyScan = (TextView) view.findViewById(R.id.txtQtyScan);

        txtShow = (TextView) view.findViewById(R.id.txtShow);
        //txtShow.setText(SO.sendJsonToServer("nik","kodetoko","tglopname","ip").toString());

        txtKode.setText(lastRecord[0]);
        txtNama.setText(lastRecord[1]);
        txtQty.setText(lastRecord[2]);
        txtHarga.setText(lastRecord[3]);
        txtQtyTotal.setText(lastRecord[4]);
        txtCountRow.setText(lastRecord[5]);

        if(lastRecord[2] == null){
            txtQtyScan.setText("1");
        } else {
            txtQtyScan.setText(lastRecord[2]);
        }

        txtLokator = (TextView) view.findViewById(R.id.txtLokator);
        txtToko = (TextView) view.findViewById(R.id.txtToko);
        txtTanggal = (TextView) view.findViewById(R.id.txtTanggal);
        txtTeam = (TextView) view.findViewById(R.id.txtTeam);
        txtLokator.setText(prefs.getString("Kode_Lokator","Belum Diatur"));
        txtToko.setText(prefs.getString("Toko","Belum Diatur"));
        txtTeam.setText(prefs.getString("Team","Belum Diatur"));
        txtTanggal.setText(prefs.getString("Tanggal_Opname","Belum Diatur"));

        team = prefs.getString("Team","-");
        jam = String.valueOf(c.get(Calendar.HOUR) +":"+c.get(Calendar.MINUTE));
        kode_toko = prefs.getString("Kode_toko","-");
        status = "1";

        btnInputQty = (ImageButton) view.findViewById(R.id.btnInputQty);
        btnInputQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QtyDialog.class);
                startActivityForResult(i,12);
            }
        });

        btnOk = (ImageButton) view.findViewById(R.id.btnInputKode);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), KodeDialog.class);
                startActivityForResult(i,11);
            }
        });
        edtKode = (EditText) view.findViewById(R.id.edtKode);
        edtKode.setCursorVisible(true);
        edtKode.setClickable(true);
        edtKode.requestFocus();
        edtKode.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_F1 :
                        Intent i = new Intent(getActivity(), QtyDialog.class);
                        startActivityForResult(i, 12);
                        break;
                    case KeyEvent.KEYCODE_F2 :
                        Intent i2 = new Intent(getActivity(), KodeDialog.class);
                        startActivityForResult(i2, 11);
                        break;
                    case KeyEvent.KEYCODE_BACK :
                        System.exit(0);
                        break;
                }

                return true;
            }
        });
        edtKode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();

                if(action == MotionEvent.ACTION_DOWN) {
                    Intent i = new Intent(getActivity(), KodeDialog.class);
                    startActivityForResult(i, 11);
                }
                return true;
            }
        });
        /*edtKode.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                return true;
            }
        });*/

        edtKode.addTextChangedListener(MyTextWatcher);
        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(

        txtQtyScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QtyDialog.class);
                startActivityForResult(i,12);
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Opname");
        }
    }

    public TextWatcher MyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!edtKode.getText().toString().equals("")) {
                edtKode.setHint("Mohon Tunggu");
                insertScan(edtKode.getText().toString());
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }


    public void getUpdateProduk(String kode_produk,String ip){

        queue = Volley.newRequestQueue(getContext());
        // SharedPreferences myPref = getSharedPreferences("OpnameSetting", MODE_PRIVATE);
        //ip = myPref.getString("IpAddress", null);
        Log.d("getproduk", "after volley request");
        //sp = new SourceProduk(getActivity());
        //sp.open();
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
                                   // edtKode.setText(kdproduk);
                                    isProduk = true;
                                    Log.d("getproduk", "insert "+String.valueOf(isProduk));

                                }

                            }
                            else {
                                //setBool(0);
                                isProduk = false;
                            }
                        }
                        catch (JSONException e) {
                            isProduk = false;
                            e.printStackTrace();
                            //lbool = false;
                            //setBool(0);
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
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
      //  stringRequest.setShouldCache(false);
        queue.add(stringRequest);
     //return isProduk;
    }

    public void insertScan( String res){
        final MediaPlayer mpFail = MediaPlayer.create(getContext(), R.raw.error);
        prDialog.show();
        SharedPreferences prefs = getActivity().getSharedPreferences("OpnameSetting",MODE_PRIVATE);
        String kodelokator = prefs.getString("Kode_Lokator", null);
        String kodetoko = prefs.getString("Kode_toko", null);
        String tanggalopname = prefs.getString("Tanggal_Opname", null);
        String team1 = prefs.getString("Team", null);
        String ip = prefs.getString("IpAddress", null);
        qty = Integer.valueOf(txtQtyScan.getText().toString());
        kode_produk = res;
        //Toast.makeText(getContext(), ""+sp.scanChecker(res), Toast.LENGTH_LONG).show();
        if(kodelokator != null && kodetoko != null && tanggalopname != null && team1 != null && ip != null) {
            if (sp.scanChecker(res).equals("EXIST")) {
                ModelOpname mo = new ModelOpname();

                mo = SO.createModelOpname(team,
                        tanggalopname, jam, kode_toko,
                        kode_lokator, kode_produk,
                        qty, status);


                if(mo.getKode_produk() != null){
                    prDialog.hide();
                    edtKode.removeTextChangedListener(MyTextWatcher);
                    edtKode.setText("");
                    edtKode.addTextChangedListener(MyTextWatcher);
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
            } else {

                //GetProduk getproduk = new GetProduk(this.getContext());
                Log.d("getproduk", res);
               // if (getproduk.getUpdateProduk(res,ip)) {
                //getproduk.getUpdateProduk(res,ip);
               // Log.d("getproduk", String.valueOf(getproduk.lbool));
              // Toast.makeText(getContext(),  "Silahkan di Scan !\n"+String.valueOf(getUpdateProduk(res,ip)), Toast.LENGTH_LONG).show();
                // if (sp.scanChecker(res).equals("EXIST") ){
               // getUpdateProduk(res,ip);
             //-   if (sp.scanChecker(res).equals("EXIST")  ){
              //-      Log.d("getproduk", "true");
                //-    Toast.makeText(getContext(),  "Silahkan di Scan Ulang Kode !\n"+res, Toast.LENGTH_LONG).show();
               //-} else{
                    mpFail.start();
                    Log.d("getproduk", "else");
                    prDialog.hide();
                    Toast.makeText(getContext(), res + "\nKode Barang Tidak Terdaftar !", Toast.LENGTH_LONG).show();
                    edtKode.removeTextChangedListener(MyTextWatcher);
                    edtKode.setHint("Scan atau klik ->");
                    edtKode.setText("");
                    edtKode.addTextChangedListener(MyTextWatcher);
                //}
            }
        } else {
            mpFail.start();
            prDialog.hide();
            Toast.makeText(getActivity(), "Mohon Setting Opname Terlebih Dahulu", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public   void onActivityResult(int requestCode, int resultCode, Intent data){
        boolean ref = false;
        if (data != null){
            String res = data.getStringExtra("result");
            switch (requestCode){
                case 11 :
                    edtKode.setText(res);
                    //insertScan(res);
                    break;
                case 12 :
                    txtQtyScan.setText(res);
                    break;
            }

        }
    }
}
