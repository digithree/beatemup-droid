package com.begrud.beatemup.app;

import android.graphics.PointF;

public class GfxElementInfo {
	
	// NOTE: positions are in normalised screen coordinates, i.e. between 0.0 and 1.0
	
	// BUTTON
	public static final int leftNavButtonNum = 8;
	public static final float leftNavButtonSize = 0.1f;
	public static final float leftNavButtonSizeTouch = 0.16f;
	public static final PointF []leftNavButtonPositions = {
		new PointF(0.25f, 0.1f), // speaker
		new PointF(0.75f, 0.1f), // play
		new PointF(0.25f, 0.5f), // portal in
		new PointF(0.75f, 0.33f), // portal out - up
		new PointF(0.75f, 0.5f), // portal out - normal
		new PointF(0.75f, 0.66f), // portal out - down
		new PointF(0.25f, 0.9f), // delete all
		new PointF(0.75f, 0.9f) // delete
	};
	
	public static final float SPEED_NORMAL = 1.f;
	public static final float SPEED_FAST = 2.f;
	public static final float SPEED_SLOW = 0.5f;
}
