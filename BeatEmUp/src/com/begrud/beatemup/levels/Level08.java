package com.begrud.beatemup.levels;

public class Level08 extends Level {
	
	public Level08() {
		super( 
				new int[]{        // sound grid: 0 - no tile, >= 1 - sounds
					1, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 4, 0, 0,
					0, 0, 5, 0, 2,
					1, 0, 0, 0, 3
				},
				new int[]{        //mod: 0 - none, 1 - up, 2 - right, 3 - down, 4 - left
					1, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0,
					0, 0, 0, 0, 0
				},
				new int[]{ 1, 3, 2, 1, 3, 2, 2 }, // sound sequence
				new int[]{ 0, 1, 1, 1, 1, 0, 0 }  // speed sequence: 0 - normal speed, 1 - fast, 2 - slow (same as GameState portal speeds)
			);
	}

}