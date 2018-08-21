package com.project.it.expert;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Accept_code extends Activity {
	private String phonenumber;
	private String check_load;
	private int counter=59;
	private double lat;
	private double lon;
	private GPSTracker gps;
	private Handler mHandler;
	boolean continue_or_stop = true;
	boolean createthread=true;
	private EditText acceptcode;
	private Button btnSendAcceptcode;
	private TextView tvRefreshCode;
	private TextView tvTimer;
	private TextView tvPhoneNumber;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private IntentFilter intentFilter;
	private BroadcastReceiver intentReciever=new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			acceptcode.setText(intent.getExtras().getString("sms"));
		}
	};
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accept_code);
		try
		{

			phonenumber = getIntent().getStringExtra("phonenumber").toString();
		}
		catch (Exception ex)
		{
			phonenumber="0";
		}
		try
		{
			check_load=getIntent().getStringExtra("check_load");
		}
		catch (Exception ex)
		{
			check_load="0";
		}
		intentFilter=new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");
		int GET_MY_PERMISSION = 1;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED)
			{
				if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_SMS))
				{
					//do nothing
				}
				else{

					ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_SMS},GET_MY_PERMISSION);
				}
			}
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
		gps = new GPSTracker(getApplicationContext());

		// check if GPS enabled
		if(gps.canGetLocation())
		{
			lat = gps.getLatitude();
			lon = gps.getLongitude();
		}
//		//Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remive page title
		acceptcode=(EditText)findViewById(R.id.etAcceptcode);
		btnSendAcceptcode=(Button)findViewById(R.id.btnSendAcceptCode);
		tvRefreshCode=(TextView) findViewById(R.id.tvRefreshCode);
		tvTimer=(TextView) findViewById(R.id.tvTimer);
		tvPhoneNumber=(TextView) findViewById(R.id.tvPhoneNumber);
		tvPhoneNumber.setText(phonenumber + "ارسال خواهد شد");
		//set font for element
//		acceptcode.setTypeface(FontMitra);
		//Start Counter Second
		startCountAnimation();
//		btnSendAcceptcode.setTypeface(FontMitra);
		btnSendAcceptcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InternetConnection ic=new InternetConnection(getApplicationContext());
				if(ic.isConnectingToInternet()){
					if(checkInsert().compareTo("0")!=0)//کنترل وارد کردن کد تاییدیه در تکست باکس
					{
						Send_AcceptCode();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
				}
			}
		});
		tvRefreshCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String query=null;
				db=dbh.getReadableDatabase();
				query="SELECT * FROM Profile";
				Cursor coursors = db.rawQuery(query,null);
				if(coursors.getCount()>0)
				{
					coursors.moveToNext();
					String Mobile;
					Mobile=coursors.getString(coursors.getColumnIndex("Mobile"));
					acceptcode.setText("");
					SendAcceptCode sendCode=new SendAcceptCode(Accept_code.this,Mobile,check_load);
					sendCode.AsyncExecute();
				}

			}
		});
		SMSReseiver.bindListener(new SmsListener() {
			@Override
			public void onMessageReceived(String messageText) {
				Log.e("Text",messageText);
				acceptcode.setText("");
				acceptcode.setText(messageText);
				Send_AcceptCode();
			}
		});
	}

	@Override
	public  void onResume() {

		super.onResume();
		registerReceiver(intentReciever,intentFilter);
	}
	@Override
	public void onPause() {

		super.onPause();
		unregisterReceiver(intentReciever);
	}
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
		if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
//			countDownTimer.cancel();
			LoadActivity(Login.class,"karbarCode","0");
		}
		return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		Accept_code.this.startActivity(intent);
	}
	public String checkInsert()
	{
		String Acceptcode = acceptcode.getText().toString();
		if(Acceptcode.compareTo("")==0) {
			Toast.makeText(getApplicationContext(), "لطفا کد تایید را وارد نمایید.", Toast.LENGTH_LONG).show();
			return  "0";
		}
		else
		{
			return Acceptcode;
		}
	}
	public void Send_AcceptCode()
	{
//		countDownTimer.cancel();
		String query="UPDATE login SET Phone ='"+phonenumber+"', AcceptCode='"+acceptcode.getText().toString()+"'";
		db=dbh.getWritableDatabase();
		db.execSQL(query);
		HmLogin hm=new HmLogin(Accept_code.this, phonenumber, acceptcode.getText().toString(),check_load,"0");//getStringLocation()
		hm.AsyncExecute();
	}
	private  String getStringLocation()
	{
		Locale locale = new Locale("fa");

		Geocoder geocoder = new Geocoder(getApplicationContext(), locale);

		List<Address> list;

		try {
			lat = gps.getLatitude();
			lon = gps.getLongitude();
			list = geocoder.getFromLocation(lat, lon, 2);

			Address address = list.get(0);


			//Toast.makeText(getApplicationContext(), "CountryCode: " + address.getCountryCode() +
//                    " ,AdminArea : " + address.getAdminArea() +
//                    " ,CountryName : " + address.getCountryName() +
//                    " ,SubLocality : " + address.getSubLocality()+address.getFeatureName(), Toast.LENGTH_SHORT).show();
			if(address.getSubLocality()!=null)
			{
				return address.getLocality();
			}
			else {
				return address.getThoroughfare();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		catch (Exception e){
			return "";
		}
	}
	public void startCountAnimation() {
//		if(countDownTimer!=null) {
//			countDownTimer.cancel();
//		}
//		countDownTimer=	new CountDownTimer(59000, 0) {
//
//			public void onTick(long millisUntilFinished) {
//				tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
//				tvRefreshCode.setVisibility(View.GONE);
//			}
//
//			public void onFinish() {
//				tvRefreshCode.setVisibility(View.VISIBLE);
//			}
//		};
//		countDownTimer.start();
		continue_or_stop=true;
		if(createthread) {
			mHandler = new Handler();
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (continue_or_stop) {
						try {
							Thread.sleep(1000); // every 60 seconds
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									if(counter!=0)
									{
										counter-=1;
										tvTimer.setText(String.valueOf(counter)+ " ثانیه");
										tvRefreshCode.setVisibility(View.GONE);
									}
									else
									{
										continue_or_stop = false;
										tvRefreshCode.setVisibility(View.VISIBLE);
									}
								}
							});
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}).start();

			createthread = false;

		}
	}
	public void onDestroy() {
		super.onDestroy();
		// Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		continue_or_stop=false;
	}
}
