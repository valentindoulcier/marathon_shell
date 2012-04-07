package com.marathon_shell;
import component.marathon_shell.MyAudioPlayer;
import component.marathon_shell.MyAudioRecorder;
import component.marathon_shell.MyDialogProgress;
import component.marathon_shell.MyGraphique;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import data.marathon_shell.Course;
import data.marathon_shell.XMLReadAndWrite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;


/**
 * Classe ScreenAccueil
 * Cette classe définie l'écran principal de l'application. Elle contient tout ce qui touche à l'analyse des backups.
 * 
 * @author  Valentin DOULCIER
 * @version 1.0
 * @see     XMLReadAndWrite
 */
public class ScreenAccueil extends Activity {
	
	/**
	 * On déclare une chaine LogTag à utiliser en tant que premier paramètre d'un Log.
	 */
	private String LogTag = "Marathon Shell";
	/**
	 * On déclare une chaine Class à utiliser en tant que second paramètre d'un Log.
	 */
	private String Class = "ScreenAnalyse - ";
	
	/**
	 * On déclare un nouvel objet XMLReadAndWrite. Celui-ci étant un singleton, ne pas faire de new mais utiliser un getInstance().
	 */
	private XMLReadAndWrite xmlReadAndWrite;
	
	/**
	 * On déclare une nouvelle instance qui est appellée à la fin du thread initial.
	 */
	private MyDialogProgress dialog;
	
	
	//////////////////////////// Ecran général //////////////////////////////////
	
	/**
	 * On déclare un ViewFlipper qui va servir à switcher d'écran entre l'écran d'accueil et celui du récapitulatif d'une course.
	 */
	private ViewFlipper vfListeEcrans;
	
	/**
	 * On déclare un Button qui permet de revenir sur l'écran d'accueil.
	 */
	private Button bAccueil;
	
	/**
	 * On déclare un Button qui permet de lancer une nouvelle course. Le clique sur ce bouton déclare une nouvelle activité (ScreenCourse).
	 */
	private Button bCourse;
	
	/**
	 * On déclare un HashMap qui sera remplit dans le onCreate(), il va contenir toutes les courses, la clé sera le nom du fichier.
	 */
	private HashMap<String, Course> hmCourses;
	
	
	//////////////////////////// Détail d'une course //////////////////////////////////
	
	/**
	 * On déclare un EditText pour afficher le nom du fichier traité.
	 */
	private EditText etNomFichier;
	
	/**
	 * On déclare un EditText pour afficher la description de la course.
	 */
	private EditText etDescription;
	
	/**
	 * On déclare un Spinner qui va proposer les options concernant la course (Modification - Suppression).
	 */
	private Spinner spOptions;
	
	/**
	 * On déclare un tableau de String qui va contenir les champs de la spinner.
	 */
	private String[] items = new String[] {"-- Choisir --", "Modifier la course", "Supprimer la course"};
	
	/**
	 * On déclare un TextView pour afficher la vitesse moyenne de la course.
	 */
	private TextView tvMoyenne;
	
	private MyGraphique monGraphiqueVitesse;
	
	private ZoomControls zoomControls;
	
	/**
	 * On déclare un ListView qui va contenir toutes les courses du jour choisit.
	 */
	private ListView lvListeFichiers;
	
	/**
	 * On déclare un ImageView qui affiche une image "Aucun événement" en cas de liste vide.
	 */
	private ImageView ivAucunEvenement;
	
	/**
	 * On déclare un ArrayList de String qui va contenir toutes les dates auxquelles on a des courses sous la forme "23-3-2012".
	 */
	private ArrayList<String> niveau;
	
	/**
	 * On déclare un double tableau (ArrayList dans ArrayList) qui va, pour chaque jour présent dans l'Arraylist niveau, contenir la liste de toutes les courses associées (le nom du fichier).
	 */
	private ArrayList<ArrayList<String>> sousNiveau;
	
	/**
	 * On déclare un CalendatView qui va servir à sélectionner un jour pour la recherche d'une course.
	 */
	private CalendarView cvCalendrier;
	
	private SlidingDrawer sdAlert;

	private ImageButton ibRecording;
	
	private ImageView ivRondRouge;
	
	private ImageButton ibPlayRouge;
	
	private ImageButton ibPlayGris;
	
	private ImageButton ibStop;
	
	private SeekBar sbAudio;
	
	private MyAudioPlayer myAudioPlayer;
	
	private MyAudioRecorder myAudioRecorder;
	
    private final Handler handler = new Handler();
	
