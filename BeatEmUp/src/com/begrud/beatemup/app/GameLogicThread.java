package com.begrud.beatemup.app;

import java.util.ArrayList;
import java.util.List;

import com.begrud.beatemup.animation.BinButtonDepressAnimation;
import com.begrud.beatemup.animation.GridFlashAnimation;
import com.begrud.beatemup.animation.ReviewGoalAnimation;
import com.begrud.beatemup.animation.TimeDecayObject;
import com.begrud.beatemup.levels.Level;
import com.begrud.beatemup.levels.Level01;
import com.begrud.beatemup.levels.Level02;
import com.begrud.beatemup.levels.Level03;
import com.begrud.beatemup.levels.Level04;
import com.begrud.beatemup.levels.Level05;
import com.begrud.beatemup.levels.Level06;
import com.begrud.beatemup.levels.Level07;
import com.begrud.beatemup.levels.Level08;
import com.begrud.beatemup.levels.Level09;
import com.begrud.beatemup.levels.Level10;

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
	
	private Level []levels;
	private int curLevelIdx;
	
	
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
		// Load all levels
		levels = new Level[] {
				new Level01(),
				new Level02(),
				new Level03(),
				new Level04(),
				new Level05(),
				new Level06(),
				new Level07(),
				new Level08(),
				new Level09(),
				new Level10()
		};
		// Load first level
		curLevelIdx = 0;
		gameState.loadLevel(levels[curLevelIdx]);
	}
	
	// coordinate is normalised and adjusted for window, not screen size
	public void touch(PointF pos, int state) {
		synchronized(syncLock) {
			Log.d("GameLogicThread", "touch: "+pos.x+", "+pos.y);
			// anywhere on screen
			if( gameState.getState() == GameState.STATE_TITLE_SCREEN ) {
				gameState.setState(GameState.STATE_TUTORIAL_1);
				return;
			} else if( gameState.getState() == GameState.STATE_TUTORIAL_1 ) {
				gameState.setState(GameState.STATE_TUTORIAL_2);
				return;
			} else if( gameState.getState() == GameState.STATE_TUTORIAL_2 ) {
				gameState.setState(GameState.STATE_STOPPED);
				return;
			}
			if( gameState.getState() == GameState.STATE_WIN ) {
				Log.d("GameLogicThread","press screen on win, next level");
				// goto next level
				curLevelIdx++;
				if( curLevelIdx < levels.length ) {
					Log.d("GameLogicThread","new level is "+curLevelIdx);
					// load level
					gameState.loadLevel(levels[curLevelIdx]);
					// change state
					gameState.setState(GameState.STATE_REVIEW);
					animationEventsList.add(new ReviewGoalAnimation(gameState, soundPlayer, levels[curLevelIdx]));
					Log.d("GameLogicThread","Done");
				} else {
					// something? shouldn't happen
					Log.d("GameLogicThread","screen press on win but no more levels");
				}
				return;
			}
			if( state == COORDS_GRID & state != GameState.STATE_WIN
					& state!= GameState.STATE_FINISHED_LEVELS ) {
				// touch grid
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
				if( gameState.getState() == GameState.STATE_STOPPED
						& gameState.getInteractionState() != GameState.INTERACTION_STATE_PORTAL_IN
						& gameState.getInteractionState() != GameState.INTERACTION_STATE_PORTAL_OUT ) {
					int sound = gameState.getGridSound(x, y);
					if( sound != GameState.GRID_SOUND_NONE ) {
						soundPlayer.play(sound);
						gameState.setGridState(x, y, GameState.GRID_STATE_ACTIVE);
						animationEventsList.add(new GridFlashAnimation(gameState, x, y));
					}
				}
				if( gameState.getInteractionState() == GameState.INTERACTION_STATE_PORTAL_IN ) {
					Log.d("GameLogicThread", "portal in SET...");
					gameState.setPortalInGrid(new Point(x,y));
					gameState.setInteractionState(GameState.INTERACTION_STATE_PORTAL_OUT); // TODO : set this to last portal out state
					Log.d("GameLogicThread", "portal out START");
				} else if( gameState.getInteractionState() == GameState.INTERACTION_STATE_PORTAL_OUT ) {
					Log.d("GameLogicThread", "portal out SET...");
					Point portalIn = gameState.getPortalInGrid();
					boolean doCreatePortal = true;
					if( portalIn == null ) {
						doCreatePortal = false;
					} else {
						if( portalIn.x == x && portalIn.y == y ) {
							doCreatePortal = false;
						}
					}
					if( !doCreatePortal ) {
						// failed, leave portal building states
						gameState.setInteractionState(GameState.INTERACTION_STATE_NONE);
						gameState.setPortalInGrid(null);
						Log.d("GameLogicThread", "portal out QUIT");
					} else {
						gameState.setPortalOutGrid(new Point(x,y));
						Log.d("GameLogicThread", "portal out to "+x+", "+y);
						if( gameState.createPortal() ) {
							Log.d("GameLogicThread", "portal out SET!");
							gameState.setInteractionState(GameState.INTERACTION_STATE_NONE);
						}
					}
				} // TODO : add for other portal out states
				else if( gameState.getInteractionState() == GameState.INTERACTION_STATE_PORTAL_DELETE ) {
					for( int i = 0 ; i < GameState.MAX_PORTALS ; i++ ) {
						Point portalIn = gameState.getPortalIn(i);
						Point portalOut = gameState.getPortalOut(i);
						if( portalIn != null && portalOut != null ) {
							if( (portalIn.x == x && portalIn.y == y) 
									|| (portalOut.x == x && portalOut.y == y) ) {
								// delete portal
								gameState.deletePortal(i);
								gameState.setInteractionState(GameState.INTERACTION_STATE_NONE);
								break;
							}
						}
					}
				}
			} else if( state == COORDS_LEFT_BAR ) {
				// left bar				
				if( leftNavButtonsBounds[GameState.BUTTON_PLAY_STOP].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: Play / stop");
					if( gameState.getState() == GameState.STATE_STOPPED ) {
						gameState.setState(GameState.STATE_PLAYING);
						// start particle
						gameState.getParticleGridPos()[0] = new Point(-1, -1);
						gameState.setParticleCurSpeed(GameState.PORTAL_OUT_SPEED_NORMAL);
						gameState.setParticleState(0, GameState.PARTICLE_STATE_ON);
						gameState.setParticleVec(0, new PointF(1.f, 0.f));
					} else if( gameState.getState() == GameState.STATE_PLAYING ) {
						gameState.setState(GameState.STATE_STOPPED);
						levels[curLevelIdx].resetActiveSequenceAndSpeed();
						// stop particle
						gameState.setParticleState(0, GameState.PARTICLE_STATE_OFF);
						gameState.setParticlePos(0,
								new PointF(GameState.PARTICLE_POSITION_DEFAULT.x,
										GameState.PARTICLE_POSITION_DEFAULT.y));
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_PORTAL].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: portal in?");
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						gameState.setInteractionState(GameState.INTERACTION_STATE_PORTAL_IN);
						Log.d("touch","left nav: portal in START");
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_NORMAL].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: portal out normal");
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						gameState.setCurrentPortalOutSpeed(GameState.PORTAL_OUT_SPEED_NORMAL);
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_UP].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: portal out normal");
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						gameState.setCurrentPortalOutSpeed(GameState.PORTAL_OUT_SPEED_FAST);
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_DOWN].contains(pos.x, pos.y) ) {
					Log.d("touch","left nav: portal out normal");
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						gameState.setCurrentPortalOutSpeed(GameState.PORTAL_OUT_SPEED_SLOW);
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_DELETEALL].contains(pos.x, pos.y) ) {
					gameState.resetPortals();
					gameState.setBinButtonState(GameState.BUTTON_PRESSED);
					animationEventsList.add(new BinButtonDepressAnimation(gameState));
				} else if( leftNavButtonsBounds[GameState.BUTTON_DELETE].contains(pos.x, pos.y) ) {
					if( gameState.getState() != GameState.STATE_REVIEW ) {
						if( gameState.getInteractionState() != GameState.INTERACTION_STATE_NONE ) {
							// reset portal setting
							gameState.setPortalInGrid(null);
						}
						gameState.setInteractionState(GameState.INTERACTION_STATE_PORTAL_DELETE);
					}
				} else if( leftNavButtonsBounds[GameState.BUTTON_SPEAKER].contains(pos.x, pos.y) ) {
					if( gameState.getState() != GameState.STATE_REVIEW
							&& gameState.getState() != GameState.STATE_WIN ) {
						// stop particles
						for( int i = 0 ; i < GameState.MAX_PARTICLES ; i++ ) {
							// stop particle
							gameState.setParticleState(i, GameState.PARTICLE_STATE_OFF);
							gameState.setParticlePos(i,
									new PointF(GameState.PARTICLE_POSITION_DEFAULT.x,
											GameState.PARTICLE_POSITION_DEFAULT.y));
						}
						// set state to review
						gameState.setState(GameState.STATE_REVIEW);
						// create exit review event
						animationEventsList.add(new ReviewGoalAnimation(gameState, soundPlayer, levels[curLevelIdx]));
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
					float mag = GameState.PARTICLE_SPEED_GRID;
					pos.x += vec.x * mag * time;
					pos.y += vec.y * mag * time;
					
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
					
					// trigger condition - from centre
					for( int j = 0 ; j < GameState.GRID_SIZE_ONE_D ; j++ ) {
						Point []lastGridPos = gameState.getParticleGridPos();
						if( gridActiveAreaBounds[j].contains(pos.x, pos.y) ) {
							int gridX = (int)(pos.x*5);
							int gridY = (int)(pos.y*5);
							if( !(lastGridPos[i].x == gridX && lastGridPos[i].y == gridY) ) {
								lastGridPos[i].x = gridX;
								lastGridPos[i].y = gridY;
								// check for portal warp
								boolean warped = false;
								Point gridPos = new Point(
										(int)(pos.x * 5),
										(int)(pos.y * 5)
										);
								for( int k = 0 ; k < GameState.MAX_PORTALS ; k++ ) {
									Point portalIn = gameState.getPortalIn(k);
									Point portalOut = gameState.getPortalOut(k);
									if( portalIn != null && portalOut != null ) {
										if( portalIn.x == gridPos.x && portalIn.y == gridPos.y ) {
											pos.x -= (float)(portalIn.x-portalOut.x)*0.2f;
											pos.y -= (float)(portalIn.y-portalOut.y)*0.2f;
											warped = true;
											// set speed out of portal
											if( gameState.getPortalOutSpeed(k) == GameState.PORTAL_OUT_SPEED_NORMAL ) {
												Log.d("GameLogicThread","Setting particle speed: normal");
												gameState.setParticleCurSpeed(GameState.PORTAL_OUT_SPEED_NORMAL);
												if( vec.x > 0.f ) {
													vec.x = GfxElementInfo.SPEED_NORMAL;
												} else if( vec.x < 0.f ) {
													vec.x = -GfxElementInfo.SPEED_NORMAL;
												}
												if( vec.y > 0.f ) {
													vec.y = GfxElementInfo.SPEED_NORMAL;
												} else if( vec.y < 0.f ) {
													vec.y = -GfxElementInfo.SPEED_NORMAL;
												}
											} else if( gameState.getPortalOutSpeed(k) == GameState.PORTAL_OUT_SPEED_FAST ) {
												Log.d("GameLogicThread","Setting particle speed: fast");
												gameState.setParticleCurSpeed(GameState.PORTAL_OUT_SPEED_FAST);
												if( vec.x > 0.f ) {
													vec.x = GfxElementInfo.SPEED_FAST;
												} else if( vec.x < 0.f ) {
													vec.x = -GfxElementInfo.SPEED_FAST;
												}
												if( vec.y > 0.f ) {
													vec.y = GfxElementInfo.SPEED_FAST;
												} else if( vec.y < 0.f ) {
													vec.y = -GfxElementInfo.SPEED_FAST;
												}
											} else if( gameState.getPortalOutSpeed(k) == GameState.PORTAL_OUT_SPEED_SLOW ) {
												Log.d("GameLogicThread","Setting particle speed: slow");
												gameState.setParticleCurSpeed(GameState.PORTAL_OUT_SPEED_SLOW);
												if( vec.x > 0.f ) {
													vec.x = GfxElementInfo.SPEED_SLOW;
												} else if( vec.x < 0.f ) {
													vec.x = -GfxElementInfo.SPEED_SLOW;
												}
												if( vec.y > 0.f ) {
													vec.y = GfxElementInfo.SPEED_SLOW;
												} else if( vec.y < 0.f ) {
													vec.y = -GfxElementInfo.SPEED_SLOW;
												}
											}
											gameState.setParticleVec(i, vec);
										}
									}
								}
								// play sound only if no warp
								if( !warped ) {
									// trigger sound if any
									int sound = gameState.getGridSound(gridX, gridY);
									if( sound != 0 ) {
										gameState.setGridState(gridX, gridY, GameState.GRID_STATE_ACTIVE);
										animationEventsList.add(new GridFlashAnimation(gameState, gridX, gridY));
										
										soundPlayer.play(sound);
									}
									// check if player has won game
									if( !levels[curLevelIdx].hasWon() ) {
										if( levels[curLevelIdx].addBeat(sound, gameState.getParticleCurSpeed()) ) {
											// win!!!
											Log.d("WINWINWIN","WINWINWINWINWINWINWINWINWINWINW");
											if( (curLevelIdx+1) >= levels.length ) {
												gameState.setState(GameState.STATE_FINISHED_LEVELS);
												Log.d("WINWINWIN","finished levels");
											} else {
												gameState.setState(GameState.STATE_WIN);
												Log.d("WINWINWIN","state win");
											}
											// stop particle
											gameState.setParticleState(0, GameState.PARTICLE_STATE_OFF);
											gameState.setParticlePos(0,
													new PointF(GameState.PARTICLE_POSITION_DEFAULT.x,
															GameState.PARTICLE_POSITION_DEFAULT.y));
											// set level won
											levels[curLevelIdx].setWon();
										}
									}
									// apply mod only if no portal
									// TODO : make this better, does not correct position
									int speedCode = gameState.getParticleCurSpeed();
									int mod = gameState.getGridMod(gridX, gridY);
									if( mod != GameState.GRID_MOD_NONE ) {
										vec.x = 0.f;
										vec.y = 0.f;
										float speed = 0.f;
										// set speed out of portal
										if( speedCode == GameState.PORTAL_OUT_SPEED_NORMAL ) {
											speed = GfxElementInfo.SPEED_NORMAL;
										} else if( speedCode == GameState.PORTAL_OUT_SPEED_FAST ) {
											speed = GfxElementInfo.SPEED_FAST;
										} else if( speedCode == GameState.PORTAL_OUT_SPEED_SLOW ) {
											speed = GfxElementInfo.SPEED_SLOW;
										}
										if( mod == GameState.GRID_MOD_UP ) {
											vec.y = -speed;
										} else if( mod == GameState.GRID_MOD_RIGHT ) {
											vec.x = speed;
										} else if( mod == GameState.GRID_MOD_DOWN ) {
											vec.y = speed;
										} else if( mod == GameState.GRID_MOD_LEFT ) {
											vec.x = -speed;
										}
										gameState.setParticleVec(i, vec);
										// quick fix, make position in centre
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
