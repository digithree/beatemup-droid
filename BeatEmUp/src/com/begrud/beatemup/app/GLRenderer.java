package com.begrud.beatemup.app;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.begrud.beatemup.R;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLSurfaceView.Renderer;


public class GLRenderer implements Renderer 
{
	/* cache the context so we can incrementally load the texture bmps as we need them */
	private Context context;

	/* cache the gl context for use with Tex obj */
	GL10 gl;
	
	private final GameState gameState;
	
	// textures
	private Tex []textures;
	
	private Tex []buttons;
	private PointF []buttonPos;
	
	private Tex []portals;
	
	private Tex []screens;
	
	private Tex []modifiers;
	
	private boolean firsttime = true;
	private float hor = 1.0f;
	private float ver = 1.0f;
	//private float hvratio = 1.0f;
	
	private float gfxvhratio = 768.0f/1024.0f;

	/** Constructor to set the handed over context */
	public GLRenderer(Context context, GameState gameState) {
		// TODO super(context)?		
		this.context = context;
		this.gameState = gameState;
	    
		/* all initialisation that involves gl calls must be done in onSurfaceCreated()
		 * instead of here.
		 * 
		 * this is because OnSufaceCreated() is called by/in the rendering thread, and gl
		 * calls can only be made by that thread.
		 */
	}
	
	private void drawButton(float xLeftNavScale, int buttonIdx, int infoIdx) {
		buttons[buttonIdx].draw(
				(xLeftNavScale*GfxElementInfo.leftNavButtonPositions[infoIdx].x),
				ver - ((ver*GfxElementInfo.leftNavButtonPositions[infoIdx].y)),  //"ver -" flips y axis
				//(ver*0.2f)+
				GfxElementInfo.leftNavButtonSize*ver*2,
				GfxElementInfo.leftNavButtonSize*ver*2);
	}

