package component.marathon_shell;

import java.util.ArrayList;
import java.util.Collections;

import com.marathon_shell.R;


import data.marathon_shell.Point;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;




public class MyGraphique extends View {

	/** La liste des points de la course */
	private ArrayList<Point> points ;
	/** origine réelle du graphique suivant l'axe des absisses */
	private int origineX ;
	/** origine réelle du graphique suivant l'axe des ordonnées */
	private int origineY ;
	/** taille de base du graphique */
	private int taille ;
	/** Point pour lequel la vitesse est maximum sur la course */
	private Point pointVitesseMax ;
	/** Echelle pour l'axe des absisses */
	private float scaleX ;
	/** Echelle pour l'axe des ordonnées */
	private float scaleY ;
	/** Zoom de base, on affiche 1/12 des points et toute la durée de la course 
	* 	Zoom x2 : 1/6 des points et la moitié de la course
	*   Zoom x4 : 1/3 des points et un quart de la course
	*   Zoom x8 : tous les points et un huitième de la course
	*/ 
	private int zoomLevel ;
	/** Pour l'axe des absisses : correspond au nombre de point à dessiner */
	private int nbPointsDessine ; 
	/** Nombre de points dans le tableau */
	private int nbPoints ;

	
	/**
	 * @return the zoomLevel
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * @param zoomLevel the zoomLevel to set
	 */
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	/**
	 * @return the nbPointsDessine
	 */
	public int getnbPointsDessine() {
		return nbPointsDessine;
	}

	/**
	 * @param nbPointsDessine the nbPointsDessine to set
	 */
	public void setnbPointsDessine(int nbPointsDessine) {
		this.nbPointsDessine = nbPointsDessine;
	}


	public MyGraphique(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		/** On se décale un peu du bord du Canvas pour pouvoir afficher les libellés des axes 
		 * sachant que pour l'origineY, on la change car par défaut dans un 
		 * canvas, l'origine est en haut à gauche et nous on la veut en bas à gauche */
		origineX = 40 ;
		origineY = context.getResources().getInteger(R.integer.graphHeight) - 40 ;
        /** Par défaut, le zoom normal (x1) et la taille de base du graphique est de 1000 */
        zoomLevel = 1 ;
        taille = context.getResources().getInteger(R.integer.graphWidth);
    	
        points = null ;
        pointVitesseMax = null ;
        nbPointsDessine = 0 ;
	}

	/**
	 * @return the points
	 */
	public ArrayList<Point> getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(ArrayList<Point> points) {
		this.points = points ;
	}

	/**
	 * @return the taille
	 */
	public int getTaille() {
		return taille;
	}

