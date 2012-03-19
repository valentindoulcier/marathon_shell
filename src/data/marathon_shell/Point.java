/**
 * 
 */
package data.marathon_shell;

/**
 * Classe contenant les fonctions principales utilisées par l'application.
 * 
 * @author Valentin DOULCIER
 * @version 1.0
 * @see Application
 *
 */
public class Point {

	
	/****************************
	 * VARIABLES
	 ****************************/
	
	/**
	 * Ces deux variables sont utilisées pour un affichage clair dans les Logs.
	 */
	@SuppressWarnings("unused")
	private String LogTag = "Marathon Shell";
	@SuppressWarnings("unused")
	private String Class = "POINT - ";
	
	int value;
	String heure;
	float vitesse;
	double latitude;
	double longitude;
	
	
	/****************************
	 * CONSTRUCTEURS
	 ****************************/
	
	
	/****************************
	 * ACCESSEURS
	 ****************************/
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * @return the vitesse
	 */
	public float getVitesse() {
		return vitesse;
	}
	
	/**
	 * @param vitesse the vitesse to set
	 */
	public void setVitesse(float vitesse) {
		this.vitesse = vitesse;
	}
	
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @return the heure
	 */
	public String getHeure() {
		return heure;
	}
	
	/**
	 * @param heure the heure to set
	 */
	public void setHeure(String heure) {
		this.heure = heure;
	}
	
	
	/****************************
	 * METHODES
	 ****************************/
	
}
