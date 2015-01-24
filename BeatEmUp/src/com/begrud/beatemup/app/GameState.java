package com.begrud.beatemup.app;

import java.util.Arrays;

import com.begrud.beatemup.levels.Level;

import android.graphics.Point;
import android.graphics.PointF;

public class GameState {
	// screens
	public static final int NUM_SCREENS = 1;
	public static final int SCREEN_GAME = 0;
	
	// particlePos
	public static final int MAX_PARTICLES = 3;
	// --- states
	public static final int PARTICLE_STATE_OFF = 0;
	public static final int PARTICLE_STATE_ON = 1;
	// --- data
	public static final float PARTICLE_SPEED_NORMAL = 0.5f; // gridState movement per second
	public static final PointF PARTICLE_POSITION_DEFAULT = new PointF(0.1f, 0.1f);
	// **---textures
	public static final int PARTICLE_TEX_DEFAULT = 3;
	
	// gridState
	public static final int GRID_SIZE_X = 5;
	public static final int GRID_SIZE_Y = 5;
	public static int GRID_SIZE_ONE_D = GRID_SIZE_X * GRID_SIZE_Y;
	// --- states
	public static final int GRID_STATE_DEFAULT = 0;
	public static final int GRID_STATE_ACTIVE = 1;
	// --- modifier
	public static final int GRID_ABILITY_NONE = 0;
	// --- sound
	public static final int GRID_SOUND_NONE = 0;
	public static final int GRID_SOUND_1 = 1;
	public static final int GRID_SOUND_2 = 2;
	// **---textures
	public static final int GRID_TEX_GREEN = 0;
	public static final int GRID_TEX_RED = 1;
	public static final int GRID_TEX_YELLOW = 2;
	public static final int GRID_TEX_PURPLE = 4;
	public static final int GRID_TEX_WHITE = 5;
	public static final int GRID_TEX_DARKBLUE = 6;
	
	// BUTTONS
	// consts
	public static final int BUTTON_NUM = 8;
	// meta
	public static final int BUTTON_SPEAKER = 0;
	public static final int BUTTON_PLAY_STOP = 1;
	public static final int BUTTON_PORTAL = 2;
	public static final int BUTTON_UP = 3;
	public static final int BUTTON_NORMAL = 4;
	public static final int BUTTON_DOWN = 5;
	public static final int BUTTON_DELETEALL = 6;
	public static final int BUTTON_DELETE = 7;
	// 
	
	// states
	// -- master
	public static final int STATE_STOPPED = 0;
	public static final int STATE_PLAYING = 1;
	public static final int STATE_REVIEW = 2;
	// portal drop states
	public static final int PORTAL_STATE_NONE = 0;
	public static final int PORTAL_STATE_IN = 1;
	public static final int PORTAL_STATE_OUT_SPEED_FAST = 2;
	public static final int PORTAL_STATE_OUT_NORMAL = 3;
	public static final int PORTAL_STATE_OUT_SPEED_SLOW = 4;
	// misc portal consts
	public static final int MAX_PORTALS = 3;
	
	private int screen;
	private int []gridState;
	private int []gridMod;
	private int []gridSound;
	
	private PointF []particlePos;
	private int []particleState;
	private PointF []particleVec;
	
	private int currentState;
	private int portalState;
	
	// portal making
	private Point portalInGrid = null;
	private Point portalOutGrid = null;
	private Point []portalIn = new Point[MAX_PORTALS];

	private Point []portalOut = new Point[MAX_PORTALS];
	private int portalPointer = 0;

	GameState() {
		clear();
	}
	
	public void clear() {
		// note: this sets all to default (0)
		screen = SCREEN_GAME;
		particlePos = new PointF[MAX_PARTICLES];
		particleState = new int[MAX_PARTICLES];
		particleVec = new PointF[MAX_PARTICLES];
		for( int i = 0 ; i < particlePos.length ; i++ ) {
			//particlePos[i] = new PointF(0.125f, 0.125f);
			particlePos[i] = new PointF(PARTICLE_POSITION_DEFAULT.x, PARTICLE_POSITION_DEFAULT.y);
			particleState[i] = PARTICLE_STATE_OFF;
			particleVec[i] = new PointF();
		}
		// TODO : remove this, for debug
		//particleState[0] = PARTICLE_STATE_ON;
		//particleVec[0] = new PointF(1.f, 0.f);
		// GRID
		resetGrid();
		// state
		currentState = STATE_STOPPED;
		// portal
		portalState = PORTAL_STATE_NONE;
	}
	
	public void resetGrid() {
		gridState = new int [GRID_SIZE_ONE_D];
		gridMod = new int [GRID_SIZE_ONE_D];
		gridSound = new int [GRID_SIZE_ONE_D];
		for( int i = 0 ; i < GRID_SIZE_ONE_D ; i++ ) {
			gridState[i] = GRID_STATE_DEFAULT;
			gridMod[i] = GRID_ABILITY_NONE;
			gridSound[i] = GRID_SOUND_NONE;
		}
		// reset portals
		portalPointer = 0;
		portalInGrid = null;
		portalOutGrid = null;
		for( int i = 0 ; i < MAX_PORTALS ; i++ ) {
			portalIn[i] = null;
			portalOut[i] = null;
		}
	}
	
