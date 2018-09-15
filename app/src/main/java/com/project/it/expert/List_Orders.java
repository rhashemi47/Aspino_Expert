	package com.project.it.expert;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.support.v4.view.GravityCompat;
    import android.support.v4.widget.DrawerLayout;
    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.Toast;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_Orders extends Activity {
        private String hamyarcode;
        private String guid;
        private ListView lstOrder;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private String Query;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        private String Table; private LinearLayout LinearOrders;
        private LinearLayout LinearCommentCustomer;
        private LinearLayout LinearCertain;
        private LinearLayout LinearSuggestions;
        private LinearLayout LinearHome;
        private LinearLayout LinearNotifications;
        private DrawerLayout mDrawer;
        private ImageView imgHumberger;
        private ImageView imgBack;

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu_list_order);
            lstOrder=(ListView)findViewById(R.id.lstOrder);
            LinearCommentCustomer = (LinearLayout) findViewById(R.id.LinearCommentCustomer);
            LinearOrders = (LinearLayout) findViewById(R.id.LinearOrders);
            LinearCertain = (LinearLayout) findViewById(R.id.LinearCertain);
            LinearSuggestions = (LinearLayout) findViewById(R.id.LinearSuggestions);
            LinearHome = (LinearLayout) findViewById(R.id.LinearHome);
            LinearNotifications = (LinearLayout) findViewById(R.id.LinearNotifications);
            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            imgHumberger = (ImageView) findViewById(R.id.imgHumberger);
            imgBack = (ImageView) findViewById(R.id.imgBack);
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
            try {
                Query=getIntent().getStringExtra("Query").toString();
            }
            catch (Exception ex)
            {
                Query="";
            }
            try {
                Table=getIntent().getStringExtra("Table").toString();
            }
            catch (Exception ex)
            {
                Table="";
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
            Cursor coursors = db.rawQuery(Query,null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Code",coursors.getString(coursors.getColumnIndex("Code_"+Table)));
                map.put("TitleOrder",coursors.getString(coursors.getColumnIndex("name")));
                map.put("OrderDate",coursors.getString(coursors.getColumnIndex("StartDate")));
                map.put("Addres",coursors.getString(coursors.getColumnIndex("AddressText")));
                map.put("Table",Table);
                valuse.add(map);
            }
            db.close();
            AdapterServices dataAdapter=new AdapterServices(this,valuse,guid,hamyarcode);
            lstOrder.setAdapter(dataAdapter);
            //**************************************************************************
            imgHumberger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            //**************************************************************************
            LinearOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   
                        String Query="SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                                "LEFT JOIN " +
                                "Servicesdetails ON " +
                                "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE BsUserServices.Suggetion='0'";
                        LoadActivity2(List_Orders.class, "hamyarcode", hamyarcode,
                                "guid", guid,"Query",Query,
                                "Table","BsUserServices");

                }
            });
            LinearCertain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   
                        String Query="SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM SuggetionsInfo " +
                                "LEFT JOIN " +
                                "BsUserServices ON " +
                                "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode"+
                                " LEFT JOIN " +
                                "Servicesdetails ON " +
                                "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='1'";
                        LoadActivity2(List_Orders.class, "hamyarcode", hamyarcode,
                                "guid", guid,"Query",Query,
                                "Table","BsUserServices");
                    }
            });
            LinearSuggestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String Query = "SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM SuggetionsInfo " +
                            "LEFT JOIN " +
                            "BsUserServices ON " +
                            "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode" +
                            " LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='0'";
                    LoadActivity2(List_Orders.class, "hamyarcode", hamyarcode,
                            "guid", guid, "Query", Query,
                            "Table", "BsUserServices");
                }

            });
            LinearHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity(List_Orders.class, "hamyarcode", hamyarcode, "guid", guid);
                }
            });
            LinearNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   
                        LoadActivity(CommentCustomer.class, "hamyarcode", hamyarcode, "guid", guid);
                    }
            });
    }
        @Override
        public void onBackPressed() {

            if (mDrawer.isDrawerOpen(GravityCompat.START)) {

                mDrawer.closeDrawer(GravityCompat.START);

            } else {

//			super.onBackPressed();
                List_Orders.this.LoadActivity(List_Orders.class, "guid", guid, "hamyarcode", hamyarcode);
            }

        }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            List_Orders.this.startActivity(intent);
        }
        public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue,
                                  String VariableName2, String VariableValue2,
                                  String VariableName3, String VariableValue3,
                                  String VariableName4, String VariableValue4)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);
            intent.putExtra(VariableName3, VariableValue3);
            intent.putExtra(VariableName4, VariableValue4);
            this.startActivity(intent);
        }
    }
