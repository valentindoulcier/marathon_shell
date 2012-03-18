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

	public void OuvertureInput(Context context)
	{
        try{
        	fIn = context.openFileInput("monFichierXML.xml");
        	isr = new InputStreamReader(fIn);
        }
        catch (Exception e) {
        	Log.e(LogTag, Class + "Ouverture Input");
        	Toast.makeText(context, "Ouverture Input",Toast.LENGTH_SHORT).show();
        }
	}
	
	public void OuvertureOutput(Context context)
	{
        try{
        	if(context.deleteFile("monFichierXML.xml"))
        		Log.i(LogTag, Class + "Delete OK");
        	else
        		Log.e(LogTag, Class + "Erreur Delete");
        	
        	fOut = context.openFileOutput("monFichierXML.xml", Context.MODE_APPEND); //MODE_APPEND //32768
        	osw = new OutputStreamWriter(fOut);
        	Write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        	//Write("<bonjour valentin=\"content\">dans sa chambre</bonjour>");
        }
        catch (Exception e) {
        	Log.e(LogTag, Class + "Ouverture Output");
        	Toast.makeText(context, "Ouverture Output",Toast.LENGTH_SHORT).show();
        }
	}
	
	public void FermetureInput(Context context)
	{
		try {
			isr.close();
			fIn.close();
		} catch (IOException e) {
			Log.e(LogTag, Class + "Fermeture Input");
			Toast.makeText(context, "Fermeture Input",Toast.LENGTH_SHORT).show();
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
	
	public String FormatPoint()
	{
		String result = "";
		
		return result;
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
	
}