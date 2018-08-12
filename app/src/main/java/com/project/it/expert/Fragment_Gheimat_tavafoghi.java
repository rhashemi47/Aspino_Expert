package com.project.it.expert;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Gheimat_tavafoghi extends Fragment {

    private ListView lstServiceRun;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
    private String karbarCode;

    public Fragment_Gheimat_tavafoghi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment__gheimat_tavafoughi, container, false);
        dbh=new DatabaseHelper(getContext());
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
        return rootView;

    }

}
