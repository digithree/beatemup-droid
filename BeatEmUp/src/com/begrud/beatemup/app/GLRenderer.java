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
	
	private Tex win;
	
	
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
		
		// draw left nav buttons
		float xLeftNavScale = (hor/2.f) - (ver/2.f);
		for( int i = 0 ; i < GfxElementInfo.leftNavButtonNum ; i++ ) {
			int idx = i;
			if( i == GameState.BUTTON_PLAY_STOP ) {
				idx = gameState.getState() == GameState.STATE_PLAYING ? 8 : 1;
			}
			buttons[idx].draw(
					(xLeftNavScale*GfxElementInfo.leftNavButtonPositions[i].x),
					ver - ((ver*GfxElementInfo.leftNavButtonPositions[i].y)),  //"ver -" flips y axis
					//(ver*0.2f)+
					GfxElementInfo.leftNavButtonSize*ver,
					GfxElementInfo.leftNavButtonSize*ver);
		}

		/* draw grid */
		float xOffset = (hor/2.f)-(ver/2.f);
		float sizeFactor = 0.2f;
		for( int j = 0 ; j < 5 ; j++ ) {
			for( int i = 0 ; i < 5 ; i++ ) {
				int gridTex = gameState.getGridTex(i, j);
				textures[gridTex].draw(
						xOffset + (ver*(sizeFactor*((float)i+0.5f))),
						ver - ((ver*(sizeFactor*((float)j+0.5f)))),  //"ver -" flips y axis
						//(ver*0.2f)+
						sizeFactor*ver,
						sizeFactor*ver);
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
				textures[GameState.PARTICLE_TEX_DEFAULT].draw(
						xOffset + (ver*pos.x),
						ver - ((ver*pos.y)),  //"ver -" flips y axis
						//(ver*0.2f)+
						sizeFactor*ver,
						sizeFactor*ver);
			}
		}
		
		// win
		if( gameState.getState() == GameState.STATE_WIN ) {
			win.draw(
					hor / 2.f,
					ver / 2.f,
					ver / 2.f,
					ver / 4.f);
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
	
		textures = new Tex[7];	//annoyingly just 1 too many...
		buttons = new Tex[9];
		portals = new Tex[4];
		
		// fullscreen images -- .25seconds loading total //
		textures[0] = new Tex(gl, this.context, R.drawable.tilegreen);
		textures[1] = new Tex(gl, this.context, R.drawable.tilered);
		textures[2] = new Tex(gl, this.context, R.drawable.tileyellow);
		textures[3] = new Tex(gl, this.context, R.drawable.particle);
		textures[4] = new Tex(gl, this.context, R.drawable.tilepurple);
		textures[5] = new Tex(gl, this.context, R.drawable.tilewhite);
		textures[6] = new Tex(gl, this.context, R.drawable.tiledarkblue);
		
		// buttons
		buttons[0] = new Tex(gl, this.context, R.drawable.buttonspeaker);
		buttons[1] = new Tex(gl, this.context, R.drawable.buttonplay);
		buttons[2] = new Tex(gl, this.context, R.drawable.buttonportal);
		buttons[3] = new Tex(gl, this.context, R.drawable.buttonup);
		buttons[4] = new Tex(gl, this.context, R.drawable.buttonnormal);
		buttons[5] = new Tex(gl, this.context, R.drawable.buttondown);
		buttons[6] = new Tex(gl, this.context, R.drawable.buttondeleteall);
		buttons[7] = new Tex(gl, this.context, R.drawable.buttondelete);
		buttons[8] = new Tex(gl, this.context, R.drawable.buttonstop);
		
		// portals
		portals[0] = new Tex(gl, this.context, R.drawable.ringblue);
		portals[1] = new Tex(gl, this.context, R.drawable.ringred);
		portals[2] = new Tex(gl, this.context, R.drawable.ringredup);
		portals[3] = new Tex(gl, this.context, R.drawable.ringreddown);
		
		// win
		win = new Tex(gl, this.context, R.drawable.win);
	}
}
