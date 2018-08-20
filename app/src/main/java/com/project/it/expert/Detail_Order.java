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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Detail_Order extends AppCompatActivity {
	private String hamyarcode;
	private String guid;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
 	private Button btnSend;
 	private TextView tvOrderCode;
 	private TextView tvDateCode;
 	private TextView tvTimeOrder;
 	private TextView tvAddressOrder;
 	private TextView tvCustomerDescription;
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

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_detail_order);
	btnSend=(Button)findViewById(R.id.btnSend);
//	btnOrders=(Button)findViewById(R.id.btnOrders);
//	btnHome=(Button)findViewById(R.id.btnHome);
	tvOrderCode=(TextView)findViewById(R.id.tvOrderCode);
	tvDateCode=(TextView)findViewById(R.id.tvDateCode);
	tvTimeOrder=(TextView)findViewById(R.id.tvTimeOrder);
	tvAddressOrder=(TextView)findViewById(R.id.tvAddressOrder);
	tvCustomerDescription=(TextView)findViewById(R.id.tvCustomerDescription);
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
	try
	{
		OrderCode = getIntent().getStringExtra("BsUserServicesID").toString();
	}
	catch (Exception ex)
	{
		OrderCode="0";
	}
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
			db = dbh.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM Supportphone", null);
			if (cursor.getCount() > 0) {
				cursor.moveToNext();
				dialContactPhone(cursor.getString(cursor.getColumnIndex("PhoneNumber")));
			}
			db.close();
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
			LoadActivity(Detail_Order.class, "hamyarcode", hamyarcode, "", "");
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
			LoadActivity(List_Orders.class, "guid", guid, "hamyarcode", hamyarcode);
		}
	});
	btnSend.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity2(Pishnahad_Gheimat.class,"guid", guid, "hamyarcode", hamyarcode,"OrderCode",OrderCode);
		}
	});
	//********************************************************************
	db=dbh.getReadableDatabase();
	Cursor cursors = db.rawQuery("SELECT * FROM BsUserServices WHERE Code='"+OrderCode+"'", null);
	if(cursors.getCount()>0)
	{
		cursors.moveToNext();
		tvOrderCode.setText(cursors.getString(cursors.getColumnIndex("Code")));
		tvDateCode.setText(cursors.getString(cursors.getColumnIndex("StartDate")));
		tvTimeOrder.setText(cursors.getString(cursors.getColumnIndex("StartTime")));
		tvAddressOrder.setText(cursors.getString(cursors.getColumnIndex("AddressText")));
		tvCustomerDescription.setText(cursors.getString(cursors.getColumnIndex("Description")));
	}
	cursors.close();
	db.close();
	//********************************************************************
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		Detail_Order.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
		Detail_Order.this.startActivity(intent);
	}
	public void dialContactPhone(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Detail_Order.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
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
				stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
				stopService(new Intent(getBaseContext(), ServiceGetNewJob.class));
				// stopService(new Intent(getBaseContext(), ServiceGetNewJobNotNotifi.class));
				stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
				db = dbh.getWritableDatabase();
				db.execSQL("DELETE FROM AmountCredit");
				db.execSQL("DELETE FROM android_metadata");
				db.execSQL("DELETE FROM BsHamyarSelectServices");
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
				db.close();
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
