	package com.project.it.expert;

    import android.app.Activity;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    public class List_Visits extends Activity {
        private String hamyarcode;
        private String guid;
        private ListView lvVisit;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnCredit;
        private Button btnOrders;
        private Button btnHome;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_visits);
            btnCredit=(Button)findViewById(R.id.btnCredit);
            btnOrders=(Button)findViewById(R.id.btnOrders);
            btnHome=(Button)findViewById(R.id.btnHome);
        lvVisit=(ListView)findViewById(R.id.listViewVisit);
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
            db.close();
        }
        db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT BsHamyarSelectServices.*,Servicesdetails.name FROM BsHamyarSelectServices " +
                    "LEFT JOIN " +
                    "Servicesdetails ON " +
                    "Servicesdetails.code=BsHamyarSelectServices.ServiceDetaileCode WHERE IsDelete='0' AND Status='5' ORDER BY StartDate DESC",null);
        if(coursors.getCount()>0)
        {
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name","شماره درخواست: "+coursors.getString(coursors.getColumnIndex("Code"))+"\n"+
                        "موضوع: "+coursors.getString(coursors.getColumnIndex("name"))+"\n"
                        +"نام متقاضی: "+coursors.getString(coursors.getColumnIndex("UserName"))+
                        " "+coursors.getString(coursors.getColumnIndex("UserFamily"))+"\n"+
                        "تاریخ حضور: "+coursors.getString(coursors.getColumnIndex("StartDate"))+"\n"+
                        "ساعت حضور: "+coursors.getString(coursors.getColumnIndex("StartTime"))+"\n"+
                        "تاریخ ثبت بازدید: "+coursors.getString(coursors.getColumnIndex("VisitDate"))+"\n"+
                        "ساعت بازدید: "+coursors.getString(coursors.getColumnIndex("VisitTime"))+"\n"+
                        "وضعیت: "+((coursors.getString(coursors.getColumnIndex("IsEmergency")).compareTo("0")==1? "عادی":"فوری")));
                map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
                valuse.add(map);
            }
            AdapterVisit dataAdapter=new AdapterVisit(List_Visits.this,valuse,guid,hamyarcode);
            lvVisit.setAdapter(dataAdapter);
        }
            db.close();
            btnCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(Credit.class, "guid",  guid, "hamyarcode", hamyarcode);
                }
            });
            btnOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(History.class, "guid", guid, "hamyarcode", hamyarcode);
                }
            });
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
                }
            });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            List_Visits.this.LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            List_Visits.this.startActivity(intent);
        }
    }