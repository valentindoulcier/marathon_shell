/**
 * 
 */
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
	 * Ces deux variables sont utilisées pour un affichage clair dans les Logs.
	 */
	private String LogTag = "Marathon Shell";
	private String Class = "XMLReadAndWrite - ";
	
	private int compteur;
	
	private String pathCourses;
	private String pathConfiguration;
	
	private FileInputStream fIn = null;
    private InputStreamReader isr = null;
    
    private FileOutputStream fOut = null;
    private OutputStreamWriter osw = null;
    
    private ArrayList<String> listeFichiersNonParses;
        
    
	/****************************
	 * CONSTRUCTEURS
	 ****************************/
	
	private XMLReadAndWrite()
	{
		listeFichiersNonParses = new ArrayList<String>();
	}
	
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
	
	public String FormatFichier(String nomFichier, String date)
	{
		return "<fichier><date>" + date + "</date><nom>" + nomFichier + "</nom></fichier>\n";
	}


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

	public void OuvertureOutput(File externalFilesDir) {
		try {
			fOut = new FileOutputStream(externalFilesDir, true);
			osw = new OutputStreamWriter(fOut);
			
	    	compteur = 0;
	    	
		} catch (FileNotFoundException e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Output with File");
		}
		
	}
	
	public void OuvertureOutputTRUNC(File externalFilesDir) {
		try {
			fOut = new FileOutputStream(externalFilesDir);
			osw = new OutputStreamWriter(fOut);
				    	
		} catch (FileNotFoundException e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Output with File");
		}
		
	}
	
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
	
	public void InsertionDebutFichierCourse(String date)
	{
		Write("<course value=\"" + date + "\">\n");
		Write("<listePoints>\n");
	}
	
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
	
	public String FormatPoint(String heure, String vitesse, String latitude, String longitude)
	{
		compteur++;
		return "<point value=\"" + compteur + "\"><heure>" + heure + "</heure><vitesse>" + vitesse + "</vitesse><latitude>" + latitude + "</latitude><longitude>" + longitude + "</longitude></point>\n";
	}
	
	public void Write(String data)
	{
		try {
			osw.write(data);
			osw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
					
					else if(xpp.getName().equals("description")) {}

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
						maCourse.ListePoints.add(monPoint);
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


	
	
}