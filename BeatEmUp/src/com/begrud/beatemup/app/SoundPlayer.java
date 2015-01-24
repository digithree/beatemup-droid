package com.begrud.beatemup.app;

import com.begrud.beatemup.R;

import android.content.Context;
import android.media.MediaPlayer;


public class SoundPlayer {
	
	private MediaPlayer mediaPlayer = null;
	
	private Context context;
	private GameState gameState;
	
	public SoundPlayer(Context context, GameState gameState) {
		this.context = context;
		this.gameState = gameState;
	}
	
	public void play(int idx) {
		if( mediaPlayer != null ) {
			// TODO : better way to stop with causing abrupt stop clipping?
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
		int resId = -1;
		if( idx >= 0 && idx < 10 ) {
			if( idx == GameState.GRID_SOUND_1 ) {
				resId = R.raw.clap1;
			} else if( idx == GameState.GRID_SOUND_2 ) {
				resId = R.raw.snare1;
			}
		}
    	if( resId >= 0 ) {
    		mediaPlayer = MediaPlayer.create(context, resId);
    		mediaPlayer.start();
    	}
    }
}
