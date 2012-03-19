package com.marathon_shell;


import data.marathon_shell.Commande;
import data.marathon_shell.XMLReadAndWrite;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * Classe Application, définie l'écran principal de l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see Commande
 *
 */


// 32 -> 31.06974
// 43 -> 42.033412
public class Application extends Activity implements LocationListener {
	
	/**
	 * On déclare un nouvel objet Commande grace auquel on pourra accéder aux méthodes présentent dans cette classe.
	 */
	
	//private Commande commande;
	XMLReadAndWrite xmlReadAndWrite;
	
	private LocationManager lm;

	private Location actualLocation;
	
	private ProgressBar pbVitesse;
	
	private TextView tvVitesse;
	private TextView tvMoyenne;
	
	private ToggleButton tbCourse;
	
	private boolean modeCourse = false;
		
	private String LogTag = "Marathon Shell";
	private String Class = "APPLICATION - ";
	
	private static int compteur = 0;
	
	private float vitesses[];
	
	private String nomFichier = "";
	private java.util.Date maDate;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application);
		
		xmlReadAndWrite = new XMLReadAndWrite();
		
		vitesses = new float[5000];
						
		lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		actualLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
		pbVitesse = (ProgressBar) findViewById(R.id.pbVitesse);
		// Max 150 km/h !!
		pbVitesse.setMax(150);
		
		tvVitesse = (TextView) findViewById(R.id.tvVitesse);
		tvMoyenne = (TextView) findViewById(R.id.tvMoyenne);
		
		tbCourse = (ToggleButton) findViewById(R.id.tbCourse);
		tbCourse.setBackgroundColor(Color.GREEN);
		
		
		tbCourse.setOnClickListener(new ToggleButton.OnClickListener() {
			public void onClick(View v) {
				if(tbCourse.isChecked()) {
					modeCourse = true;
					
					maDate = new java.util.Date();
					nomFichier = "Course_" + maDate.getDate() + "-" + maDate.getMonth() + "-" + (maDate.getYear() + 1900) + "_" + maDate.getHours() + "-" + maDate.getMinutes() + "-" + maDate.getSeconds() + ".xml";
					
					Log.v(LogTag, Class + nomFichier);
					
					xmlReadAndWrite.OuvertureInput(getApplicationContext(), "monFichierXML.xml");
					xmlReadAndWrite.OuvertureOutput(getApplicationContext(), "monFichierXML.xml");
					
					compteur = 0;
					tvMoyenne.setText("0 km/h");
					tbCourse.setBackgroundColor(Color.RED);
					Log.v(LogTag, Class + "Mode COURSE : ON");
				} else {
					modeCourse = false;

					Log.w(LogTag, xmlReadAndWrite.Read());
					xmlReadAndWrite.FermetureOutput(getApplicationContext());
					xmlReadAndWrite.FermetureInput(getApplicationContext());
					
					tbCourse.setBackgroundColor(Color.GREEN);
					tvMoyenne.setText(getMoyenne() + "km/h");
					Log.v(LogTag, Class + "Mode COURSE : OFF");
				}
			}
		});

	}
		
	@Override
	protected void onPause()
	{
		super.onPause();
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
	
	public float getMoyenne()
	{
		float moyenne = 0;
		
		for (int i = 0; i < compteur; i++)
		{
			moyenne += vitesses[i];
		}
		
		
		moyenne /= compteur;
		
		return moyenne;
	}

	
	public void onLocationChanged(Location location)
	{
		actualLocation = location;
		
		if(modeCourse)
		{
			vitesses[compteur] = (getSpeed() * 36) / 10.0f;
			compteur++;
		}
		
		tvVitesse.setText( (int)(getSpeed() * 36) / 10.0f + " km/h" );
		pbVitesse.setProgress((int)(getSpeed() * 3.6));
	}

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
}












































/*
public String getGPSString() {

	String clock = "C" + new java.util.Date().toString();

	String loc = null;

	if (actualLocation != null) {

		StringBuffer sb = new StringBuffer();

		sb.append("N" + Location.convert(actualLocation.getLatitude(), Location.FORMAT_SECONDS));
		sb.append(" E" + Location.convert(actualLocation.getLongitude(), Location.FORMAT_SECONDS));

		//if (actualLocation.hasAltitude() == true)
		{
			sb.append(" A" + actualLocation.getAltitude());
		}

		//if (actualLocation.hasAccuracy() == true)
		{
			sb.append(" D" + round(actualLocation.getAccuracy()));
		}

		//if (actualLocation.hasSpeed() == true)
		{
			float kph = round((float)(actualLocation.getSpeed() / 0.277)); // converts m/s to kph
			sb.append(" S" + round(actualLocation.getSpeed()) + "m " + kph + "k");
		}

		//if (actualLocation.hasBearing() == true)
		{
			sb.append(" B" + round(actualLocation.getBearing()));
		}

		sb.append(" T" + new java.util.Date(actualLocation.getTime()).toString());
		sb.append(" " + clock);

		loc = sb.toString();

	} else {
		loc = clock;
	}

	return loc;

}

public Location getUpdatedGPSLocation() {

	actualLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	return actualLocation;
}

public Location getGPSLocation() {
	return actualLocation;
}

public long getLocationAge() {
	if (actualLocation != null) {
		return (System.currentTimeMillis() - actualLocation.getTime());
	} else {
		return (0);
	}
}

public float getBearing() {
	if ( actualLocation != null &&actualLocation.hasBearing() == true) {
		return (actualLocation.getBearing());
	} else {
		return (-1);
	}
}
*/