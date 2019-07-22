package com.project.it.expert;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import java.io.IOException;
import java.util.Calendar;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by hashemi on 01/23/2018.
 */

public class Joziat_Sefaresh extends AppCompatActivity{
    private String hamyarcode;
    private String guid;
    private String BsUserServicesID;
    private	DatabaseHelper dbh;
    private SQLiteDatabase db;
    private TextView tvUserNameOrder;
    private TextView tvFinalPrice;
    private TextView tvStatusService;
    private TextView tvContentTypeService;
    private TextView tvDateService;
    private TextView tvContentAddress;
    private TextView tvCodeService;
    private TextView tvDateAndTimeVisitService;
    private TextView tvStartService;
    private TextView tvFinishService;
    private ImageView imgUser;
    private LinearLayout LinearCallUser;
    private LinearLayout LinearMessageUser;
    private LinearLayout LinearButtonBottom;
    private Button btnShowMap;
    private Button btnVisit;
    private Button btnEditPrice;
    private Button btnStartJob;
    private String latStr;
    private String lonStr;
    private boolean SuggetionFinal;
    private String UserID="0";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joziat_sefaresh);
        //***************************************************************
        tvUserNameOrder=(TextView) findViewById(R.id.tvUserNameOrder);
        tvFinalPrice=(TextView) findViewById(R.id.tvFinalPrice);
        tvStatusService=(TextView) findViewById(R.id.tvStatusService);
        tvContentTypeService=(TextView) findViewById(R.id.tvContentTypeService);
        tvDateService=(TextView) findViewById(R.id.tvDateService);
        tvContentAddress=(TextView) findViewById(R.id.tvContentAddress);
        tvCodeService=(TextView) findViewById(R.id.tvCodeService);
        tvDateAndTimeVisitService=(TextView) findViewById(R.id.tvDateAndTimeVisitService);
        tvStartService=(TextView) findViewById(R.id.tvStartService);
        tvFinishService=(TextView) findViewById(R.id.tvFinishService);
        imgUser=(ImageView) findViewById(R.id.imgUser);
        LinearCallUser=(LinearLayout) findViewById(R.id.LinearCallUser);
        LinearMessageUser=(LinearLayout) findViewById(R.id.LinearMessageUser);
        LinearButtonBottom=(LinearLayout) findViewById(R.id.LinearButtonBottom);
        btnShowMap=(Button) findViewById(R.id.btnShowMap);
        btnVisit=(Button) findViewById(R.id.btnVisit);
        btnEditPrice=(Button) findViewById(R.id.btnEditPrice);
        btnStartJob=(Button) findViewById(R.id.btnStartJob);
        //***************************************************************

        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
            BsUserServicesID = getIntent().getStringExtra("BsUserServicesID").toString();
        }
        catch (Exception e)
        {
            Toast.makeText(Joziat_Sefaresh.this,"خطا در بارگزاری اطلاعات",Toast.LENGTH_LONG).show();
        }
        dbh=new DatabaseHelper(getApplicationContext());
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
        //********************************************************************
        db=dbh.getReadableDatabase();
        Cursor cursors = db.rawQuery("SELECT * FROM StartDateService WHERE BsUserServiceCode='"+BsUserServicesID+"'", null);
        if(cursors.getCount()>0)
        {
            cursors.moveToNext();
            if(cursors.getString(cursors.getColumnIndex("UserCode")).compareTo("0")!=0) {
                SuggetionFinal=true;
                btnStartJob.setText("اتمام کار");
            }
            else
            {
                SuggetionFinal=false;
            }
        }
        else
        {
            SuggetionFinal=false;
        }
        cursors.close();
        if(db.isOpen()){db.close();}
        //********************************************************************
        db=dbh.getReadableDatabase();
        String Query="SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM BsUserServices " +
                "LEFT JOIN " +
                "SuggetionsInfo ON " +
                "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode"+
                " LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='1' AND SuggetionsInfo.BsUserServicesCode='"+BsUserServicesID+"'";
        Cursor coursors = db.rawQuery(Query, null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();
                latStr=coursors.getString(coursors.getColumnIndex("Lat"));
                lonStr=coursors.getString(coursors.getColumnIndex("Lng"));
                UserID=coursors.getString(coursors.getColumnIndex("UserCode"));
                try
                {
                    tvUserNameOrder.setText(coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily")));
                }
                catch (Exception ex)
                {
                    tvUserNameOrder.setText(" * ");
                }
                try
                {
                    tvStatusService.setText(TranslateStatus(coursors.getString(coursors.getColumnIndex("Status"))));
                }
                catch (Exception ex)
                {
                    tvStatusService.setText(" * ");
                }
                try
                {
                    tvContentTypeService.setText(coursors.getString(coursors.getColumnIndex("name")));
                }
                catch (Exception ex)
                {
                    tvContentTypeService.setText(" * ");
                }
                try
                {
                    tvFinalPrice.setText(coursors.getString(coursors.getColumnIndex("Price")));
                }
                catch (Exception ex)
                {
                    tvFinalPrice.setText(" * ");
                }
                try
                {
                    tvDateService.setText(coursors.getString(coursors.getColumnIndex("StartDate")) + " ساعت " + coursors.getString(coursors.getColumnIndex("StartTime")));
                }
                catch (Exception ex)
                {
                    tvDateService.setText(" * ");
                }
                try
                {
                    tvContentAddress.setText(coursors.getString(coursors.getColumnIndex("AddressText")));
                }
                catch (Exception ex)
                {
                    tvContentAddress.setText(" * ");
                }
                try
                {
                    tvCodeService.setText(coursors.getString(coursors.getColumnIndex("Code_BsUserServices")));
                }
                catch (Exception ex)
                {
                    tvCodeService.setText(" * ");
                }
                try
                {
                    tvDateAndTimeVisitService.setText(coursors.getString(coursors.getColumnIndex("VisitDate")) + " ساعت " + coursors.getString(coursors.getColumnIndex("VisitTime")));
                }
                catch (Exception ex)
                {
                    tvDateAndTimeVisitService.setText(" * ");
                }
                try
                {
                    tvStartService.setText(coursors.getString(coursors.getColumnIndex("StartDate")) + " ساعت " + coursors.getString(coursors.getColumnIndex("StartTime")));
                }
                catch (Exception ex)
                {
                    tvStartService.setText(" * ");
                }
                try
                {
                    tvFinishService.setText(coursors.getString(coursors.getColumnIndex("EndDate")) + " ساعت " + coursors.getString(coursors.getColumnIndex("EndTime")));
                }
                catch (Exception ex)
                {
                    tvFinishService.setText(" * ");
                }
            }
            if(db.isOpen()){db.close();}

        btnStartJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(SuggetionFinal)
                {

                    SyncFinalJob syncFinalJob =new SyncFinalJob(Joziat_Sefaresh.this,guid,hamyarcode,BsUserServicesID);
                    syncFinalJob.AsyncExecute();
                }
                else {

                    SyncStartJob syncStartJob =new SyncStartJob(Joziat_Sefaresh.this,guid,hamyarcode,BsUserServicesID,UserID);
                    syncStartJob.AsyncExecute();
                }
            }
        });
        btnEditPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(SuggetionFinal)
                {
                    LoadActivity2(Pishnahad_Gheimat_Final.class, "guid", guid,
                            "hamyarcode", hamyarcode,
                            "OrderCode", BsUserServicesID,
                            "Table","BsUserServices");
                }
                else {
                    LoadActivity2(Pishnahad_Gheimat.class, "guid", guid,
                            "hamyarcode", hamyarcode,
                            "OrderCode", BsUserServicesID,
                            "Table","BsUserServices");
                }
            }
        });
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoadActivity3(Map.class,"guid",guid,
                        "hamyarcode",hamyarcode,
                        "latStr",latStr,
                        "lonStr",lonStr,
                        "BsUserServicesID",BsUserServicesID);
            }
        });
