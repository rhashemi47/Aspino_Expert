package com.project.it.expert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGettHamyarIntroductionTime {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String hamyarcode;
	private String WsResponse;
	private boolean CuShowDialog=true;
	//Contractor
	public SyncGettHamyarIntroductionTime(Activity activity, String hamyarcode) {
		this.activity = activity;
		this.hamyarcode=hamyarcode;
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
        		CallWsMethod("InsertHamyarIntroductionTime");
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
	            	Toast.makeText(this.activity.getApplicationContext(), "خطایی رخداده است", Toast.LENGTH_LONG).show();
					//LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
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
		PropertyInfo HamyarCodePI = new PropertyInfo();
		//Set Name
		HamyarCodePI.setName("pHamyarCode");
		//Set Value
		HamyarCodePI.setValue(this.hamyarcode);
		//Set dataType
		HamyarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCodePI);
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
		String[] value=WsResponse.split("##");
		String query=null;
		PersianCalendar calNow=new PersianCalendar();
		calNow.setPersianDate(calNow.getPersianYear(),calNow.getPersianMonth()+1,calNow.getPersianDay());
		String strNow=String.valueOf(calNow.getPersianYear())+String.valueOf(calNow.getPersianMonth())+String.valueOf(calNow.getPersianDay());
		try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}} catch (Exception ex){	db=dbh.getWritableDatabase();	}
		String ContentMessage="وقت مصاحبه برای شما در تاریخ "+ value[1] +" ساعت "+value[2]+" تعیین گردید. ";
		query="INSERT INTO messages " +
				"(Code_messages," +
				"Title," +
				"Content," +
				"InsertDate," +
				"IsReade," +
				"IsDelete," +
				"IsSend" +
				" )" +
				"VALUES" +
				"('"+value[0]+
				"','"+"وقت مصاحبه"+
				"','"+ContentMessage+
				"','"+strNow+
				"','"+"0"+
				"','"+"0"+
				"','"+"1"+
				"')";
		db.execSQL(query);if(db.isOpen()){db.close();}
		if(db.isOpen()){db.close();}
		Toast.makeText(this.activity.getApplicationContext(),ContentMessage,Toast.LENGTH_LONG);
	}
}
