package com.begrud.beatemup.app;

import java.util.ArrayList;
import java.util.List;

import com.begrud.beatemup.animation.GridFlashAnimation;
import com.begrud.beatemup.animation.TimeDecayObject;
import com.begrud.beatemup.levels.Level1_1;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;


public class GameLogicThread extends Thread {
	
	public static final int COORDS_GRID = 0;
	public static final int COORDS_LEFT_BAR = 1;
	public static final int COORDS_RIGHT_BAR = 2;
	
	private final Context context;
	private final GameState gameState;
	
	private boolean running = false;
	
	GameLogicThread(Context context, GameState gameState) {
		this.gameState = gameState;
		this.context = context;
		init();
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		while(running) {
			long curTime = System.currentTimeMillis();
			double elapsedTime = ((double)(curTime - lastTime))/1000;
			process(elapsedTime);
			lastTime = curTime;
		}
	}
	
	// ------ LOGIC
	
	// bounds
	// --grid
	private final float triggerArea = 0.01f;
	private RectF []gridActiveAreaBounds;
	// --left nav
	private RectF []leftNavButtonsBounds;
	
	private Object syncLock = new Object();
	
	private SoundPlayer soundPlayer;
	
	private List<TimeDecayObject> animationEventsList = new ArrayList<TimeDecayObject>();
	
	private float insideGridPortalWarpPadding = 0.1f;
	
	
	private void init() {
		soundPlayer = new SoundPlayer(context, gameState);
		// create bounds
		// grid
		gridActiveAreaBounds = new RectF[GameState.GRID_SIZE_ONE_D];
		int idx = 0;
		for( int j = 0 ; j < 5 ; j++ ) {
			for( int k = 0 ; k < 5 ; k++ ) {
				float x = 0.1f + ((float)k*0.2f);
				float y = 0.1f + ((float)j*0.2f);
				gridActiveAreaBounds[idx++] = new RectF(
						x - triggerArea, y - triggerArea,
						x + triggerArea, y + triggerArea
						);
			}
		}
		// left nav buttons
		leftNavButtonsBounds = new RectF[GfxElementInfo.leftNavButtonNum];
		float buttonHalfSize = GfxElementInfo.leftNavButtonSizeTouch/2.f;
		for( int i = 0 ; i < GfxElementInfo.leftNavButtonNum ; i++ ) {
			leftNavButtonsBounds[i] = new RectF(
					GfxElementInfo.leftNavButtonPositions[i].x - buttonHalfSize,
					GfxElementInfo.leftNavButtonPositions[i].y - buttonHalfSize,
					GfxElementInfo.leftNavButtonPositions[i].x + buttonHalfSize,
					GfxElementInfo.leftNavButtonPositions[i].y + buttonHalfSize
					);
		}
		// Load first level
		gameState.loadLevel(new Level1_1());
	}
	
