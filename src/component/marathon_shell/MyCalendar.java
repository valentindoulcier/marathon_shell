package component.marathon_shell;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.marathon_shell.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Classe qui represente notre calendrier et qui herite de ImageView
 * @author Houssam
 *
 */
public class MyCalendar extends ImageView {
	
	
	/**
	 * Coordonnees qui serviront a definir la taille de mes cellules
	 */
	Resources res = getResources();
	
    private static int WEEK_TOP_MARGIN = 74;
    private static int WEEK_LEFT_MARGIN = 40;
    private static int CELL_WIDTH = 58;
    private static int CELL_HEIGH = 53;
    private static int CELL_MARGIN_TOP = 92;
    private static int CELL_MARGIN_LEFT = 39;
    private static float CELL_TEXT_SIZE;

	
	private Calendar myCalendar = null;
    private Drawable mWeekTitle = null;
    private Cell mToday = null;
    private Cell[][] mCells = new Cell[6][7];
    
    private OnCellTouchListener mOnCellTouchListener = null;
    
    MonthDisplayHelper mHelper;
    
    Drawable mDecoration = null;
    

    // Cette Classe nous permet de dessiner une cellule avec les proprietes désirées
	public class Cell {

		// Pour dessiner nos rectangles sur les canvas.
		public Rect mBound;
		
		// Jour du mois : de 1 à 31
		public int jour = 1;
		
		// Objet Paint qui nous permettra de dessiner sur nos cellules
		protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		
		int dx;
		
		int dy;
		
		/**
		 * 1er constructeur de ma classe 
		 * @param dayOfMon
		 * @param rect
		 * @param textSize
		 * @param bold
		 */
		public Cell(int monJour, Rect rect, float textSize, boolean bold) {
			
			jour = monJour;
			mBound = rect;
			mPaint.setTextSize(textSize);	//26f
			mPaint.setColor(Color.BLACK);
			if(bold) mPaint.setFakeBoldText(true);

			// On retourne la largeur du texte 
			dx = (int) mPaint.measureText(String.valueOf(jour)) / 2;

			// On retourne l'espace libre en dessous et au dessus du texte
			dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
		}

		public void onTouch(Cell cell) {
			// TODO Auto-generated method stub
			
		}
	}

	

	
    
    
   /**
    * Interface qui comprend une methode qui servira de listener pour les cellules  
    * @author Houssam
    *
    */
	public interface OnCellTouchListener {
    	public void onTouch(Cell cell);
    }

	

	/**
	 * 1er constructeur de ma classe
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyCalendar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDecoration = context.getResources().getDrawable(R.drawable.typeb_calendar_today);		
		initCalendarView();
	}
	
	/**
	 * Fonction qui me permet d'initialiser mon Calendrier
	 */
	private void initCalendarView() {
		
		myCalendar = Calendar.getInstance();
		
		WEEK_TOP_MARGIN  = (int) res.getDimension(R.dimen.week_top_margin);
		WEEK_LEFT_MARGIN = (int) res.getDimension(R.dimen.week_left_margin);
		CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
		CELL_HEIGH = (int) res.getDimension(R.dimen.cell_heigh);
		CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
		CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);
		CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
				
		setImageResource(R.drawable.back);
		mWeekTitle = res.getDrawable(R.drawable.jours);
		
