package com.project.it.expert;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by hashemi on 02/18/2018.
 */

public class ServiceGetAllHamyarRequest extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String hamyarcode;
    private String guid;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        continue_or_stop=true;
        if(createthread) {
            mHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (continue_or_stop) {
                        try {
                            if (PublicVariable.thread_RequestHamyar) {
                                Thread.sleep(6000); // every 60 seconds
                                mHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        dbh = new DatabaseHelper(getApplicationContext());
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
                                       try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                                        Cursor coursors = db.rawQuery("SELECT * FROM login", null);
                                        for (int i = 0; i < coursors.getCount(); i++) {
                                            coursors.moveToNext();
                                            guid = coursors.getString(coursors.getColumnIndex("guid"));
                                            hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                        }
                                        SyncGetAllHamyarRequest syncGetAllHamyarRequest = new SyncGetAllHamyarRequest(getApplicationContext(), guid, hamyarcode, false);
                                        syncGetAllHamyarRequest.AsyncExecute();
                                        if(db.isOpen()){db.close();}
                                    }
                                });
                            }
                        }catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }
            }).start();
            createthread=false;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
    }
}
