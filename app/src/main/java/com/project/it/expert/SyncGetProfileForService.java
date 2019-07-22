package com.project.it.expert;

import android.app.ProgressDialog;
import android.content.Context;
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

public class SyncGetProfileForService {

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
	public SyncGetProfileForService(Context activity, String guid, String hamyarcode) {
		this.activity = activity;
		this.guid = guid;
		this.hamyarcode=hamyarcode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.thread_Profile=false;
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

		} catch (IOException ioe) {
			PublicVariable.thread_Profile=true;
			throw new Error("Unable to create database");

		}

		try {

			dbh.openDataBase();

		} catch (SQLException sqle) {
			PublicVariable.thread_Profile=true;
			throw sqle;
		}
	}

	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				SyncGetProfileForService.AsyncCallWS task = new SyncGetProfileForService.AsyncCallWS(this.activity);
				task.execute();
			}
			catch (Exception e) {
				PublicVariable.thread_Profile=true;
				e.printStackTrace();
			}
		}
		else
		{
			PublicVariable.thread_Profile=true;
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
        		CallWsMethod("GetHamyarProfile");
        	}
	    	catch (Exception e) {
				PublicVariable.thread_Profile=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
				PublicVariable.thread_Profile=true;
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
				PublicVariable.thread_Profile=true;
        		//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {PublicVariable.thread_Profile=true;}
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
		String query;
		try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
			value=WsResponse.split("##");
			query="UPDATE Profile SET " +
					"Code_Profile='" + value[0] + "' , " +
					"Name='" + value[1] + "' , " +
					"Fam='" + value[2] + "' , " +
					"BthDate='" + value[3] + "' , " +
					"ShSh='" + value[4]+ "' , " +
					"BirthplaceCode='" + value[5]+ "' , " +
					"Sader='" + value[6]+ "' , " +
					"StartDate='" + value[7]+ "' , " +
					"Address='" + value[8]+ "' , " +
					"Tel='" + value[9]+ "' , " +
					"Mobile='" + value[10]+ "' , " +
					"ReagentName='" + value[11]+ "' , " +
					"AccountNumber='" + value[12]+ "' , " +
					"HamyarNumber='" + value[13]+ "' , " +
					"IsEmrgency='" + value[14]+ "' , " +
					"Status='" + value[15]+ "' , " +
					"HamyarCodeForReagent='" + value[16]+ "' , " +
					"ShabaNumber='" + value[17]+ "' , " +
					"AccountNameOwner='" + value[18]+ "' , " +
					"BankName='" + value[19]+ "' , " +
					"DateStart='" + value[20]+ "'";
		db.execSQL(query);if(db.isOpen()){db.close();}
		if(db.isOpen()){db.close();}
		SyncGetRating syncGetRating=new SyncGetRating(activity,guid,hamyarcode);
		syncGetRating.AsyncExecute();
    }
}
