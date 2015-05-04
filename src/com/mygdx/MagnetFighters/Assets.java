package com.mygdx.MagnetFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets
{
	static final float scaledWidth = Gdx.graphics.getWidth()/Constants.PIXELS_TO_METERS;
	static final float scaledHeight = (Gdx.graphics.getHeight()/2)/Constants.PIXELS_TO_METERS;

	//Importing textures
	static final Texture menu=new Texture(Gdx.files.internal("Logo Screen2.png"));
	static final Texture computerlab=new Texture(Gdx.files.internal("background2.jpg"));
	static final Texture testtubes=new Texture(Gdx.files.internal("testtube.jpg"));
	static final Texture bandsaws=new Texture(Gdx.files.internal("bandsaw.jpg"));
	static final Texture winscreen=new Texture(Gdx.files.internal("You Win2.png"));
	static final Texture player1walk=new Texture(Gdx.files.internal("human.png"));
	static final Texture player1attack=new Texture(Gdx.files.internal("human_kick_calc.png"));
	static final Texture player2walk=new Texture(Gdx.files.internal("human2.png"));
	static final Texture player2attack=new Texture(Gdx.files.internal("human_kick_calc2.png"));
	static final Texture weapon=new Texture(Gdx.files.internal("ball.png"));
	static final Texture platformTexture=new Texture(Gdx.files.internal("platformcomp.jpg"));
	static final Texture calculatorTexture=new Texture(Gdx.files.internal("calculator.png"));
	static final Texture gradeTexture=new Texture(Gdx.files.internal("grade.png"));

	//Edge Platform settings
	static final boolean floor_enabled=false;
	static final EdgePlatform floor=new EdgePlatform(-scaledWidth*100,-scaledHeight,scaledWidth*100,-scaledHeight,0,0,0.05f);

	static final boolean leftedge_enabled=false;
	static final EdgePlatform leftedge=new EdgePlatform(-scaledWidth/2,-scaledHeight*100,-scaledWidth/2,scaledHeight*100,0,0,0f);

	static final boolean rightedge_enabled=false;
	static final EdgePlatform rightedge=new EdgePlatform(scaledWidth/2,-scaledHeight*100,scaledWidth/2,scaledHeight*100,0,0,0f);

	//Floating platforms
	static final FloatingPlatform[] complabPlatforms=
		{
		new FloatingPlatform(platformTexture,-675*Constants.ADJUSTED_WIDTH,-175*Constants.ADJUSTED_HEIGHT,2000*Constants.ADJUSTED_WIDTH,80*Constants.ADJUSTED_HEIGHT)
		};

	static final FloatingPlatform[] testtubePlatforms=
		{
		new FloatingPlatform(platformTexture,-770,145,
				new float[]
				{
				0f,0f, 
				2.3f,-0.2f,
				1f,-10f,
				-0.8f,-10f
							}),

		new FloatingPlatform(platformTexture,-435,120,
				new float[]
				{
				0f,0f, 
				2.1f,0.3f,
				5f,-10f,
				3f,-10f
						}),
						
		new FloatingPlatform(platformTexture,-118,107,
				new float[]
				{
				0f,0f, 
				2.1f,0.3f,
				5f,-10f,
				3f,-10f
						}),
		new FloatingPlatform(platformTexture,460,70,
				new float[]
				{
				0f,0f, 
				1.8f,0.4f,
				6f,-10f,
				4f,-10f
						}),
		};
	{
		for (int i=0;i<testtubePlatforms.length;i++)
			testtubePlatforms[i].friction=1f;
	}


	//Weapon settings
	static float weaponSize=45f;

	//Item settings
	static float initItems=0;
	
	static float labSpawn=-Gdx.graphics.getWidth()/2;
	
	static float[] tubeSpawn={-700f,-400f,-100f,480f};
	

}