	// coordinate is normalised and adjusted for window, not screen size
	public void touch(PointF pos, int state) {
		synchronized(syncLock) {
			Log.d("GameLogicThread", "touch: "+pos.x+", "+pos.y);
			if( state == COORDS_GRID ) {
				// grid
				int x = (int)(pos.x * 5);
				int y = (int)(pos.y * 5);
				Log.d("GameLogicThread", "touch grix: "+x+", "+y);
				/*
				if( gameState.getGridSound(x, y) == GameState.GRID_SOUND_NONE) {
					gameState.setGridSound(x, y, GameState.GRID_SOUND_1);
				} else {
					gameState.setGridSound(x, y, GameState.GRID_SOUND_NONE);
				}
				*/
				// condition on portal state
				if( gameState.getPortalState() == GameState.PORTAL_STATE_IN ) {
					Log.d("GameLogicThread", "portal in SET...");
					gameState.setPortalInGrid(new Point(x,y));
					gameState.setPortalState(GameState.PORTAL_STATE_OUT_NORMAL); // TODO : set this to last portal out state
					Log.d("GameLogicThread", "portal out normal START");
				} else if( gameState.getPortalState() == GameState.PORTAL_STATE_OUT_NORMAL ) {
					Log.d("GameLogicThread", "portal out normal SET...");
					Point portalIn = gameState.getPortalInGrid();
					if( portalIn.x != x && portalIn.y != y ) {
						gameState.setPortalOutGrid(new Point(x,y));
						// TODO : make portal!
						if( gameState.createPortal() ) {
							Log.d("GameLogicThread", "portal out normal SET!");
							gameState.setPortalState(GameState.PORTAL_STATE_NONE);
						}
					} else {
						// failed, leave portal building states
						gameState.setPortalState(GameState.PORTAL_STATE_NONE);
						gameState.setPortalInGrid(null);
						Log.d("GameLogicThread", "portal out normal QUIT");
					}
				} // TODO : add for other portal out states
			} else if( state == COORDS_LEFT_BAR ) {
				// left bar				
				if( leftNavButtonsBounds[GameState.BUTTON_PLAY_STOP].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: Play / stop");
					if( gameState.getState() == GameState.STATE_STOPPED ) {
						gameState.setState(GameState.STATE_PLAYING);
						// start particle
						gameState.setParticleState(0, GameState.PARTICLE_STATE_ON);
						gameState.setParticleVec(0, new PointF(1.f, 0.f));
					} else if( gameState.getState() == GameState.STATE_PLAYING ) {
						gameState.setState(GameState.STATE_STOPPED);
						// stop particle
						gameState.setParticleState(0, GameState.PARTICLE_STATE_OFF);
						gameState.setParticlePos(0,
								new PointF(GameState.PARTICLE_POSITION_DEFAULT.x,
										GameState.PARTICLE_POSITION_DEFAULT.y));
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_PORTAL].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: portal in?");
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						if( gameState.getPortalState() == GameState.PORTAL_STATE_NONE ) {
							gameState.setPortalState(GameState.PORTAL_STATE_IN);
							Log.d("touch","left nav: portal in START");
						}
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_NORMAL].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: portal out normal");
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						// not important now
					}
				}
			}
		}
	}
	
	private void process(double time) {
		synchronized(syncLock) {
			// update animation events
			for( int i = 0 ; i < animationEventsList.size() ; i++ ) {
				TimeDecayObject tdo = animationEventsList.get(i);
				if( !tdo.update((float)time) ) {
					animationEventsList.remove(tdo); // TODO : check if this is safe?!
					//Log.d("GameLogicThread","animation event "+i+" removed");
				}
			}
			// update particle positions
			for( int i = 0 ; i < GameState.MAX_PARTICLES ; i++ ) {
				if( gameState.getParticleState(i) == GameState.PARTICLE_STATE_ON ) {
					PointF pos = gameState.getParticlePos(i);
					PointF vec = gameState.getParticleVec(i);
					float mag = GameState.PARTICLE_SPEED_NORMAL;
					pos.x += vec.x * mag * time;
					pos.y += vec.y * mag * time;
					// check for portal warp
					boolean warped = false;
					Point gridPos = new Point(
							(int)(pos.x * 5),
							(int)(pos.y * 5)
							);
					for( int j = 0 ; j < GameState.MAX_PORTALS ; j++ ) {
						Point portalIn = gameState.getPortalIn(j);
						Point portalOut = gameState.getPortalOut(j);
						if( portalIn != null && portalOut != null ) {
							if( portalIn.x == gridPos.x && portalIn.y == gridPos.y ) {
								PointF insidePos = new PointF(
										(pos.x - (0.2f * (float)gridPos.x)) * 5.f,
										(pos.y - (0.2f * (float)gridPos.y)) * 5.f
										);
								if( insidePos.x < insideGridPortalWarpPadding
										&& vec.x < 0.f ) {
									// going left
									pos.x -= (float)(portalIn.x-(portalOut.x+1))*0.2f;
									pos.y -= (float)(portalIn.y-portalOut.y)*0.2f;
								} else if( insidePos.x >= (1.f-insideGridPortalWarpPadding)
										&& vec.x > 0.f ) {
									// going right
									pos.x -= (float)(portalIn.x-(portalOut.x-1))*0.2f;
									pos.y -= (float)(portalIn.y-portalOut.y)*0.2f;
								} else if( insidePos.y < insideGridPortalWarpPadding
										&& vec.y < 0.f ) {
									// going up
									pos.x -= (float)(portalIn.x-portalOut.x)*0.2f;
									pos.y -= (float)(portalIn.y-(portalOut.y+1))*0.2f;
								} else if( insidePos.y >= (1.f-insideGridPortalWarpPadding)
										&& vec.y > 0.f ) {
									// going down
									pos.x -= (float)(portalIn.x-portalOut.x)*0.2f;
									pos.y -= (float)(portalIn.y-(portalOut.y-1))*0.2f;
								}
								//pos.x -= (float)(portalIn.x-portalOut.x)*0.2f;
								//pos.y -= (float)(portalIn.y-portalOut.y)*0.2f;
							}
						}
					}
					// wrap bounds (only if not warped by portal)
					if( !warped ) {
						if( pos.x >= 1.f ) {
							pos.x -= 1.f;
						} else if( pos.x < 0.f ) {
							pos.x += 1.f;
						}
						if( pos.y >= 1.f ) {
							pos.y -= 1.f;
						} else if( pos.y < 0.f ) {
							pos.y += 1.f;
						}
					}
					// trigger condition
					for( int j = 0 ; j < GameState.GRID_SIZE_ONE_D ; j++ ) {
						if( gridActiveAreaBounds[j].contains(pos.x, pos.y) ) {
							int gridX = (int)(pos.x*5);
							int gridY = (int)(pos.y*5);
							gameState.setGridState(gridX, gridY, GameState.GRID_STATE_ACTIVE);
							animationEventsList.add(new GridFlashAnimation(gameState, gridX, gridY));
							soundPlayer.play(gameState.getGridSound(gridX, gridY));
							// TODO : reactivate sound!
							//Log.d("GameLogicThread","grid ("+gridX+", "+gridY+") active");
						}
					}
				}
			}
		}
	}
}
