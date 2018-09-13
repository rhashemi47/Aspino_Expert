package com.project.it.expert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit extends Activity {
	private String hamyarcode;
	private String guid;
	private String TypeCatch="0";
	private DatabaseHelper dbh;
	private TextView tvContentCredits;
	private TextView tvValueIncome;
	private SQLiteDatabase db;
	private Button btnIncreseCredit;
	private Button btnFromBank;
	private Button btnFromAspino;
	private Button btnTwoThousand;
	private Button btnFiftyThousand;
	private Button btnOneHundredThousand;
	private ImageView imgHumberger;
	private ImageView imgBack;
	private DrawerLayout mDrawer;
	private LinearLayout LinearCallSupporter;
	private LinearLayout LinearRole;
	private LinearLayout LinearAboutAspino;
	private LinearLayout LinearLogout;
	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	private EditText etInsertPriceUser;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_credite);
	setupWindowAnimations();
	btnFromBank=(Button)findViewById(R.id.btnFromBank);
	btnFromAspino=(Button)findViewById(R.id.btnFromAspino);
	btnTwoThousand=(Button)findViewById(R.id.btnTwoThousand);
	btnFiftyThousand=(Button)findViewById(R.id.btnFiftyThousand);
	btnOneHundredThousand=(Button)findViewById(R.id.btnOneHundredThousand);
	btnIncreseCredit=(Button)findViewById(R.id.btnIncreseCredit);
	etInsertPriceUser=(EditText)findViewById(R.id.etInsertPriceUser);
	tvContentCredits=(TextView) findViewById(R.id.tvContentCredits);
	tvValueIncome=(TextView) findViewById(R.id.tvValueIncome);
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
	try
	{
		String Content="";
		Cursor coursors = db.rawQuery("SELECT * FROM AmountCredit", null);
		if (coursors.getCount() > 0) {
			coursors.moveToNext();
			String splitStr[]=coursors.getString(coursors.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				Content+=splitStr[0];
			}
			else
			{
				Content+=coursors.getString(coursors.getColumnIndex("Amount"));
			}
		}
		else
		{
		}
		if(Content.compareTo("")==0){
			tvContentCredits.setText("0"+" ریال");
		}
		else {
			tvContentCredits.setText(Content+" ریال");
		}
	}
	catch (Exception ex)
	{
		tvContentCredits.setText("0"+" ریال");
	}
	btnIncreseCredit.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(etInsertPriceUser.getText().length()>0) {
				SyncInsertHamyarCredit syncInsertHamyarCredit = new SyncInsertHamyarCredit(Credit.this,etInsertPriceUser.getText().toString() , hamyarcode, "1", "10004", "تست");
				syncInsertHamyarCredit.AsyncExecute();
			}
			else
			{
				Toast.makeText(Credit.this, "لطفا مبلغ مورد نظر خود را به ریال وارد نمایید", Toast.LENGTH_SHORT).show();
			}
		}
	});
	btnFromBank.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			btnFromBank.setBackgroundResource(R.drawable.rounded_textview_currency2);
			btnFromAspino.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnFromBank.setTextColor(Color.parseColor("#FFFFFF"));
			btnFromAspino.setTextColor(Color.parseColor("#272a95"));

			TypeCatch="0";
		}
	});
	btnFromAspino.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			btnFromAspino.setBackgroundResource(R.drawable.rounded_textview_currency2);
			btnFromBank.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnFromAspino.setTextColor(Color.parseColor("#FFFFFFFF"));
			btnFromBank.setTextColor(Color.parseColor("#272a95"));
			TypeCatch="1";
		}
	});
	btnTwoThousand.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			btnTwoThousand.setBackgroundResource(R.drawable.rounded_textview_currency2);
			btnFiftyThousand.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnOneHundredThousand.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnTwoThousand.setTextColor(Color.parseColor("#FFFFFF"));
			btnFiftyThousand.setTextColor(Color.parseColor("#272a95"));
			btnOneHundredThousand.setTextColor(Color.parseColor("#272a95"));
			etInsertPriceUser.setText("20000");
		}
	});
	btnFiftyThousand.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			btnFiftyThousand.setBackgroundResource(R.drawable.rounded_textview_currency2);
			btnTwoThousand.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnOneHundredThousand.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnFiftyThousand.setTextColor(Color.parseColor("#FFFFFF"));
			btnTwoThousand.setTextColor(Color.parseColor("#272a95"));
			btnOneHundredThousand.setTextColor(Color.parseColor("#272a95"));
			etInsertPriceUser.setText("50000");
		}
	});
	btnOneHundredThousand.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			btnOneHundredThousand.setBackgroundResource(R.drawable.rounded_textview_currency2);
			btnFiftyThousand.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnTwoThousand.setBackgroundResource(R.drawable.rounded_textview_currency1);
			btnOneHundredThousand.setTextColor(Color.parseColor("#FFFFFF"));
			btnFiftyThousand.setTextColor(Color.parseColor("#272a95"));
			btnTwoThousand.setTextColor(Color.parseColor("#272a95"));
			etInsertPriceUser.setText("100000");
		}
	});
}
	public void dialContactPhone(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Credit.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
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
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		Credit.this.startActivity(intent);
	}
	private void setupWindowAnimations() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Fade fade = new Fade();
			fade.setDuration(1000);
			getWindow().setEnterTransition(fade);
		}
	}
}
