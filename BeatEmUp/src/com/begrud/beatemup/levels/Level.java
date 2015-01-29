package com.begrud.beatemup.levels;

import java.util.Arrays;

import android.util.Log;

public class Level {
	
	protected int []GRID_SOUNDS;
	protected int []GRID_MOD; //0 - none, 1 - up, 2 - right, 3 - down, 4 - left
	protected int []SEQUENCE;
	protected int []SPEED;  // 0 - normal speed, 1 - fast, 2 - slow (same as GameState portal speeds)

	private int []activeSequence;
	private int []activeSpeed;

	private boolean won = false;
	

	protected Level(int []gridSounds, int []gridMod, int []seq, int []speed) {
		this.GRID_SOUNDS = gridSounds;
		this.GRID_MOD = gridMod;
		this.SEQUENCE = seq;
		this.SPEED = speed;
		activeSequence = new int[SEQUENCE.length];
		activeSpeed = new int[SEQUENCE.length];
		resetActiveSequenceAndSpeed();
	}
	
	public boolean hasWon() {
		return won;
	}
	
	public void setWon() {
		won = true;
	}

	public int[] getGridSounds() {
		return GRID_SOUNDS;
	}
	
	public int[] getGridMod() {
		return GRID_MOD;
	}

	public void setGridSounds(int[] gridSounds) {
		this.GRID_SOUNDS = gridSounds;
	}
	
	public int[] getGoalSpeeds() {
		return SPEED;
	}
	
	public int[] getSEQUENCE() {
		return SEQUENCE;
	}
	
	// win logic
	// activeSequence has head at end (i.e. idx SEQUENCE_LENGHT-1) of array
	public boolean addBeat( int sound, int speed ) {
		for( int i = 1 ; i < SEQUENCE.length ; i++ ) {
			activeSequence[i-1] = activeSequence[i];
			activeSpeed[i-1] = activeSpeed[i];
		}
		activeSequence[SEQUENCE.length-1] = sound;
		activeSpeed[SEQUENCE.length-1] = speed;
		Log.d("Level","seq: "+Arrays.toString(activeSequence)+" == "+Arrays.toString(SEQUENCE));
		Log.d("Level","seq: "+Arrays.toString(activeSpeed)+" == "+Arrays.toString(SPEED));
		return sequenceIsComplete();
	}
	
	public boolean sequenceIsComplete() {
		return Arrays.equals(activeSequence, SEQUENCE) && Arrays.equals(activeSpeed, SPEED);
	}
	
	public int[] getActiveSequence() {
		return activeSequence;
	}
	
	public int[] getActiveSpeed() {
		return activeSpeed;
	}
	
	public void resetActiveSequenceAndSpeed() {
		for( int i = 0 ; i < SEQUENCE.length ; i++ ) {
			activeSequence[i] = -1;
			activeSpeed[i] = -1;
		}
	}
}
