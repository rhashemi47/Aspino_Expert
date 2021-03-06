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

public class SyncProfile {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String phonenumber;
	private String acceptcode;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncProfile(Activity activity, String guid, String hamyarcode,String phonenumber,String acceptcode) {
		this.activity = activity;
		this.guid = guid;
		this.hamyarcode=hamyarcode;
		this.phonenumber=phonenumber;
		this.acceptcode=acceptcode;
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
        		CallWsMethod("GetHamyarProfile");
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
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
	            }
				else if(WsResponse.toString().compareTo("2") == 0)
				{
					Toast.makeText(this.activity.getApplicationContext(), "متخصص شناسایی نشد!", Toast.LENGTH_LONG).show();
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
		String query=null;
		try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
		db.execSQL("DELETE FROM Profile");
			value=WsResponse.split("##");
			query="INSERT INTO Profile " +
					"(Code_Profile," +
					"Name," +
					"Fam," +
					"BthDate," +
					"ShSh," +
					"BirthplaceCode," +
					"Sader," +
					"StartDate," +
					"Address," +
					"Tel," +
					"Mobile," +
					"ReagentName," +
					"AccountNumber," +
					"HamyarNumber," +
					"IsEmrgency," +
					"Status" +
					",HamyarCodeForReagent" +
					",ShabaNumber" +
					",AccountNameOwner" +
					",BankName" +
					",City" +
					" )" +
					"VALUES" +
					"('"+ value[0]+
					"','"+value[1]+
					"','"+value[2]+
					"','"+value[3]+
					"','"+value[4]+
					"','"+value[5]+
					"','"+value[6]+
					"','"+value[7]+
					"','"+value[8]+
					"','"+value[9]+
					"','"+value[10]+
					"','"+value[11]+
					"','"+value[12]+
					"','"+value[13]+
					"','"+value[14]+
					"','"+value[15]+
					"','"+value[16]+
					"','"+value[17]+
					"','"+value[18]+
					"','"+value[19]+
					"','"+value[20]+
					"')";
		db.execSQL(query);if(db.isOpen()){db.close();}
		if(db.isOpen()){db.close();}
		SyncCity syncCity=new SyncCity(this.activity,value[20],phonenumber,acceptcode);
		syncCity.AsyncExecute();
		SyncGetRating syncGetRating=new SyncGetRating(activity.getApplicationContext(),guid,hamyarcode);
		syncGetRating.AsyncExecute();
		SyncProfilePic syncProfilePic=new SyncProfilePic(activity,guid,hamyarcode);
		syncProfilePic.AsyncExecute();
    }
}
