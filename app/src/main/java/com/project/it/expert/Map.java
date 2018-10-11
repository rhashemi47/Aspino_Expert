package com.project.it.expert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class Map extends AppCompatActivity {
    public double lat = 0;
    private double lon = 0;
    private String hamyarcode;
    private String guid;
    private String BsUserServicesID;
    private GoogleMap map;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private GPSTracker  gps;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Handler mHandler;
    private boolean continue_or_stop=true;
    private AlertDialog.Builder alertDialog = null;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
            BsUserServicesID = getIntent().getStringExtra("BsUserServicesID").toString();
            lat = Float.valueOf(getIntent().getStringExtra("latStr").toString());
            lon = Float.valueOf(getIntent().getStringExtra("lonStr").toString());
        }
        catch (Exception e)
        {
            Toast.makeText(Map.this,"خطا در بارگزاری اطلاعات",Toast.LENGTH_LONG).show();
            lat=0;
            lon=0;
        }
        dbh = new DatabaseHelper(getApplicationContext());
        try {

            dbh.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbh.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }

//********************
   Run_thered();
//******************************************************************************
}

    private void Run_thered() {
        mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (continue_or_stop) {
                    try {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {
//                                    continue_or_stop=false;
                                    ActivityCompat.requestPermissions(Map.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);

//            return;
                                }
                                else {
                                    Check_GPS();
                                }
                            }
                        });

                        Thread.sleep(1000); // every 5 seconds
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    private void Check_GPS() {
        gps = new GPSTracker(Map.this);
        if (gps.canGetLocation()) {
            continue_or_stop=false;
            PrepareMap();
        } else {
            if (alertDialog == null) {
                alertDialog = new AlertDialog.Builder(Map.this);

                // Setting Dialog Title
                alertDialog.setTitle("تنظیمات جی پی اس");

                // Setting Dialog Message
                alertDialog.setMessage("جی پی اس شما غیرفعال می باشد.لطفا جهت کار کرد صحیح نرم افزار آن را فعال نمایید");

                // On pressing Settings button
                alertDialog.setPositiveButton("فعال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        continue_or_stop = true;
                        Run_thered();
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        continue_or_stop = true;
                        Run_thered();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

                LoadActivity(Joziat_Sefaresh.class, "guid", guid,"hamyarcode",hamyarcode,"BsUserServicesID",BsUserServicesID);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue,
                             String VariableName2, String VariableValue2,
                             String VariableName3, String VariableValue3) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        this.startActivity(intent);
    }
    public void PrepareMap()
    {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
                LatLng point;
                point = new LatLng(lat, lon);
                map.addMarker(new MarkerOptions().position(point).title("محل سرویس دهی").icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));


                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
//                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng latLng) {
//                        map.clear();
//                        map.addMarker(new MarkerOptions().position(latLng).title("محل سرویس دهی").icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
//                        lat=latLng.latitude;
//                        lon=latLng.longitude;
//                    }
//                });
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
//                        continue_or_stop = true;
                    } else {
                        // Permission Denied
//                        continue_or_stop = true;
                        Toast.makeText(Map.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                catch (Exception ex)
                {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

