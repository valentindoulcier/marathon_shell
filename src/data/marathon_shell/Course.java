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
	
	ArrayList<Point> ListePoints;
	
	/****************************
	 * CONSTRUCTEURS
	 ****************************/
	
	public Course()
	{
		ListePoints = new ArrayList<Point>();
	}
	
	
	/****************************
	 * ACCESSEURS
	 ****************************/
	
	
	/****************************
	 * METHODES
	 ****************************/
	
	public void AfficherListe()
	{
		for (int i = 0; i < ListePoints.size(); i++)
		{
			System.out.println("Valeur : " + ListePoints.get(i).getValue() + " Heure : " + ListePoints.get(i).getHeure() + " Vitesse : " + ListePoints.get(i).getVitesse() + " Latitude : " + ListePoints.get(i).getLatitude() + " Longitude : " + ListePoints.get(i).getLongitude());
		}
	}
	
	public float getMoyenne()
	{
		float moyenne = 0;
		
		for (int i = 0; i < ListePoints.size(); i++)
		{
			moyenne += ListePoints.get(i).getVitesse();
		}
		
		moyenne /= ListePoints.size();
		
		return moyenne;
	}
	
	public double getDistance()
	{
		double distance = 0;
		
		for (int i = 0; i < ListePoints.size(); i++)
		{
			distance += Math.sqrt(Math.pow(ListePoints.get(i).getLatitude() - ListePoints.get(i).getLatitude(), 2.0) + Math.pow(ListePoints.get(i).getLongitude() - ListePoints.get(i).getLongitude(), 2.0));
		}		
		return distance;
	}

}
