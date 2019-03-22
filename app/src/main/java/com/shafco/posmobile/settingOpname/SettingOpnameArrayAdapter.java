package com.shafco.posmobile.settingOpname;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shafco.posmobile.R;

/**
 * Created by BoTaXs on 09-06-2017.
 */

public class SettingOpnameArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public SettingOpnameArrayAdapter(Context context, String[] values){
        super(context, R.layout.list_setting_opname, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.list_setting_opname, parent, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
        TextView txtValue = (TextView) rowView.findViewById(R.id.txtValue);

        String[] getValues = values[position].split("@@");
        String Title = getValues[0];
        String Value = getValues[1];

        txtTitle.setText(Title);
        txtValue.setText(Value);
        // Change icon based on name\

        return rowView;
    }
}
