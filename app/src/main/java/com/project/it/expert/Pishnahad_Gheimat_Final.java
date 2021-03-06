package com.project.it.expert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Pishnahad_Gheimat_Final extends AppCompatActivity {
	private String hamyarcode;
	private String guid;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private TextView tvOrderCode;
	private TextView tvOrderDate;
	private TextView tvOrderTime;
	private TextView tvOrderAddress;
	private Button Send;
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
	private String OrderCode;
	private String Price="0";
	private int positionTab=0;
	private EditText etGheimat;
	private String Table;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_pishnahad_gheimat_final);
	Send=(Button) findViewById(R.id.Send);
	etGheimat=(EditText) findViewById(R.id.etGheimat);
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

		if(db.isOpen()){db.close();}
	}
	try
	{
		OrderCode = getIntent().getStringExtra("OrderCode").toString();
	}
	catch (Exception ex)
	{
		OrderCode="0";
	}
	imgHumberger = (ImageView) findViewById(R.id.imgHumberger);
	imgBack = (ImageView) findViewById(R.id.imgBack);
	mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	LinearCallSupporter = (LinearLayout) findViewById(R.id.LinearCallSupporter);
	LinearRole = (LinearLayout) findViewById(R.id.LinearRole);
	LinearAboutAspino = (LinearLayout) findViewById(R.id.LinearAboutAspino);
	LinearLogout = (LinearLayout) findViewById(R.id.LinearLogout);
	tvOrderCode= (TextView) findViewById(R.id.tvOrderCode);
	tvOrderDate= (TextView) findViewById(R.id.tvOrderDate);
	tvOrderTime= (TextView) findViewById(R.id.tvOrderTime);
	tvOrderAddress= (TextView) findViewById(R.id.tvOrderAddress);
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
			LoadActivity(Pishnahad_Gheimat_Final.class, "hamyarcode", hamyarcode, "", "");
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
//			LoadActivity2(Detail_Order.class, "guid", guid, "hamyarcode", hamyarcode);
			onBackPressed();
		}
	});
	//********************************************************************
	db=dbh.getReadableDatabase();
	Cursor cursors = db.rawQuery("SELECT * FROM BsUserServices WHERE Code_BsUserServices='"+OrderCode+"'", null);
	if(cursors.getCount()>0)
	{
		cursors.moveToNext();
		tvOrderCode.setText(cursors.getString(cursors.getColumnIndex("Code_BsUserServices")));
		tvOrderDate.setText(cursors.getString(cursors.getColumnIndex("StartDate")));
		tvOrderTime.setText(cursors.getString(cursors.getColumnIndex("StartTime")));
		tvOrderAddress.setText(cursors.getString(cursors.getColumnIndex("AddressText")));
	}
	cursors.close();
	if(db.isOpen()){db.close();}
	//*********************************************************************
	Send.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Price=etGheimat.getText().toString();
			SyncUpdateHamyarRequestFinalPrice syncUpdateHamyarRequestFinalPrice=new SyncUpdateHamyarRequestFinalPrice(Pishnahad_Gheimat_Final.this,OrderCode,Price);
			syncUpdateHamyarRequestFinalPrice.AsyncExecute();

		}
	});
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		Pishnahad_Gheimat_Final.this.startActivity(intent);
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
		Pishnahad_Gheimat_Final.this.startActivity(intent);
	}
	public void dialContactPhone(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Pishnahad_Gheimat_Final.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
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

			LoadActivity2(Detail_Order.class, "guid", guid,
					"hamyarcode", hamyarcode,
					"BsUserServicesID",OrderCode,
					"Table",Table);
		}

	}
}
