package com.begrud.beatemup.app;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	/* The OpenGL view */
	private GLView glView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initiate the Open GL view and
        // create an instance with this activity
		glView = new GLView(this);
        //glSurfaceView = new GLSurfaceView(this);
        
        // set our renderer to be the main renderer with
        // the current activity context
        
		/* this "launches" the render thread & logic thread */
        setContentView(glView);
    }

	@Override
	protected void onResume() {
		super.onResume();
		//glSurfaceView.onResume();
		glView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//glSurfaceView.onPause();
		glView.onPause();
	}
}
