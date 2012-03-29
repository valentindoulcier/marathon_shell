/**
 * 
 */
package com.marathon_shell;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import data.marathon_shell.XMLReadAndWrite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author Valentin
 *
 */
public class ScreenCourse extends Activity implements LocationListener {
	
	private String LogTag = "Marathon Shell";
	private String Class = "ScreenCourse - ";
	
	private XMLReadAndWrite xmlReadAndWrite;
	
	private LocationManager lm;

	private Location actualLocation;
	
	private boolean modeCourse = false;
	
	private String nomFichier = "";
	private SimpleDateFormat maDate;
	private Date Aujourdhui;

	private TextView tvVitesse;
	private ProgressBar pbVitesse;
	
	private ToggleButton tbCourse;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);
		
		
		
		
		// Bloque la mise ne veille du téléphone pour éviter les déconnections involontaires
		// lors de l'appel de la méthode onPause()
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock pmwl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, LogTag);
		pmwl.acquire();
		
		xmlReadAndWrite = XMLReadAndWrite.getInstance();
		
		lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		actualLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		tvVitesse = (TextView) findViewById(R.id.tvVitesse);
		
		pbVitesse = (ProgressBar) findViewById(R.id.pbVitesse1);
		// Max 150 km/h !!
		pbVitesse.setMax(150);
		
		tbCourse = (ToggleButton) findViewById(R.id.tbCourse);
		tbCourse.setBackgroundColor(Color.GREEN);
		//tbCourse.setEnabled(false);
		
		
		tbCourse.setOnClickListener(new ToggleButton.OnClickListener() {
			public void onClick(View v) {
				if(tbCourse.isChecked()) {
					modeCourse = true;

					maDate = new SimpleDateFormat("yyyy-M-d_kk-mm-ss", Locale.FRANCE);
					Aujourdhui = new Date();

					nomFichier = "Course_" + maDate.format(Aujourdhui) + ".xml";
					
					xmlReadAndWrite.getListeFichiersNonParses().add(nomFichier);

					maDate = new SimpleDateFormat("d-M-yyyy", Locale.FRANCE);
					Aujourdhui = new Date();

					xmlReadAndWrite.AjouterCourse(getApplicationContext(), "ListeCourses.xml", nomFichier, maDate.format(Aujourdhui));

					//Log.v(LogTag, Class + nomFichier);
					File monFic = new File(xmlReadAndWrite.getPathCourses(), nomFichier);

					xmlReadAndWrite.OuvertureOutput(monFic);
					xmlReadAndWrite.InsertionDebutFichierCourse(maDate.format(Aujourdhui));

					tbCourse.setBackgroundColor(Color.RED);
					Log.v(LogTag, Class + "Mode COURSE : ON");
				} else {
					quitterModeCourse();
				}
			}
		});
	}
	
	
	@Override
	protected void onPause()
	{
		super.onPause();
		/*
		modeCourse = false;

		xmlReadAndWrite.InsertionFinFichierCourse();
		xmlReadAndWrite.FermetureOutput(getApplicationContext());

		tbCourse.setBackgroundColor(Color.GREEN);
		Log.v(LogTag, Class + "Mode COURSE : OFF");
		
		finish();
		*/
		lm.removeUpdates(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500l, 0, this);
	}
	
	
	public float getSpeed()
	{
		if (actualLocation.hasSpeed() == true)
		{
			return (actualLocation.getSpeed());
		}
		else
		{
			return (-1);
		}
	}
	

	
	public void onLocationChanged(Location location)
	{
		actualLocation = location;
		
		if(modeCourse)
		{
			maDate = new SimpleDateFormat("kk:mm:ss", Locale.FRANCE);
			Aujourdhui = new Date();
			xmlReadAndWrite.Write(xmlReadAndWrite.FormatPoint(	String.valueOf(maDate.format(Aujourdhui)),
																String.valueOf((getSpeed()*36) / 10.0f),
																String.valueOf(actualLocation.getLatitude()),
																String.valueOf(actualLocation.getLongitude())));
		}
		
		tvVitesse.setText( (int)(getSpeed() * 36) / 10.0f + " km/h" );
		pbVitesse.setProgress((int)(getSpeed() * 3.6));
	}

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	quitterModeCourse();
            return true;
        }
        return super.onKeyDown(keyCode, msg);
    }
	
	protected void quitterModeCourse() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		
		alertDialog.setTitle("Validez-vous la fin de la course ?!");
		//alertDialog.setIcon(R.drawable.quitter);
		alertDialog.setCanceledOnTouchOutside(true);
		
		alertDialog.setButton("Oui", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		
		alertDialog.setButton2("Non", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				alertDialog.cancel();
				tbCourse.setChecked(true);
			} 
		});
		alertDialog.show();		
	}

}
