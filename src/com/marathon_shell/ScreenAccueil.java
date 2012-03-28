package com.marathon_shell;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import component.marathon_shell.MyDialogProgress;

import data.marathon_shell.Commande;
import data.marathon_shell.Course;
import data.marathon_shell.XMLReadAndWrite;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ListView;
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
public class ScreenAccueil extends Activity {
	
	/**
	 * On déclare un nouvel objet Commande grace auquel on pourra accéder aux méthodes présentent dans cette classe.
	 */
	public static final int DIALOG = 0;

	private String LogTag = "Marathon Shell";
	private String Class = "ScreenAnalyse - ";
	
	private XMLReadAndWrite xmlReadAndWrite;
	
	private TextView tvMoyenne;
	
	ListView lvListeFichiers;
	
	private ArrayList<String> niveau;
	private ArrayList<ArrayList<String>> sousNiveau;
	
	private EditText etNomFichier;
	private ViewFlipper vfListeEcrans;
	private Button bAccueil;
	private Button bCourse;
	private Spinner spOptions;
	
	private CalendarView cvCalendrier;
	private String[] items = new String[] {"-- Choisir --", "Modifier la course", "Supprimer la course"};

	
	HashMap<String, Course> hmCourses;
	
	//private ProgressDialog dialog;
	private MyDialogProgress dialog;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);
		// Bloque la mise ne veille du téléphone pour éviter les déconnections involontaires
		// lors de l'appel de la méthode onPause()
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock pmwl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, LogTag);
		pmwl.acquire();
		

		cvCalendrier = (CalendarView) findViewById(R.id.cvCalendrier);
		
		
		dialog = new MyDialogProgress(ScreenAccueil.this, cvCalendrier);
		dialog.setTitle("Marathon Shell");
		dialog.setMessage("Please wait while loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		
		
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
		
		lvListeFichiers = (ListView) findViewById(R.id.lvListeFichiers);
		lvListeFichiers.getContext().setTheme(R.style.ListeBackGround);
		
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

		cvCalendrier.getContext().setTheme(R.style.CalendarBackGround);

		cvCalendrier.setOnDateChangeListener(new OnDateChangeListener() {

			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

				String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);

				if (niveau.contains(date))
				{
					int indice = niveau.indexOf(date);
					Log.w(LogTag, "J'ai des courses - " + indice);

					ArrayList<String> nom = new ArrayList<String>();

					for (int i = 0; i < sousNiveau.get(indice).size(); i++)
						nom.add(hmCourses.get(sousNiveau.get(indice).get(i)).getNomFichier());

					lvListeFichiers.setAdapter(new ArrayAdapter<String>(ScreenAccueil.this, android.R.layout.simple_list_item_1, nom));
				}
				else
				{
					Log.w(LogTag, "J'ai pas de courses");
					lvListeFichiers.setAdapter(new ArrayAdapter<String>(ScreenAccueil.this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
				}
			}
		});

		bCourse.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ScreenAccueil.this, ScreenCourse.class);
				startActivity(intent);
			}
		});
		
		
		lvListeFichiers.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				chargerData((String)lvListeFichiers.getItemAtPosition(position));
			}
		});
		

		bAccueil.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				vfListeEcrans.setDisplayedChild(0);
			}
		});
		
		

		Thread background1 = new Thread(new Runnable(){
			public void run() {
				xmlReadAndWrite.ParserXMLFichiers(getApplicationContext(), niveau, sousNiveau);
				Log.e(LogTag, "TAILLE DE NIVEAU " + niveau.size());
						
				for (int i = 0; i < sousNiveau.size(); i++)
					for (int j = 0; j < sousNiveau.get(i).size(); j++)
					{
						hmCourses.put(sousNiveau.get(i).get(j), xmlReadAndWrite.ParserXMLCourse(getApplicationContext(), sousNiveau.get(i).get(j)));
					}
				//afficheFin();
				dialog.dismiss();
			}
		});

		dialog.show();
		background1.start();
	}
	/*
	private void afficheFin() {
		long today = cvCalendrier.getDate();
		cvCalendrier.setDate(0);
		cvCalendrier.setDate(today);
	}*/
	
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
			Log.w(LogTag, "J'ai des courses Resume");
			
			while(!xmlReadAndWrite.getListeFichiersNonParses().isEmpty())
			{
				hmCourses.put(xmlReadAndWrite.getListeFichiersNonParses().get(0), xmlReadAndWrite.ParserXMLCourse(getApplicationContext(), xmlReadAndWrite.getListeFichiersNonParses().get(0)));
				
				SimpleDateFormat maDate = new SimpleDateFormat("d-M-yyyy", Locale.FRANCE);
				Date Aujourdhui = new Date();

				String nomFichier = maDate.format(Aujourdhui);
				if(!niveau.contains(nomFichier))
				{
					Log.i(LogTag, Class + "nouveau niveau " + nomFichier);
					niveau.add(nomFichier);
					sousNiveau.add(new ArrayList<String>());
				}
				
				sousNiveau.get(niveau.size() - 1).add(xmlReadAndWrite.getListeFichiersNonParses().get(0));
				xmlReadAndWrite.getListeFichiersNonParses().remove(0);
			}

			long today = cvCalendrier.getDate();
			cvCalendrier.setDate(0);
			cvCalendrier.setDate(today);
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
	
	/*
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Marathon Shell");
		dialog.setMessage("Please wait while loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
    }*/

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