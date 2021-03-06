package com.project.it.expert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.project.it.expert.Date.ChangeDate;
import com.project.it.expert.viewbadger.BadgeView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity{
    private String hamyarcode;
    private String guid;
    private boolean isActive=false;
    private String StrToday;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String countMessage;
    private String countVisit;
    private TextView tvOrders;
    private TextView tvDateMessage;
    private TextView tvContentMessage;
    private TextView tvDayOfWeek;
    private TextView tvTitleMounth;
    private TextView tvDayOfMounth;
    private TextView tvToday;
    private TextView tvUserName;
    private TextView tvRateNumber;
    private TextView tvSabegheKar;
    private TextView tvCountSelectService;
    private RatingBar RatingHamyar;
    private ImageView imgHumberger;
    private ImageView right_white_arrow;
    private ImageView left_white_arrow;
    private ImageView imgSmile;
    private DrawerLayout mDrawer;
    private LinearLayout LinearCallSupporter;
    private LinearLayout LinearRole;
    private LinearLayout LinearAboutAspino;
    private LinearLayout LinearLogout;
    private LinearLayout LinearIncome;
    private LinearLayout LinearCredite;
    private LinearLayout LinearBankID;
    private LinearLayout LinearKarnameMali;
    private LinearLayout LinearCommentCustomer;
    private LinearLayout LinearOrders;
    private LinearLayout LinearCertain;
    private LinearLayout LinearSuggestions;
    private LinearLayout LinearHome;
    private LinearLayout LinearNotifications;
    private LinearLayout LinearCheckIsOrder;
    private NotificationBadge badgeOrders;
    private NotificationBadge badgeCertain;
    private NotificationBadge badgeOffers;
    private NotificationBadge badgeNotification;
    private ImageView imgUser;
    private ir.hamsaa.persiandatepicker.util.PersianCalendar calNow;

//    private Button btnServices;
//    //private Button btnCredit;
//    //private Button btnOrders;
//    //private Button btnHome;
//    ArrayList<String> slides;
    ImageView imageView;
    private String AppVersion;
    private TextView tvShowIncome;
    private TextView tvCountOrder;
    private boolean continue_or_stop=true;
    private Handler mHandler;
    boolean createthread=true;

    //    Custom_ViewFlipper viewFlipper;
//    GestureDetector mGestureDetector;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu_mainmenu);
        setupWindowAnimations();
        //****************************************************************
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        if(version.length()>0) {
            AppVersion = version;
            WsDownLoadUpdate wsDownLoadUpdate=new WsDownLoadUpdate(MainMenu.this,AppVersion, PublicVariable.LinkFileTextCheckVersion,PublicVariable.DownloadAppUpdateLinkAPK);
            wsDownLoadUpdate.AsyncExecute();
        }
        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
        tvShowIncome = (TextView) findViewById(R.id.tvShowIncome);
        tvOrders = (TextView) findViewById(R.id.tvOrders);
        tvDayOfWeek = (TextView) findViewById(R.id.tvDayOfWeek);
        tvTitleMounth = (TextView) findViewById(R.id.tvTitleMounth);
        tvDayOfMounth = (TextView) findViewById(R.id.tvDayOfMounth);
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvDateMessage = (TextView) findViewById(R.id.tvDateMessage);
        tvContentMessage = (TextView) findViewById(R.id.tvContentMessage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvCountSelectService = (TextView) findViewById(R.id.tvCountSelectService);
        tvCountOrder = (TextView) findViewById(R.id.tvCountOrder);
        tvSabegheKar = (TextView) findViewById(R.id.tvSabegheKar);
        imgHumberger = (ImageView) findViewById(R.id.imgHumberger);
        right_white_arrow = (ImageView) findViewById(R.id.right_white_arrow);
        left_white_arrow = (ImageView) findViewById(R.id.left_white_arrow);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        imgSmile = (ImageView) findViewById(R.id.imgSmile);
        tvRateNumber = (TextView) findViewById(R.id.tvRateNumber);
        RatingHamyar = (RatingBar) findViewById(R.id.RatingHamyar);
        LinearIncome = (LinearLayout) findViewById(R.id.LinearIncome);
        LinearCredite = (LinearLayout) findViewById(R.id.LinearCredite);
        LinearBankID = (LinearLayout) findViewById(R.id.LinearBankID);
        LinearKarnameMali = (LinearLayout) findViewById(R.id.LinearKarnameMali);
        LinearCommentCustomer = (LinearLayout) findViewById(R.id.LinearCommentCustomer);
        LinearOrders = (LinearLayout) findViewById(R.id.LinearOrders);
        LinearCertain = (LinearLayout) findViewById(R.id.LinearCertain);
        LinearSuggestions = (LinearLayout) findViewById(R.id.LinearSuggestions);
        LinearHome = (LinearLayout) findViewById(R.id.LinearHome);
        LinearNotifications = (LinearLayout) findViewById(R.id.LinearNotifications);
        LinearCheckIsOrder = (LinearLayout) findViewById(R.id.LinearCheckIsOrder);
        badgeOrders=(NotificationBadge) findViewById(R.id.badgeOrders);
        badgeCertain=(NotificationBadge) findViewById(R.id.badgeCertain);
        badgeOffers=(NotificationBadge) findViewById(R.id.badgeOffers);
        badgeNotification=(NotificationBadge) findViewById(R.id.badgeNotification);

        //***************************************************************************************
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
       try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
        Cursor coursors = db.rawQuery("SELECT * FROM messages ORDER BY Code_messages desc", null);
        if (coursors.getCount() > 0) {
//            countMessage = String.valueOf(coursors.getCount());
            coursors.moveToNext();
            tvDateMessage.setText(coursors.getString(coursors.getColumnIndex("InsertDate")));
            tvContentMessage.setText(coursors.getString(coursors.getColumnIndex("Content")));
        }
        coursors = db.rawQuery("SELECT * FROM BsUserServices WHERE Status='5'", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            tvCountOrder.setText(String.valueOf(coursors.getCount()));
        }
        else
        {
            tvCountOrder.setText("0");
        }
//        coursors = db.rawQuery("SELECT * FROM BsUserServices WHERE Status='1'", null);
//        if (coursors.getCount() > 0) {
//            coursors.moveToNext();
//            LinearCheckIsOrder.setVisibility(View.GONE);
//        }
//        else
//        {
//            LinearCheckIsOrder.setVisibility(View.VISIBLE);
//        }
        coursors = db.rawQuery("SELECT * FROM Profile", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            tvSabegheKar.setText(coursors.getString(coursors.getColumnIndex("DateStart")) + "ماه");
        }
        else
        {
            tvSabegheKar.setText("0"+ "ماه");
        }
        try {
            hamyarcode = getIntent().getStringExtra("hamyarcode");
            guid = getIntent().getStringExtra("guid");
            if (hamyarcode == null || guid == null)
            {

                Cursor cursors = null;
               try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                cursors = db.rawQuery("SELECT * FROM login", null);
                if (cursors.getCount() > 0)
                {
                    cursors.moveToNext();
                    String Result = cursors.getString(cursors.getColumnIndex("islogin"));
                    if (Result.compareTo("0") == 0)
                    {
                        isActive=false;
                        LoadActivity(Login.class, "hamyarcode", "0", "guid", "0");
                    }
                    else {
                        db=dbh.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM Profile", null);
                        if (cursor.getCount() > 0) {
                            cursor.moveToNext();
                            String Status=cursor.getString(cursor.getColumnIndex("Status"));
                            if (Result.compareTo("1") == 0 && Status.compareTo("0") == 0) {
                                Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                                isActive = false;
                            }
                            else
                            {
                                isActive = true;
                            }
                        }
                    }
                }
                else
                {
                    isActive=false;
                    LoadActivity(Login.class, "hamyarcode", "0", "guid", "0");
                }
            }
            else
            {
                Cursor cursors = null;
               try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                cursors = db.rawQuery("SELECT * FROM login", null);
                if (cursors.getCount() > 0)
                {
                    cursors.moveToNext();
                    String Result = cursors.getString(cursors.getColumnIndex("islogin") );
                    Cursor cursor=db.rawQuery("SELECT * FROM Profile", null);
                    if(cursor.getCount()>0) {
                        cursor.moveToNext();
                        if (Result.compareTo("1") == 0 && cursor.getString(cursor.getColumnIndex("Status")).compareTo("1") == 0) {

                            hamyarcode = cursors.getString(cursors.getColumnIndex("hamyarcode"));
                            guid = cursors.getString(cursors.getColumnIndex("guid"));
                            isActive = true;
                        } else {
                            isActive = false;
//                            LoadActivity(Login.class, "hamyarcode", "0", "guid", "0");
                        }
                    }
                    cursor.close();
                }
            }

            if(db.isOpen()){db.close();}
        } catch (Exception e) {
            throw new Error("Error Opne Activity");
        }
        //************************Check Active*******************************************************
        continue_or_stop=true;
            mHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (continue_or_stop) {
                        try {
                            Thread.sleep(1000);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Cursor cursors = null;
                                   try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                                    cursors = db.rawQuery("SELECT * FROM login", null);
                                    if (cursors.getCount() > 0)
                                    {
                                        cursors.moveToNext();
                                        String Result = cursors.getString(cursors.getColumnIndex("islogin") );
                                        Cursor cursor=db.rawQuery("SELECT * FROM Profile", null);
                                        if(cursor.getCount()>0) {
                                            cursor.moveToNext();
                                            if (Result.compareTo("1") == 0 && cursor.getString(cursor.getColumnIndex("Status")).compareTo("1") == 0) {

                                                hamyarcode = cursors.getString(cursors.getColumnIndex("hamyarcode"));
                                                guid = cursors.getString(cursors.getColumnIndex("guid"));
                                                isActive = true;
                                            } else {
                                                isActive = false;
                                            }
                                        }
                                        cursor.close();
                                    }
                                    if(db.isOpen())
                                    {
                                        if(db.isOpen()){db.close();}
                                    }
                                    if(!cursors.isClosed())
                                    {
                                        cursors.close();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
            }).start();
        //************************Profile*******************************************************
        Bitmap bmp = null;
        db=dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile",null);
        if(cursor.getCount()>0){
            cursor.moveToNext();
            tvUserName.setText(cursor.getString(cursor.getColumnIndex("Name")) + " " + cursor.getString(cursor.getColumnIndex("Fam")));
            float rating=Float.parseFloat(cursor.getString(cursor.getColumnIndex("Rate")));
            tvRateNumber.setText(EnToFa(cursor.getString(cursor.getColumnIndex("Rate"))));
            RatingHamyar.setRating(rating);
            try
            {
                if(cursor.getString(cursor.getColumnIndex("Pic")).length()>0) {
                    bmp = convertToBitmap(cursor.getString(cursor.getColumnIndex("Pic")));
                }
            }
            catch (Exception ex)
            {
                bmp= BitmapFactory.decodeResource(getResources(),R.drawable.logo1);
            }
        }
        else {
            bmp= BitmapFactory.decodeResource(getResources(),R.drawable.logo1);
        }

        if(db.isOpen()){db.close();}
        imgUser.setImageBitmap(bmp);
        //************************Calender*******************************************************
        calNow=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
        calNow.setPersianDate(calNow.getPersianYear(),calNow.getPersianMonth(),calNow.getPersianDay());
        String mounth,day;
        if(calNow.getPersianMonth()<10)
        {
            mounth="0"+String.valueOf(calNow.getPersianMonth());
        }
        else
        {
            mounth=String.valueOf(calNow.getPersianMonth());
        }
        if(calNow.getPersianDay()<10)
        {
            day="0"+String.valueOf(calNow.getPersianDay());
        }
        else
        {
            day=String.valueOf(calNow.getPersianDay());
        }
        tvDayOfWeek.setText(calNow.getPersianWeekDayName());
        tvTitleMounth.setText(calNow.getPersianMonthName());
        tvDayOfMounth.setText(EnToFa(String.valueOf(day)));
        StrToday=calNow.getPersianYear() + "/" + mounth + "/" + day;
        String Query="SELECT SuggetionsInfo.*,BsUserServices.* FROM BsUserServices " +
                "LEFT JOIN  " +
                "SuggetionsInfo ON  " +
                "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode" +
                " WHERE SuggetionsInfo.ConfirmByUser='1' AND StartDate='"+StrToday+"'";
        try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
        Cursor cursorCountSelectService=db.rawQuery(Query,null);
        if(cursorCountSelectService.getCount()>0)
        {
            cursorCountSelectService.moveToNext();
            imgSmile.setVisibility(View.GONE);
            tvCountSelectService.setText("سرویس در درست اجرا: "+cursorCountSelectService.getCount());
        }
        else
        {
            imgSmile.setVisibility(View.VISIBLE);
            tvCountSelectService.setText("وقت استراحته امروز سفارشی برای انجام ندارید");
        }
        if(!cursorCountSelectService.isClosed())
        {
            cursorCountSelectService.close();
        }
        if(db.isOpen())
        {
            db.close();
        }

        //********************************************************************
        LinearCallSupporter = (LinearLayout) findViewById(R.id.LinearCallSupporter);
        LinearRole = (LinearLayout) findViewById(R.id.LinearRole);
        LinearAboutAspino = (LinearLayout) findViewById(R.id.LinearAboutAspino);
        LinearLogout = (LinearLayout) findViewById(R.id.LinearLogout);

        LinearCallSupporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                Cursor cursor = db.rawQuery("SELECT * FROM Supportphone", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    dialContactPhone(cursor.getString(cursor.getColumnIndex("PhoneNumber")));
                }
                if(db.isOpen()){db.close();}
            }
        });
        LinearRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoadActivity(YourCommitment.class, "hamyarcode", hamyarcode, "guid", guid);
                Intent urlCall = new Intent(Intent.ACTION_VIEW);
                urlCall.setData(Uri.parse(PublicVariable.site));
                startActivity(urlCall);
            }
        });
        LinearAboutAspino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urlCall = new Intent(Intent.ACTION_VIEW);
                urlCall.setData(Uri.parse(PublicVariable.site));
                startActivity(urlCall);
            }
        });
        LinearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        LinearIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    LoadActivity(Income.class, "hamyarcode", hamyarcode, "guid", guid);
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearCredite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    LoadActivity(Credit.class, "hamyarcode", hamyarcode, "guid", guid);
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearBankID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    LoadActivity(Id_Bank.class, "hamyarcode", hamyarcode, "guid", guid);
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }

            }
        });
        LinearKarnameMali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    LoadActivity(KarnameMali.class, "hamyarcode", hamyarcode, "guid", guid);
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }

            }
        });
        LinearCommentCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    LoadActivity(CommentCustomer.class, "hamyarcode", hamyarcode, "guid", guid);
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }

            }
        });
        //**************************************************************************
        Query="SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                "LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE BsUserServices.Suggetion='0'  AND Read='0' ORDER BY BsUserServices.Code_BsUserServices DESC";
        if(!db.isOpen()) {

            db = dbh.getReadableDatabase();
        }
        coursors = db.rawQuery(Query,null);
        if(coursors.getCount()>0)
        {
            badgeOrders.setVisibility(View.VISIBLE);
            badgeOrders.setNumber(coursors.getCount());
        }
        if(db.isOpen()){db.close();}
        Query="SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM BsUserServices " +
                "LEFT JOIN " +
                "SuggetionsInfo  ON " +
                "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode"+
                " LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='1'";
        if(!db.isOpen()) {

            db = dbh.getReadableDatabase();
        }
        coursors = db.rawQuery(Query,null);
        if(coursors.getCount()>0)
        {
            badgeCertain.setVisibility(View.VISIBLE);
            badgeCertain.setNumber(coursors.getCount());
        }
        if(db.isOpen()){db.close();}
        Query = "SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM BsUserServices " +
                "LEFT JOIN " +
                "SuggetionsInfo ON " +
                "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode" +
                " LEFT JOIN " +
                "Servicesdetails ON " +
                "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='0'";
        if(!db.isOpen()) {

            db = dbh.getReadableDatabase();
        }
        coursors = db.rawQuery(Query,null);
        if(coursors.getCount()>0)
        {
            badgeOffers.setVisibility(View.VISIBLE);
            badgeOffers.setNumber(coursors.getCount());
        }
        if(db.isOpen()){db.close();}
        Query = "SELECT * FROM messages WHERE IsReade='0'";
        if(!db.isOpen()) {

            db = dbh.getReadableDatabase();
        }
        coursors = db.rawQuery(Query,null);
        if(coursors.getCount()>0)
        {
            badgeNotification.setVisibility(View.VISIBLE);
            badgeNotification.setNumber(coursors.getCount());
        }
        if(db.isOpen()){db.close();}
        //**************************************************************************
        LinearOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    String Query="SELECT BsUserServices.*,Servicesdetails.name FROM BsUserServices " +
                            "LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE BsUserServices.Suggetion='0' ORDER BY BsUserServices.Code_BsUserServices DESC";
                    LoadActivity2(List_Orders.class,
                            "hamyarcode", hamyarcode,
                            "guid", guid,
                            "Query",Query,
                            "tvTitlePageName","سفارش های مشتریان",
                            "Table","BsUserServices");
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }

            }
        });
        LinearCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    String Query="SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM BsUserServices " +
                            "LEFT JOIN " +
                            "SuggetionsInfo ON " +
                            "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode"+
                            " LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='1'";
                    LoadActivity2(List_Sertains.class, "hamyarcode", hamyarcode,
                            "guid", guid,"Query",Query,
                            "tvTitlePageName","قطعی",
                            "Table","BsUserServices");
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    String Query="SELECT SuggetionsInfo.*,BsUserServices.*,Servicesdetails.* FROM BsUserServices " +
                            "LEFT JOIN " +
                            "SuggetionsInfo ON " +
                            "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode"+
                            " LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code_Servicesdetails=BsUserServices.ServiceDetaileCode WHERE SuggetionsInfo.ConfirmByUser='0'";
                    LoadActivity2(List_Orders.class,
                            "hamyarcode", hamyarcode,
                            "guid", guid,
                            "Query",Query,
                            "tvTitlePageName","پیشنهادها",
                            "Table","BsUserServices");
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(MainMenu.class, "hamyarcode", hamyarcode, "guid", guid);
            }
        });
        LinearNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActive) {
                    LoadActivity(List_Messages.class, "hamyarcode", hamyarcode, "guid", guid);
                }
                else {
                    Toast.makeText(MainMenu.this, "شما فعال نشده اید", Toast.LENGTH_LONG).show();
                }
            }
        });
        left_white_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mounth,day;
                if(calNow.getPersianMonth()<10)
                {
                    mounth="0"+String.valueOf(calNow.getPersianMonth());
                }
                else
                {
                    mounth=String.valueOf(calNow.getPersianMonth());
                }
                if(calNow.getPersianDay()<10)
                {
                    day="0"+String.valueOf(calNow.getPersianDay());
                }
                else
                {
                    day=String.valueOf(calNow.getPersianDay());
                }
                //***************************************
                String DateMiladi= faToEn(ChangeDate.changeFarsiToMiladi(String.valueOf(calNow.getPersianYear()) + "/"
                        + mounth + "/"
                        + day));
                String spMiladi[]=DateMiladi.split("/");
                String Query="select DateTime('" + spMiladi[0] + "-" + spMiladi[1] + "-" + spMiladi[2]+"', 'LocalTime', '+1 Day') as result";
                db=dbh.getReadableDatabase();
                Cursor cursor=db.rawQuery(Query,null);
                if(cursor.getCount()>0)
                {
                    cursor.moveToNext();
                    String resultDate=cursor.getString(cursor.getColumnIndex("result"));
                    String sp[]=resultDate.split(" ");
                    String DateShamsi=faToEn(ChangeDate.changeMiladiToFarsi(sp[0].replace("-","/")));
                    String spShamsi[]=DateShamsi.split("/");
//                    ir.hamsaa.persiandatepicker.util.PersianCalendar calResult=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
                    calNow.setPersianDate(Integer.parseInt(spShamsi[0]),Integer.parseInt(spShamsi[1]),Integer.parseInt(spShamsi[2]));
                    tvDayOfWeek.setText(calNow.getPersianWeekDayName());
                    tvDayOfMounth.setText(EnToFa(spShamsi[2]));
                    tvTitleMounth.setText(calNow.getPersianMonthName());
                    Query="SELECT SuggetionsInfo.*,BsUserServices.* FROM BsUserServices " +
                        "LEFT JOIN  " +
                        "SuggetionsInfo ON  " +
                        "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode" +
                        " WHERE SuggetionsInfo.ConfirmByUser='1' AND StartDate='"+DateShamsi+"'";
                    try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                    Cursor cursorCountSelectService=db.rawQuery(Query,null);
                    if(cursorCountSelectService.getCount()>0)
                    {
                        cursorCountSelectService.moveToNext();
                        imgSmile.setVisibility(View.GONE);
                        tvCountSelectService.setText("سرویس در درست اجرا: "+cursorCountSelectService.getCount());
                    }
                    else
                    {
                        imgSmile.setVisibility(View.VISIBLE);
                        tvCountSelectService.setText("وقت استراحته امروز سفارشی برای انجام ندارید");
                    }
                    if(!cursorCountSelectService.isClosed())
                    {
                        cursorCountSelectService.close();
                    }
                }
                if(db.isOpen())
                {
                    db.close();
                }
            }
        });
        right_white_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mounth,day;
                if(calNow.getPersianMonth()<10)
                {
                    mounth="0"+String.valueOf(calNow.getPersianMonth());
                }
                else
                {
                    mounth=String.valueOf(calNow.getPersianMonth());
                }
                if(calNow.getPersianDay()<10)
                {
                    day="0"+String.valueOf(calNow.getPersianDay());
                }
                else
                {
                    day=String.valueOf(calNow.getPersianDay());
                }
                //***************************************
                String DateMiladi= faToEn(ChangeDate.changeFarsiToMiladi(String.valueOf(calNow.getPersianYear()) + "/"
                        + mounth + "/"
                        + day));
                String spMiladi[]=DateMiladi.split("/");
                String Query="select DateTime('" + spMiladi[0] + "-" + spMiladi[1] + "-" + spMiladi[2]+" 00:00:00', 'LocalTime', '-1 Day') as result";
                db=dbh.getReadableDatabase();
                Cursor cursor=db.rawQuery(Query,null);
                if(cursor.getCount()>0)
                {
                    cursor.moveToNext();
                    String resultDate=cursor.getString(cursor.getColumnIndex("result"));
                    String sp[]=resultDate.split(" ");
                    sp[0]=sp[0].replace("-","/");
                    String DateShamsi=faToEn(ChangeDate.changeMiladiToFarsi(sp[0]));
                    String spShamsi[]=DateShamsi.split("/");
//                    ir.hamsaa.persiandatepicker.util.PersianCalendar calResult=new ir.hamsaa.persiandatepicker.util.PersianCalendar();
                    calNow.setPersianDate(Integer.parseInt(spShamsi[0]),Integer.parseInt(spShamsi[1]),Integer.parseInt(spShamsi[2]));
                    tvDayOfWeek.setText(calNow.getPersianWeekDayName());
                    tvDayOfMounth.setText(EnToFa(spShamsi[2]));
                    tvTitleMounth.setText(calNow.getPersianMonthName());
                    //***************************Coutn Service Select From Calculate******************
                    Query="SELECT SuggetionsInfo.*,BsUserServices.* FROM BsUserServices " +
                            "LEFT JOIN  " +
                            "SuggetionsInfo ON  " +
                            "BsUserServices.code_BsUserServices=SuggetionsInfo.BsUserServicesCode" +
                            " WHERE SuggetionsInfo.ConfirmByUser='1' AND StartDate='"+DateShamsi+"'";
                    try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                    Cursor cursorCountSelectService=db.rawQuery(Query,null);
                    if(cursorCountSelectService.getCount()>0)
                    {
                        cursorCountSelectService.moveToNext();
                        imgSmile.setVisibility(View.GONE);
                        tvCountSelectService.setText("سرویس در درست اجرا: "+cursorCountSelectService.getCount());
                    }
                    else
                    {
                        imgSmile.setVisibility(View.VISIBLE);
                        tvCountSelectService.setText("وقت استراحته امروز سفارشی برای انجام ندارید");
                    }
                    if(!cursorCountSelectService.isClosed())
                    {
                        cursorCountSelectService.close();
                    }
                }
                if(db.isOpen())
                {
                    db.close();
                }
            }
        });
        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spShamsi[]=StrToday.split("/");
                calNow.setPersianDate(Integer.parseInt(spShamsi[0]),Integer.parseInt(spShamsi[1]),Integer.parseInt(spShamsi[2]));
                tvDayOfWeek.setText(calNow.getPersianWeekDayName());
                tvDayOfMounth.setText(EnToFa(spShamsi[2]));
                tvTitleMounth.setText(calNow.getPersianMonthName());
            }
        });
        //********************************************************************
        BadgeView badge1 = new BadgeView(this, tvOrders);
        badge1.setText("12");
        badge1.setBadgePosition(BadgeView.POSITION_CENTER);
        //****************************************************************
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainMenu.this));
        if(isActive)
        {
            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
            //startService(new Intent(getBaseContext(), ServiceGetLocation.class));
            startService(new Intent(getBaseContext(), ServiceGetIncome.class));
            startService(new Intent(getBaseContext(), ServiceGetAllHamyarRequest.class));
            startService(new Intent(getBaseContext(), ServiceGetSelectJob.class));
            startService(new Intent(getBaseContext(), ServiceSyncMessage.class));
            startService(new Intent(getBaseContext(), ServiceGetKarnameMali.class));
            startService(new Intent(getBaseContext(), ServiceGetHamyarCredite.class));
        }
        startService(new Intent(getBaseContext(), ServiceGetProfile.class));
        startService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
        //**************************************************************************
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //**************************************************************************
        imgHumberger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
        db=dbh.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT * FROM Income",null);
        int intIncome=0;
        for(int i=0;i<cur.getCount();i++)
        {
            cur.moveToNext();
            intIncome=intIncome+Integer.parseInt(cur.getString(cur.getColumnIndex("Price")));
        }
        cur.close();
        if(db.isOpen()){db.close();}
        tvShowIncome.setText(String.valueOf(intIncome));
        db=dbh.getReadableDatabase();
        cur=db.rawQuery("SELECT * FROM credits",null);
        tvShowIncome.setText("0");
        for(int i=0;i<cur.getCount();i++)
        {
            try {
                tvShowIncome.setText(cur.getString(cur.getColumnIndex("Price")));
            }
            catch (Exception ex)
            {
                tvShowIncome.setText("0");
            }
        }
        cur.close();
        if(db.isOpen()){db.close();}
    }


    public void Logout() {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

        // set a negative/no button and create a listener
        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        // set a positive/yes button and create a listener
        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Declare Object From Get Internet Connection Status For Check Internet Status
                //stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
                stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
                stopService(new Intent(getBaseContext(), ServiceGetProfile.class));
                stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
                stopService(new Intent(getBaseContext(), ServiceGetIncome.class));
                stopService(new Intent(getBaseContext(), ServiceGetAllHamyarRequest.class));
                stopService(new Intent(getBaseContext(), ServiceGetSelectJob.class));
                stopService(new Intent(getBaseContext(), ServiceSyncMessage.class));
                db = dbh.getWritableDatabase();
                db.execSQL("DELETE FROM AmountCredit");
                db.execSQL("DELETE FROM android_metadata");
                //db.execSQL("DELETE FROM BsHamyarSelectServices");
                db.execSQL("DELETE FROM BsUserServices");
                db.execSQL("DELETE FROM credits");
                db.execSQL("DELETE FROM DateTB");
                db.execSQL("DELETE FROM education");
                db.execSQL("DELETE FROM exprtise");
                db.execSQL("DELETE FROM FaktorUserDetailes");
                db.execSQL("DELETE FROM HeadFactor");
                db.execSQL("DELETE FROM HmFactorService");
                db.execSQL("DELETE FROM HmFactorTools");
                db.execSQL("DELETE FROM HmFactorTools_List");
                db.execSQL("DELETE FROM InsertFaktorUserDetailes");
                db.execSQL("DELETE FROM login");
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM Profile");
                db.execSQL("DELETE FROM services");
                db.execSQL("DELETE FROM servicesdetails");
                db.execSQL("DELETE FROM Slider");
                db.execSQL("DELETE FROM sqlite_sequence");
                db.execSQL("DELETE FROM Supportphone");
                db.execSQL("DELETE FROM Unit");
                db.execSQL("DELETE FROM UpdateApp");
                if(db.isOpen()){db.close();}
                System.exit(0);
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
    public void dialContactPhone(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainMenu.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        startActivity(callIntent);
    }

    public static String faToEn(String num) {
        return num
                .replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9");
    }
    public static String EnToFa(String num) {
        return num
                .replace("0", "۰")
                .replace("1", "۱")
                .replace("2", "۲")
                .replace("3", "۳")
                .replace("4", "۴")
                .replace("5", "۵")
                .replace("6", "۶")
                .replace("7", "۷")
                .replace("8", "۸")
                .replace("9", "۹");
    }
    private void ExitApplication()
    {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");

        // set a negative/no button and create a listener
        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        // set a positive/yes button and create a listener
        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Declare Object From Get Internet Connection Status For Check Internet Status
//                System.exit(0);
                Intent startMain = new Intent(Intent.ACTION_MAIN);

                startMain.addCategory(Intent.CATEGORY_HOME);

                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(startMain);

                finish();
                arg0.dismiss();

            }
        });

        alertbox.show();
    }
    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {

            mDrawer.closeDrawer(GravityCompat.START);

        } else {

//			super.onBackPressed();
            ExitApplication();
        }

    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
    {
        Intent intent = new Intent(getApplicationContext(),Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue,
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
        this.startActivity(intent);
    }
	  public Bitmap convertToBitmap(String base){
          Bitmap Bmp=null;
          try
          {
              byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
              Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
              return Bmp;
          }
          catch (Exception e)
          {
              e.printStackTrace();
              return Bmp;
          }
	  }
    protected void onStart() {

        super.onStart();
        if(isActive)
        {
            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
            //startService(new Intent(getBaseContext(), ServiceGetLocation.class));
            startService(new Intent(getBaseContext(), ServiceGetIncome.class));
            startService(new Intent(getBaseContext(), ServiceGetAllHamyarRequest.class));
            startService(new Intent(getBaseContext(), ServiceGetSelectJob.class));
            startService(new Intent(getBaseContext(), ServiceSyncMessage.class));
            startService(new Intent(getBaseContext(), ServiceGetKarnameMali.class));
            startService(new Intent(getBaseContext(), ServiceGetHamyarCredite.class));
        }
        //startService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));

    }
    protected void onStop() {

        super.onStop();
       // stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        if(isActive)
        {
            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
            //startService(new Intent(getBaseContext(), ServiceGetLocation.class));
            startService(new Intent(getBaseContext(), ServiceGetIncome.class));
            startService(new Intent(getBaseContext(), ServiceGetAllHamyarRequest.class));
            startService(new Intent(getBaseContext(), ServiceGetSelectJob.class));
            startService(new Intent(getBaseContext(), ServiceSyncMessage.class));
            startService(new Intent(getBaseContext(), ServiceGetKarnameMali.class));
            startService(new Intent(getBaseContext(), ServiceGetHamyarCredite.class));
        }
    }
    protected void onPause() {

        super.onPause();
        //stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        if(isActive)
        {
            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
            //startService(new Intent(getBaseContext(), ServiceGetLocation.class));
            startService(new Intent(getBaseContext(), ServiceGetIncome.class));
            startService(new Intent(getBaseContext(), ServiceGetAllHamyarRequest.class));
            startService(new Intent(getBaseContext(), ServiceGetSelectJob.class));
            startService(new Intent(getBaseContext(), ServiceSyncMessage.class));
            startService(new Intent(getBaseContext(), ServiceGetKarnameMali.class));
            startService(new Intent(getBaseContext(), ServiceGetHamyarCredite.class));
        }
    }
    protected void onDestroy() {

        super.onDestroy();
        //stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
        if(isActive)
        {
            startService(new Intent(getBaseContext(), ServiceGetNewJob.class));
            //startService(new Intent(getBaseContext(), ServiceGetLocation.class));
            startService(new Intent(getBaseContext(), ServiceGetIncome.class));
            startService(new Intent(getBaseContext(), ServiceGetAllHamyarRequest.class));
            startService(new Intent(getBaseContext(), ServiceGetSelectJob.class));
            startService(new Intent(getBaseContext(), ServiceSyncMessage.class));
            startService(new Intent(getBaseContext(), ServiceGetKarnameMali.class));
            startService(new Intent(getBaseContext(), ServiceGetHamyarCredite.class));
        }
    }
    void sharecode(String shareStr)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "آسپینو" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
    }
    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);
        }
    }
}
