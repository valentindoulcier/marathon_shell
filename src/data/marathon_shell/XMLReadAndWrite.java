package data.marathon_shell;


import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Classe contenant les fonctions principales utilisées par l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see ScreenAnalyse
 *
 */

public class XMLReadAndWrite {


	private static XMLReadAndWrite instance = null;


	/****************************
	 * VARIABLES
	 ****************************/

	/**
	 * On déclare une chaine LogTag à utiliser en tant que premier paramètre d'un Log.
	 */
	private String LogTag = "Marathon Shell";
	/**
	 * On déclare une chaine Class à utiliser en tant que second paramètre d'un Log.
	 */
	private String Class = "XMLReadAndWrite - ";
	
	/**
	 * Compteur qui s'autoincrémente, est utilisé dans la balise <point> en tant que attribut value.
	 */
	private int compteur;
	
	/**
	 * Contient le chemin absolu vers les fichiers de courses.
	 */
	private String pathCourses;
	
	/**
	 * Contient le chemin absolu vers le fichier ListeCourse.xml
	 */
	private String pathConfiguration;
	
	/**
	 * Accesseurs aux fichiers
	 */
	private FileInputStream fIn = null;
    private InputStreamReader isr = null;
    
    private FileOutputStream fOut = null;
    private OutputStreamWriter osw = null;
    
    /**
     * Tableau des courses à ajouter.
     */
    private ArrayList<String> listeFichiersNonParses;
        
    
	/****************************
	 * CONSTRUCTEURS
	 ****************************/
	
    /**
     * Constructeur du singleton
     */
	private XMLReadAndWrite()
	{
		listeFichiersNonParses = new ArrayList<String>();
	}
	
	/**
	 * Récupère l'instance du singleton
	 * @return
	 */
	public static XMLReadAndWrite getInstance(){
	      if(instance == null){
	         instance = new XMLReadAndWrite();
	      }
	      return instance;
	   }
	
	
	/****************************
	 * ACCESSEURS
	 ****************************/
	
	/**
	 * @return the listeFichiersNonParses
	 */
	public ArrayList<String> getListeFichiersNonParses() {
		return listeFichiersNonParses;
	}

	/**
	 * @param listeFichiersNonParses the listeFichiersNonParses to set
	 */
	public void setListeFichiersNonParses(ArrayList<String> listeFichiersNonParses) {
		this.listeFichiersNonParses = listeFichiersNonParses;
	}

	/**
	 * @return the pathCourses
	 */
	public String getPathCourses() {
		return pathCourses;
	}


	/**
	 * @param pathCourses the pathCourses to set
	 */
	public void setPathCourses(String pathCourses) {
		this.pathCourses = pathCourses;
	}
	
	
	/**
	 * @return the pathConfiguration
	 */
	public String getPathConfiguration() {
		return pathConfiguration;
	}


	/**
	 * @param pathConfiguration the pathConfiguration to set
	 */
	public void setPathConfiguration(String pathConfiguration) {
		this.pathConfiguration = pathConfiguration;
	}


	/****************************
	 * METHODES
	 ****************************/
	
	/**
	 * Fonction qui créé initialement l'arborescence des fichiers sur la carte mémoire de la tablette.
	 */
	public void CreerArborescence()
	{
		//CREATION DU DOSSIER PRINCIPAL - MARATHON SHELL
		File DossierMarathonShell = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Marathon_Shell");
		DossierMarathonShell.mkdir();

		//CREATION DU DOSSIER CONFIGURATION
		File DossierConfiguration = new File(DossierMarathonShell.getPath(), "Configuration");
		DossierConfiguration.mkdir();
		setPathConfiguration(DossierConfiguration.getPath());

		//CREATION DU DOSSIER COURSES
		File DossierCourses = new File(DossierMarathonShell.getPath(), "Courses");
		DossierCourses.mkdir();
		setPathCourses(DossierCourses.getPath());
	}
	
	/**
	 * Fonction qui ajoute une Course dans le fichier ListeCourse.xml
	 * 
	 * @param context
	 * @param nomFichierConfig
	 * @param nomCourse
	 * @param date
	 */
	public void AjouterCourse(Context context, String nomFichierConfig, String nomCourse, String date)
	{
	
		//CREATION DU FICHIER CONFIGURATION
		File FichierConfiguration = new File(getPathConfiguration(), nomFichierConfig);
		OuvertureOutput(FichierConfiguration);
		
		if(!FichierConfiguration.exists())
		{
			Write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		}
		
		Write(FormatFichier(nomCourse, date));
		FermetureOutput(context);
	}
	
