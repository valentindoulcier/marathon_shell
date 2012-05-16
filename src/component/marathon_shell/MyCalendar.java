package component.marathon_shell;

import java.util.Calendar;

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
	 * 
	 * @author Houssam
	 * Cette Classe nous permet de dessiner une cellule avec les proprietes désirees
	 *
	 */
	public class Cell {
		
	    
		/**
		 * Pour pouvoir dessiner nos rectangles sur les canvas
		 */
		protected Rect mBound = null;
		protected int mDayOfMonth = 1;	// Jour du mois : de 1 a 31
		/**
		 * Objet Paint qui nous permettra de dessiner sur nos cellules 
		 */
		protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		
		int dx, dy;
		
		/**
		 * 1er constructeur de ma classe 
		 * @param dayOfMon
		 * @param rect
		 * @param textSize
		 * @param bold
		 */
		public Cell(int dayOfMon, Rect rect, float textSize, boolean bold) {
			mDayOfMonth = dayOfMon;
			mBound = rect;
			mPaint.setTextSize(textSize/*26f*/);
			mPaint.setColor(Color.BLACK);
			if(bold) mPaint.setFakeBoldText(true);
			/**
			 * On retourne la largeur du texte 
			 */
			dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
			/**
			 * On retourne l'espace libre en dessous et au dessus du texte
			 */
			dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
		}
		
		/**
		 *  2eme sonctructeur de ma classe
		 * @param dayOfMon
		 * @param rect
		 * @param textSize
		 */
		public Cell(int dayOfMon, Rect rect, float textSize) {
			this(dayOfMon, rect, textSize, false);
		}
		
		/**
		 * Classe qui me permet de dessiner un Canvas
		 * @param canvas
		 */
		protected void draw(Canvas canvas) {
			canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy, mPaint);
		}
		
		/**
		 * 
		 * @return int mDayOfMonth : Jour du mois 
		 */
		public int getDayOfMonth() {
			return mDayOfMonth;
		}
		
		/**
		 * Fonction qui teste si deux coordonnees font partie d'un rectangle ou pas .
		 * @param x
		 * @param y
		 * @return 
		 */
		public boolean hitTest(int x, int y) {
			return mBound.contains(x, y); 
		}
		
		/**
		 * Fonction qui retourne un rectangle
		 * @return 
		 */
		public Rect getBound() {
			return mBound;
		}
		
		/**
		 * Fonction qui sert a afficher un jour du mois dans une cellule
		 */
		public String toString() {
			return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
		}
		
	}

	

	/**
	 * Coordonnees qui serviront a definir la taille de mes cellules
	 */

    private static int WEEK_TOP_MARGIN = 74;
    private static int WEEK_LEFT_MARGIN = 40;
    private static int CELL_WIDTH = 58;
    private static int CELL_HEIGH = 53;
    private static int CELL_MARGIN_TOP = 92;
    private static int CELL_MARGIN_LEFT = 39;
    private static float CELL_TEXT_SIZE;

	
	private Calendar mRightNow = null;
    private Drawable mWeekTitle = null;
    private Cell mToday = null;
    private Cell[][] mCells = new Cell[6][7];
    private OnCellTouchListener mOnCellTouchListener = null;
    MonthDisplayHelper mHelper;
    Drawable mDecoration = null;
    
    
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
    */
	public MyCalendar(Context context) {
		this(context, null);
		
	}
	
	/**
	 * 2eme constructeur de ma classe
	 * @param context
	 * @param attrs
	 */
	public MyCalendar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 3eme constructeur de ma classe
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
		mRightNow = Calendar.getInstance();
		// Je prepare mes variables statics
		
		/**
		 * 
		 * Initialisations des coordonnees
		 * 
		 */
		Resources res = getResources();
		WEEK_TOP_MARGIN  = (int) res.getDimension(R.dimen.week_top_margin);
		WEEK_LEFT_MARGIN = (int) res.getDimension(R.dimen.week_left_margin);
		CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
		CELL_HEIGH = (int) res.getDimension(R.dimen.cell_heigh);
		CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
		CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);

		CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
		// Je Fixe mes images de BackGround
		
		setImageResource(R.drawable.back);
		mWeekTitle = res.getDrawable(R.drawable.jours);
		
		// J'initialise  mon mHelper avec l'annee et le mois en cours 
		mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR), mRightNow.get(Calendar.MONTH));

    }
	
	/**
	 * Fonction qui initialise les cellules qui utilise une classe membre _calendar
	 */
	private void initCells() {
	    class _calendar {
	    	public int day;
	    	public boolean thisMonth;
	    	public _calendar(int d, boolean b) {
	    		day = d;
	    		thisMonth = b;
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
		for(int week=0; week<mCells.length; week++) {
			for(int day=0; day<mCells[week].length; day++) {
				if(tmp[week][day].thisMonth) {
					if(day==0 || day==6 )
						mCells[week][day] = new RedCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
					else 
						mCells[week][day] = new Cell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				} else {
					mCells[week][day] = new GrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				}
				
				Bound.offset(CELL_WIDTH, 0); // passer a la colonne suivante
				
				/**
				 * Dessiner un contour en rouge sur le jour d'aujourd'hui
				 */
				if(tmp[week][day].day==thisDay && tmp[week][day].thisMonth) {
					mToday = mCells[week][day];
					mDecoration.setBounds(mToday.getBound());
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
    	mRightNow.setTimeInMillis(milliseconds);
    	initCells();
    	this.invalidate();
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
     * Fonction qui nous permet de savoir si on est au 1er jour du mois 
     * @param day
     * @return
     */
    public boolean firstDay(int day) {
    	return day==1;
    }
    
    /**
     * Fonction qui nous permet de savoir si on est au dernier jour du mois 
     * @param day
     * @return
     */
    public boolean lastDay(int day) {
    	return mHelper.getNumberOfDaysInMonth()==day;
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
    public Calendar getDate() {
    	return mRightNow;
    }
    
   /**
    * 
    */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(mOnCellTouchListener!=null){
	    	for(Cell[] week : mCells) {
				for(Cell day : week) {
					if(day.hitTest((int)event.getX(), (int)event.getY())) {
						mOnCellTouchListener.onTouch(day);
					}
				}
			}
    	}
    	return super.onTouchEvent(event);
    }
  
    /**
     * 
     * @param p
     */
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
				day.draw(canvas);			
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
	public class GrayCell extends Cell {
		public GrayCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			mPaint.setColor(Color.LTGRAY);
		}			
	}
	
	/**
	 * Cette Classe herite de Cell et qui nous permet d'ecrire en rouge dans nos cellules
	 * @author Houssam
	 *
	 */
	private class RedCell extends Cell {
		public RedCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			mPaint.setColor(0xdddd0000);
		}
	
		
	}

}