		// J'initialise  mon mHelper avec l'annee et le mois en cours 
		mHelper = new MonthDisplayHelper(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), 2);

    }
	
	/**
	 * Fonction qui initialise les cellules qui utilise une classe membre _calendar
	 */
	private void initCells()
	{
	    class _calendar {
	    	public int day;
	    	public boolean thisMonth;
	    	public _calendar(int jour, boolean month) {
	    		day = jour;
	    		thisMonth = month;
	    	}
	    	public _calendar(int d) {
	    		this(d, false);
	    	}
	    };
	    
	    _calendar tmp[][] = new _calendar[6][7];
	    
	    for(int i=0; i<tmp.length; i++) {
	    	int n[] = mHelper.getDigitsForRow(i);
	    	for(int d=0; d<n.length; d++) {
	    		if(mHelper.isWithinCurrentMonth(i,d))
	    			tmp[i][d] = new _calendar(n[d], true);
	    		else
	    			tmp[i][d] = new _calendar(n[d]);
	    	}
	    }
	    
        /**
         * On récupère la date courante a l'aide de la methode getInstance() de la classe Calendar
         */
	    Calendar today = Calendar.getInstance();
	    int thisDay = 0;
	    mToday = null;
	    
	    if(mHelper.getYear()==today.get(Calendar.YEAR) && mHelper.getMonth()==today.get(Calendar.MONTH)) {
	    	thisDay = today.get(Calendar.DAY_OF_MONTH);
	    }
	    
		// Construction de nos cellules
		Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH+CELL_MARGIN_LEFT, CELL_HEIGH+CELL_MARGIN_TOP);
		for(int week=0; week<mCells.length; week++)
		{
			for(int day=0; day<mCells[week].length; day++)
			{
				if(tmp[week][day].thisMonth) {
					if(day==5 || day==6 )
						mCells[week][day] = new RedCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
					else 
						mCells[week][day] = new Cell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE, false);
				} else {
					mCells[week][day] = new GrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				}
				
				Bound.offset(CELL_WIDTH, 0); // passer a la colonne suivante
				
				/**
				 * Dessiner un contour en rouge sur le jour d'aujourd'hui
				 */
				if(tmp[week][day].day==thisDay && tmp[week][day].thisMonth) {
					mToday = mCells[week][day];
					mDecoration.setBounds(mToday.mBound);
				}
			}
			Bound.offset(0, CELL_HEIGH); // passer à la 1ere colonne de la ligne suivante 
			Bound.left = CELL_MARGIN_LEFT;
			Bound.right = CELL_MARGIN_LEFT+CELL_WIDTH;
		}		
	}
	
	/**
	 * 
	 */
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		//Rect re = getDrawable().getBounds();
		//WEEK_LEFT_MARGIN = CELL_MARGIN_LEFT = (right-left - re.width()) / 2;
		mWeekTitle.setBounds(WEEK_LEFT_MARGIN, WEEK_TOP_MARGIN, WEEK_LEFT_MARGIN+mWeekTitle.getMinimumWidth(), WEEK_TOP_MARGIN+mWeekTitle.getMinimumHeight());
		initCells();
		super.onLayout(changed, left, top, right, bottom);
	}
	
	/**
	 * 
	 * @param milliseconds
	 */
    public void setTimeInMillis(long milliseconds) {
    	myCalendar.setTimeInMillis(milliseconds);
    	initCells();
    	invalidate();
    }
    


    
    /**
     *  Fonction qui permet d'initialiser une cellule avec l'annee et le mois present 
     */
    public void goToday() {
    	Calendar cal = Calendar.getInstance();
    	mHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    	initCells();
    	invalidate();
    }
    
    /**
     * Fontion qui permet de retourner la date courante
     * @return
     */
    public String getDate() {
    	SimpleDateFormat maDate = new SimpleDateFormat("dd-MM-yyyy");
    	Date myDate = myCalendar.getTime();
    	return maDate.format(myDate);
    }
    
   /**
    * 
    */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(mOnCellTouchListener!=null){
	    	for(Cell[] week : mCells) {
				for(Cell day : week) {
					if(day.mBound.contains((int)event.getX(), (int)event.getY())) {
						mOnCellTouchListener.onTouch(day);
					}
				}
			}
    	}
    	return super.onTouchEvent(event);
    }
  

    public void setOnCellTouchListener(OnCellTouchListener p) {
		mOnCellTouchListener = p;
	}

    
    /**
     * Fonction qui permet de dessiner un Canvas
     */
	@Override
	protected void onDraw(Canvas canvas) {
		// dessiner le backGround
		super.onDraw(canvas);
		mWeekTitle.draw(canvas);
		
		// dessiner les cellules
		for(Cell[] week : mCells) {
			for(Cell day : week) {
				canvas.drawText(String.valueOf(day.jour), day.mBound.centerX() - day.dx, day.mBound.centerY() + day.dy, day.mPaint);
			}
		}
		
		
		// Dessiner le jour recent
		if(mDecoration!=null && mToday!=null) {
			mDecoration.draw(canvas);
		}
	}
	
	/**
	 * Cette Classe herite de Cell et qui nous permet d'ecrire en gris dans nos cellules
	 * @author Houssam
	 *
	 */
	public class GrayCell extends Cell
	{
		public GrayCell(int dayOfMon, Rect rect, float s)
		{
			super(dayOfMon, rect, s, false);
			mPaint.setColor(Color.LTGRAY);
		}			
	}
	
	
	/**
	 * Cette Classe herite de Cell et qui nous permet d'ecrire en rouge dans nos cellules
	 * @author Houssam
	 *
	 */
	private class RedCell extends Cell
	{
		public RedCell(int dayOfMon, Rect rect, float s)
		{
			super(dayOfMon, rect, s, false);
			mPaint.setColor(Color.RED);
		}
	}
	
    
    /**
     * Fonction qui nous permet de passer au mois suivant 
     */
    public void nextMonth() {
    	mHelper.nextMonth();
    	initCells();
    	invalidate();
    }
    
    /**
     * Fonction qui nous permet de passer au mois precedent 
     */
    public void previousMonth() {
    	mHelper.previousMonth();
    	initCells();
    	invalidate();
    }
    
    
	
	

    
    /**
     * 
     * @return
     */
    public int getYear() {
    	return mHelper.getYear();
    }
    
    /**
     * 
     * @return
     */
    public int getMonth() {
    	return mHelper.getMonth();
    }

}



/*
    public boolean firstDay(int day) {
    	return day==1;
    }
    
    
    public boolean lastDay(int day) {
    	return mHelper.getNumberOfDaysInMonth()==day;
    }
*/
