package com.project.it.expert;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Gheimat_final extends Fragment {


    private ListView lstServiceRun;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
    private EditText etGheimat;

    public Fragment_Gheimat_final() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment__gheimat_final, container, false);
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

        etGheimat = (EditText)rootView.findViewById(R.id.etGheimat);
        etGheimat.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                db=dbh.getWritableDatabase();
                db.execSQL("DELETE FROM TempValue");
                String query="INSERT INTO TempValue (value) VALUES('"+etGheimat.getText().toString()+"')";
                db.execSQL(query);
                db.close();
            }
        });
        return rootView;

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etGheimat = (EditText) view.findViewById(R.id.etGheimat);
        etGheimat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                db=dbh.getWritableDatabase();
                db.execSQL("DELETE FROM TempValue");
                String query="INSERT INTO TempValue (value) VALUES('"+etGheimat.getText().toString()+"')";
                db.execSQL(query);
                db.close();
            }
        });
    }
}
