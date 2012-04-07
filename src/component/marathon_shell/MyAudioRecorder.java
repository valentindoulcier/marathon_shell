/**
 * 
 */
package component.marathon_shell;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

/**
 * @author Valentin DOULCIER
 *
 */
public class MyAudioRecorder {

	/**
	 * On déclare une chaine LogTag à utiliser en tant que premier paramètre d'un Log.
	 */
	@SuppressWarnings("unused")
	private String LogTag = "Marathon Shell";
	/**
	 * On déclare une chaine Class à utiliser en tant que second paramètre d'un Log.
	 */
	@SuppressWarnings("unused")
	private String Class = "MyAudioRecorder - ";

	final MediaRecorder recorder = new MediaRecorder();
	final String path;

	/**
	 * Creates a new audio recording at the given path (relative to root of SD card).
	 */
	public MyAudioRecorder(String path) {
		this.path = FormattagePath(path);
	}

	private String FormattagePath(String path) {
		if (!path.startsWith("/")) {
			path = "/Download/Marathon_Shell/Courses/" + path;
		}
		if (!path.contains(".")) {
			path += ".mp3";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
	}

	/**
	 * Starts a new recording.
	 */
	public void start() throws IOException {
		String state = android.os.Environment.getExternalStorageState();
		if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
			throw new IOException("SD Card is not mounted.  It is " + state + ".");
		}

		// make sure the directory we plan to store the recording in exists
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created.");
		}

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}

	/**
	 * Stops a recording that has been previously started.
	 */
	public void stop() throws IOException {
		recorder.stop();
		recorder.release();
	}

}