	@Override
	public void onDrawFrame(GL10 gl) 
	{
		float bg0x = ((hor/2) - (hor*gfxvhratio/2));
		//float bg0y = ((ver/2) - ((ver/gfxvhratio)/2));
		
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

		// Drawing
		gl.glTranslatef(0.0f, 0.0f, -5.0f);		// move 5 units INTO the screen
												// is the same as moving the camera 5 units away
		
		/* DRAWSCREEN BASED ON GAMESTATE */
		
		// left nav background (grey)
		float xLeftNavScale = (hor/2.f) - (ver/2.f);
		textures[7].draw(
				0+(xLeftNavScale/2.f),
				(ver/2.f),
				xLeftNavScale,
				ver
				);
		// draw left nav buttons
		/*
		 * buttons[0] = new Tex(gl, this.context, R.drawable.questionup);   //depress
			buttons[1] = new Tex(gl, this.context, R.drawable.playup); 		alternate
			buttons[2] = new Tex(gl, this.context, R.drawable.portalenterup);   //depress
			buttons[3] = new Tex(gl, this.context, R.drawable.portalexitfastup);  state
			buttons[4] = new Tex(gl, this.context, R.drawable.portalexitup);  		state
			buttons[5] = new Tex(gl, this.context, R.drawable.portalexitslowup);   state
			buttons[6] = new Tex(gl, this.context, R.drawable.binup);      //depress
			buttons[7] = new Tex(gl, this.context, R.drawable.xup);       //state
			buttons[8] = new Tex(gl, this.context, R.drawable.pauseup);   alternate
			// pressed
		buttons[9] = new Tex(gl, this.context, R.drawable.questiondown);
		buttons[10] = new Tex(gl, this.context, R.drawable.portalenterdown);
		buttons[11] = new Tex(gl, this.context, R.drawable.bindown);
		buttons[12] = new Tex(gl, this.context, R.drawable.xdown);
		 */
		// replay
		drawButton(xLeftNavScale, 0, 0);
		// play / pause
		if( gameState.getState() == GameState.STATE_PLAYING ) {
			drawButton(xLeftNavScale, 8, 1);
		} else {
			drawButton(xLeftNavScale, 1, 1);
		}
		// portal enter
		if( gameState.getInteractionState() == GameState.INTERACTION_STATE_NONE 
				|| gameState.getInteractionState() == GameState.INTERACTION_STATE_PORTAL_DELETE ) {
			drawButton(xLeftNavScale, 2, 2);
		} else {
			drawButton(xLeftNavScale, 10, 2);
		}
		// portal exit
		// - portal fast
		if( gameState.getCurrentPortalOutSpeed() != GameState.PORTAL_OUT_SPEED_FAST ) {
			drawButton(xLeftNavScale, 3, 3);
		} else {
			drawButton(xLeftNavScale, 13, 3);
		}
		// - portal normal
		if( gameState.getCurrentPortalOutSpeed() != GameState.PORTAL_OUT_SPEED_NORMAL ) {
			drawButton(xLeftNavScale, 4, 4);
		} else {
			drawButton(xLeftNavScale, 14, 4);
		}
		// - portal slow
		if( gameState.getCurrentPortalOutSpeed() != GameState.PORTAL_OUT_SPEED_SLOW ) {
			drawButton(xLeftNavScale, 5, 5);
		} else {
			drawButton(xLeftNavScale, 15, 5);
		}
		// bin
		if( gameState.getBinButtonState() == GameState.BUTTON_UNPRESSED ) {
			drawButton(xLeftNavScale, 6, 6);
		} else {
			drawButton(xLeftNavScale, 11, 6);
		}
		// delete
		if( gameState.getInteractionState() != GameState.INTERACTION_STATE_PORTAL_DELETE ) {
			drawButton(xLeftNavScale, 7, 7);
		} else {
			drawButton(xLeftNavScale, 12, 7);
		}
		
		// draw grid notes
		float xOffset = (hor/2.f)-(ver/2.f);
		float sizeFactor = 0.2f;
		for( int j = 0 ; j < 5 ; j++ ) {
			for( int i = 0 ; i < 5 ; i++ ) {
				int gridTex = gameState.getGridTex(i, j);
				textures[0].draw(
						xOffset + (ver*(sizeFactor*((float)i+0.5f))),
						ver - ((ver*(sizeFactor*((float)j+0.5f)))),  //"ver -" flips y axis
						//(ver*0.2f)+
						sizeFactor*ver,
						sizeFactor*ver);
				if( gridTex < 100 ) {
					textures[gridTex].draw(
							xOffset + (ver*(sizeFactor*((float)i+0.5f))),
							ver - ((ver*(sizeFactor*((float)j+0.5f)))),  //"ver -" flips y axis
							//(ver*0.2f)+
							sizeFactor*ver,
							sizeFactor*ver);
				} else {
					gridTex -= 100;
					textures[gridTex].draw(
							xOffset + (ver*(sizeFactor*((float)i+0.5f))),
							ver - ((ver*(sizeFactor*((float)j+0.5f)))),  //"ver -" flips y axis
							//(ver*0.2f)+
							sizeFactor*ver*0.5f,
							sizeFactor*ver*0.5f);
				}
			}
		}
		
		// draw modifiers
		for( int j = 0 ; j < 5 ; j++ ) {
			for( int i = 0 ; i < 5 ; i++ ) {
				int mod = gameState.getGridMod(i, j);
				if( mod != GameState.GRID_MOD_NONE ) {
					modifiers[mod-1].draw(
							xOffset + (ver*(sizeFactor*((float)i+0.5f))),
							ver - ((ver*(sizeFactor*((float)j+0.5f)))),  //"ver -" flips y axis
							//(ver*0.2f)+
							sizeFactor*ver,
							sizeFactor*ver);
				}
			}
		}
		
		// draw portals
		// -- temp portals
		// portal in
		if( gameState.getPortalInGrid() != null ) {
			Point portalIn = gameState.getPortalInGrid();
			portals[0].draw(
					xOffset + (ver*(sizeFactor*((float)portalIn.x+0.5f))),
					ver - ((ver*(sizeFactor*((float)portalIn.y+0.5f)))),  //"ver -" flips y axis
					//(ver*0.2f)+
					sizeFactor*ver,
					sizeFactor*ver);
		}
		// portal out
		if( gameState.getPortalOutGrid() != null ) {
			Point portalOut = gameState.getPortalOutGrid();
			int speed = gameState.getCurrentPortalOutSpeed();
			portals[1+speed].draw(
					xOffset + (ver*(sizeFactor*((float)portalOut.x+0.5f))),
					ver - ((ver*(sizeFactor*((float)portalOut.y+0.5f)))),  //"ver -" flips y axis
					//(ver*0.2f)+
					sizeFactor*ver,
					sizeFactor*ver);
		}
		// -- created portals
		for( int i = 0 ; i < GameState.MAX_PORTALS ; i++ ) {
			Point portalIn = gameState.getPortalIn(i);
			Point portalOut = gameState.getPortalOut(i);
			if( portalIn != null && portalOut != null ) {
				// portal in
				portals[0].draw(
						xOffset + (ver*(sizeFactor*((float)portalIn.x+0.5f))),
						ver - ((ver*(sizeFactor*((float)portalIn.y+0.5f)))),  //"ver -" flips y axis
						//(ver*0.2f)+
						sizeFactor*ver,
						sizeFactor*ver);
				// portal out
				int speed = gameState.getPortalOutSpeed(i); 
				portals[1+speed].draw(
						xOffset + (ver*(sizeFactor*((float)portalOut.x+0.5f))),
						ver - ((ver*(sizeFactor*((float)portalOut.y+0.5f)))),  //"ver -" flips y axis
						//(ver*0.2f)+
						sizeFactor*ver,
						sizeFactor*ver);
			}
		}
		
		// draw particle
		for( int i = 0 ; i < GameState.MAX_PARTICLES ; i++ ) {
			if( gameState.getParticleState(i) == GameState.PARTICLE_STATE_ON ) {
				PointF pos = gameState.getParticlePos(i);
				textures[6].draw(
						xOffset + (ver*pos.x),
						ver - ((ver*pos.y)),  //"ver -" flips y axis
						//(ver*0.2f)+
						sizeFactor*ver,
						sizeFactor*ver);
			}
		}
		
		// draw right hints
		//xLeftNavScale
		// draw bg
		textures[7].draw(
				ver + xOffset + (xLeftNavScale/2.f),
				(ver/2.f),
				xLeftNavScale,
				ver
				);
		// draw hints
		float hintScaleFactor = 0.5f;
		sizeFactor *= hintScaleFactor;
		for( int i = 0 ; i < gameState.getCurLevel().getSEQUENCE().length ; i++ ) {
			int seqElement = gameState.getCurLevel().getSEQUENCE()[i];
			if( seqElement != -1 ) {
				if( seqElement == 0 ) {
					seqElement = 7;
				}
				textures[seqElement].draw(
						ver + xOffset + (xLeftNavScale * 0.75f),
						ver - ((ver*(sizeFactor*((float)i+0.5f)))),  //"ver -" flips y axis
						sizeFactor*ver,
						sizeFactor*ver);
				// overlay speed if not normal speed
				int speed = gameState.getCurLevel().getGoalSpeeds()[i];
				if( speed == GameState.PORTAL_OUT_SPEED_FAST ) {
					buttons[3].draw( // fast
							ver + xOffset + (xLeftNavScale * 0.75f),
							ver - ((ver*(sizeFactor*((float)i+0.5f)))),  //"ver -" flips y axis
							sizeFactor*ver*0.5f,
							sizeFactor*ver*0.5f);
				} else if( speed == GameState.PORTAL_OUT_SPEED_SLOW ) {
					buttons[5].draw( // fast
							ver + xOffset + (xLeftNavScale * 0.75f),
							ver - ((ver*(sizeFactor*((float)i+0.5f)))),  //"ver -" flips y axis
							sizeFactor*ver*0.5f,
							sizeFactor*ver*0.5f);
				}
			}
		}
		// draw current list of last passed tiles
		for( int i = 0 ; i < gameState.getCurLevel().getSEQUENCE().length ; i++ ) {
			int seqElement = gameState.getCurLevel().getActiveSequence()[i];
			if( seqElement != -1 ) {
				if( seqElement == 0 ) {
					seqElement = 7;
				}
				textures[seqElement].draw(
						ver + xOffset + (xLeftNavScale * 0.25f),
						ver - ((ver*(sizeFactor*((float)i+0.5f)))),  //"ver -" flips y axis
						sizeFactor*ver,
						sizeFactor*ver);
				// overlay speed if not normal speed
				int speed = gameState.getCurLevel().getActiveSpeed()[i];
				if( speed == GameState.PORTAL_OUT_SPEED_FAST ) {
					buttons[3].draw( // fast
							ver + xOffset + (xLeftNavScale * 0.25f),
							ver - ((ver*(sizeFactor*((float)i+0.5f)))),  //"ver -" flips y axis
							sizeFactor*ver*0.5f,
							sizeFactor*ver*0.5f);
				} else if( speed == GameState.PORTAL_OUT_SPEED_SLOW ) {
					buttons[5].draw( // fast
							ver + xOffset + (xLeftNavScale * 0.25f),
							ver - ((ver*(sizeFactor*((float)i+0.5f)))),  //"ver -" flips y axis
							sizeFactor*ver*0.5f,
							sizeFactor*ver*0.5f);
				}
			}
		}
		
		// win
		if( gameState.getState() == GameState.STATE_WIN ) {
			screens[0].draw(
					hor / 2.f,
					ver / 2.f,
					hor,
					ver);
		} else if( gameState.getState() == GameState.STATE_REVIEW ) {
			screens[1].draw(
					hor / 2.f,
					ver / 2.f,
					hor,
					ver);
		} else if( gameState.getState() == GameState.STATE_FINISHED_LEVELS ) {
			screens[2].draw(
					hor / 2.f,
					ver / 2.f,
					hor,
					ver);
		}
		// intro
		if( gameState.getState() == GameState.STATE_TITLE_SCREEN ) {
			screens[3].draw(
					hor / 2.f,
					ver / 2.f,
					hor,
					ver);
		} else if( gameState.getState() == GameState.STATE_TUTORIAL_1 ) {
			screens[4].draw(
					hor / 2.f,
					ver / 2.f,
					hor,
					ver);
		} else if( gameState.getState() == GameState.STATE_TUTORIAL_2 ) {
			screens[5].draw(
					hor / 2.f,
					ver / 2.f,
					hor,
					ver);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(firsttime==true)
		{
			hor = width;
			ver = height;
			
			if(ver>hor)
			{
				ver = width;
				hor = height;
			}
			
			//hvratio = width/height;
			firsttime=false;
		
			//Log.d("SurfaceChanged","received width and height " + 
			//	Integer.valueOf(width).toString() + " " + Integer.valueOf(height).toString());
			
			if(height == 0) { 						//Prevent A Divide By Zero By
				height = 1; 						//Making Height Equal One
			}
	
			gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
			gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
			gl.glLoadIdentity(); 					//Reset The Projection Matrix
	
			//Calculate The Aspect Ratio Of The Window
			gl.glOrthof(0.0f, hor, 0.0f, ver, 10.0f, -10.0f);
	
			gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
			gl.glLoadIdentity(); 					//Reset The Modelview Matrix
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		/* enable transparency in textures */
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 

		/* cache the gl context for use with Tex obj */
		this.gl = gl;
	
		textures = new Tex[8];
		buttons = new Tex[16];
		portals = new Tex[4];
		screens = new Tex[6];
		modifiers = new Tex[4];
		
		// fullscreen images -- .25seconds loading total //
		textures[0] = new Tex(gl, this.context, R.drawable.tileground01);
		textures[1] = new Tex(gl, this.context, R.drawable.tilebeet01);
		textures[2] = new Tex(gl, this.context, R.drawable.tilebeet02);
		textures[3] = new Tex(gl, this.context, R.drawable.tilebeet03);
		textures[4] = new Tex(gl, this.context, R.drawable.tilebeet04);
		textures[5] = new Tex(gl, this.context, R.drawable.tilebeet05);
		textures[6] = new Tex(gl, this.context, R.drawable.doodad);
		textures[7] = new Tex(gl, this.context, R.drawable.backgroundgrey);
		
		// buttons
		buttons[0] = new Tex(gl, this.context, R.drawable.questionup);
		buttons[1] = new Tex(gl, this.context, R.drawable.playup);
		buttons[2] = new Tex(gl, this.context, R.drawable.portalenterup);
		buttons[3] = new Tex(gl, this.context, R.drawable.portalexitfastup);
		buttons[4] = new Tex(gl, this.context, R.drawable.portalexitup);
		buttons[5] = new Tex(gl, this.context, R.drawable.portalexitslowup);
		buttons[6] = new Tex(gl, this.context, R.drawable.binup);
		buttons[7] = new Tex(gl, this.context, R.drawable.xup);
		buttons[8] = new Tex(gl, this.context, R.drawable.pauseup);
		// pressed
		buttons[9] = new Tex(gl, this.context, R.drawable.questiondown);
		buttons[10] = new Tex(gl, this.context, R.drawable.portalenterdown);
		buttons[11] = new Tex(gl, this.context, R.drawable.bindown);
		buttons[12] = new Tex(gl, this.context, R.drawable.xdown);
		// portal out
		buttons[13] = new Tex(gl, this.context, R.drawable.portalexitfastdown);
		buttons[14] = new Tex(gl, this.context, R.drawable.portalexitdown);
		buttons[15] = new Tex(gl, this.context, R.drawable.portalexitslowdown);
		
		// portals
		portals[0] = new Tex(gl, this.context, R.drawable.tileportalblue);
		portals[1] = new Tex(gl, this.context, R.drawable.tileportalorange);
		portals[2] = new Tex(gl, this.context, R.drawable.tileportalorange);
		portals[3] = new Tex(gl, this.context, R.drawable.tileportalorange);
		
		// win
		screens[0] = new Tex(gl, this.context, R.drawable.screenlevelcomplete);
		screens[1] = new Tex(gl, this.context, R.drawable.screenlevelreview);
		screens[2] = new Tex(gl, this.context, R.drawable.screencredits);
		screens[3] = new Tex(gl, this.context, R.drawable.introscreen01);
		screens[4] = new Tex(gl, this.context, R.drawable.introscreen02);
		screens[5] = new Tex(gl, this.context, R.drawable.introscreen03);
		
		modifiers[0] = new Tex(gl, this.context, R.drawable.moddirectionup);
		modifiers[1] = new Tex(gl, this.context, R.drawable.moddirectionright);
		modifiers[2] = new Tex(gl, this.context, R.drawable.moddirectiondown);
		modifiers[3] = new Tex(gl, this.context, R.drawable.moddirectionleft);
	}
}
