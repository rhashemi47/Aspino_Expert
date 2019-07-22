package com.project.it.expert;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

/**
 * Created by hashemi on 02/17/2018.
 */

public class SyncGetSelectJobs2 {
	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	SQLiteDatabase db_check;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String LastHamyarSelectUserServiceCode;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncGetSelectJobs2(Context activity, String guid, String hamyarcode, String LastHamyarSelectUserServiceCode) {
		this.activity = activity;
		this.guid = guid;
		this.LastHamyarSelectUserServiceCode=LastHamyarSelectUserServiceCode;
		this.hamyarcode=hamyarcode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.thread_SelectJob=false;
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

		} catch (IOException ioe) {
			PublicVariable.thread_SelectJob=true;
			throw new Error("Unable to create database");

		}

		try {

			dbh.openDataBase();

		} catch (SQLException sqle) {

			PublicVariable.thread_SelectJob=true;
			throw sqle;
		}
	}

	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				SyncGetSelectJobs2.AsyncCallWS task = new SyncGetSelectJobs2.AsyncCallWS(this.activity);
				task.execute();
			}
			catch (Exception e) {
				PublicVariable.thread_SelectJob=true;

				e.printStackTrace();
			}
		}
		else
		{
			PublicVariable.thread_SelectJob=true;
			//Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(this.activity);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try
			{
				CallWsMethod("GetHamyarUserServiceSelect");
			}
			catch (Exception e) {
				PublicVariable.thread_SelectJob=true;
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				PublicVariable.thread_SelectJob=true;
				if (WsResponse.toString().compareTo("ER") == 0) {
//					Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();

				} else if (WsResponse.toString().compareTo("0") == 0) {
//					Toast.makeText(this.activity.getApplicationContext(), "متخصص شناسایی نشد!", Toast.LENGTH_LONG).show();
				} else if (WsResponse.toString().compareTo("2") == 0) {
//					Toast.makeText(this.activity.getApplicationContext(), "متخصص شناسایی نشد!", Toast.LENGTH_LONG).show();
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
				PublicVariable.thread_SelectJob=true;
			}
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
		res = WsResponse.split("@@");

		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			if(Check_Null(value[0]))
			{
				try
				{
					if (!db.isOpen()) {
						db = dbh.getReadableDatabase();
					}
				}catch (Exception ex)
				{
					db = dbh.getReadableDatabase();
				}
				query = "SELECT * FROM BsUserServices  WHERE code_BsUserServices=" + value[0];
				Cursor coursors = db.rawQuery(query, null);
				if (coursors.getCount() > 0)//Just show 10 Notification
				{
					coursors.moveToNext();
					String Status=coursors.getString(coursors.getColumnIndex("Status"));
					if(Status.compareTo(value[34])!=0) {
						query = "SELECT * FROM Servicesdetails  WHERE code_Servicesdetails=" + value[4];
						Cursor coursor = db.rawQuery(query, null);
						if (coursor.getCount() > 0)
						{
							coursor.moveToNext();
							String Detail="سرویس "+coursor.getString(coursor.getColumnIndex("name"))+" "+ TranslateStatus(Status);
							runNotification("آسپینو", Detail, i, value[0],"BsUserServices", Joziat_Sefaresh.class);
						}
					}
				}
				if(!coursors.isClosed())
				{
					coursors.close();
				}
				try {
					if (db.isOpen()) {
						db.close();
					}
				}catch (Exception ex)
				{}
				try
				{
					if (!db.isOpen()) {
						db = dbh.getWritableDatabase();
					}
				}catch (Exception ex)
				{
					db = dbh.getWritableDatabase();
				}
//				query = "UPDATE BsUserServices SET Status='1' , Read='0' WHERE Code_BsUserServices='" + value[0] + "'";
				query = "UPDATE BsUserServices SET " +
						"Code_BsUserServices='" +value[0] + "'," +
						"UserCode='" +value[1] + "'," +
						"UserName='" +value[2] + "'," +
						"UserFamily='" +value[3] + "'," +
						"ServiceDetaileCode='" +value[4] + "'," +
						"MaleCount='" +value[5] + "'," +
						"FemaleCount='" +value[6] + "'," +
						"StartDate='" +value[7] + "'," +
						"EndDate='" +value[8] + "'," +
						"AddressCode='" +value[9] + "'," +
						"AddressText='" +value[10] + "'," +
						"Lat='" +value[11] + "'," +
						"Lng='" +value[12] + "'," +
						"City='" +value[13] + "'," +
						"State='" +value[14] + "'," +
						"Description='" +value[15] + "'," +
						"IsEmergency='" +value[16] + "'," +
						"InsertUser='" +value[17] + "'," +
						"InsertDate='" +value[18] + "'," +
						"StartTime='" +value[19] + "'," +
						"EndTime='" +value[20] + "'," +
						"HamyarCount='" +value[21] + "'," +
						"PeriodicServices='" +value[22] + "'," +
						"EducationGrade='" +value[23] + "'," +
						"FieldOfStudy='" +value[24] + "'," +
						"StudentGender='" +value[25] + "'," +
						"TeacherGender='" +value[26] + "'," +
						"EducationTitle='" +value[27] + "'," +
						"ArtField='" +value[28] + "'," +
						"CarWashType='" +value[29] + "'," +
						"CarType='" +value[30] + "'," +
						"Language='" +value[31] + "'," +
						"ArtFieldOther='" +value[32] + "'," +
						"UserPhone='" +value[33] + "'," +
						"Status='" +value[34] + "'" +
						"WHERE Code_BsUserServices='" + value[0] + "'";
				db.execSQL(query);
				try {
					if (db.isOpen()) {
						db.close();
					}
				}catch (Exception ex)
				{

				}
			}
			else
			{
				try {
					if (!db.isOpen()) {
						db = dbh.getWritableDatabase();
					}
				}catch (Exception ex)
				{
					db = dbh.getWritableDatabase();
				}
				query = "INSERT INTO BsUserServices (" +
						"Code_BsUserServices," +
						"UserCode," +
						"UserName," +
						"UserFamily," +
						"ServiceDetaileCode," +
						"MaleCount," +
						"FemaleCount," +
						"StartDate," +
						"EndDate," +
						"AddressCode," +
						"AddressText," +
						"Lat," +
						"Lng," +
						"City," +
						"State," +
						"Description," +
						"IsEmergency," +
						"InsertUser," +
						"InsertDate," +
						"StartTime," +
						"EndTime," +
						"HamyarCount," +
						"PeriodicServices," +
						"EducationGrade," +
						"FieldOfStudy," +
						"StudentGender," +
						"TeacherGender," +
						"EducationTitle," +
						"ArtField," +
						"CarWashType," +
						"CarType," +
						"Language," +
						"ArtFieldOther," +
						"UserPhone," +
						"Status" +
						") VALUES('" +
						value[0] +
						"','" + value[1] +
						"','" + value[2] +
						"','" + value[3] +
						"','" + value[4] +
						"','" + value[5] +
						"','" + value[6] +
						"','" + value[7] +
						"','" + value[8] +
						"','" + value[9] +
						"','" + value[10] +
						"','" + value[11] +
						"','" + value[12] +
						"','" + value[13] +
						"','" + value[14] +
						"','" + value[15] +
						"','" + value[16] +
						"','" + value[17] +
						"','" + value[18] +
						"','" + value[19] +
						"','" + value[20] +
						"','" + value[21] +
						"','" + value[22] +
						"','" + value[23] +
						"','" + value[24] +
						"','" + value[25] +
						"','" + value[26] +
						"','" + value[27] +
						"','" + value[28] +
						"','" + value[29] +
						"','" + value[30] +
						"','" + value[31] +
						"','" + value[32] +
						"','" + value[33] +
						"','" + value[34] +
						"')";
				db.execSQL(query);
                try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                query = "SELECT * FROM Servicesdetails  WHERE code_Servicesdetails=" + value[4];
                Cursor coursors = db.rawQuery(query, null);
                if (coursors.getCount() > 0 && i < 10)//Just show 10 Notification
                {
                    coursors.moveToNext();
					String Detail="سرویس "+coursors.getString(coursors.getColumnIndex("name"))+" "+ TranslateStatus(value[34]);
					runNotification("آسپینو", Detail, i, value[0],"BsUserServices", Joziat_Sefaresh.class);
                }
				if(!coursors.isClosed())
				{
					coursors.close();
				}
				try {
					if (db.isOpen()) {
						db.close();
					}
				}catch (Exception ex)
				{

				}
			}
		}
		if(db.isOpen()){db.close();}
	}
	public boolean Check_Null(String Code)
	{
		db=dbh.getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT * FROM BsUserServices WHERE Code_BsUserServices='" +Code +"'",null);
		if(cursor.getCount()>0) {
			if(!cursor.isClosed())
			{
				cursor.close();
			}
			if(db.isOpen()){db.close();}
			return true;
		}
		else
		{
			if(!cursor.isClosed())
			{
				cursor.close();
			}
			if(db.isOpen()){db.close();}
			return false;
		}
	}
    public void runNotification(String title,String detail,int id,String BsUserServicesID,String Table,Class<?> Cls)
    {
        NotificationClass notifi=new NotificationClass(this.activity);
        notifi.Notificationm(this.activity,title,detail,BsUserServicesID,Table,id,Cls);
    }
	public String TranslateStatus(String status)
	{
		String StrStatus="";
		switch (status)
		{
			case "0":
				StrStatus="آزاد شد";
				break;
			case "1":
				StrStatus="در نوبت انجام قرار گرفت";
				break;
			case "2":
				StrStatus="در حال انجام است";
				break;
			case "3":
				StrStatus="لغو شد";
				break;
			case "4":
				StrStatus="اتمام و تسویه شده است";
				break;
			case "5":
				StrStatus="اتمام و تسویه نشده است";
				break;
			case "6":
				StrStatus="اعلام شکایت شده است";
				break;
			case "7":
				StrStatus="درحال پیگیری شکایت و یا رفع خسارت می باشد";
				break;
			case "8":
				StrStatus=" توسط متخصص رفع عیب و خسارت شده است";
				break;
			case "9":
				StrStatus="پرداخت خسارت";
				break;
			case "10":
				StrStatus="پرداخت جریمه";
				break;
			case "11":
				StrStatus="تسویه حساب با متخصص";
				break;
			case "12":
				StrStatus="متوقف و تسویه شده است";
				break;
			case "13":
				StrStatus="متوقف و تسویه نشده است";
				break;
		}
		return StrStatus;
	}
}