	/**
	 * Fonction qui met à jour la fichier ListeCourse.xml lors de la suppression d'une course.
	 * 
	 * @param context
	 * @param nomFichierConfig
	 * @param contenu
	 */
	public void MAJFichierListeCourse(Context context, String nomFichierConfig, String contenu)
	{
		//CREATION DU FICHIER CONFIGURATION
		File FichierConfiguration = new File(getPathConfiguration(), nomFichierConfig);
				
		OuvertureOutputTRUNC(FichierConfiguration);
		
		if(!FichierConfiguration.exists())
		{
			Write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		}
		
		Write(contenu);
		FermetureOutput(context);
	}
	
	/**
	 * Fonction qui formatte la chaine à écrire.
	 * 
	 * @param nomFichier
	 * @param date
	 * @return
	 */
	public String FormatFichier(String nomFichier, String date)
	{
		return "<fichier><date>" + date + "</date><nom>" + nomFichier + "</nom></fichier>\n";
	}


	/**
	 * Ouverture d'un fichier INPUT grace à son nom.
	 * 
	 * @param context
	 * @param nomFichier
	 */
	public void OuvertureInput(Context context, String nomFichier)
	{
        try{
        	fIn = context.openFileInput(nomFichier);
        	isr = new InputStreamReader(fIn);
        }
        catch (Exception e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Input");
        	Toast.makeText(context, "Erreur Ouverture Input",Toast.LENGTH_SHORT).show();
        }
	}
	
	/**
	 * Ouverture d'un fichier INPUT.
	 * 
	 * @param externalFilesDir
	 */
	public void OuvertureInput(File externalFilesDir)
	{
        try{
        	fIn = new FileInputStream(externalFilesDir);
        	isr = new InputStreamReader(fIn);
        }
        catch (Exception e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Input");
        }
	}
	
	/**
	 * OUverture d'un fichier OUTPUT grace au nom du fichier.
	 * 
	 * @param context
	 * @param nomFichier
	 */
	public void OuvertureOutput(Context context, String nomFichier)
	{
        try{
        	/*if(context.deleteFile(nomFichier))
        		Log.i(LogTag, Class + "Delete OK");
        	else
        		Log.e(LogTag, Class + "Erreur Delete");*/
        	
        	fOut = context.openFileOutput(nomFichier, Context.MODE_APPEND);
        	osw = new OutputStreamWriter(fOut);
        	
        	compteur = 0;
        	Write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        }
        catch (Exception e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Output");
        	Toast.makeText(context, "Erreur Ouverture Output",Toast.LENGTH_SHORT).show();
        }
	}

	/**
	 * Ouverture d'un fichier en OUTPUT.
	 * 
	 * @param externalFilesDir
	 */
	public void OuvertureOutput(File externalFilesDir) {
		try {
			fOut = new FileOutputStream(externalFilesDir, true);
			osw = new OutputStreamWriter(fOut);
			
	    	compteur = 0;
	    	
		} catch (FileNotFoundException e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Output with File");
		}
		
	}
	
	/**
	 * Ouverture d'un fichier en mode TRUNCATE
	 * Nécessaire lors de la réécriture.
	 * 
	 * @param externalFilesDir
	 */
	public void OuvertureOutputTRUNC(File externalFilesDir) {
		try {
			fOut = new FileOutputStream(externalFilesDir);
			osw = new OutputStreamWriter(fOut);
				    	
		} catch (FileNotFoundException e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Output with File");
		}
	}
	
