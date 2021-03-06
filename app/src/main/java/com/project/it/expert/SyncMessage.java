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

public class SyncMessage {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;

	private String GUID;
	private String HamyarCode;
	private String WsResponse;
	private String LastMessageCode;
	//private String acceptcode;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncMessage(Context activity, String GUID, String HamyarCode, String LastMessageCode) {
		this.activity = activity;

		this.LastMessageCode=LastMessageCode;
		this.GUID=GUID;
		this.HamyarCode=HamyarCode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.thread_Message=false;
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

   		} catch (IOException ioe) {

			PublicVariable.thread_Message=true;
   			throw new Error("Unable to create database");

   		}

   		try {

   			dbh.openDataBase();

   		} catch (SQLException sqle) {

			PublicVariable.thread_Message=true;
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

				 PublicVariable.thread_Message=true;
	            e.printStackTrace();
			 }
		}
		else
		{
			PublicVariable.thread_Message=true;
			//akeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;
		
		public AsyncCallWS(Context activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("GetMessagesForAll");
        	}
	    	catch (Exception e) {
				PublicVariable.thread_Message=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.thread_Message=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//akeText(this.activity.getApplicationContext(), "پیام جدیدی اعلام نشده", Toast.LENGTH_LONG).show();

	            }
				else if(WsResponse.toString().compareTo("0") == 0)
				{
					//akeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد!", Toast.LENGTH_LONG).show();
				}
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
	            }
        	}
        	else
        	{
				PublicVariable.thread_Message=true;
        		//akeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {
				PublicVariable.thread_Message=true;}
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
		PropertyInfo GUIDPI = new PropertyInfo();
		//Set Name
		GUIDPI.setName("GUID");
		//Set Value
		GUIDPI.setValue(this.GUID);
		//Set dataType
		GUIDPI.setType(String.class);
		//Add the property to request object
		request.addProperty(GUIDPI);
		//*****************************************************
		PropertyInfo HamyarCodePI = new PropertyInfo();
		//Set Name
		HamyarCodePI.setName("HamyarCode");
		//Set Value
		HamyarCodePI.setValue(this.HamyarCode);
		//Set dataType
		HamyarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCodePI);
		//**********************************************
		PropertyInfo LastMessageCodePI = new PropertyInfo();
		//Set Name
		LastMessageCodePI.setName("LastMessageCode");
		//Set Value
		LastMessageCodePI.setValue(this.LastMessageCode);
		//Set dataType
		LastMessageCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LastMessageCodePI);
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
	
	
	public void InsertDataFromWsToDb(String AllRecord)
    {
		String[] value;
		String[] res;
		String query=null;
		boolean isFirst=IsFristInsert();
		res=WsResponse.split("@@");
		try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
		for(int i=0;i<res.length;i++){
			value=res[i].split("##");
			query="INSERT INTO messages (Code_messages," +
					"Title" +
					",Content" +
					",InsertDate,IsReade,IsSend) VALUES('"+value[0]+
					"','"+value[1]+
					"','"+value[2]+
					"','"+value[3]+
					"','"+value[4]+
					"','0')";
			db.execSQL(query);if(db.isOpen()){db.close();}
			if(db.isOpen()){db.close();}
			if(!isFirst && value[4].compareTo("0")==0) {
				runNotification("بسپارینا", value[1], i, "",value[0], ShowMessage.class);
			}
		}
    }
	public void runNotification(String title,String TitleMessage,int id,String Table,String MessageCode,Class<?> Cls)
	{
		//todo
		NotificationClass notifi=new NotificationClass(this.activity);
		notifi.Notificationm(this.activity,title,TitleMessage,MessageCode,Table,id,Cls);
	}
	public boolean IsFristInsert()
	{
		db=dbh.getReadableDatabase();
		String query = "SELECT * FROM messages";
		Cursor cursor= db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			if(db.isOpen()){db.close();}
			return false;
		}
		else
		{
			if(db.isOpen()){db.close();}
			return true;
		}
	}

}
