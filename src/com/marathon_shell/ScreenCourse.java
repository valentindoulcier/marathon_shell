/**
 * 
 */
package com.marathon_shell;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Classe ScreenCourse
 * Cette classe définie l'écran course de l'application.
 * 
 * @author  Valentin DOULCIER
 * @version 1.0
 * @see     XMLReadAndWrite
 */
public class ScreenCourse extends Activity implements LocationListener {
	
	
	/**
	 * On déclare une chaine LogTag à utiliser en tant que premier paramètre d'un Log.
	 */
	private String LogTag = "Marathon Shell";
	/**
	 * On déclare une chaine Class à utiliser en tant que second paramètre d'un Log.
	 */
	private String Class = "ScreenCourse - ";
	
	/**
	 * On déclare un nouvel objet XMLReadAndWrite. Celui-ci étant un singleton, ne pas faire de new mais utiliser un getInstance().
	 */
	private XMLReadAndWrite xmlReadAndWrite;
	
	/**
	 * On déclare les variables nécessaires pour le GPS
	 */
	private LocationManager lm;
	private Location actualLocation;
	
	/**
	 * On déclare un boolean qui nous permet de savoir si nous sommes en course ou pas.
	 */
	private boolean modeCourse = false;
	
	/**
	 * On déclare une variable de type String qui va nous servir à créer et stocker le nom du fichier associé à la course.
	 */
	private String nomFichier = "";
	
	/**
	 * On déclare les variables nécessaires pour l'utilisation de dates.
	 */
	private SimpleDateFormat maDate;
	private Date Aujourdhui;

	/**
	 * On déclare un TextView qui va afficher la vitesse instantannée.
	 */
	private TextView tvVitesse;
	
	/**
	 * On déclare une ProgressBar qui va visuellement montrer l'état de la vitesse.
	 */
	private ProgressBar pbVitesse;
	
	/**
	 * On déclare une SeekBar qui va visuellement montrer l'état de la vitesse.
	 */
	private SeekBar sbVitesse;
	
	/**
	 * On déclare un Chronometer qui va afficher le décompte du temps restant.
	 */
	private Chronometer chCompteARebours;
	
	/**
	 * On déclare un Togglebutton qui nous permets de lancer et de stopper la course.
	 */
	private ToggleButton tbCourse;
	
