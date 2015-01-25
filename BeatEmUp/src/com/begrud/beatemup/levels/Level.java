package com.begrud.beatemup.levels;

import java.util.Arrays;

import android.util.Log;

public class Level {
	
	protected int []GRID_SOUNDS;
	protected int SEQUENCE_LENGHT;
	protected int []SEQUENCE;
	protected int []SPEED;  // 0 - normal speed, 1 - fast, 2 - slow (same as GameState portal speeds)
	
	private int []activeSequence;
	private int []activeSpeed;
	

	protected Level(int []gridSounds, int seqLen, int []seq, int []speed) {
		this.GRID_SOUNDS = gridSounds;
		this.SEQUENCE_LENGHT = seqLen;
		this.SEQUENCE = seq;
		this.SPEED = speed;
		activeSequence = new int[SEQUENCE_LENGHT];
		activeSpeed = new int[SEQUENCE_LENGHT];
		for( int i = 0 ; i < SEQUENCE_LENGHT ; i++ ) {
			activeSequence[i] = -1;
			activeSpeed[i] = -1;
		}
	}
	
	public void init() {
		// NOTE: manually call this in subclass constructor
		// set it up
	}

	public int[] getGridSounds() {
		return GRID_SOUNDS;
	}

	public void setGridSounds(int[] gridSounds) {
		this.GRID_SOUNDS = gridSounds;
	}
	
	// win logic
	// activeSequence has head at end (i.e. idx SEQUENCE_LENGHT-1) of array
	public boolean addBeat( int sound, int speed ) {
		for( int i = 1 ; i < SEQUENCE_LENGHT ; i++ ) {
			activeSequence[i-1] = activeSequence[i];
			activeSpeed[i-1] = activeSpeed[i];
		}
		activeSequence[SEQUENCE_LENGHT-1] = sound;
		activeSpeed[SEQUENCE_LENGHT-1] = speed;
		return sequenceIsComplete();
	}
	
	public boolean sequenceIsComplete() {
		return Arrays.equals(activeSequence, SEQUENCE) && Arrays.equals(activeSpeed, SPEED);
	}
}
