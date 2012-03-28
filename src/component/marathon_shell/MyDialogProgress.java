package component.marathon_shell;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.CalendarView;

public class MyDialogProgress extends ProgressDialog {

	private CalendarView cvCalendrier;

	public MyDialogProgress(Context context, CalendarView calendar) {
		super(context);
		cvCalendrier = calendar;
	}
	
	protected void onStop() {
		long today = cvCalendrier.getDate();
		cvCalendrier.setDate(0);
		cvCalendrier.setDate(today);
	}

}
