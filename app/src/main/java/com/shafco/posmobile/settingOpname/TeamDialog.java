package com.shafco.posmobile.settingOpname;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.shafco.posmobile.R;

/**
 * Created by BoTaXs on 09-06-2017.
 */

public class TeamDialog extends Activity {
    private Spinner spinner;
    private Button btnOk;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_team);

        String[] val = new String[]{"T1","T2","T3"};
        spinner = (Spinner) findViewById(R.id.team_spinner);
        ArrayAdapter<String> teamArray= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, val);
        teamArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(teamArray);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(
                new View.OnClickListener() {
                    Intent i = new Intent();
                    @Override
                    public void onClick(View v) {
                        i.putExtra("result",spinner.getSelectedItem().toString());
                        setResult(02, i);
                        finish();
                    }
                }
        );

    }
}
