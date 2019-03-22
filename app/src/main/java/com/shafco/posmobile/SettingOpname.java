package com.shafco.posmobile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
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
import com.shafco.posmobile.database.SourceToko;
import com.shafco.posmobile.settingOpname.IpDialog;
import com.shafco.posmobile.settingOpname.LokatorDialog;
import com.shafco.posmobile.settingOpname.SettingOpnameArrayAdapter;
import com.shafco.posmobile.settingOpname.TeamDialog;
import com.shafco.posmobile.settingOpname.TokoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingOpname.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingOpname#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingOpname extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ProgressDialog prDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingOpname() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingOpname.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingOpname newInstance(String param1, String param2) {
        SettingOpname fragment = new SettingOpname();
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

        View view= inflater.inflate(R.layout.fragment_setting_opname, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Setting Opname");
        }
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        prDialog = new ProgressDialog(getActivity());
        prDialog.setMessage("Mohon Tunggus ...");
        prDialog.setCancelable(false);
        final SharedPreferences prefs = getActivity().getSharedPreferences("OpnameSetting", Context.MODE_PRIVATE);
        String[] values = new String[5];
        values[0] = ("Ip Address@@"+prefs.getString("IpAddress","-"));
        values[1] = ("Tanggal Opname@@"+prefs.getString("Tanggal_Opname","-"));
        values[2] = ("Team@@"+prefs.getString("Team","-"));
        values[3] = ("Toko@@"+prefs.getString("Toko","-"));
        values[4] = ("Lokator@@"+prefs.getString("Kode_Lokator","-"));

        ListView list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(new SettingOpnameArrayAdapter(getActivity(),values));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    /*case 0 :
                        Intent i0 = new Intent(getActivity(), IpDialog.class);
                        i0.putExtra("ip",prefs.getString("IpAddress","-"));
                        startActivityForResult(i0,00);
                        break;*/
                    case 1 :
                        showDateDialog();
                        break;
                    case 2 :
                        Intent i2 = new Intent(getActivity(), TeamDialog.class);
                        startActivityForResult(i2,02);
                        break;
                    /*case 3 :
                        Intent i3 = new Intent(getActivity(), TokoDialog.class);
                        startActivityForResult(i3,03);
                        break;*/
                    case 4 :
                        Intent i4 = new Intent(getActivity(), LokatorDialog.class);
                        startActivityForResult(i4,04);
                        break;
                }
            }
        });

        // Here we will can create click listners etc for all the gui elements on the fragment.
        // For eg: Button btn1= (Button) view.findViewById(R.id.frag1_btn1);
        // btn1.setOnclickListener(...

        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Setting Opname");
        }
    }

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
        // NOTE : We changed the Uri to String.
        void onFragmentInteraction(String title);
    }
    @Override
    public   void onActivityResult(int requestCode, int resultCode, Intent data){
        SharedPreferences.Editor edit = getActivity().getSharedPreferences("OpnameSetting",Context.MODE_PRIVATE).edit();
        if (data != null){
            String res = data.getStringExtra("result");
            switch (requestCode){
                case 00 : edit.putString("IpAddress",res); break;
                case 01 : break;
                case 02 : edit.putString("Team",res); break;
                case 03 :
                    SourceToko st = new SourceToko(getActivity());
                    st.open();
                    String[] set = st.getKodeToko(res).split("@@");
                    edit.putString("Kode_toko", set[0]);
                    edit.putString("Toko",set[1]);
                    break;
                case 04 : edit.putString("Kode_Lokator",res); break;
            }
        }
        if(edit.commit()){
            Fragment currentFragment = getFragmentManager().findFragmentByTag("settingopname");
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }
    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                SharedPreferences.Editor edit = getActivity().getSharedPreferences("OpnameSetting",Context.MODE_PRIVATE).edit();
                edit.putString("Tanggal_Opname",dateFormatter.format(newDate.getTime()));
                if(edit.commit()){
                    Fragment currentFragment = getFragmentManager().findFragmentByTag("settingopname");
                    FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commit();
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    public void getDataToko() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://www.shafco.com/mob_stockopname/get_toko.php";
      //  Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
        prDialog.show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try{
                            JSONObject results = response.getJSONObject("response");
                            String res = results.getString("result");
                            if(res.equals("SUCCESS")) {
                                prDialog.hide();
                                //String[] toko = results.getString("detail").split(",,");

                                Intent i3 = new Intent(getActivity(), TokoDialog.class);
                                i3.putExtra("toko",results.getString("detail"));
                                startActivityForResult(i3,03);
                                /*lv.setAdapter(adapter);
                                adapter = new ArrayAdapter<String>(TokoDialog.this, android.R.layout.simple_list_item_1,
                                        android.R.id.text1, toko);*/
                            } else if(res.equals("fail")) {
                                prDialog.hide();
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
                            message = "Koneksi Terputus! Mohon periksa konektifikasi anda.";
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
