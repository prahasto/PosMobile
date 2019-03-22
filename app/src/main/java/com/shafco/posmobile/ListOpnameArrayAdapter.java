package com.shafco.posmobile;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by BoTaXs on 15-06-2017.
 */

public class ListOpnameArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ListOpnameArrayAdapter(Context context, String[] values){
        super(context, R.layout.opname_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.opname_list_item2, parent, false);
        TextView txtRow = (TextView) rowView.findViewById(R.id.txtRow);
        TextView txtKode = (TextView) rowView.findViewById(R.id.txtKode);
        TextView txtNama = (TextView) rowView.findViewById(R.id.txtNama);
        TextView txtTanggalJam = (TextView) rowView.findViewById(R.id.txtTanggalJam);
        TextView txtTeam = (TextView) rowView.findViewById(R.id.txtTeam);
        TextView txtQty = (TextView) rowView.findViewById(R.id.txtQty);
        TextView txtQtyTotal = (TextView) rowView.findViewById(R.id.txtQtyTotal);
        TextView txtTotal = (TextView) rowView.findViewById(R.id.txtTotal);
        String[] all = values[position].split("@@");
        if(all[0].equals(null) || all[0] == null || all[0].equals("")){
            rowView.setVisibility(View.GONE);
        } else {
            int qtyTot = Integer.valueOf(all[2]);
            int tot = Integer.valueOf(all[2]);
            for (int i = 0; i < position; i++) {
                String[] getQty = values[i].split("@@");
                if (all[0].equals(getQty[0])) {
                    qtyTot += Integer.valueOf(getQty[2]);
                }
                tot += Integer.valueOf(getQty[2]);
            }
            txtRow.setText("#" + Integer.valueOf(position + 1));
            txtKode.setText(all[0]);
            txtNama.setText(all[1]);
            txtTanggalJam.setText(" ) " + all[4]);
            txtTeam.setText("Team " + all[3].substring(1)+ "| Lokator " + all[5]);
            if(all[2].substring(0,1).equals("-")){
                txtQty.setText(all[2]);
            } else {
                txtQty.setText("+" + all[2]);
            }
            txtQtyTotal.setText(String.valueOf(qtyTot));
            txtTotal.setText(String.valueOf(tot));
        }
        return rowView;
    }
}
