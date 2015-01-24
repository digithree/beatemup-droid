package com.begrud.beatemup.app;

import android.graphics.Color;
import android.graphics.PointF;

public class GfxElementInfo {
	
	// DRAWING & CLICK INFO
	// 		indices correspond to those in GameState elements
	
	// positions are in normalised screen coordinates, i.e. between 0.0 and 1.0
	public static final PointF []drawingPositions = {
		// -= HOME MENU =-
		// gameLogo1/2
		new PointF(0.5f,0.25f), new PointF(0.75f,0.37f),
		// parentsNote
		new PointF(0.5f,0.5f),
		// -= GAME =-
		// stars
		new PointF(0.5f,0.1f),
		// item1/2/3
		new PointF(0.3f,0.35f), new PointF(0.5f,0.6f), new PointF(0.7f,0.35f),
		// smuggles
		new PointF(0.8f,0.66f),
		// success -- TODO what is this?
		new PointF(0.5f,0.5f)
	};
	
	// size (circle diameter), in normal percentage of screen height (full height = 1.0)
	//		note: negative values mean entire screen
	public static final float []drawingSizes = {
		// -= HOME MENU =-
		// gameLogo1/2
		0.19f, 0.19f,
		// parentsNote
		0.5f,
		// -= GAME =-
		// stars
		0.094f,		//row height used as scale
		// item1/2/3
		0.34f, 0.34f, 0.34f, //0.14f,0.14f,0.14f
		// smuggles
		0.44f,	//0.12f
		// success
		0.3f
	};
	
	// one array per element, each colour corresponds directly to a state
	public static final int [][]colors_debug = {
		// -= HOME MENU =-
		// gameLogo1/2
		{ Color.rgb(165, 214, 228), Color.rgb(45, 250, 55) },
		{ Color.rgb(165, 214, 228), Color.rgb(45, 250, 55) },
		// parentsNote
		{ Color.argb(0,0,0,0), Color.GRAY },
		// -= GAME =-
		// stars
		{ Color.argb(0,0,0,0), Color.rgb(150, 150, 26), Color.rgb(185, 185, 26),
				Color.rgb(220, 220, 26), Color.rgb(250, 250, 26) },
		// item1/2/3
		{ Color.argb(0,0,0,0), Color.rgb(165, 214, 228), Color.RED, Color.GREEN },
		{ Color.argb(0,0,0,0), Color.rgb(165, 214, 228), Color.RED, Color.GREEN },
		{ Color.argb(0,0,0,0), Color.rgb(165, 214, 228), Color.RED, Color.GREEN },
		// smuggles
		{ Color.rgb(87, 185, 198), Color.rgb(246, 71, 82), Color.rgb(104, 183, 42) },
		// success
		{ Color.argb(0,0,0,0), Color.WHITE }
	};

	// EXTRA (non animating) CLICK AREA INFO
	public static final int NUM_EXTRA_ELEMENTS = 4;
	// -= HOME MENU =-
	public static final int EX_PARENTNOTE_ICON = 0;
	// -= GAME =-
	public static final int EX_HOME = 1;
	public static final int EX_PAUSE = 2;
	public static final int EX_REPLAY = 3;
	
	public static final PointF []extraPositions = {
		// -= HOME MENU =-
		// parentNote icon
		new PointF(0.88f,0.84f),
		// -= GAME =-
		// home
		new PointF(0.88f,0.14f),
		// pause
		new PointF(0.5f,0.873f),
		// replay
		new PointF(0.096f,0.873f)
	};
	
	public static final float []extraSizes = {
		// -= HOME MENU =-
		// parentNote icon
		0.13f,
		// -= GAME =-
		// home
		0.1f,
		// pause
		0.08f,
		// replay
		0.08f
	};
	
	public static final int []extraColors_debug = {
		// -= HOME MENU =-
		// parentNote icon
		Color.rgb(249, 102, 17),
		// -= GAME =-
		// home
		Color.rgb(189, 167, 131),
		// pause
		Color.rgb(248, 248, 70),
		// replay
		Color.rgb(70, 164, 44)
	};
	
	// debug: names of game items
	public static final String [][]gameItemNames = {
		// colours
		{
			"dubh",
			"gorm",
			"glas",
			"liath",
			"oráiste",
			"bándearg",
			"corcra",
			"dearg",
			"bán",
			"buí"
		},
		// food
		{
			"úll",
			"banana",
			"cairéad",
			"seacláid",
			"criospaí",
			"iasc",
			"bainne",
			"oráiste",
			"pish",
			"prátí"
		}
	};
}
