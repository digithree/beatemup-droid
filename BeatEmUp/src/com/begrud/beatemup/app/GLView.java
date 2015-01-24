package com.begrud.beatemup.app;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GLView extends GLSurfaceView 
{
	
	//private SurfaceHolder holder;
	
	private GameLogicThread stateLoopThread;
	//private RenderThread renderThread;
	private GameState gameState;
	
	RectF windowBounds = null;
	
	private SurfaceHolder holder;	/* why do we need this as a persistent field in the obj? */
	//private Context context;		/* why do we need to cache this? */
	
	public GLView(final Context context)
	{
		super(context);

		gameState = new GameState();
		
		/* this "launches" the rendering thread */
		this.setRenderer(new GLRenderer(context, gameState));
		
		/* 
		 * this launches the game logic thread 
		 * 
		 * why do we have to launch the logic thread in this way?
		 * 
		 * what is getHolder()?  what is SurfaceHolder?  why is it
		 * all being done via callbacks?
		 * 
		 * WHY?
		 * 
		 * find out later.
		 * 
		 * */

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
           @Override
           public void surfaceDestroyed(SurfaceHolder holder) {
        	   if( stateLoopThread != null ) {
                  boolean retry = true;
                  stateLoopThread.setRunning(false);
                  while (retry) {
                     try {
                    	 stateLoopThread.join();
                           retry = false;
                     } catch (InterruptedException e) {
                     }
                  }
        	   }
           }

           @Override
           public void surfaceCreated(SurfaceHolder holder) {
        	   stateLoopThread = new GameLogicThread(context, gameState);
        	   stateLoopThread.setRunning(true);
        	   stateLoopThread.start();
           }

           @Override
           public void surfaceChanged(SurfaceHolder holder, int format,
                         int width, int height) {
        	   //float bgBitmapRatio = 768.f/1024.f;						// <--- HARD CODED!
        	   //float windowWidth = ((float)width) *  bgBitmapRatio;
        	   //float spacingX = (width - windowWidth)/2;
        	   //windowBounds = new RectF(spacingX,0,(float)width-spacingX,(float)height);
        	   float xOffset = (width/2.f)-(height/2.f); 
        	   windowBounds = new RectF(xOffset, 0, width - xOffset, height);
        	   //System.out.println("Screen dim: "+width+" x "+height);
        	   //System.out.println("Window dim: "+windowBounds.width()+" x "+windowBounds.height());
           }
        });

	}
	
	@Override
    public boolean onTouchEvent(final MotionEvent event) 
    {
		/* Drop in DEMO touch event handling here */
		
    	if( event.getAction() == MotionEvent.ACTION_DOWN ) {
    		//Log.d("onTouchEvent","ACTION_DOWN: ("+event.getX()+", "+event.getY()+")");
    		if( windowBounds != null ) {
	    		PointF pos = new PointF(event.getX(), event.getY());
	    		if( pos.x >= windowBounds.left && pos.x < windowBounds.right ) {
	    			pos.x -= windowBounds.left;
	    			pos.x /= windowBounds.width();
	    			pos.y /= windowBounds.height();
	    			//Log.d("onTouchEvent","send to stateLoopThread: ("+pos.x+", "+pos.y+")");
	    			stateLoopThread.touch(pos);
	    		}
    		} //else {
    			//Log.d("onTouchEvent","windowBounds are null!");
    		//}
    	}
    	return true;
		
        //queueEvent(new Runnable(){
        //    public void run() {
        //        mRenderer.setColor(event.getX() / getWidth(),
        //                event.getY() / getHeight(), 1.0f);
        //    }});
        //return true;
    }
}
