package component.marathon_shell;

import data.marathon_shell.XMLReadAndWrite;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.CalendarView;


/**
 * Classe MyDialogProgress
 * Cette classe redéfinie l'objet Liste. La fonction est exécutée à la fin du thread initial et permet l'affichage.
 * 
 * @author  Valentin DOULCIER
 * @version 1.0
 * @see     XMLReadAndWrite
 */
@SuppressWarnings("unused")
public class MyDialogProgress extends ProgressDialog {

	private MyCalendar cvCalendrier;

	public MyDialogProgress(Context context, MyCalendar calendar) {
		super(context);
		cvCalendrier = calendar;
	}
	
	protected void onStop() {
		long today = cvCalendrier.getDate().getTimeInMillis();
		//cvCalendrier.setDate(0);
		//cvCalendrier.setDate(today);
	}

}
