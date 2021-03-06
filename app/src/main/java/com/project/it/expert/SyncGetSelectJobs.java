package com.project.it.expert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetSelectJobs {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String LastHamyarSelectUserServiceCode;
	//private String acceptcode;
	private boolean CuShowDialog = true;

	//Contractor
	public SyncGetSelectJobs(Activity activity, String guid, String hamyarcode, String LastHamyarSelectUserServiceCode) {
		this.activity = activity;
		this.guid = guid;
		this.LastHamyarSelectUserServiceCode = LastHamyarSelectUserServiceCode;
		this.hamyarcode = hamyarcode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();

		dbh = new DatabaseHelper(this.activity.getApplicationContext());
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
	}

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else {
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;

		public AsyncCallWS(Activity activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);		    		    this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetHamyarUserServiceSelect");
			} catch (Exception e) {
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				if (WsResponse.toString().compareTo("ER") == 0) {
					Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				} else if (WsResponse.toString().compareTo("0") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "سرویسی اعلام نشده", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
					db=dbh.getReadableDatabase();
					Cursor cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code_BsUserServices AS INT)),0)as code FROM BsUserServices", null);
					String LastHamyarUserServiceCode = null;
					if(cursors.getCount()>0)
					{
						cursors.moveToNext();
						LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
					}
					SyncJobs jobs=new SyncJobs(this.activity, guid,hamyarcode,LastHamyarUserServiceCode);
					jobs.AsyncExecute();
				} else if (WsResponse.toString().compareTo("2") == 0) {
					Toast.makeText(this.activity.getApplicationContext(), "متخصص شناسایی نشد!", Toast.LENGTH_LONG).show();
				} else {
					InsertDataFromWsToDb(WsResponse);
				}
			} else {
				//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
			}

			if(db.isOpen()){db.close();}
		}

		@Override
		protected void onPreExecute() {
			if (CuShowDialog) {
				this.dialog.setMessage("در حال پردازش");
				this.dialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}


	public void CallWsMethod(String METHOD_NAME) {
		//Create request
		SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
		PropertyInfo GuidPI = new PropertyInfo();
		//Set Name
		GuidPI.setName("GUID");
		//Set Value
		GuidPI.setValue(this.guid);
		//Set dataType
		GuidPI.setType(String.class);
		//Add the property to request object
		request.addProperty(GuidPI);
		//*****************************************************
		PropertyInfo HamyarCodePI = new PropertyInfo();
		//Set Name
		HamyarCodePI.setName("HamyarCode");
		//Set Value
		HamyarCodePI.setValue(this.hamyarcode);
		//Set dataType
		HamyarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCodePI);
		//*****************************************************
		PropertyInfo LastUserServiceCodePI = new PropertyInfo();
		//Set Name
		LastUserServiceCodePI.setName("LastHamyarUserServiceCode");
		//Set Value
		LastUserServiceCodePI.setValue(this.LastHamyarSelectUserServiceCode);
		//Set dataType
		LastUserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LastUserServiceCodePI);
		//Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Set output SOAP object
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(PV.URL);
		try {
			//Invoke web service
			androidHttpTransport.call("http://tempuri.org/" + METHOD_NAME, envelope);
			//Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			//Assign it to FinalResultForCheck static variable
			WsResponse = response.toString();
			if (WsResponse == null) WsResponse = "ER";
		} catch (Exception e) {
			WsResponse = "ER";
			e.printStackTrace();
		}
	}


	public void InsertDataFromWsToDb(String AllRecord) {
		String[] res;
		String[] value;
		String query = null;
		String LastHamyarUserServiceCode = null;
		res = WsResponse.split("@@");
		db = dbh.getWritableDatabase();
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			query = "UPDATE BsUserServices SET Status='1' , Read='0' WHERE Code_BsUserServices='" + value[0] + "'";
			db.execSQL(query);if(db.isOpen()){db.close();}
		}
		Cursor cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code_BsUserServices AS INT)),0) as code FROM BsUserServices", null);
		if(cursors.getCount()>0)
		{
			cursors.moveToNext();
			LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
		}

		if(db.isOpen()){db.close();}
		SyncJobs jobs=new SyncJobs(this.activity, guid,hamyarcode,LastHamyarUserServiceCode);
		jobs.AsyncExecute();
	}
}
