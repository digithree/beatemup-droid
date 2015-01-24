package com.begrud.beatemup.app;

import java.util.ArrayList;
import java.util.List;

import com.begrud.beatemup.animation.GridFlashAnimation;
import com.begrud.beatemup.animation.TimeDecayObject;
import com.begrud.beatemup.levels.Level1_1;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;


public class GameLogicThread extends Thread {
	
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
	
	private final float triggerArea = 0.01f;
	private RectF []gridActiveAreaBounds;
	
	
	private Object syncLock = new Object();
	
	private SoundPlayer soundPlayer;
	
	private List<TimeDecayObject> animationEventsList = new ArrayList<TimeDecayObject>();
	
	
	private void init() {
		soundPlayer = new SoundPlayer(context, gameState);
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
		// Load first level
		gameState.loadLevel(new Level1_1());
	}
	
	// coordinate is normalised and adjusted for window, not screen size
	public void touch(PointF pos) {
		synchronized(syncLock) {
			Log.d("GameLogicThread", "touch: "+pos.x+", "+pos.y);
			// test touch test touch test
			int x = (int)(pos.x * 5);
			int y = (int)(pos.y * 5);
			if( gameState.getGridSound(x, y) == GameState.GRID_SOUND_NONE) {
				gameState.setGridSound(x, y, GameState.GRID_SOUND_1);
			} else {
				gameState.setGridSound(x, y, GameState.GRID_SOUND_NONE);
			}
			/*
			if( gameState.getScreenState() == GameState.SCREEN_HOMEMENU ) {
				if( gameState.getElementState(GameState.EL_PARENTSNOTE) == 1 ) { // parent note is visible
					// cancel the parents note
					gameState.setElementState(GameState.EL_PARENTSNOTE, 0);
				} else { // parent note is invisible
					if( gameItemBoundsExtra[GfxElementInfo.EX_PARENTNOTE_ICON].contains(pos.x, pos.y) ) {
						// state must be 0 (invisible) to get here, set to visible
						gameState.setElementState(GameState.EL_PARENTSNOTE, 1);
					} else if( gameItemBounds[GameState.EL_GAME1LOGO].contains(pos.x, pos.y) ) {
						gameState.setCurrentGame(GameState.GAME_1);
						gameState.setCurrentState(GameState.STATE_NEW_ROUND);
						gameState.setScreenState(GameState.SCREEN_GAME);
						waitTime = GameState.WAIT_TIME[gameState.getCurrentState()];
						// clear graphics for game
						for( int i = 3 ; i < 9 ; i++ ) {
							gameState.setElementState(i, 0);
						}
						gameState.setElementState(GameState.EL_STARS,gameState.getGameProgress(
								gameState.getCurrentGame()));
					} else if( gameItemBounds[GameState.EL_GAME2LOGO].contains(pos.x, pos.y) ) {
						gameState.setCurrentGame(GameState.GAME_2);
						gameState.setCurrentState(GameState.STATE_NEW_ROUND);
						gameState.setScreenState(GameState.SCREEN_GAME);
						waitTime = GameState.WAIT_TIME[gameState.getCurrentState()];
						// clear graphics for game
						for( int i = 3 ; i < 9 ; i++ ) {
							gameState.setElementState(i, 0);
						}
						gameState.setElementState(GameState.EL_STARS,gameState.getGameProgress(
								gameState.getCurrentGame()));
					}
				}
			} else if( gameState.getScreenState() == GameState.SCREEN_GAME ) {
				// check for standard elements
				if( gameItemBoundsExtra[GfxElementInfo.EX_HOME].contains(pos.x, pos.y) ) {
					gameState.setScreenState(GameState.SCREEN_HOMEMENU);
					gameState.setCurrentState(GameState.STATE_HOME);
				}
				// TODO : implement pause
				if( gameState.getCurrentState() == GameState.STATE_WAIT_FOR_INPUT ) {
					// check for replay (replays audio for correct item)
					if( gameItemBoundsExtra[GfxElementInfo.EX_REPLAY].contains(pos.x, pos.y) ) {
						soundPlayer.play(gameItems[correctItem]);
					}
					// check for collision with items
					for( int i = 0 ; i < 3 ; i++ ) {
						if( gameItemBounds[GameState.EL_ITEM1+i].contains(pos.x, pos.y) ) {
							touchedItem = i;
							Log.d("GameLogicThread", "touched sprite: "+touchedItem);
							break;
						}
					}
					if( touchedItem != -1 ) {
						Log.d("GameLogicThread","got touchedSprite: "+touchedItem);
						if( touchedItem == correctItem ) {
							gameState.setCurrentState(GameState.STATE_INPUT_CORRECT);
							gameState.setElementState(GameState.EL_ITEM1+touchedItem, 3);	// item red
							game'State.setElementState(GameState.EL_SMUGGLES, 2);	// smuggles red
							soundPlayer.play(SoundPlayer.SOUND_CORRECT);
						} else {
							gameState.setCurrentState(GameState.STATE_INPUT_INCORRECT);
							gameState.setElementState(GameState.EL_ITEM1+touchedItem, 2);	// item green
							gameState.setElementState(GameState.EL_SMUGGLES, 1);	// smuggles green
							soundPlayer.play(SoundPlayer.SOUND_INCORRECT);
						}
						waitTime = GameState.WAIT_TIME[gameState.getCurrentState()];
						Log.d("GameLogicThread", "gameState = "+gameState.getCurrentState());
						Log.d("GameLogicThread", "wait time for this state: "+GameState.WAIT_TIME[gameState.getCurrentState()]);
						Log.d("GameLogicThread", "(In)Correct: set wait time to "+waitTime);
					}
				}
			}
			*/
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
					// trigger condition
					for( int j = 0 ; j < GameState.GRID_SIZE_ONE_D ; j++ ) {
						if( gridActiveAreaBounds[j].contains(pos.x, pos.y) ) {
							int gridX = (int)(pos.x*5);
							int gridY = (int)(pos.y*5);
							gameState.setGridState(gridX, gridY, GameState.GRID_STATE_ACTIVE);
							animationEventsList.add(new GridFlashAnimation(gameState, gridX, gridY));
							soundPlayer.play(gameState.getGridSound(gridX, gridY));
							//Log.d("GameLogicThread","grid ("+gridX+", "+gridY+") active");
						}
					}
				}
			}
			/*
			//Log.d("animate", "time elapsed: "+time);
			if( gameState.getScreenState() == GameState.SCREEN_HOMEMENU ) {
				// ?
			} else if( gameState.getScreenState() == GameState.SCREEN_GAME ) {
				if( gameState.getCurrentState() != GameState.STATE_WAIT_FOR_INPUT ) {
					waitTime -= time;
					if( waitTime <= 0.f ) { // timer has elapsed
						// moving FROM these current state
						if( gameState.getCurrentState() == GameState.STATE_NEW_ROUND ) {
							// randomly pick a set of 3 elements from the available items
							gameItems[0] = (int)(Math.random() * 10);
							int choice = (int)(Math.random() * 10);
							while( choice == gameItems[0] ) {
								choice = (int)(Math.random() * 10);
							}
							gameItems[1] = choice;
							choice = (int)(Math.random() * 9);
							while( choice == gameItems[0] || choice == gameItems[1] ) {
								choice = (int)(Math.random() * 10);
							}
							gameItems[2] = choice;
							gameState.setCurrentRoundItems(gameItems);
							// randomly select the correct item (subscript)
							correctItem = (int)(Math.random() * 3);
							gameState.setCurrentState(GameState.STATE_REVEAL_ITEM_1);
							// do next item reveal
							gameState.setElementState(GameState.EL_ITEM1, 1);
							soundPlayer.play(gameItems[0]);
						} else if( gameState.getCurrentState() == GameState.STATE_REVEAL_ITEM_1 ) {
							gameState.setCurrentState(GameState.STATE_REVEAL_ITEM_2);
							// do next item reveal
							gameState.setElementState(GameState.EL_ITEM2, 1);
							soundPlayer.play(gameItems[1]);
						} else if( gameState.getCurrentState() == GameState.STATE_REVEAL_ITEM_2 ) {
							gameState.setCurrentState(GameState.STATE_REVEAL_ITEM_3);
							// do next item reveal
							gameState.setElementState(GameState.EL_ITEM3, 1);
							soundPlayer.play(gameItems[2]);
						} else if( gameState.getCurrentState() == GameState.STATE_REVEAL_ITEM_3 ) {
							gameState.setCurrentState(GameState.STATE_CORRECT_QUE);
							soundPlayer.play(gameItems[correctItem]);
						} else if( gameState.getCurrentState() == GameState.STATE_CORRECT_QUE ) {
							gameState.setCurrentState(GameState.STATE_WAIT_FOR_INPUT);
						} else if( gameState.getCurrentState() == GameState.STATE_INPUT_INCORRECT ) {
							gameState.setCurrentState(GameState.STATE_WAIT_FOR_INPUT);
							gameState.setElementState(GameState.EL_ITEM1+touchedItem, 1);
							gameState.setElementState(GameState.EL_SMUGGLES, 0);	// smuggles red
							touchedItem = -1;
						} else if( gameState.getCurrentState() == GameState.STATE_INPUT_CORRECT ) {					
							// reset elements
							gameState.setElementState(GameState.EL_SMUGGLES, 0);
							gameState.setElementState(GameState.EL_ITEM1, 0);
							gameState.setElementState(GameState.EL_ITEM2, 0);
							gameState.setElementState(GameState.EL_ITEM3, 0);
							// add star
							if( gameState.addStarToCurrentGame() ) {
								// finished!
								gameState.setCurrentState(GameState.STATE_WON);
								soundPlayer.play(SoundPlayer.SOUND_ALL_STARS);
								gameState.setElementState(GameState.EL_SUCCESS,1);
							} else {
								// continue
								gameState.setCurrentState(GameState.STATE_NEW_ROUND);
							}
							touchedItem = -1;
						} else if( gameState.getCurrentState() == GameState.STATE_WON ) {
							gameState.setElementState(GameState.EL_SUCCESS,0);
							gameState.setScreenState(GameState.SCREEN_HOMEMENU);
							gameState.setCurrentState(GameState.STATE_HOME);
							gameState.setElementState(GameState.EL_GAME1LOGO+gameState.getCurrentGame(),1);
							// TODO : set some other flag that the game is complete, not just it's visual rep.
						}
						waitTime = GameState.WAIT_TIME[gameState.getCurrentState()];
					}
				}
			}
			*/
		}
	}
}
