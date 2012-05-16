package data.marathon_shell;

import java.util.ArrayList;

public class Parametres {

private double forceAerodynamique ;
private double forceFrottement;
private double pousseeMotrice;
private double poidsConducteur;
private double distanceCircuit;
private double tempsMax;
private double vitesseMax;
private double coupleMoteur ;


public Parametres() {
	super();
}

public Parametres(ArrayList<String> params)
{
	forceAerodynamique = Double.parseDouble(params.get(0));
	forceFrottement = Double.parseDouble(params.get(1));
	pousseeMotrice = Double.parseDouble(params.get(2));
	poidsConducteur = Double.parseDouble(params.get(3));
	vitesseMax = Double.parseDouble(params.get(4));
	tempsMax = Double.parseDouble(params.get(5));
	distanceCircuit = Double.parseDouble(params.get(6));
	coupleMoteur = Double.parseDouble(params.get(7));
}




/**
 * @return the forceAerodynamique
 */
public double getForceAerodynamique() {
	return forceAerodynamique;
}

/**
 * @param forceAerodynamique the forceAerodynamique to set
 */
public void setForceAerodynamique(double forceAerodynamique) {
	this.forceAerodynamique = forceAerodynamique;
}

/**
 * @return the forceFrottement
 */
public double getForceFrottement() {
	return forceFrottement;
}

/**
 * @param forceFrottement the forceFrottement to set
 */
public void setForceFrottement(double forceFrottement) {
	this.forceFrottement = forceFrottement;
}

/**
 * @return the pousseeMotrice
 */
public double getPousseeMotrice() {
	return pousseeMotrice;
}

/**
 * @param pousseeMotrice the pousseeMotrice to set
 */
public void setPousseeMotrice(double pousseeMotrice) {
	this.pousseeMotrice = pousseeMotrice;
}

/**
 * @return the distanceCircuit
 */
public double getDistanceCircuit() {
	return distanceCircuit;
}

/**
 * @param distanceCircuit the distanceCircuit to set
 */
public void setDistanceCircuit(double distanceCircuit) {
	this.distanceCircuit = distanceCircuit;
}

/**
 * @return the tempsMax
 */
public double getTempsMax() {
	return tempsMax;
}

/**
 * @param tempsMax the tempsMax to set
 */
public void setTempsMax(double tempsMax) {
	this.tempsMax = tempsMax;
}

/**
 * @return the vitesseMax
 */
public double getVitesseMax() {
	return vitesseMax;
}

/**
 * @param vitesseMax the vitesseMax to set
 */
public void setVitesseMax(double vitesseMax) {
	this.vitesseMax = vitesseMax;
}

/**
 * @return the poidsConducteur
 */
public double getPoidsConducteur() {
	return poidsConducteur;
}


/**
 * @param poidsConducteur the poidsConducteur to set
 */
public void setPoidsConducteur(double poidsConducteur) {
	this.poidsConducteur = poidsConducteur;
}


/**
 * @return the coupleMoteur
 */
public double getCoupleMoteur() {
	return coupleMoteur;
}


/**
 * @param coupleMoteur the coupleMoteur to set
 */
public void setCoupleMoteur(double coupleMoteur) {
	this.coupleMoteur = coupleMoteur;
}





}