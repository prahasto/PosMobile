package com.shafco.posmobile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.shafco.posmobile.R;
import com.shafco.posmobile.database.SourceOpname;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_list_opname extends Fragment {
    SourceOpname so;
    private Opname.OnFragmentInteractionListener mListener;
    public fragment_list_opname() {
        // Required empty public constructor
    }
    ListView list;
    ProgressDialog progressDialog;
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("List Opname");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Opname.OnFragmentInteractionListener) {
            mListener = (Opname.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // NOTE : We changed the Uri to String.
        void onFragmentInteraction(String title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fragment_list_opname, container, false);
        so = new SourceOpname(getActivity());
        so.open();
        if (mListener != null) {
            mListener.onFragmentInteraction("List Opname");
        }
        String kode_toko, kode_lokator;
        SharedPreferences pref = getActivity().getSharedPreferences("OpnameSetting",getContext().MODE_PRIVATE);
        kode_toko = pref.getString("Kode_toko",null);
        kode_lokator =pref.getString("Kode_Lokator",null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Wait");
        list = (ListView) view.findViewById(R.id.listView);
//        int len = so.listView().length;
            //Toast.makeText(getContext(), ""+len, Toast.LENGTH_SHORT).show();
            progressDialog.show();
            list.setAdapter(new ListOpnameArrayAdapter(getActivity(),  so.listView(kode_toko,kode_lokator)));



        progressDialog.hide();
        // Inflate the layout for this fragment
        return view;
    }

}
