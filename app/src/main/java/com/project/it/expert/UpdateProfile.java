package com.project.it.expert;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class UpdateProfile {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String UserCode;
	private String Month;
	private String Year;
	private String Day;
	private String WsResponse;
	private String ReagentCode;
	private boolean CuShowDialog=true;
	private String[] res;
	//Contractor
	public UpdateProfile(Activity activity, String UserCode, String Year, String Month, String Day, String ReagentCode) {
		this.activity = activity;
		this.UserCode=UserCode;
		this.Year=Year;
		this.Month=Month;
		this.ReagentCode=ReagentCode;
		this.Day=Day;

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
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
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
        		CallWsMethod("UpdateUserBthDate");
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
        		res=WsResponse.split("##");
	            if(res[0].toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	         
	            else
	            {
	            	InsertDataFromWsToDb();
	            }
        	}
        	else
        	{
        		//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
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
	    PropertyInfo UserCodePI = new PropertyInfo();
	    //Set Name
		UserCodePI.setName("UserCode");
	    //Set Value
		UserCodePI.setValue(this.UserCode);
	    //Set dataType
		UserCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(UserCodePI);
	    //*******************************************************************
	    PropertyInfo YearPI = new PropertyInfo();
	    //Set Name
		YearPI.setName("Year");
	    //Set Value
		YearPI.setValue(this.Year);
	    //Set dataType
		YearPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(YearPI);
	    //*********************************************************************
	    PropertyInfo MonthPI = new PropertyInfo();
	    //Set Name
		MonthPI.setName("Month");
	    //Set Value
		MonthPI.setValue(this.Month);
	    //Set dataType
		MonthPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(MonthPI);
	    //**********************************************************************
	    PropertyInfo DayPI = new PropertyInfo();
	    //Set Name
		DayPI.setName("Day");
	    //Set Value
		DayPI.setValue(this.Day);
	    //Set dataType
		DayPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(DayPI);
	    //*********
	    PropertyInfo ReagentCodePI = new PropertyInfo();
	    //Set Name
		ReagentCodePI.setName("ReagentCodeStr");
	    //Set Value
		ReagentCodePI.setValue(this.ReagentCode);
	    //Set dataType
		ReagentCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(ReagentCodePI);
	    
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
		db=dbh.getWritableDatabase();
		db.execSQL("UPDATE Profile SET BthDate='"+Year+"/"+Month+"/"+Day+"'");
		db.close();
		Toast.makeText(this.activity, "ثبت شد", Toast.LENGTH_SHORT).show();
    }
}
