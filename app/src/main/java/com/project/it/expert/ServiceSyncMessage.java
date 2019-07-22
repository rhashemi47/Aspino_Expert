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

public class ServiceSyncMessage extends Service {
    Handler mHandler;
    boolean continue_or_stop = true;
    boolean createthread=true;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String guid;
    private String hamyarcode;
    private String LastMessageCode="0";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        akeText(this, "Service Started", Toast.LENGTH_LONG).show();
        continue_or_stop=true;
        if(createthread) {
            mHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (continue_or_stop) {
                        try {
                            if (PublicVariable.thread_Message) {
                                Thread.sleep(6000); // every 6 seconds
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
                                            hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
                                            guid = coursors.getString(coursors.getColumnIndex("guid"));
                                        }
                                       try { if(!db.isOpen()) {  db = dbh.getReadableDatabase();}} catch (Exception ex){ db = dbh.getReadableDatabase();}
                                        coursors = db.rawQuery("SELECT ifnull(MAX(CAST (code_messages AS INT)),0)as code FROM messages", null);
                                        if (coursors.getCount() > 0) {
                                            coursors.moveToNext();
                                            LastMessageCode = coursors.getString(coursors.getColumnIndex("code"));
                                        }
                                        if(db.isOpen()){db.close();}
                                        SyncMessage syncMessage = new SyncMessage(getApplicationContext(), guid, hamyarcode, LastMessageCode);
                                        syncMessage.AsyncExecute();
                                    }
                                });
                            }
                        } catch (Exception e) {
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
       // akeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        continue_or_stop=false;
    }
}
