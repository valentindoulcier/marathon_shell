/**
 * 
 */
package data.marathon_shell;

import java.util.ArrayList;

import android.util.Log;


/**
 * Classe Course
 * Elle contient les fonctions principales utilisées par l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see ScreenAnalyse
 *
 */
public class Course {
	
	/****************************
	 * VARIABLES
	 ****************************/
	
	/**
	 * Ces deux variables sont utilisées pour un affichage clair dans les Logs.
	 */
	private String LogTag = "Marathon Shell";
	private String Class = "COURSE - ";
	
	private ArrayList<Point> ListePoints;
	
	private String dateCourse;
	private String nomFichier;
	private String description;


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
	
	/**
	 * @return the listePoints
	 */
	public ArrayList<Point> getListePoints() {
		return ListePoints;
	}


	/**
	 * @param listePoints the listePoints to set
	 */
	public void setListePoints(ArrayList<Point> listePoints) {
		ListePoints = listePoints;
	}


	/**
	 * @return the nomFichier
	 */
	public String getNomFichier() {
		return nomFichier;
	}


	/**
	 * @param nomFichier the nomFichier to set
	 */
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	
	/**
	 * @return the dateCourse
	 */
	public String getDateCourse() {
		return dateCourse;
	}


	/**
	 * @param dateCourse the dateCourse to set
	 */
	public void setDateCourse(String dateCourse) {
		this.dateCourse = dateCourse;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/****************************
	 * METHODES
	 ****************************/
	
	/**
	 * Fonction Debug
	 */
	public void AfficherListe()
	{
		for (int i = 0; i < ListePoints.size(); i++)
		{
			System.out.println("Valeur : " + ListePoints.get(i).getValue() + " Heure : " + ListePoints.get(i).getHeure() + " Vitesse : " + ListePoints.get(i).getVitesse() + " Latitude : " + ListePoints.get(i).getLatitude() + " Longitude : " + ListePoints.get(i).getLongitude());
		}
	}
	
	/**
	 * Fonction qui calcule la moyenne de la course this.
	 * @return la vitesse
	 */
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
	
	/**
	 * Fonction qui calcule la distance parcourue
	 * @return la distance
	 */
	public double getDistance()
	{
		double distance = 0;
		
		for (int i = 0; i < ListePoints.size() -1; i++)
		{
			distance += Math.sqrt(Math.pow(ListePoints.get(i+1).getLatitude() - ListePoints.get(i).getLatitude(), 2.0) + Math.pow(ListePoints.get(i+1).getLongitude() - ListePoints.get(i).getLongitude(), 2.0));
		}
		Log.e(LogTag, Class + distance);
		return distance;
	}

}
