package com.begrud.beatemup.levels;

public class Level04 extends Level {
	
	public Level04() {
		super( 
				new int[]{        // sound grid: 0 - no tile, >= 1 - sounds
					1, 0, 0, 0, 2,
					0, 0, 0, 0, 1,
					0, 0, 0, 0, 0,
					0, 0, 2, 0, 0,
					0, 2, 4, 0, 4
				},
				new int[]{        //mod: 0 - none, 1 - up, 2 - right, 3 - down, 4 - left
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 1, 0, 0
				},
				new int[]{ 1, 2, 4, 2, 1, 2 }, // sound sequence
				new int[]{ 0, 0, 0, 0, 0, 0 }  // speed sequence: 0 - normal speed, 1 - fast, 2 - slow (same as GameState portal speeds)
			);

	}

}