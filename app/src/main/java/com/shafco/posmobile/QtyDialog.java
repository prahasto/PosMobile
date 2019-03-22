package com.shafco.posmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by BoTaXs on 09-06-2017.
 */

public class QtyDialog extends Activity {
    private EditText edtQty;
    private Button btnOk;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_qty);
        edtQty = (EditText) findViewById(R.id.edtQty);
        edtQty.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
                    if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_BACK) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_ENTER:
                                if(!edtQty.getText().toString().equals("")) {
                                    Intent i = new Intent();
                                    i.putExtra("result", edtQty.getText().toString());
                                    setResult(12,i);
                                    finish();
                                } else {
                                    finish();
                                }
                                break;
                            case KeyEvent.KEYCODE_BACK:
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

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtQty.getText().toString().equals("")) {
                    Intent i = new Intent();
                    i.putExtra("result", edtQty.getText().toString());
                    setResult(12,i);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }
}