//        btnCansel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                LayoutInflater li = LayoutInflater.from(Joziat_Sefaresh.this);
//                View promptsView = li.inflate(R.layout.cansel, null);
//                AlertDialog.Builder alertbox = new AlertDialog.Builder(Joziat_Sefaresh.this);
//                //set view
//                alertbox.setView(promptsView);
//                final EditText descriptionCansel = (EditText) promptsView.findViewById(R.id.etCansel);
//
//                alertbox
//                        .setCancelable(true)
//                        .setPositiveButton("بله",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        // get user input and set it to result
//                                        // edit text
//                                        SyncCanselJob syncCanselJob=new SyncCanselJob(Joziat_Sefaresh.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
//                                        coursors.getString(coursors.getColumnIndex("id")),descriptionCansel.getText().toString());
//                                        syncCanselJob.AsyncExecute();
//                                    }
//                                })
//                        .setNegativeButton("خیر",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertbox.create();
//
//                // show it
//                alertDialog.show();
//
//            }
//        });
//        btnPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                LayoutInflater li = LayoutInflater.from(Joziat_Sefaresh.this);
//                View promptsView = li.inflate(R.layout.cansel, null);
//                AlertDialog.Builder alertbox = new AlertDialog.Builder(Joziat_Sefaresh.this);
//                //set view
//                alertbox.setView(promptsView);
//                final EditText descriptionCansel = (EditText) promptsView.findViewById(R.id.etCansel);
//
//                alertbox
//                        .setCancelable(true)
//                        .setPositiveButton("بله",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        // get user input and set it to result
//                                        // edit text
//                                        SyncPauseJob syncPauseJob=new SyncPauseJob(Joziat_Sefaresh.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
//                                                coursors.getString(coursors.getColumnIndex("id")));
//                                        syncPauseJob.AsyncExecute();
//                                    }
//                                })
//                        .setNegativeButton("خیر",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertbox.create();
//
//                // show it
//                alertDialog.show();
//            }
//        });
        btnVisit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            PersianDatePickerDialog picker = new PersianDatePickerDialog(Joziat_Sefaresh.this);
                                            picker.setPositiveButtonString("تایید");
                                            picker.setNegativeButton("انصراف");
                                            picker.setTodayButton("امروز");
                                            picker.setTodayButtonVisible(true);
                                            //  picker.setInitDate(initDate);
                                            picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
                                            picker.setMinYear(1300);
                                            picker.setActionTextColor(Color.GRAY);
                                            picker.setListener(new Listener() {

                                                @Override
                                                public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
                                                    //Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
                                                    String StartYear = String.valueOf(persianCalendar.getPersianYear());
                                                    String StartMonth, StartDay;
                                                    if (persianCalendar.getPersianMonth() < 10) {
                                                        StartMonth = "0" + String.valueOf(persianCalendar.getPersianMonth() + 1);
                                                    } else {
                                                        StartMonth = String.valueOf(persianCalendar.getPersianMonth() + 1);
                                                    }
                                                    if (persianCalendar.getPersianDay() < 10) {
                                                        StartDay = "0" + String.valueOf(persianCalendar.getPersianDay());
                                                    } else {
                                                        StartDay = String.valueOf(persianCalendar.getPersianDay());
                                                    }
                                                    db = dbh.getWritableDatabase();
                                                    String query = "UPDATE  DateTB SET Date = '" + StartYear + "/" + StartMonth + "/" + StartDay + "'";
                                                    db.execSQL(query);if(db.isOpen()){db.close();}
                                                    if(db.isOpen()){db.close();}
                                                    GetTime();
                                                }

                                                @Override
                                                public void onDismissed() {

                                                }
                                            });
                                            picker.show();
                                        }
                                    });



