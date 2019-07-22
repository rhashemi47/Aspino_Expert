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

public class SyncGetKarnameMali {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncGetKarnameMali(Context activity, String guid, String hamyarcode) {
		this.activity = activity;
		this.guid = guid;
		this.hamyarcode=hamyarcode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.thread_Income=false;
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

   		} catch (IOException ioe) {

			PublicVariable.thread_Income=true;
   			throw new Error("Unable to create database");

   		}

   		try {

   			dbh.openDataBase();

   		} catch (SQLException sqle) {
			PublicVariable.thread_Income=true;
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
				 PublicVariable.thread_Income=true;
	            e.printStackTrace();
			 }
		}
		else
		{
			PublicVariable.thread_Income=true;
			//Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;
		
		public AsyncCallWS(Context activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);
		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("GetHamyarKarnamehMali");
        	}
	    	catch (Exception e) {
				PublicVariable.thread_Income=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.thread_Income=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
	            }
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					//Toast.makeText(this.activity.getApplicationContext(), "متخصص شناسایی نشد!", Toast.LENGTH_LONG).show();
				}
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
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
            catch (Exception e) {
				PublicVariable.thread_Income=true;
			}
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
		String query;
		res=WsResponse.split("@@");
		try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
		for(int i=0;i<res.length;i++) {

			value=res[i].split("##");
			if(check(value[0])) {
				try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
				query = "INSERT INTO KarnameMali (Code_KarnameMali," +
						"Type" +
						",Price" +
						",Date" +
						") VALUES('" + value[0] +
						"','" + value[1] +
						"','" + value[2] +
						"','" + value[3] +
						"')";
				db.execSQL(query);
				if(db.isOpen()){db.close();}
			}
		}
    }
    public boolean check(String Code)
	{
		try { if(!db.isOpen()) { db=dbh.getReadableDatabase();}} catch (Exception ex){	db=dbh.getReadableDatabase();	}
		String query = "SELECT * FROM KarnameMali WHERE Code_KarnameMali='" +Code+"'";
		Cursor cursor= db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			cursor.close();
			db.close();
			return false;
		}
		else
		{
			cursor.close();
			db.close();
			return true;
		}
	}
}
