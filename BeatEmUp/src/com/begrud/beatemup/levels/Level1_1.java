package com.begrud.beatemup.levels;

import android.util.Log;

public class Level1_1 extends Level {
	
	public Level1_1() {
		super( 
				new int[]{
					1, 2, 0, 0, 2,
					0, 0, 0, 0, 0,
					0, 0, 1, 1, 2,
					0, 2, 0, 0, 0,
					2, 1, 0, 1, 0
				},
				5,
				new int[]{ 1, 2, 1, 1, 2 }, // sound sequence
				new int[]{ 0, 0, 1, 1, 0 }  // speed sequence
			);
		init();
	}
	
	@Override
	public void init() {
		Log.d("Level1_1","Loaded grid sounds");
	}

}