	private boolean test = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);
		
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
		 * On créé l'arborescence des dossiers sur la carte mémoire.
		 */
		xmlReadAndWrite.CreerArborescence();
		
		
		/**
		 * On va associer chaque objet déclaré à l'objet graphique contenu dans le layout.
		 */
		
		bCourse = (Button) findViewById(R.id.bCourse1);
		
		sbAudio = (SeekBar) findViewById(R.id.sbAudio);
		
		ibStop = (ImageButton) findViewById(R.id.ibStop);
		
		bAccueil = (Button) findViewById(R.id.bAccueil1);
		
		spOptions = (Spinner) findViewById(R.id.spOptions);
		
		tvMoyenne = (TextView) findViewById(R.id.tvMoyenne);
		
		sdAlert = (SlidingDrawer) findViewById(R.id.sdAlert1);
		
		ivRondRouge = (ImageView) findViewById(R.id.ivRondRouge);
		
		ibPlayGris = (ImageButton) findViewById(R.id.ibPlayGris);
		
		etNomFichier = (EditText) findViewById(R.id.etNomFichier);
		
		ibRecording = (ImageButton) findViewById(R.id.ibRecording);
		
		ibPlayRouge = (ImageButton) findViewById(R.id.ibPlayRouge);
		
		etDescription = (EditText) findViewById(R.id.etDescription);
		
		cvCalendrier = (CalendarView) findViewById(R.id.cvCalendrier);
		
		zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
		
		vfListeEcrans = (ViewFlipper) findViewById(R.id.vfListeEcrans);
		
		lvListeFichiers = (ListView) findViewById(R.id.lvListeFichiers);
		
		ivAucunEvenement = (ImageView) findViewById(R.id.ivAucunEvenement);

		monGraphiqueVitesse = (MyGraphique) findViewById(R.id.monGraphique);
				
		
		/**
		 * On initialise plusieurs variables dont on a besoin.
		 */
        
        //monGraphiqueVitesse = new MyGraphique(getApplicationContext(), null);
        		
		niveau = new ArrayList<String>();
		
		hmCourses = new HashMap<String, Course>();
		
		sousNiveau = new ArrayList<ArrayList<String>>();
		
		dialog = new MyDialogProgress(ScreenAccueil.this, cvCalendrier);
		
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		
		
		
		/**
		 * On associe éventuellement des propriétés à nos éléments graphiques
		 */
		
		dialog.setTitle("Marathon Shell");
		dialog.setMessage("Please wait while loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		
		lvListeFichiers.getContext().setTheme(R.style.ListeBackGround);
		
		spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spOptions.setAdapter(spAdapter);
		
		cvCalendrier.getContext().setTheme(R.style.CalendarBackGround);
		
		ivRondRouge.setVisibility(View.INVISIBLE);
		
		ibPlayRouge.setVisibility(View.INVISIBLE);
		
		ibStop.setEnabled(false);
		
		
		
		///////////////////// Ensemble des LISTENERS //////////////////////////
		
		ibPlayGris.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String name = etNomFichier.getText().toString();
				name = name.substring(0, name.indexOf(".xml"));
				name += ".mp3";
				File tempo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Marathon_Shell/Courses/" + name);
				if(tempo.exists())
				{
					ibPlayRouge.setVisibility(View.VISIBLE);
					
					myAudioPlayer = new MyAudioPlayer(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Marathon_Shell/Courses/" + name);
					sbAudio.setMax(myAudioPlayer.getMediaPlayer().getDuration());
				
					startPlayProgressUpdater();
					
					ibStop.setEnabled(true);
				}
				else
					Toast.makeText(getApplicationContext(), "Pas de fichiers son", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		ibStop.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String name = etNomFichier.getText().toString();
				name = name.substring(0, name.indexOf(".xml"));
				name += ".mp3";
				File tempo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Marathon_Shell/Courses/" + name);
				if(tempo.exists())
				{
						ibPlayRouge.setVisibility(View.INVISIBLE);
						myAudioPlayer.getMediaPlayer().stop();
				}
			}
		});
		
		sbAudio.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, android.view.MotionEvent event) {
				myAudioPlayer.seekChange(v);
				return false;
			}
		});
		
		
		ibRecording.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(!test)
				{
					test = true;
					String name = etNomFichier.getText().toString();
					name = name.substring(0, name.indexOf(".xml"));
					myAudioRecorder = new MyAudioRecorder(name);
					try {
						ivRondRouge.setVisibility(View.VISIBLE);
						myAudioRecorder.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					test = false;
					try {
						ivRondRouge.setVisibility(View.INVISIBLE);
						myAudioRecorder.stop();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		
		/**
		 * Listener de la spinner.
		 * 
		 * Choix 1 : MODIFIER : On appelle la fonction qui enable tous les champs.
		 * Choix 2 : SUPPRIMER : On appelle la fonction qui propose la suppression de la course
		 */
		spOptions.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 1)
				{
					Log.v(LogTag, Class + "Choix spinner : MODIFIER");
					//deverouillerData();
					sdAlert.animateClose();
				}
				else if (arg2 == 2)
				{
					if(etDescription.isEnabled())
					{
						modifierCourse();
					}
					Log.v(LogTag, Class + "Choix spinner : SUPPRIMER");
					//supprimerCourse();
					sdAlert.animateOpen();
				}
				//spOptions.setSelection(0);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				//RIEN FAIRE
			}
		});
		
		
		/**
		 * Listener sur le calendrier.
		 * La fonction instanciée est inSelectedDateChange, autrement dit, quand l'utilisateur change de jour.
		 */
		cvCalendrier.setOnDateChangeListener(new OnDateChangeListener() {
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

				if(spOptions.getSelectedItemPosition() == 1)
				{
					modifierCourse();
				}

				String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);

				//Si on a des courses à cette date, alors on les affiche dans a la liste.
				if (niveau.contains(date))
				{
					ivAucunEvenement.setVisibility(View.INVISIBLE);

					int indice = niveau.indexOf(date);

					ArrayList<String> nom = new ArrayList<String>();

					for (int i = 0; i < sousNiveau.get(indice).size(); i++)
					{
						String name = hmCourses.get(sousNiveau.get(indice).get(i)).getNomFichier();
						name = name.substring(0, name.indexOf(".xml"));
						String splitNom [] = name.split("_");
						String splitDate [] = splitNom[1].split("-");
						String splitHeure [] = splitNom[2].split("-");

						nom.add(splitNom[0] + " du " + splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0] + " à " + splitHeure[0] + "h" + splitHeure[1] + "m" + splitHeure[2] + "s");
					}

					lvListeFichiers.setAdapter(new ArrayAdapter<String>(ScreenAccueil.this, android.R.layout.simple_list_item_1, nom));
				}
				//Si on n'a pas de courses pour ce jour, alors on affiche l'image.
				else
				{
					ivAucunEvenement.setVisibility(View.VISIBLE);
					lvListeFichiers.setAdapter(new ArrayAdapter<String>(ScreenAccueil.this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
				}
			}
		});
		

		/**
		 * Listener du bouton Course
		 * On déclare une nouvelle instance et on démarre l'activité.
		 */
		bCourse.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Log.d(LogTag, Class + "Démarrage ScreenCourse activity");
				Intent intent = new Intent(ScreenAccueil.this, ScreenCourse.class);
				startActivity(intent);
			}
		});
		
		
		/**
		 * Listener sur les items de la liste
		 * Lorsqu'on clique sur un item de la liste, alors on recompose le nom du fichier et on appelle la fonction chargerData(nomFichier).
		 */
		lvListeFichiers.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				if(spOptions.getSelectedItemPosition() == 1)
				{
					modifierCourse();
				}
				
				String tempo = (String)lvListeFichiers.getItemAtPosition(position);
				String splitNom [] = tempo.split(" ");
				String splitDate [] = splitNom[2].split("-");
				String date = splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
				String resultat = splitNom[0] + "_" + date + "_";
				resultat += splitNom[4].substring(0, splitNom[4].indexOf("h")) + "-";
				resultat += splitNom[4].substring(splitNom[4].indexOf("h") + 1, splitNom[4].indexOf("m")) + "-";
				resultat += splitNom[4].substring(splitNom[4].indexOf("m") + 1, splitNom[4].indexOf("s")) + ".xml";
				
				Log.d(LogTag, Class + "Chargement de " + resultat);

				chargerData(resultat);
			}
		});
		

		/**
		 * Listener du bouton accueil
		 * On se contente de changer l'écran du ViewFlipper.
		 */
		bAccueil.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				
				if(spOptions.getSelectedItemPosition() == 1)
				{
					modifierCourse();
				}
				
				vfListeEcrans.setDisplayedChild(0);
			}
		});
		
		
		zoomControls.setOnZoomInClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int zoom = 0;
				zoom = monGraphiqueVitesse.getZoomLevel();
				/** Si on est pas au zoom maximum (x8) on augmente le zoom i.e. x2 */
				if (zoom != 8) 
				{
					monGraphiqueVitesse.setZoomLevel(zoom*2);
					monGraphiqueVitesse.setLayoutParams(new LayoutParams(monGraphiqueVitesse.getTaille()*monGraphiqueVitesse.getZoomLevel(), LayoutParams.MATCH_PARENT));////////////////////////////////////////////////////////////////////
					/* monGraphiqueVitesse.invalidate(); Il ne faut pas le faire car setLayoutParams fait déjà un appel
					 * impplicite à invalidate() */
				}
			}
		});
		
		zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			public void onClick(View v) {
				int zoom = 0;
				zoom = monGraphiqueVitesse.getZoomLevel();
				/** Si on est pas au zoom minimum (x1) on diminue le zoom i.e. /2 */
				if (zoom != 1)
				{
					monGraphiqueVitesse.setZoomLevel(zoom/2);
					monGraphiqueVitesse.setLayoutParams(new LayoutParams(monGraphiqueVitesse.getTaille()*monGraphiqueVitesse.getZoomLevel(), LayoutParams.MATCH_PARENT));////////////////////////////////////////////////////////////////////
					/* monGraphiqueVitesse.invalidate(); Il ne faut pas le faire car setLayoutParams fait déjà un appel
					 * impplicite à invalidate() */
				}
			}
		});
		
		/**
		 * Thread qui va s'exécuter au démarrage de l'application.
		 * Il a pour but de remplir le HashMap avec l'ensemble des courses. Pour celà, on parse le fichier qui contient la liste des courses.
		 */
		Thread background1 = new Thread(new Runnable(){
			public void run() {
				xmlReadAndWrite.ParserXMLFichiers(getApplicationContext(), niveau, sousNiveau);
				Log.e(LogTag, "TAILLE DE NIVEAU " + niveau.size());
						
				for (int i = 0; i < sousNiveau.size(); i++)
					for (int j = 0; j < sousNiveau.get(i).size(); j++)
					{
						hmCourses.put(sousNiveau.get(i).get(j), xmlReadAndWrite.ParserXMLCourse(getApplicationContext(), sousNiveau.get(i).get(j)));
					}
				dialog.dismiss();
			}
		});
				

		/**
		 * Pendant le thread, on charge l'image de LOADING
		 */
		dialog.show();
		
		/**
		 * On démarre le thread
		 */
		background1.start();
	}
	
	
	/**
	 * Méthode onPause(), est appellée lorsque l'on change d'application. Elle n'est alors pas fermée, elle est en pause.
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	
	/**
	 * Méthode onResume(), est appellée lorsque l'on revient sur l'application, que ce soit au démarrage ou suite à un onPause().
	 * Dans notre cas, le onResume() est appelé lors de la fin de l'activité Course, on charge donc la nouvelle course dans la liste, et on met à jour la liste des fichiers.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if (!xmlReadAndWrite.getListeFichiersNonParses().isEmpty())
		{			
			while(!xmlReadAndWrite.getListeFichiersNonParses().isEmpty())
			{
				hmCourses.put(xmlReadAndWrite.getListeFichiersNonParses().get(0), xmlReadAndWrite.ParserXMLCourse(getApplicationContext(), xmlReadAndWrite.getListeFichiersNonParses().get(0)));
				
				SimpleDateFormat maDate = new SimpleDateFormat("d-M-yyyy", Locale.FRANCE);
				Date Aujourdhui = new Date();

				String nomFichier = maDate.format(Aujourdhui);
				if(!niveau.contains(nomFichier))
				{
					//Log.i(LogTag, Class + "nouveau niveau " + nomFichier);
					niveau.add(nomFichier);
					sousNiveau.add(new ArrayList<String>());
				}
				sousNiveau.get(niveau.size() - 1).add(xmlReadAndWrite.getListeFichiersNonParses().get(0));
				xmlReadAndWrite.getListeFichiersNonParses().remove(0);
			}
			long today = cvCalendrier.getDate();
			cvCalendrier.setDate(0);
			cvCalendrier.setDate(today);
			
			Log.v(LogTag, Class + "Course ajoutée avec succès");
		}
	}
	
	
	/**
	 * ChargerDate va afficher à l'écran l'ensemble des informations que l'on souhaite concernant la course (moyenne de vitesse, graphique, ...).
	 * @param nomCourse Ce paramètre est le nom du fichier de la course.
	 */
	public void chargerData(String nomCourse)
	{
		vfListeEcrans.setDisplayedChild(1);
		
		etNomFichier.setText(nomCourse);
		
		etDescription.setText(hmCourses.get(nomCourse).getDescription());
		
		tvMoyenne.setText(String.valueOf(hmCourses.get(nomCourse).getMoyenne()));
		
		monGraphiqueVitesse.setPoints(hmCourses.get(nomCourse).getListePoints());

		verouillerData();
	}
	
	/**
	 * On disable chaque champ de cet écran pour protéger l'accès aux données.
	 */
	public void verouillerData()
	{
		etNomFichier.setEnabled(false);
		
		etDescription.setEnabled(false);
	}
	
	/**
	 * On enable chaque champ de cet écran pour permettre la modification des données.
	 */
	public void deverouillerData()
	{
		etNomFichier.setEnabled(true);
		
		etDescription.setEnabled(true);
	}
	
	public void startPlayProgressUpdater() {
    	sbAudio.setProgress(myAudioPlayer.getMediaPlayer().getCurrentPosition());

		if (myAudioPlayer.getMediaPlayer().isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	startPlayProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}else{
    		myAudioPlayer.getMediaPlayer().pause();
    		sbAudio.setProgress(0);
    	}
    } 

	
	/**
	 * Méthode qui permet de supprimer une course.
	 * On met à jour la liste des courses et on réécris dans le fichier ListeCourses.xml
	 */
	protected void supprimerCourse() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		
		alertDialog.setTitle("Confirmez-vous la demande de suppression ?!");
		//alertDialog.setIcon(R.drawable.quitter);
		alertDialog.setCanceledOnTouchOutside(true);
		
		alertDialog.setButton("Oui", new AlertDialog.OnClickListener() {
			@SuppressWarnings("unused")
			public void onClick(DialogInterface arg0, int arg1) {
				String nomFic = etNomFichier.getText().toString();
				String date = hmCourses.get(nomFic).getDateCourse();
				String newFic = "";
				int indiceNiveau = niveau.indexOf(hmCourses.get(nomFic).getDateCourse());
				int indiceSSNiveau = sousNiveau.get(indiceNiveau).indexOf(nomFic);
				
				//Log.v(LogTag, Class + "Date : " + date + " INDICE Niveau : " + indiceNiveau + " INDICE Sous-niveau : " + indiceSSNiveau + " NOM : " + nomFic);
				
				hmCourses.remove(nomFic);
				
				sousNiveau.get(indiceNiveau).remove(nomFic);
				
				if(sousNiveau.get(indiceNiveau).size() == 0)
				{
					Log.d(LogTag, Class + "Suppression du jour entier dans Niveau");
					sousNiveau.remove(indiceNiveau);
					niveau.remove(date);
				}
				
				for (int i = 0; i < sousNiveau.size(); i++)
					for (int j = 0; j < sousNiveau.get(i).size(); j++)
					{
						newFic += xmlReadAndWrite.FormatFichier(sousNiveau.get(i).get(j).toString(), niveau.get(i));
					}
				
				xmlReadAndWrite.MAJFichierListeCourse(getApplicationContext(), "ListeCourses.xml", newFic);
				
				Log.d(LogTag, Class + "Suppression de la course " + nomFic);
				
				long today = cvCalendrier.getDate();
				cvCalendrier.setDate(0);
				cvCalendrier.setDate(today);
				
				vfListeEcrans.setDisplayedChild(0);
			}
		});
		
		alertDialog.setButton2("Non", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				spOptions.setSelection(0);
				alertDialog.cancel();
			} 
		});
		alertDialog.show();		
	}
	
	
	
	/**
	 * Méthode qui permet de modifier une course.
	 * On met à jour course, et on ré-écrit le fichier xml de la course.
	 */
	protected void modifierCourse() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		
		alertDialog.setTitle("Voulez-vous enregitrer les modifications ?!");
		//alertDialog.setIcon(R.drawable.quitter);
		alertDialog.setCanceledOnTouchOutside(false);
		
		alertDialog.setButton("Oui", new AlertDialog.OnClickListener() {
			@SuppressWarnings("unused")
			public void onClick(DialogInterface arg0, int arg1) {
				spOptions.setSelection(0);
				String nomFic = etNomFichier.getText().toString();
				String date = hmCourses.get(nomFic).getDateCourse();
				String newFic = "";
				int indiceNiveau = niveau.indexOf(hmCourses.get(nomFic).getDateCourse());
				int indiceSSNiveau = sousNiveau.get(indiceNiveau).indexOf(nomFic);
				
				hmCourses.get(nomFic).setDescription(etDescription.getText().toString());
				
				//xmlReadAndWrite.MAJFichierListeCourse(getApplicationContext(), "ListeCourses.xml", newFic);
				
				Log.d(LogTag, Class + "Modification de la course " + nomFic);
				
				verouillerData();
				//vfListeEcrans.setDisplayedChild(0);
			}
		});
		
		alertDialog.setButton2("Non", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				verouillerData();
				spOptions.setSelection(0);
				alertDialog.cancel();
			} 
		});
		alertDialog.show();
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