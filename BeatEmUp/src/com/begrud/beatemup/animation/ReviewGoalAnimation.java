package com.begrud.beatemup.animation;

import com.begrud.beatemup.app.GameState;
import com.begrud.beatemup.app.GfxElementInfo;
import com.begrud.beatemup.app.SoundPlayer;
import com.begrud.beatemup.levels.Level;

public class ReviewGoalAnimation extends TimeDecayObject {

	private GameState gameState;
	private SoundPlayer soundPlayer;
	
	int []seq;
	int []speeds;
	int curIdx = 0;
	float curIdxTimeLeft;
	boolean running;
	
	public ReviewGoalAnimation(GameState gameState, SoundPlayer soundPlayer, Level level) {
		this.gameState = gameState;
		this.soundPlayer = soundPlayer;
		seq = level.getSEQUENCE();
		speeds = level.getGoalSpeeds();
		// calculate time
		float lifeTime = 0.f;
		for( int i = 0 ; i < seq.length ; i++ ) {
			lifeTime += getSpeedTime(i) * GameState.PARTICLE_SPEED_REVIEW; 
		}
		curIdxTimeLeft = getSpeedTime(curIdx) * GameState.PARTICLE_SPEED_REVIEW;
		setWaitTime(lifeTime);
		running = true;
		// trigger first note
		soundPlayer.play(seq[curIdx]);
	}
	
	public float getSpeedTime(int idx) {
		float speed = GfxElementInfo.SPEED_NORMAL;
		if( speeds[idx] == GameState.PORTAL_OUT_SPEED_FAST ) {
			speed = GfxElementInfo.SPEED_FAST;
		} else if( speeds[idx] == GameState.PORTAL_OUT_SPEED_SLOW ) {
			speed = GfxElementInfo.SPEED_SLOW;
		}
		return 1.f / speed;
	}
	
	@Override
	public boolean update( float deltaTime ) {
		boolean runOut = hasTimeRunOut(deltaTime);
		
		if( running ) {
			curIdxTimeLeft -= deltaTime;
			if( curIdxTimeLeft < 0.f ) {
				curIdx++;
				if( curIdx < seq.length ) {
					// trigger next note
					soundPlayer.play(seq[curIdx]);
					curIdxTimeLeft += getSpeedTime(curIdx) * GameState.PARTICLE_SPEED_REVIEW;
				} else {
					running = false;
				}
			}
		}
		
		if( !runOut ) {
        	return true;
        }
		// finished, do finish thing
		gameState.setState(GameState.STATE_STOPPED);
		gameState.getCurLevel().resetActiveSequenceAndSpeed();
        return false; //kill! return true to keep alive
	}

}
