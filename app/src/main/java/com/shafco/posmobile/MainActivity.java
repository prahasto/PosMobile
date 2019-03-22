package com.shafco.posmobile;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.DateFormat;
import java.util.Calendar;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shafco.posmobile.database.DBHelper;
import com.shafco.posmobile.database.ModelProduk;
import com.shafco.posmobile.database.SourceLokator;
import com.shafco.posmobile.database.SourceOpname;
import com.shafco.posmobile.database.SourceProduk;
import com.shafco.posmobile.database.SourceToko;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity
        implements
        SettingOpname.OnFragmentInteractionListener,
        Opname.OnFragmentInteractionListener,
        fragment_list_opname.OnFragmentInteractionListener,

        NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog prDialog;
    SourceLokator sl;
    TextView txt_nik, txtShow, txt_nama;
    String kodeToko, tglOpname,team,lokator, nik, nama, ip;
    SourceOpname so;
    UpdateLokator ul;

    Calendar c = Calendar.getInstance();
    private Context context = this;
    private ProgressDialog progress;
    SourceProduk sp;
    @Override
    public void onFragmentInteraction(String title) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        nik = prefs.getString("nik",null);
        nama = prefs.getString("nama", null);
        SharedPreferences pref = getSharedPreferences("OpnameSetting", MODE_PRIVATE);
        kodeToko = pref.getString("Kode_toko","-");
        tglOpname = pref.getString("Tanggal_Opname","-");
        ip = pref.getString("IpAddress","-");
        lokator = pref.getString("Kode_Lokator","-");
        team = pref.getString("Team","-");
       if(nik == null){
            Intent i = new Intent(context, LoginActivity.class);
            startActivity(i);
            finish();
        }

        sp = new SourceProduk(this);
        sp.open();

        so = new SourceOpname(this);
        so.open();



        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Opname());
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_nik = (TextView) headerView.findViewById(R.id.txt_nik);
        txt_nik.setText(nik);
        txt_nama = (TextView) headerView.findViewById(R.id.txt_nama);
        txt_nama.setText(nama);

       /* NavigationMenuItemView navigationMenuItemView= (NavigationMenuItemView) findViewById(R.id.nav_logout);
        navigationMenuItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        }); */
        //txtShow.setText(so.sendJsonToServer(kodeToko, kodeLokator, Ip, tanggalOpname, team));

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {

           SharedPreferences pref = getSharedPreferences("OpnameSetting",MODE_PRIVATE);
           if(!pref.getString("Tanggal_Opname", "-").equals("-")){
               upload();

            }
           // exportDB();
            return true;
        } else if (id == R.id.action_cek_update) {
            Intent i = new Intent(this, UpdaterActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_to_csv) {
            //Log.d("csv", "before export");
            if ( so.exportDatabase(team,kodeToko)) {
                Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "DB Failed!", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.action_download) {
            exportDB();
            Toast.makeText(this, "DB Download!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_getlokator) {
            //exportDB();
            sl = new SourceLokator(this);
            sl.open();
           sl.delete();
            Intent i = new Intent(this, UpdateLokator.class);
            startActivity(i);
          //  Toast.makeText(this, "Lokator Done!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String tag = null;
        if (id == R.id.nav_list_opname) {
            fragment = new fragment_list_opname();
            tag = "listopname";
        } else if (id == R.id.nav_setting_opname) {
            fragment = new SettingOpname();
            tag = "settingopname";
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_opname) {
            fragment = new Opname();
            tag = "opname";
        }else if (id == R.id.nav_close) {
            finish();
            System.exit(0);
        }else if (id == R.id.nav_delete) {

            deleteopname();

            return true;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment,tag);
            ft.commit();
        }
        DrawerLayout mDrawerLayout;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();
        return true;
    }


    private void exportDB(){
       String datenow = DateFormat.getDateTimeInstance().format(new Date());
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);//Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.shafco.newstockopname" +"/databases/"+"stockopname_master.db";//+today.toString();
        String backupDBPath = datenow+"_stockopname_master.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        alertDialogBuilder.setMessage("Anda melakukan logout ?")
            .setCancelable(false)
            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = getSharedPreferences("LoginPref", MODE_PRIVATE).edit();
                    editor.clear();
                    SharedPreferences.Editor edit = getSharedPreferences("OpnameSetting", MODE_PRIVATE).edit();
                    edit.clear();
                    if(editor.commit() && edit.commit()     ){
                        Intent i = new Intent(context, MainActivity.class);
                        finish();
                        startActivity(i);
                    }
                }
            })
            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deleteopname() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        alertDialogBuilder.setMessage("Anda yakin akan menghapus data opname ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportDB();
                        if ( so.deleteOpname()) {
                            Toast.makeText(getApplicationContext(), "DB Exported!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "DB Failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void upload(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        alertDialogBuilder.setMessage("Anda yakin akan mengupload opname sekarang ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(context, UploadDialog.class);
                            startActivity(i);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



}

