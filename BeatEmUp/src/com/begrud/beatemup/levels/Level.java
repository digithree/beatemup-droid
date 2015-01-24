package com.begrud.beatemup.levels;

import com.begrud.beatemup.app.GameState;

public class Level {
	
	private int []gridSounds = new int[GameState.GRID_SIZE_ONE_D];

	public Level() {
		init();
	}
	
	public void init() {
		// set it up
	}

	public int[] getGridSounds() {
		return gridSounds;
	}

	public void setGridSounds(int[] gridSounds) {
		this.gridSounds = gridSounds;
	}
}
