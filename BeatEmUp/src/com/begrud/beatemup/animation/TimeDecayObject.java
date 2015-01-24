package com.begrud.beatemup.animation;

public abstract class TimeDecayObject {
	float waitTime;
	
	public TimeDecayObject() {
		waitTime = 0.f;
	}
	
	public TimeDecayObject( float _waitTime) {
		waitTime = _waitTime;
	}
	
	public boolean update( float deltaTime ) {
		return false; //kill! return true to keep alive
	}
	
	protected void setWaitTime( float _waitTime ) {
        waitTime = _waitTime;
    }
    
    protected float getWaitTime() { return waitTime; }
    
    protected boolean hasTimeRunOut( float deltaTime ) {
        waitTime -= deltaTime;
        if( waitTime > 0 )
            return false;
        return true;
    }
}
