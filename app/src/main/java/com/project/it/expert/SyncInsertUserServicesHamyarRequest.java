package com.project.it.expert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SyncInsertUserServicesHamyarRequest {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String UserServiceCode;
	private String Price;
	private String Description;
	//private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncInsertUserServicesHamyarRequest(Activity activity,
											   String guid,
											   String hamyarcode,
											   String UserServiceCode,
											   String Description,
											   String Price) {
		this.activity = activity;
		this.guid = guid;
		this.UserServiceCode=UserServiceCode;
		this.hamyarcode=hamyarcode;
		this.Price=Price;
		this.Description=Description;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();

		dbh=new DatabaseHelper(this.activity.getApplicationContext());
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
	
	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			}	
			 catch (Exception e) {

	            e.printStackTrace();
			 }
		}
		else
		{
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;
		
		public AsyncCallWS(Activity activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);
		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertUserServicesHamyarRequest");
        	}
	    	catch (Exception e) {
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
	            if(WsResponse.compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطایی رخداده است", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
	            }
				else if(WsResponse.compareTo("1") == 0)
				{
					InsertDataFromWsToDb();
				}
        	}
        	else
        	{
				Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {}
        }
 
        @Override
        protected void onPreExecute() {
        	if(CuShowDialog)
        	{
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
	    //*****************************************************
		PropertyInfo HamyarCodePI = new PropertyInfo();
		//Set Name
		HamyarCodePI.setName("pHamyarCode");
		//Set Value
		HamyarCodePI.setValue(this.hamyarcode);
		//Set dataType
		HamyarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCodePI);
		//*****************************************************
		PropertyInfo UserServiceCodePI = new PropertyInfo();
		//Set Name
		UserServiceCodePI.setName("BsUserServicesCode");
		//Set Value
		UserServiceCodePI.setValue(this.UserServiceCode);
		//Set dataType
		UserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(UserServiceCodePI);
		//*****************************************************
		PropertyInfo PricePI = new PropertyInfo();
		//Set Name
		PricePI.setName("Price");
		//Set Value
		PricePI.setValue(this.Price);
		//Set dataType
		PricePI.setType(String.class);
		//Add the property to request object
		request.addProperty(PricePI);
		//*****************************************************
		PropertyInfo DescriptionPI = new PropertyInfo();
		//Set Name
		DescriptionPI.setName("Description");
		//Set Value
		DescriptionPI.setValue(this.Description);
		//Set dataType
		DescriptionPI.setType(String.class);
		//Add the property to request object
		request.addProperty(DescriptionPI);
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
	        androidHttpTransport.call("http://tempuri.org/"+METHOD_NAME, envelope);
	        //Get the response
	        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	        //Assign it to FinalResultForCheck static variable
	        WsResponse = response.toString();	
	        if(WsResponse == null) WsResponse="ER";
	    } catch (Exception e) {
	    	WsResponse = "ER";
	    	e.printStackTrace();
	    }
	}
	
	
	public void InsertDataFromWsToDb()
    {
//		String query=null;
//		Cursor coursors;
//		db=dbh.getReadableDatabase();
//		query = "SELECT * FROM BsUserServices WHERE code='" + UserServiceCode+"'";
//		coursors = db.rawQuery(query,null);
//		if(coursors.getCount()>0)
//		{
//			coursors.moveToNext();
//			query="INSERT INTO Suggestions (" +
//					"Code," +
//					"UserCode" +
//					",UserName" +
//					",UserFamily" +
//					",UserPhone" +
//					",ServiceDetaileCode" +
//					",MaleCount" +
//					",FemaleCount" +
//					",StartDate" +
//					",EndDate" +
//					",AddressCode" +
//					",AddressText" +
//					",Lat" +
//					",Lng" +
//					",City" +
//					",State" +
//					",Description" +
//					",IsEmergency," +
//					"InsertUser" +
//					",StartTime" +
//					",EndTime" +
//					",HamyarCount" +
//					",PeriodicServices" +
//					",EducationGrade" +
//					",FieldOfStudy" +
//					",StudentGender" +
//					",TeacherGender" +
//					",EducationTitle" +
//					",ArtField" +
//					",CarWashType" +
//					",CarType" +
//					",Language" +
//					",InsertDate" +
//					",ArtFieldOther" +
//					",IsDelete" +
//					",Status" +
//					") VALUES('"+
//						  coursors.getString(coursors.getColumnIndex("Code"))+
//					"','"+coursors.getString(coursors.getColumnIndex("UserCode"))+
//					"','"+coursors.getString(coursors.getColumnIndex("UserName"))+
//					"','"+coursors.getString(coursors.getColumnIndex("UserFamily"))+
//					"','"+coursors.getString(coursors.getColumnIndex("UserPhone"))+
//					"','"+coursors.getString(coursors.getColumnIndex("ServiceDetaileCode"))+
//					"','"+coursors.getString(coursors.getColumnIndex("MaleCount"))+
//					"','"+coursors.getString(coursors.getColumnIndex("FemaleCount"))+
//					"','"+coursors.getString(coursors.getColumnIndex("StartDate"))+
//					"','"+coursors.getString(coursors.getColumnIndex("EndDate"))+
//					"','"+coursors.getString(coursors.getColumnIndex("AddressCode"))+
//					"','"+coursors.getString(coursors.getColumnIndex("AddressText"))+
//					"','"+coursors.getString(coursors.getColumnIndex("Lat"))+
//					"','"+coursors.getString(coursors.getColumnIndex("Lng"))+
//					"','"+coursors.getString(coursors.getColumnIndex("City"))+
//					"','"+coursors.getString(coursors.getColumnIndex("State"))+
//					"','"+coursors.getString(coursors.getColumnIndex("Description"))+
//					"','"+coursors.getString(coursors.getColumnIndex("IsEmergency"))+
//					"','"+coursors.getString(coursors.getColumnIndex("InsertUser"))+
//					"','"+coursors.getString(coursors.getColumnIndex("StartTime"))+
//					"','"+coursors.getString(coursors.getColumnIndex("EndTime"))+
//					"','"+coursors.getString(coursors.getColumnIndex("HamyarCount"))+
//					"','"+coursors.getString(coursors.getColumnIndex("PeriodicServices"))+
//					"','"+coursors.getString(coursors.getColumnIndex("EducationGrade"))+
//					"','"+coursors.getString(coursors.getColumnIndex("FieldOfStudy"))+
//					"','"+coursors.getString(coursors.getColumnIndex("StudentGender"))+
//					"','"+coursors.getString(coursors.getColumnIndex("TeacherGender"))+
//					"','"+coursors.getString(coursors.getColumnIndex("EducationTitle"))+
//					"','"+coursors.getString(coursors.getColumnIndex("ArtField"))+
//					"','"+coursors.getString(coursors.getColumnIndex("CarWashType"))+
//					"','"+coursors.getString(coursors.getColumnIndex("CarType"))+
//					"','"+coursors.getString(coursors.getColumnIndex("Language"))+
//					"','"+coursors.getString(coursors.getColumnIndex("InsertDate"))+
//					"','"+coursors.getString(coursors.getColumnIndex("ArtFieldOther"))+
//					"','" + "0" +
//					"','" + "1')";//status 1 is select- 2 is pause - 3 is pause - 4 is cansel - 5 is visit - 6 is perfactor
//			db=dbh.getWritableDatabase();
//			db.execSQL(query);if(db.isOpen()){db.close();}
//			query = "DELETE  FROM BsUserServices WHERE Code=" + UserServiceCode;
//			db.execSQL(query);if(db.isOpen()){db.close();}
//			Toast.makeText(this.activity.getApplicationContext(), "پیشنهاد شما ارسال گردید", Toast.LENGTH_LONG).show();
//			if(!coursors.isClosed())
//			{
//				coursors.close();
//			}
//			if(db.isOpen()) {
//				if(db.isOpen()){db.close();}
//			}
//		}
		SyncGetAllHamyarRequest syncGetAllHamyarRequest=new SyncGetAllHamyarRequest(activity.getApplicationContext(),guid,hamyarcode,false);
		syncGetAllHamyarRequest.AsyncExecute();
		LoadActivity(MainMenu.class,"guid", guid,"hamyarcode",hamyarcode,"BsUserServicesID",UserServiceCode);
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2, String VariableName3, String VariableValue3)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
		activity.startActivity(intent);
	}
}