	/**
	 * Fermeture d'un fichier INPUT.
	 * 
	 * @param context
	 */
	public void FermetureInput(Context context)
	{
		try {
			isr.close();
			fIn.close();
		} catch (IOException e) {
			Log.e(LogTag, Class + "Fermeture Input");
			Toast.makeText(context, "Erreur Fermeture Input",Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Fermeture d'un fichier en OUTPUT.
	 * 
	 * @param context
	 */
	public void FermetureOutput(Context context)
	{
		try {
			osw.close();
			fOut.close();
		} catch (IOException e) {
			Log.e(LogTag, Class + "Fermeture Output");
			Toast.makeText(context, "Fermeture Output",Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Fonction qui initialise le contenu du fichier.
	 * 
	 * @param date
	 */
	public void InsertionDebutFichierCourse(String date)
	{
		Write("<course value=\"" + date + "\">\n");
		Write("<listePoints>\n");
	}
	
	/**
	 * Fonction qui finalise le contenu d'un fichier.
	 */
	public void InsertionFinFichierCourse()
	{
		Write("</listePoints>\n");
		Write("<description>\n");
		
		Write("</description>\n");
		Write("</course>");
	}
	
	public void InsertionDebutFichierListeFichiers()
	{
		Write("<listeFichiers>\n");
	}
	
	public void InsertionFinFichierListeFichiers()
	{
		Write("</listeFichiers>\n");
	}
	
	/**
	 * Fonction qui formatte le contenu d'un fichier de course (formattage d'un point).
	 * 
	 * @param heure
	 * @param vitesse
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public String FormatPoint(String heure, String vitesse, String latitude, String longitude)
	{
		compteur++;
		return "<point value=\"" + compteur + "\"><heure>" + heure + "</heure><vitesse>" + vitesse + "</vitesse><latitude>" + latitude + "</latitude><longitude>" + longitude + "</longitude></point>\n";
	}
	
	/**
	 * Fonction qui écrit la chaine data dans le fichier.
	 * 
	 * @param data
	 */
	public void Write(String data)
	{
		try {
			osw.write(data);
			osw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction qui lit le contenu d'un fichier.
	 * 
	 * @return
	 */
	public String Read()
	{
		BufferedReader reader = new BufferedReader(isr);
		String str;
		StringBuilder buf = new StringBuilder();
		
		try {
			while((str = reader.readLine()) != null)
				buf.append(str + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
	
	/**
	 * Fonction qui parse un fichier course en vue d'effectuer un traitement.
	 * On remplit un objet de type course.
	 * 
	 * @param context
	 * @param nomFichier
	 * @return
	 */
	public Course ParserXMLCourse(Context context, String nomFichier)
	{
		File FichierCourse = new File(pathCourses, nomFichier);
		OuvertureInput(FichierCourse);
		
		XmlPullParserFactory factory;
		Point monPoint = null;
		Course maCourse = null;
		
		try
		{
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			xpp.setInput(new StringReader(Read()));
			
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_DOCUMENT){}
					//System.out.println("Start document");
				
				else if(eventType == XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("course")){
						maCourse = new Course();
						maCourse.setNomFichier(nomFichier);
						maCourse.setDateCourse(xpp.getAttributeValue(0));
					}
					
					else if(xpp.getName().equals("listePoints")) {}
					
					else if(xpp.getName().equals("description"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							maCourse.setDescription(xpp.getText());
					}

					else if(xpp.getName().equals("point"))
					{
						monPoint = new Point();
						monPoint.setValue(Integer.valueOf(xpp.getAttributeValue(0)));
					}
					
					else if(xpp.getName().equals("heure"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							monPoint.setHeure(xpp.getText());
					}

					else if(xpp.getName().equals("vitesse"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							monPoint.setVitesse(Float.valueOf(xpp.getText()));
					}

					else if(xpp.getName().equals("latitude"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							monPoint.setLatitude(Float.valueOf(xpp.getText()));
					}

					else if(xpp.getName().equals("longitude"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							monPoint.setLongitude(Float.valueOf(xpp.getText()));
					}
				}
				
				else if(eventType == XmlPullParser.END_TAG)
				{
					if(xpp.getName().equals("point"))
					{
						maCourse.getListePoints().add(monPoint);
					}
				}
				
				eventType = xpp.next();
			}
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//System.out.println("End document");
		//maCourse.AfficherListe();
		
		FermetureInput(context);
		
		return maCourse;
	}
	
	/**
	 * Fonction qui parse le fichier ListeFichier.xml en vue de récupérer la liste des courses (affichage dans la liste).
	 * 
	 * @param context
	 * @param niveau
	 * @param sousNiveau
	 */
	public void ParserXMLFichiers(Context context, ArrayList<String> niveau, ArrayList<ArrayList<String>> sousNiveau)
	{
		File FichierConfiguration = new File(pathConfiguration, "ListeCourses.xml");
		
		if(FichierConfiguration.exists())
		{
			OuvertureInput(FichierConfiguration);

			XmlPullParserFactory factory;

			try
			{
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				xpp.setInput(new StringReader(Read()));

				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					if(eventType == XmlPullParser.START_DOCUMENT){}
						//System.out.println("Start document");

					else if(eventType == XmlPullParser.START_TAG)
					{
						if(xpp.getName().equals("listeFichiers")){}

						else if(xpp.getName().equals("fichier"))
						{}

						else if(xpp.getName().equals("nom"))
						{
							eventType = xpp.next();
							if(eventType == XmlPullParser.TEXT)
							{
								sousNiveau.get(niveau.size() - 1).add(xpp.getText());
							}
						}

						else if(xpp.getName().equals("date"))
						{
							eventType = xpp.next();
							if(eventType == XmlPullParser.TEXT)
							{
								if(!niveau.contains(xpp.getText()))
								{
									niveau.add(xpp.getText());
									sousNiveau.add(new ArrayList<String>());
								}
							}
						}
					}

					else if(eventType == XmlPullParser.END_TAG)
					{
						if(xpp.getName().equals("fichier"))
						{
							;
						}
					}

					eventType = xpp.next();
				}
			}
			catch (XmlPullParserException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			//System.out.println("End document");

			FermetureInput(context);
		}
	}
	
	
	
	
	
	/**
     * Fonction qui formatte le contenu d'un fichier de parametres (formattage de parametres).
     * @param forceAerodynamique
     * @param forceFrottement
     * @param pousseeMotrice
     * @param poidsConducteur
     * @return
     */
    public String FormatParametres(String  forceAerodynamique , String forceFrottement,
    String pousseeMotrice, String poidsConducteur, String vitesseMax, String tempsMax, String distance, String couple)
    {
    
        return "<parametres>\n<forceAerodynamique>" + forceAerodynamique + "</forceAerodynamique>\n" +
                "<forceFrottement>" + forceFrottement + "</forceFrottement>\n"+ 
        		"<pousseeMotrice>" + pousseeMotrice + "</pousseeMotrice>\n" +
                "<poidsConducteur>" + poidsConducteur + "</poidsConducteur>\n" +
                "<vitesseMax>" + vitesseMax + "</vitesseMax>\n" +
                "<tempsMaximum>" + tempsMax + "</tempsMaximum>\n" +
                "<distance>" + distance + "</distance>\n" +
                "<coupleMoteur>" + couple + "</coupleMoteur>\n" +
                "</parametres>\n";
    }
	
	
    
	/**
	 * Fonction qui met à jour la fichier parametres.xml lors de la modification de ceux la.
	 * 
	 * @param context
	 * @param nomFichierConfig
	 * @param contenu
	 */
	public void MAJFichierParametres(Context context, String nomFichierConfig, String contenu)
	{
		//CREATION DU FICHIER CONFIGURATION
		File FichierConfiguration = new File(getPathConfiguration(), nomFichierConfig);
				
		OuvertureOutputTRUNC(FichierConfiguration);
		
		Write(contenu);
		FermetureOutput(context);
	}
	
	public Parametres ParserXMLParametres(Context context, String nomFichier)
	{
		Parametres parametres = null ;
		File fichierParametres = new File(pathConfiguration, nomFichier);
		
		/** Si le fichier n'existe pas, je le cré avec des valeurs par défaut */
		if (!fichierParametres.exists())
		{
			parametres = CreerParametresParDefaut(fichierParametres);
			return parametres ;
		}
		
		/** Sinon on le parcours pour récupérer les informations */
		
		OuvertureInput(fichierParametres);
		
		XmlPullParserFactory factory;
		
		
		try
		{
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			xpp.setInput(new StringReader(Read()));
			
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_DOCUMENT){}
				
				else if(eventType == XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("parametres")){
						parametres = new Parametres();
					}

					else if(xpp.getName().equals("forceAerodynamique"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setForceAerodynamique(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("forceAerodynamique"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setForceAerodynamique(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("forceFrottement"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setForceFrottement(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("pousseeMotrice"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setPousseeMotrice(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("poidsConducteur"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setPoidsConducteur(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("vitesseMax"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setVitesseMax(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("tempsMaximum"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setTempsMax(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("distance"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setDistanceCircuit(Double.parseDouble(xpp.getText()));
					}
					
					else if(xpp.getName().equals("coupleMoteur"))
					{
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT)
							parametres.setCoupleMoteur(Double.parseDouble(xpp.getText()));
					}
				}
				eventType = xpp.next();
			}
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		FermetureInput(context);
		
		return parametres ;
	}
	
	public Parametres CreerParametresParDefaut(File fichierParams)
	{
		ArrayList<String> parametres = new ArrayList<String>();
		parametres.add("0.0345");
		parametres.add("10");
		parametres.add("69");
		parametres.add("65");
		parametres.add("40");
		parametres.add("39");
		parametres.add("3.1");
		parametres.add("0.5");
		Parametres params = new Parametres(parametres);
		String lineToWrite = FormatParametres(parametres.get(0),parametres.get(1),parametres.get(2),
									parametres.get(3),parametres.get(4),parametres.get(5),
									parametres.get(6),parametres.get(7));
		
		OuvertureOutputTRUNC(fichierParams);
		
		Write(lineToWrite);
		
		try {
			osw.close();
			fOut.close();
		} catch (IOException e) {
			Log.e(LogTag, Class + "Fermeture Output");
		}
		
		return params ;
	}
	
	
}