	public void loadLevel(Level level) {
		resetGrid();
		gridSound = Arrays.copyOf(level.getGridSounds(), GRID_SIZE_ONE_D);
	}
	
	// getter / setter for screen
	public int getScreenState() {
		return screen;
	}
	
	public boolean setScreenState(int state) {
		if( state >= 0 && state < NUM_SCREENS ) {
			screen = state;
			return true;
		}
		return false;
	}
	
	// getter / setter for gridState
	public int getGridState(int x, int y) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			return gridState[idx];
		}
		return 0;
	}
	
	public boolean setGridState(int x, int y, int state) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			gridState[idx] = state;
			return true;
		}
		return false;
	}
	
	// getter / setter for gridState
	public int getGridMod(int x, int y) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			return gridMod[idx];
		}
		return 0;
	}
	
	public boolean setGridMod(int x, int y, int state) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			gridMod[idx] = state;
			return true;
		}
		return false;
	}
		
	// getter / setter for gridState
	public int getGridSound(int x, int y) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			return gridSound[idx];
		}
		return 0;
	}
	
	public boolean setGridSound(int x, int y, int state) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			gridSound[idx] = state;
			return true;
		}
		return false;
	}
	
	public int getGridTex(int x, int y) {
		int idx = ((y*GRID_SIZE_X)+x);
		if( idx >= 0 && idx < GRID_SIZE_ONE_D ) {
			if( gridState[idx] == GRID_STATE_ACTIVE ) {
				return GRID_TEX_GREEN;
			} else {
				if( gridSound[idx] == GRID_SOUND_NONE ) {
					return GRID_TEX_WHITE;
				} else if( gridSound[idx] == GRID_SOUND_1 ) {
					return GRID_TEX_DARKBLUE;
				} else if( gridSound[idx] == GRID_SOUND_2 ) {
					return GRID_TEX_YELLOW;
				}
			}
		}
		return 0;
	}
	
	// getter / setter for particle position
	public PointF getParticlePos(int idx) {
		if( idx >= 0 && idx < MAX_PARTICLES ) {
			return particlePos[idx];
		}
		return null;
	}
	
	public boolean setParticlePos(int idx, PointF pos) {
		if( idx >= 0 && idx < MAX_PARTICLES ) {
			particlePos[idx] = new PointF(pos.x, pos.y);
			return true;
		}
		return false;
	}
	
	// getter / setter for particle state
	public int getParticleState(int idx) {
		if( idx >= 0 && idx < MAX_PARTICLES ) {
			return particleState[idx];
		}
		return 0;
	}
	
	public boolean setParticleState(int idx, int state) {
		if( idx >= 0 && idx < MAX_PARTICLES ) {
			particleState[idx] = state;
			return true;
		}
		return false;
	}
	
	// getter / setter for particle vector
	public PointF getParticleVec(int idx) {
		if( idx >= 0 && idx < MAX_PARTICLES ) {
			return particleVec[idx];
		}
		return null;
	}
	
	public boolean setParticleVec(int idx, PointF pos) {
		if( idx >= 0 && idx < MAX_PARTICLES ) {
			particleVec[idx] = pos;
			return true;
		}
		return false;
	}
	
	public int getState() {
		return currentState;
	}
	
	public void setState(int state) {
		currentState = state;
	}
	
	
	// portal state
	public int getPortalState() {
		return portalState;
	}

	public void setPortalState(int portalState) {
		this.portalState = portalState;
	}
	
	// portal building
	public Point getPortalInGrid() {
		return portalInGrid;
	}

	public void setPortalInGrid(Point portalInGrid) {
		this.portalInGrid = portalInGrid;
	}
	
	public Point getPortalOutGrid() {
		return portalOutGrid;
	}

	public void setPortalOutGrid(Point portalOutGrid) {
		this.portalOutGrid = portalOutGrid;
	}
	
	// CREATE PORTAL
	public boolean createPortal() {
		//portalInGrid
		//portalOutGrid
		if( portalPointer < 3 ) {
			portalIn[portalPointer] = new Point(portalInGrid.x, portalInGrid.y);
			portalOut[portalPointer] = new Point(portalOutGrid.x, portalOutGrid.y);
			portalInGrid = null;
			portalOutGrid = null;
			portalPointer++;
			return true;
		}
		portalInGrid = null;
		portalOutGrid = null;
		return false;
	}
	
	// get created portals
	public Point getPortalIn(int idx) {
		if( idx >= 0 && idx < MAX_PORTALS ) {
			return portalIn[idx];
		}
		return null;
	}

	public Point getPortalOut(int idx) {
		if( idx >= 0 && idx < MAX_PORTALS ) {
			return portalOut[idx];
		}
		return null;
	}
}
