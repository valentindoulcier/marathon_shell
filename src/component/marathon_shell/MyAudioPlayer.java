/**
 * 
 */
package component.marathon_shell;

import java.io.IOException;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

/**
 * @author Valentin
 *
 */
public class MyAudioPlayer {
	
	/**
	 * On d�clare une chaine LogTag � utiliser en tant que premier param�tre d'un Log.
	 */
	@SuppressWarnings("unused")
	private String LogTag = "Marathon Shell";
	/**
	 * On d�clare une chaine Class � utiliser en tant que second param�tre d'un Log.
	 */
	@SuppressWarnings("unused")
	private String Class = "MyAudioPlayer - ";
	
    private MediaPlayer mediaPlayer;
    
    /**
	 * @return the mediaPlayer
	 */
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}


	/**
	 * @param mediaPlayer the mediaPlayer to set
	 */
	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

    
    public MyAudioPlayer(String path) {
    	mediaPlayer = new MediaPlayer();
    	try {
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
	    	mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

    // This is event handler thumb moving event
    public void seekChange(View v){
    	if(mediaPlayer.isPlaying()){
	    	SeekBar sb = (SeekBar)v;
			mediaPlayer.seekTo(sb.getProgress());
		}
    }


}
