package com.marathon_shell;
import component.marathon_shell.ExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import data.marathon_shell.Commande;
import data.marathon_shell.Course;
import data.marathon_shell.XMLReadAndWrite;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;


/**
 * Classe Application, définie l'écran principal de l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see Commande
 *
 */
public class ScreenAnalyse extends Activity {
	
	/**
	 * On déclare un nouvel objet Commande grace auquel on pourra accéder aux méthodes présentent dans cette classe.
	 */

	private String LogTag = "Marathon Shell";
	private String Class = "ScreenAnalyse - ";
	
	private XMLReadAndWrite xmlReadAndWrite;
	
	private TextView tvMoyenne;
	
	ExpandableListView elListeFichiers;
	ExpandableListViewAdapter adapter;
	
	private ArrayList<String> niveau;
	private ArrayList<ArrayList<String>> sousNiveau;
	
	private EditText etNomFichier;
	private ViewFlipper vfListeEcrans;
	private Button bAccueil;
	private Button bCourse;
	private Spinner spOptions;
	private String[] items = new String[] {"-- Choisir --", "Modifier la course", "Supprimer la course"};

	
	HashMap<String, Course> hmCourses;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analyse);
		// Bloque la mise ne veille du téléphone pour éviter les déconnections involontaires
		// lors de l'appel de la méthode onPause()
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock pmwl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, LogTag);
		pmwl.acquire();
		
		xmlReadAndWrite = XMLReadAndWrite.getInstance();
		
		xmlReadAndWrite.CreerArborescence();
		niveau = new ArrayList<String>();
		sousNiveau = new ArrayList<ArrayList<String>>();
		
		hmCourses = new HashMap<String, Course>();
		
		tvMoyenne = (TextView) findViewById(R.id.tvMoyenne);
		
		
		etNomFichier = (EditText) findViewById(R.id.etNomFichier);
		vfListeEcrans = (ViewFlipper) findViewById(R.id.vfListeEcrans);
		bAccueil = (Button) findViewById(R.id.bAccueil);
		bCourse = (Button) findViewById(R.id.bCourse);
		
		spOptions = (Spinner) findViewById(R.id.spOptions);
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spOptions.setAdapter(spAdapter);
		spOptions.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 1)
				{
					//Modifier
				}
				else if (arg2 == 2)
				{
					//Supprimer
				}
				//Log.e(LogTag, Class + arg0.getItemAtPosition(arg2).toString());
				spOptions.setSelection(0);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//RIEN FAIRE
			}

		});

	
		
		bCourse.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ScreenAnalyse.this, ScreenCourse.class);
				startActivity(intent);
			}
		});
		
		elListeFichiers = (ExpandableListView) findViewById(R.id.elListeFichiers);

		elListeFichiers.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//Log.e("LONG2", "LONNNNNNNG CLIC2 " + arg2);
				return false;
			}
		});
		elListeFichiers.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				//Log.e("LONG", "LONNNNNNNG CLIC");
				return false;
			}
		});
		
		elListeFichiers.setOnChildClickListener(new OnChildClickListener() {
			
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				//Log.w(LogTag, Class + groupPosition + " - " + childPosition + " - " + sousNiveau.get(groupPosition).get(childPosition));
				chargerData(sousNiveau.get(groupPosition).get(childPosition));
				return false;
			}
		});
		
		
		bAccueil.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				vfListeEcrans.setDisplayedChild(0);
			}
		});
		
		
		niveau.clear();
		for(int i = sousNiveau.size() - 1; i >= 0; i--)
			sousNiveau.remove(i);

		xmlReadAndWrite.ParserXMLFichiers(getApplicationContext(), niveau, sousNiveau);
		adapter = new ExpandableListViewAdapter(niveau, sousNiveau, getApplicationContext());
		elListeFichiers.setAdapter(adapter);
		
		for (int i = 0; i < sousNiveau.size(); i++)
			for (int j = 0; j < sousNiveau.get(i).size(); j++)
			{
				hmCourses.put(sousNiveau.get(i).get(j), xmlReadAndWrite.ParserXMLCourse(getApplicationContext(), sousNiveau.get(i).get(j)));
			}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		if (!xmlReadAndWrite.getListeFichiersNonParses().isEmpty())
		{
			while(!xmlReadAndWrite.getListeFichiersNonParses().isEmpty())
			{
				adapter.setEnfant(xmlReadAndWrite.getListeFichiersNonParses().get(0));
				hmCourses.put(xmlReadAndWrite.getListeFichiersNonParses().get(0), xmlReadAndWrite.ParserXMLCourse(getApplicationContext(), xmlReadAndWrite.getListeFichiersNonParses().get(0)));
				xmlReadAndWrite.getListeFichiersNonParses().remove(0);
			}
			elListeFichiers.setAdapter(adapter);
		}
	}
	
	
	public void chargerData(String nomCourse)
	{
		vfListeEcrans.setDisplayedChild(1);
		etNomFichier.setText(nomCourse);
		tvMoyenne.setText(String.valueOf(hmCourses.get(nomCourse).getMoyenne()));
		verouillerData();
	}
	
	public void verouillerData()
	{
		etNomFichier.setEnabled(false);
	}
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