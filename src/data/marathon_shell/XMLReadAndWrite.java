/**
 * 
 */
package data.marathon_shell;


import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.widget.Toast;

/**
 * Classe contenant les fonctions principales utilisées par l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see Application
 *
 */

public class XMLReadAndWrite{


	/****************************
	 * VARIABLES
	 ****************************/
	
	/**
	 * Ces deux variables sont utilisées pour un affichage clair dans les Logs.
	 */
	private String LogTag = "Marathon Shell";
	private String Class = "XMLReadAndWrite - ";
	
	private int compteur;
	
	private FileInputStream fIn = null;
    private InputStreamReader isr = null;
    
    private FileOutputStream fOut = null;
    private OutputStreamWriter osw = null;
    
    
	/****************************
	 * CONSTRUCTEURS
	 ****************************/
	
	public XMLReadAndWrite()
	{

	}
	
	
	/****************************
	 * ACCESSEURS
	 ****************************/
	
	
	
	
	/****************************
	 * METHODES
	 ****************************/

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
        	Log.i(LogTag, Class + "avant ecriture");
        	Write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        	Log.i(LogTag, Class + "apres ecriture");
        }
        catch (Exception e) {
        	Log.e(LogTag, Class + "Erreur Ouverture Output");
        	Toast.makeText(context, "Erreur Ouverture Output",Toast.LENGTH_SHORT).show();
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
	
	public void InsertionDebutFichier(String date)
	{
		Write("<course value=\"" + date + "\">\n");
		Write("<listePoints>\n");
	}
	
	public void InsertionFinFichier()
	{
		Write("</listePoints>\n");
		Write("<description>\n");
		
		Write("</description>\n");
		Write("</course>");
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
	
	public void ParserXML(Context context, String nomFichier)
	{
		OuvertureInput(context, nomFichier);
		
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
				if(eventType == XmlPullParser.START_DOCUMENT)
					System.out.println("Start document");
				
				else if(eventType == XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals("course")){ Log.w(LogTag, Class + " Course");
						maCourse = new Course();}
					
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
		
		System.out.println("End document");
		maCourse.AfficherListe();
		Log.e(LogTag, Class + "Nombre de points : " + maCourse.ListePoints.size());
		
		FermetureInput(context);
	}
	
}