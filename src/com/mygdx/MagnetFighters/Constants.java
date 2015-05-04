package com.mygdx.MagnetFighters;

import com.badlogic.gdx.Gdx;

public class Constants
{
	final static int FRAMEROWS=2;
	final static int FRAMECOLS=8;
	final static float PIXELS_TO_METERS = 100f;
	final static float GRAVITY=-9.8f;
	final static float STEP=1/40f;
    final static short PHYSICS_ENTITY = 0x1;    // 0001
    final static short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
    final static float INITIAL_HEALTH=100f;
	final static int ADJUSTED_WIDTH=Gdx.graphics.getWidth()/1920;
	final static int ADJUSTED_HEIGHT=Gdx.graphics.getWidth()/1080;
	final static float PUNCH_SOUND_LENGTH=0.2f;
	final static float JUMP_SOUND_LENGTH=0.2f;
	final static float ITEM_SOUND_LENGTH=0.35f;
	final static float IMPACT_SOUND_LENGTH=0.4f;
	final static boolean DEBUG=true;
	final static float COMPLAB_FRICTION=0.5f;
	final static float TESTTUBE_FRICTION=1f;
	static int stage=1;
}