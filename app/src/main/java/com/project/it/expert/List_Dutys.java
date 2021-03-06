	package com.project.it.expert;

    import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_Dutys extends Activity {
        private String hamyarcode;
        private String guid;
        private ListView lvDutys;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        //private Button btnCredit;
        //private Button btnOrders;
        //private Button btnHome;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_dutys);
//            btnCredit=(Button)findViewById(R.id.btnCredit);
//            btnOrders=(Button)findViewById(R.id.btnOrders);
//            btnHome=(Button)findViewById(R.id.btnHome);
        lvDutys=(ListView)findViewById(R.id.listViewDutys);
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
        try
        {
            hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
            guid = getIntent().getStringExtra("guid").toString();
        }
        catch (Exception e)
        {
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                guid=coursors.getString(coursors.getColumnIndex("guid"));
                hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
            }
            if(db.isOpen()){db.close();}
        }
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE BsUserServices.Status='1'",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name","شماره درخواست: "+coursors.getString(coursors.getColumnIndex("Code_BsHamyarSelectServices"))+"\n"+
                        "موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                        +"نام متقاضی: "+coursors.getString(coursors.getColumnIndex("UserName"))+" "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                        "تاریخ حضور: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+"ساعت حضور: "+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n"+
                        "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
                map.put("Code",coursors.getString(coursors.getColumnIndex("BsUserServices.Code_BsUserServices")));
                map.put("UserPhone",coursors.getString(coursors.getColumnIndex("UserPhone")));
                valuse.add(map);
            }
            if(db.isOpen()){db.close();}
            AdapterDutys dataAdapter=new AdapterDutys(this,valuse,guid,hamyarcode);
            lvDutys.setAdapter(dataAdapter);

//
//            btnCredit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
//                }
//            });
//            btnOrders.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
//                }
//            });
//            btnHome.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
//                }
//            });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            List_Dutys.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            List_Dutys.this.startActivity(intent);
        }
    }
