/**
 * 
 */
package data.marathon_shell;

import java.util.ArrayList;


/**
 * Classe contenant les fonctions principales utilisées par l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see Application
 *
 */
public class Course {
	
	/****************************
	 * VARIABLES
	 ****************************/
	
	/**
	 * Ces deux variables sont utilisées pour un affichage clair dans les Logs.
	 */
	@SuppressWarnings("unused")
	private String LogTag = "Marathon Shell";
	@SuppressWarnings("unused")
	private String Class = "COURSE - ";
	
	ArrayList<Point> ListePoints = new ArrayList<Point>();
	
	/****************************
	 * CONSTRUCTEURS
	 ****************************/
	
	
	/****************************
	 * ACCESSEURS
	 ****************************/
	
	
	/****************************
	 * METHODES
	 ****************************/
	
	/*
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
	*/

}
