package com.begrud.beatemup.animation;


import com.begrud.beatemup.app.GameState;


public class GridFlashAnimation extends TimeDecayObject {
	
	private float LIFE_TIME = 0.3f;
	private GameState gameState;
	// grid location
	private int x;
	private int y;

	public GridFlashAnimation(GameState gameState, int x, int y) {
		this.gameState = gameState;
		setWaitTime(LIFE_TIME);
		this.x = x;
		this.y = y;
	}

	public boolean update( float deltaTime ) {
		boolean runOut = hasTimeRunOut(deltaTime);
		
		if( !runOut ) {
        	return true;
        }
		// finished, do finish thing
		gameState.setGridState(x, y, GameState.GRID_STATE_DEFAULT);
        return false; //kill! return true to keep alive
	}
}
