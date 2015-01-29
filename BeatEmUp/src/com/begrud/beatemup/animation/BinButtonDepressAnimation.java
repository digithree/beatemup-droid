package com.begrud.beatemup.animation;

import com.begrud.beatemup.app.GameState;

public class BinButtonDepressAnimation extends TimeDecayObject {

	private float LIFE_TIME = 0.3f;
	private GameState gameState;

	public BinButtonDepressAnimation(GameState gameState) {
		this.gameState = gameState;
		setWaitTime(LIFE_TIME);
	}

	public boolean update( float deltaTime ) {
		boolean runOut = hasTimeRunOut(deltaTime);
		
		if( !runOut ) {
        	return true;
        }
		// finished, do finish thing
		gameState.setBinButtonState(GameState.BUTTON_UNPRESSED);
        return false; //kill! return true to keep alive
	}

}
