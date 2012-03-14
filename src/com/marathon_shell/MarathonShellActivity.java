package com.marathon_shell;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MarathonShellActivity extends Activity implements LocationListener {
	
	private LocationManager lm;

	private Location actualLocation;
	
	private ProgressBar vitesse;
	
	private TextView textviewVitesse;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application);
		
		lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		actualLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
		vitesse = (ProgressBar) findViewById(R.id.progressBarVitesse);
		// Max 150 km/h !!
		vitesse.setMax(150);
		
		textviewVitesse = (TextView) findViewById(R.id.textViewVitesse);
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

	
	public void onLocationChanged(Location location)
	{
		actualLocation = location;

		textviewVitesse.setText( (int)(getSpeed() * 36) / 10.0f + " km/h" );
		vitesse.setProgress((int)(getSpeed() * 3.6));
	}

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
}