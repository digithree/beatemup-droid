package com.begrud.beatemup.animation;

import com.begrud.beatemup.app.GameState;

public class WinMessageOffAnimation extends TimeDecayObject {

	private float LIFE_TIME = 0.3f;
	private GameState gameState;
	
	public WinMessageOffAnimation(GameState gameState) {
		this.gameState = gameState;
	}

	public WinMessageOffAnimation(float _waitTime) {
		super(_waitTime);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean update( float deltaTime ) {
		boolean runOut = hasTimeRunOut(deltaTime);
		
		if( !runOut ) {
        	return true;
        }
		// finished, do finish thing
		gameState.setState(GameState.STATE_STOPPED);
        return false; //kill! return true to keep alive
	}

}
