package com.begrud.beatemup.levels;

import android.util.Log;

public class Level1_1 extends Level {

	private int []GRID_SOUNDS = {
			1, 2, 0, 0, 2,
			0, 0, 0, 0, 0,
			0, 0, 1, 1, 2,
			0, 2, 0, 0, 0,
			2, 1, 0, 1, 0
	};
	
	
	// Correct sequence
	private int []SEQUENCE = {
			1, 2, 1, 1, 2
	};
	
	// 1 - normal speed, 2 - double, etc.
	private int []SPEED = {
			1, 1, 2, 2, 1
	};
	
	public Level1_1() {
		super();
		init();
	}
	
	@Override
	public void init() {
		setGridSounds(GRID_SOUNDS);
		Log.d("Level1_1","Loaded grid sounds");
	}

}
