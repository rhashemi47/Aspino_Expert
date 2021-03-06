package com.project.it.expert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity {
	private String hamyarcode;
	private String guid;
	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
//	//private Button btnCredit;
//	//private Button btnOrders;
//	//private Button btnHome;
//	private GoogleMap map;
//	private Typeface FontMitra;
//	private LatLng point;
	private ImageView imgHumberger;
	private ImageView imgBack;
	private DrawerLayout mDrawer;
	private LinearLayout LinearCallSupporter;
	private LinearLayout LinearRole;
	private LinearLayout LinearAboutAspino;
	private LinearLayout LinearLogout;
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_about);
//	btnCredit=(Button)findViewById(R.id.btnCredit);
//	btnOrders=(Button)findViewById(R.id.btnOrders);
//	btnHome=(Button)findViewById(R.id.btnHome);
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
		try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}}	catch (Exception ex){	 db = dbh.getReadableDatabase();	}
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			guid=coursors.getString(coursors.getColumnIndex("guid"));
			hamyarcode=coursors.getString(coursors.getColumnIndex("hamyarcode"));
		}

		if(db.isOpen()){db.close();}
	}
	Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvTextAbout);
	txtContent.setTypeface(FontMitra);
	//********************************************************************
	imgHumberger = (ImageView) findViewById(R.id.imgHumberger);
	imgBack = (ImageView) findViewById(R.id.imgBack);
	mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	LinearCallSupporter = (LinearLayout) findViewById(R.id.LinearCallSupporter);
	LinearRole = (LinearLayout) findViewById(R.id.LinearRole);
	LinearAboutAspino = (LinearLayout) findViewById(R.id.LinearAboutAspino);
	LinearLogout = (LinearLayout) findViewById(R.id.LinearLogout);

	LinearCallSupporter.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();};
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
			LoadActivity(YourCommitment.class, "hamyarcode", hamyarcode, "", "");
		}
	});
	LinearAboutAspino.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(About.class, "hamyarcode", hamyarcode, "", "");
		}
	});
	LinearLogout.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Logout();
		}
	});
	imgHumberger.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDrawer.openDrawer(GravityCompat.START);
		}
	});
	imgBack.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
	//********************************************************************
}
//@Override
//public boolean onKeyDown( int keyCode, KeyEvent event )  {
//    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
//    	LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
//    }
//
//    return super.onKeyDown( keyCode, event );
//}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		About.this.startActivity(intent);
	}
	public void dialContactPhone(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(About.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		startActivity(callIntent);
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
				// stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
				stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
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
	@Override
	public void onBackPressed() {

		if (mDrawer.isDrawerOpen(GravityCompat.START)) {

			mDrawer.closeDrawer(GravityCompat.START);

		} else {

//			super.onBackPressed();
			LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
		}

	}
}
