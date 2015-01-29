package com.begrud.beatemup.animation;

import com.begrud.beatemup.app.GameState;

public class WinMessageOffAnimation extends TimeDecayObject {

	private float LIFE_TIME = 2.f;
	private GameState gameState;
	
	public WinMessageOffAnimation(GameState gameState) {
		this.gameState = gameState;
		setWaitTime(LIFE_TIME);
	}
	
	@Override
	public boolean update( float deltaTime ) {
		boolean runOut = hasTimeRunOut(deltaTime);
		
		if( !runOut ) {
        	return true;
        }
		// finished, do finish thing
		gameState.setState(GameState.STATE_STOPPED);
		gameState.getCurLevel().resetActiveSequenceAndSpeed();
        return false; //kill! return true to keep alive
	}

}
