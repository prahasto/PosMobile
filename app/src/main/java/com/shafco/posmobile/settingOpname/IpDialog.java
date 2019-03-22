package com.shafco.posmobile.settingOpname;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shafco.posmobile.R;

/**
 * Created by BoTaXs on 09-06-2017.
 */

public class IpDialog extends Activity {
    private EditText edtIp;
    private Button btnOk;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_ip);

        Intent i = getIntent();
        String ipNow = i.getStringExtra("ip");

        edtIp = (EditText) findViewById(R.id.edtIpAddress);
        btnOk = (Button) findViewById(R.id.btnOk);

        edtIp.setText(ipNow);
        btnOk.setOnClickListener(new View.OnClickListener() {
            Intent i = new Intent();
            @Override
            public void onClick(View v) {
                i.putExtra("result",edtIp.getText().toString());
                setResult(02, i);
                finish();
            }
        });
    }
}
