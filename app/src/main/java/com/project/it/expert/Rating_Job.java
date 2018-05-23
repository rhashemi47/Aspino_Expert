package com.project.it.expert;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Rating_Job extends AppCompatActivity {
	private String hamyarcode;
	private String guid;
	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
	private RatingBar ratingBarZaher;
	private RatingBar ratingBarBarkhord;
	private RatingBar ratingBarHozooreBeMoghe;
	private RatingBar ratingBarGheymat;
	private RatingBar ratingBarTahvil;
	private RatingBar ratingBarKeyfiyat;
	private String StrratingBarZaher;
	private String StrratingBarBarkhord;
	private String StrratingBarHozooreBeMoghe;
	private String StrratingBarGheymat;
	private String StrratingBarTahvil;
	private String StrratingBarKeyfiyat;
	private Button btnSendRate;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.about);
//	btnCredit=(Button)findViewById(R.id.btnCredit);
//	btnOrders=(Button)findViewById(R.id.btnOrders);
	btnSendRate=(Button)findViewById(R.id.btnSendRate);
	ratingBarZaher=(RatingBar)findViewById(R.id.ratingBarZaher);
	ratingBarBarkhord=(RatingBar)findViewById(R.id.ratingBarBarkhord);
	ratingBarHozooreBeMoghe=(RatingBar)findViewById(R.id.ratingBarHozooreBeMoghe);
	ratingBarGheymat=(RatingBar)findViewById(R.id.ratingBarGheymat);
	ratingBarTahvil=(RatingBar)findViewById(R.id.ratingBarTahvil);
	ratingBarKeyfiyat=(RatingBar)findViewById(R.id.ratingBarKeyfiyat);
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
	try {
		hamyarcode = getIntent().getStringExtra("hamyarcode").toString();
		guid = getIntent().getStringExtra("guid").toString();
	} catch (Exception e) {
		Cursor coursors = db.rawQuery("SELECT * FROM login", null);
		for (int i = 0; i < coursors.getCount(); i++) {
			coursors.moveToNext();
			guid = coursors.getString(coursors.getColumnIndex("guid"));
			hamyarcode = coursors.getString(coursors.getColumnIndex("hamyarcode"));
		}

		db.close();
	}
	btnSendRate.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			StrratingBarZaher=String.valueOf(ratingBarZaher.getRating());
			StrratingBarBarkhord=String.valueOf(ratingBarBarkhord.getRating());
			StrratingBarHozooreBeMoghe=String.valueOf(ratingBarHozooreBeMoghe.getRating());
			StrratingBarGheymat=String.valueOf(ratingBarGheymat.getRating());
			StrratingBarTahvil=String.valueOf(ratingBarTahvil.getRating());
			StrratingBarKeyfiyat=String.valueOf(ratingBarKeyfiyat.getRating());
		}
});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity(MainMenu.class, "guid", guid, "hamyarcode", hamyarcode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		Rating_Job.this.startActivity(intent);
	}
}
