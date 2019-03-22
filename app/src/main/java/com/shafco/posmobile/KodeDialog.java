package com.shafco.posmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by BoTaXs on 09-06-2017.
 */

public class KodeDialog extends Activity {
    private EditText edtKode;
    private Button btnOk;
    Context context = KodeDialog.this;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_kode);
        edtKode = (EditText) findViewById(R.id.edtKode);
        edtKode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtKode.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_BACK) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            Intent i = new Intent();
                            i.putExtra("result", edtKode.getText().toString());
                            setResult(12, i);
                            finish();
                            break;
                        case KeyEvent.KEYCODE_BACK :
                            finish();
                            break;
                    }
                    return true;
                } else {
                  return false;
                }
            }
        });
        btnOk = (Button) findViewById(R.id.btnOk);
        edtKode.requestFocus();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("result",edtKode.getText().toString());
                setResult(11,i);
                finish();
            }
        });
    }

}
