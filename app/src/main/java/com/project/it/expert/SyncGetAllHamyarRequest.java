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

public class SyncGetAllHamyarRequest {
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
    private boolean CuShowDialog=false;
    private boolean notifocationEnable;
    //Contractor
    public SyncGetAllHamyarRequest(Context activity, String guid, String hamyarcode, boolean notifocationEnable) {
        this.activity = activity;
        this.guid = guid;
        this.hamyarcode=hamyarcode;
        this.notifocationEnable=notifocationEnable;
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
                SyncGetAllHamyarRequest.AsyncCallWS task = new SyncGetAllHamyarRequest.AsyncCallWS(this.activity);
                task.execute();
            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }
        else
        {
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
                CallWsMethod("GetAllHamyarRequest");
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
                    //Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
                }
                else if(WsResponse.toString().compareTo("0") == 0)
                {
                    //Toast.makeText(this.activity.getApplicationContext(), "خطا", Toast.LENGTH_LONG).show();
                }
                else if(WsResponse.toString().compareTo("2") == 0)
                {
                    //Toast.makeText(this.activity.getApplicationContext(), "خطا", Toast.LENGTH_LONG).show();
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
        String[] res;
        String[] value;
        String query=null;
        res=WsResponse.split("@@");
        db=dbh.getWritableDatabase();
        db.execSQL("DELETE FROM SuggetionsInfo");
        for(int i=0;i<res.length;i++) {
            value = res[i].split("##");
            query = "INSERT INTO SuggetionsInfo (" +
                        "Code_SuggetionsInfo," +
                        "BsUserServicesCode," +
                        "Price," +
                        "InsertDate," +
                        "ConfirmByUser," +
                        "ConfirmDate," +
                        "FinalPrice" +
                        ") VALUES('" +
                        value[0] +
                        "','" + value[1] +
                        "','" + value[2] +
                        "','" + value[3] +
                        "','" + value[4] +
                        "','" + value[5] +
                        "','" + value[6] +
                        "')";
                db.execSQL(query);
            query = "UPDATE BsUserServices SET Suggetion='1' WHERE Code_BsUserServices='" + value[1] +"'";
            db.execSQL(query);
                if (notifocationEnable) {
                    db = dbh.getReadableDatabase();
                    query = "SELECT * FROM Servicesdetails  WHERE code_Servicesdetails=" + value[1];
                    Cursor coursors = db.rawQuery(query, null);
                    if (coursors.getCount() > 0 && i < 10)//Just show 10 Notification
                    {
                        coursors.moveToNext();
                        runNotification("آسپینو", coursors.getString(coursors.getColumnIndex("name")), i, value[0], Pishnahad_Gheimat.class);
                    }
                }
        }
        db.close();
    }

    public void runNotification(String title,String detail,int id,String BsUserServicesID,Class<?> Cls)
    {
        NotificationClass notifi=new NotificationClass();
        notifi.Notificationm(this.activity,title,detail,BsUserServicesID,id,Cls);
    }
}