	/**
	 * @param taille the taille to set
	 */
	public void setTaille(int taille) {
		this.taille = taille;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawColor(Color.WHITE);
    	Paint paint = new Paint();
    	
    	if (points == null)
    		return ;
    	
    	int i = 0 ;
    	int indicePointAdessine = 0 ;
    	
    	nbPoints = points.size();
    	
    	/** On dessine les axes en noir */
		paint.setColor(Color.BLACK);
		canvas.drawLine(origineX, origineY, origineX, 20, paint);
		canvas.drawLine(origineX, origineY, getWidth() - origineX, origineY, paint);

		/** Pour les petites flèches au bout des axes ... */
		Path path = new Path();
		path.moveTo(0, 5);
		path.lineTo(5, 0);
		path.lineTo(10, 5);
		path.close();
		path.offset(origineX-5, 15);
		canvas.drawPath(path, paint);
		path.reset();

		path.moveTo(0, 0);
		path.lineTo(5, 5);
		path.lineTo(0, 10);
		path.close();
		path.offset(getWidth() - origineX, origineY-5);
		canvas.drawPath(path, paint);
		
		/** Les libellés des axes ... */
		canvas.drawText("V", 30, 15, paint);
		canvas.drawText("t", getWidth() - 30, getHeight() - 30, paint);
		
		/** Echelle pour l'axe des ordonnées (vitesse), le +10 étant une marge pour l'affichage */
		scaleY = origineY / (140 + 10) ; 
		
		/** On dessine des repères de vitesse pour les points tous les 10km/h */ 
		i = 0 ;
		paint.setTextAlign(Paint.Align.CENTER);
		for (i = 10 ; i <= 140 ; i = i + 10)
		{
			paint.setColor(Color.LTGRAY);
			canvas.drawLine(origineX, origineY - i*scaleY,
					getWidth() - origineX, origineY - i*scaleY, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText(Integer.toString(i), 25, origineY - i*scaleY, paint);
		}
		
    	if (nbPoints != 0)
    	{
    		nbPointsDessine = nbPoints*zoomLevel / 16 ;
    		pointVitesseMax = Collections.max(points);
    		/** Echelle pour l'axe des ordonnées (vitesse), le +10 étant une marge pour l'affichage */
    		scaleY = origineY / (pointVitesseMax.getVitesse() + 10) ; 

    		/** Echelle sur l'axe des absisses (temps), le +1 étant une marge pour l'affichage */
    		scaleX = (getWidth()-origineX)/(nbPointsDessine + 1); 

    		/** Du Debug à virer ... */
    		//System.out.printf("nbPoints : %d \n",nbPoints);
    		//System.out.printf("Zoom : %d \n",zoomLevel);
    		//System.out.printf("largeur : %d \n",getWidth());
    		//System.out.printf("largeur théorique : %d \n",taille*zoomLevel);
    		//System.out.printf("Nombre de points dessinés : %d \n",nbPointsDessine);
    		//System.out.printf("vitesseMax : %f \n",pointVitesseMax.getVitesse());
    		//System.out.printf("ScaleX : %f \n",scaleX);
    		//System.out.printf("OrigineY : %d \n",origineY);

    		
    		/** On dessine des repères de vitesse pour les points tous les 10km/h */ 
    		/*
    		i = 0 ;
    		paint.setTextAlign(Paint.Align.CENTER);
    		for (i = 10 ; i <= pointVitesseMax.getVitesse() ; i = i + 10)
    		{
    			paint.setColor(Color.LTGRAY);
    			canvas.drawLine(origineX, origineY - i*scaleY,
    					getWidth() - origineX, origineY - i*scaleY, paint);
    			paint.setColor(Color.BLACK);
    			canvas.drawText(Integer.toString(i), 25, origineY - i*scaleY, paint);
    		}*/

    		/** On dessine des repères de temps pour les points */ 
    		i = 0 ;
    		canvas.drawText(points.get(i).getHeure(), 40, getHeight()-20, paint);
    		paint.setTextAlign(Paint.Align.CENTER);
    		for (i = nbPointsDessine/4 ; i < nbPointsDessine ; i=i+nbPointsDessine/4)
    		{
    			paint.setColor(Color.LTGRAY);
    			canvas.drawLine(40+i*scaleX, origineY,
    					40+i*scaleX, 20, paint);
    			paint.setColor(Color.BLACK);
    			canvas.drawText(points.get(i).getHeure(), 40+i*scaleX, getHeight()-20, paint);
    		}

    		indicePointAdessine = 0 ;
    		/** On dessine chacun des points */
    		paint.setColor(Color.RED);
    		for (i = 0 ; i < nbPointsDessine ; i++)
    		{
    			canvas.drawCircle(scaleX*i + origineX , origineY - (points.get(indicePointAdessine).getVitesse()*scaleY), 2, paint);
    			/** on relie les points entre eux i.e. je test si i!=0 car je relie un point avec son
    			 * prédécesseur donc pour le premier point, il n'y en a pas */
    			if (indicePointAdessine != 0)
    				canvas.drawLine(scaleX*i + origineX, origineY - (points.get(indicePointAdessine).getVitesse()*scaleY), 
    						scaleX*(i-1) + origineX, origineY - (points.get(indicePointAdessine-(16/zoomLevel)).getVitesse()*scaleY), paint);

    			indicePointAdessine = indicePointAdessine + (16/zoomLevel);

    		}

    		super.onDraw(canvas);
    	}
	}
}