	List<Double> listVitesses = new ArrayList<Double>();
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);
				
		/**
		 * Ici, on va bloquer la mise en veille de la tablette pour éviter les déconnections involontaires.
		 * Ceci nécessite la déclaration d'une permission dans le manifest !!
		 */
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock pmwl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, LogTag);
		pmwl.acquire();
		
		
		/**
		 * On récupére l'instance du singleton XMLReadAndWrite.
		 */
		xmlReadAndWrite = XMLReadAndWrite.getInstance();
		
		
		/**
		 * On va associer chaque objet déclaré à l'objet graphique contenu dans le layout.
		 */
		
		sbVitesse = (SeekBar) findViewById(R.id.sbVitesse);
		
		tvVitesse = (TextView) findViewById(R.id.tvVitesse);
		
		tbCourse = (ToggleButton) findViewById(R.id.tbCourse);
		
		pbVitesse = (ProgressBar) findViewById(R.id.pbVitesse);
		
		chCompteARebours = (Chronometer) findViewById(R.id.chCompteARebours);
		
		
		/**
		 * On initialise plusieurs variables dont on a besoin.
		 */
		
		lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		actualLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		
		/**
		 * On associe éventuellement des propriétés à nos éléments graphiques
		 */
		
		pbVitesse.setMax(150);
		
		sbVitesse.setMax(150);
		
		tbCourse.setBackgroundColor(Color.GREEN);
		//tbCourse.setEnabled(false);
		
		chCompteARebours.setText("00:45:00");
		
		
		
		///////////////////// Ensemble des LISTENERS //////////////////////////
		
		
		/**
		 * Listener du ToggleButton
		 * 
		 * ON : On débute la course.
		 * 	Dans ce mode, on initialise une course : on déclare le nom du fichier, on l'ajoute dans un tableau, on créé le fichier...
		 * OFF : On finit la course
		 * 	Dans ce mode, on stoppe l'enregistrement des données, on termine l'activité et on revient à l'écrand d'accueil.
		 */
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

					File monFic = new File(xmlReadAndWrite.getPathCourses(), nomFichier);

					xmlReadAndWrite.OuvertureOutput(monFic);
					xmlReadAndWrite.InsertionDebutFichierCourse(maDate.format(Aujourdhui));

					tbCourse.setBackgroundColor(Color.RED);
					
					MyCount counter = new MyCount(2700000, 1000); //Init du temps : 45 minutes = 2 700 000 millisecondes
				    counter.start();

					Log.d(LogTag, Class + "Mode COURSE : ON");
				} else {
					quitterModeCourse();
				}
			}
		});
	}
	
	/**
	 * Méthode onPause(), est appellée lorsque l'on change d'application. Elle n'est alors pas fermée, elle est en pause.
	 */
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

	
	/**
	 * Méthode onResume(), est appellée lorsque l'on revient sur l'application, que ce soit au démarrage ou suite à un onPause().
	 * Dans notre cas, le onResume() est appelé lors de la fin de l'activité Course, on charge donc la nouvelle course dans la liste, et on met à jour la liste des fichiers.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500l, 0, this);
	}
	
	
	/**
	 * Fonction qui calcule la vitesse en temps réel.
	 * @return
	 */
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
	
	
	/**
	 * Fonction qui calcule la vitesse en temps réel.
	 * @return
	 */
	public double getMoyenne()
	{
		double total = 0;
		
		for (Double vitesse : listVitesses)
		{
		    total += vitesse;
		}
		
		return (total / listVitesses.size());
	}
	

	/**
	 * Listener du GPS, onLocationChanged
	 * On enregistre un point dans le fichier de la course à chaque modification.
	 */
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
		
		listVitesses.add((double)(getSpeed() * 3.6));
		
		tvVitesse.setText( (int)(getSpeed() * 36) / 10.0f + " km/h" );
		pbVitesse.setRotationX(45.0f);
		pbVitesse.setBackgroundColor(R.color.Rouge);
		pbVitesse.setProgress((int)(getSpeed() * 3.6));
		
		sbVitesse.setProgress((int)(getSpeed() * 3.6));
		sbVitesse.setThumbOffset((int)getMoyenne());
	}

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	
	/**
	 * Méthode qui bloque les touches android visible dans le bandeau en bas.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Log.v(LogTag, Class + "Tentative de KEYCODE_BACK");
        	quitterModeCourse();
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_HOME) {
        	Log.v(LogTag, Class + "Tentative de KEYCODE_HOME");
        	quitterModeCourse();
            return true;
        }
        return super.onKeyDown(keyCode, msg);
    }
	
	
	/**
	 * Méthode qui demande la confirmation avant de quitter une course.
	 */
	protected void quitterModeCourse() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		
		alertDialog.setTitle("Validez-vous la fin de la course ?!");
		//alertDialog.setIcon(R.drawable.quitter);
		alertDialog.setCanceledOnTouchOutside(true);
		
		alertDialog.setButton("Oui", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Log.d(LogTag, Class + "Mode COURSE : OFF");
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
	
	/**
	 * Fonction qui gère le compte à rebours
	 */
	public class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			chCompteARebours.setText("00:00:00");
		}

		public void onTick(long millisUntilFinished) {
			int seconds = (int) (millisUntilFinished / 1000) % 60 ;
			int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
			int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);

			if (seconds < 50)
			{
				chCompteARebours.setTextColor(Color.RED);
			}

			chCompteARebours.setText( String.format("%02d:%02d:%02d", hours,minutes,seconds));
		}
	}
}