//        btnResume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                SyncResumeJob syncResumeJob=new SyncResumeJob(Joziat_Sefaresh.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
//                        coursors.getString(coursors.getColumnIndex("id")));
//                syncResumeJob.AsyncExecute();
//            }
//        });
//        btnFinal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                SyncFinalJob syncFinalJob=new SyncFinalJob(Joziat_Sefaresh.this,guid,hamyarcode,coursors.getString(coursors.getColumnIndex("Code")),
//                        coursors.getString(coursors.getColumnIndex("id")));
//                syncFinalJob.AsyncExecute();
//            }
//        });
//        btnPerFactor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                String query = "SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
//                        "LEFT JOIN " +
//                        "Servicesdetails ON " +
//                        "Servicesdetails.code=BsUserServices.ServiceDetaileCode AND BsUserServices.Code_BsUserServices=" + BsUserServicesID;
//                db=dbh.getReadableDatabase();
//                coursors = db.rawQuery(query, null);
//                if (coursors.getCount()>0) {
//                    coursors.moveToNext();
//                    LoadActivity_PerFactor(Save_Per_Factor.class,"tab",tab,"BsUserServicesID",BsUserServicesID,"ServiceDetaileCode",coursors.getString(coursors.getColumnIndex("ServiceDetaileCode")));
//                }
//
//                if(db.isOpen()){db.close();}
//            }
//        });
        LinearCallUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Joziat_Sefaresh.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(Joziat_Sefaresh.this, android.Manifest.permission.CALL_PHONE))
                    {
                        //do nothing
                    }
                    else{

                        ActivityCompat.requestPermissions(Joziat_Sefaresh.this,new String[]{android.Manifest.permission.CALL_PHONE},2);
                    }

                }
               try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                Cursor cursorPhone;
                cursorPhone = db.rawQuery("SELECT * FROM BsUserServices WHERE Code_BsUserServices='"+BsUserServicesID+"'", null);
                if (cursorPhone.getCount() > 0) {
                    cursorPhone.moveToNext();
                    dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("UserPhone")));
                }
                if(db.isOpen()){db.close();}
            }
        });


    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            String Query="SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM SuggetionsInfo " +
                    "LEFT JOIN " +
                    "BsUserServices ON " +
                    "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode"+
                    " LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='1'";
            LoadActivity2(List_Sertains.class, "hamyarcode", hamyarcode,
                    "guid", guid,"Query",Query,
                    "Table","BsUserServices");
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        Joziat_Sefaresh.this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3, String VariableName4, String VariableValue4)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        intent.putExtra(VariableName4, VariableValue4);
        Joziat_Sefaresh.this.startActivity(intent);
    }
    public void LoadActivity3(Class<?> Cls, String VariableName, String VariableValue,
                              String VariableName2, String VariableValue2,
                              String VariableName3, String VariableValue3,
                              String VariableName4, String VariableValue4,
                              String VariableName5, String VariableValue5)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        intent.putExtra(VariableName4, VariableValue4);
        intent.putExtra(VariableName5, VariableValue5);
        Joziat_Sefaresh.this.startActivity(intent);
    }
    public void GetTime()
    {
        Calendar now = Calendar.getInstance();
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
                try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
                String query="UPDATE  DateTB SET Time = '" +String.valueOf(hourOfDay)+":"+String.valueOf(minute)+"'";
                db.execSQL(query);if(db.isOpen()){db.close();}
                db=dbh.getReadableDatabase();
                query="SELECT * FROM DateTB";
                Cursor c=db.rawQuery(query,null);
                if(c.getCount()>0)
                {
                    c.moveToNext();
                    String[] DateTB = c.getString(c.getColumnIndex("Date")).split("/");
                    String[] TimeTB = c.getString(c.getColumnIndex("Time")).split(":");
                    SyncVisitJob syncVisitJob = new SyncVisitJob(Joziat_Sefaresh.this, guid, hamyarcode, BsUserServicesID, DateTB[0], DateTB[1], DateTB[2], TimeTB[0], TimeTB[1]);
                    syncVisitJob.AsyncExecute();
                }
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        mTimePicker.setTitle("انتخاب زمان");
        mTimePicker.setButton(BUTTON_POSITIVE,"تایید",mTimePicker);
        mTimePicker.setButton(BUTTON_NEGATIVE,"انصراف",mTimePicker);
        mTimePicker.show();

    }
    public void dialContactPhone(String phoneNumber) {
        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        startActivity(callIntent);
    }
    public String TranslateStatus(String status)
    {
        String StrStatus="";
        switch (status)
        {
            case "0":
                StrStatus="آزاد شد";
                break;
            case "1":
                StrStatus="در نوبت انجام قرار گرفت";
                break;
            case "2":
                StrStatus="در حال انجام است";
                break;
            case "3":
                StrStatus="لغو شد";
                break;
            case "4":
                StrStatus="اتمام و تسویه شده است";
                break;
            case "5":
                StrStatus="اتمام و تسویه نشده است";
                break;
            case "6":
                StrStatus="اعلام شکایت شده است";
                break;
            case "7":
                StrStatus="درحال پیگیری شکایت و یا رفع خسارت می باشد";
                break;
            case "8":
                StrStatus=" توسط متخصص رفع عیب و خسارت شده است";
                break;
            case "9":
                StrStatus="پرداخت خسارت";
                break;
            case "10":
                StrStatus="پرداخت جریمه";
                break;
            case "11":
                StrStatus="تسویه حساب با متخصص";
                break;
            case "12":
                StrStatus="متوقف و تسویه شده است";
                break;
            case "13":
                StrStatus="متوقف و تسویه نشده است";
                break;
        }
        return StrStatus;
    }
}
