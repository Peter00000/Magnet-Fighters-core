package com.mygdx.MagnetFighters;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;

public class Player
{
	Sprite sprite;
	Texture walkSheet;
	Texture attackSheet;
	TextureRegion[] walkFrames;
	TextureRegion[] attackFrames;
	TextureRegion currentFrame;
	boolean facingLeft;
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	final float DENSITY=0.5f;
	final float RESTITUTION=0f;
	final float FRICTION=0.7f;
	int frameRows;
	int frameCols;
	Animation walkAnimation;
	boolean isJumping;
	float stateTime;
	final float INITX=20f;
	final float INITY=500f;
	final float XVELOCITY=3f;
	final float YVELOCITY=7.5f;
	final float FALLSPEED=1f;
	final int INITIAL_SPRITE_INDEX=6;
	final int JUMP_SPRITE_INDEX=3;
	final float MASS=80f;
	Color color;
	Fixture contactFixture;
	int numFootContacts;
	boolean kick=false;
	int kick_iterator=0;
	boolean contact=false;
	boolean isKicked=false;
	boolean kickAttack=false;
	FixtureDef box;
	float health=Constants.INITIAL_HEALTH;
	float invincibility;
	boolean kickColor=false;
	boolean calculatorEquipped=false;
	boolean calculatorAttack;
	float calculatorDuration=100f;
	boolean lockPlayerRight=false;
	boolean lockPlayerLeft=false;


	public Player(Texture sheet,Texture attack, int rows, int cols)
	{
		frameRows=rows;
		frameCols=cols;
		walkSheet=sheet;
		attackSheet=attack;
		numFootContacts=0;
	}

	public void createPlayer()
	{
		World temp=new World(new Vector2(0f,0f),false);
		temp.dispose();
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()
				/frameCols, walkSheet.getHeight()/frameRows);
		TextureRegion[][] tmp2 = TextureRegion.split(attackSheet, attackSheet.getWidth()
				/frameCols, attackSheet.getHeight()/frameRows);
		walkFrames=new TextureRegion[frameCols*frameRows];
		attackFrames=new TextureRegion[frameCols*frameRows];
		int index=0;
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameCols; j++) 
			{
				walkFrames[index] = tmp[i][j];
				attackFrames[index]=tmp2[i][j];
				index++;
			}
		}
		walkAnimation = new Animation(0.025f, walkFrames);
		sprite=new Sprite(walkFrames[INITIAL_SPRITE_INDEX]);
		if (Constants.stage==1)
			sprite.setPosition(-(float)Math.random()*Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*2);
		if (Constants.stage==2)
		{
			int rand=(int)(Math.random()*4);
			if (rand==0)
				sprite.setPosition(-700, Gdx.graphics.getHeight()*2);
			if (rand==1)
				sprite.setPosition(-400,Gdx.graphics.getHeight()*2);
			if (rand==2)
				sprite.setPosition(-100,Gdx.graphics.getHeight()*2);
			if (rand==3)
				sprite.setPosition(480,Gdx.graphics.getHeight()*2);
		}
		if (Constants.stage==3)
		{
			int rand=(int)(Math.random()*2);
			if (rand==0)
				sprite.setPosition((float)Math.random()*(Gdx.graphics.getWidth()/2+Assets.bandsawSpawn[0])-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*2);
			else
				sprite.setPosition((float)Math.random()*(Gdx.graphics.getWidth()/2-Assets.bandsawSpawn[1])+Assets.bandsawSpawn[1], Gdx.graphics.getHeight()*2);
		}
		bodyDef=new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.fixedRotation=true;
		bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/Constants.PIXELS_TO_METERS,(sprite.getY()) / Constants.PIXELS_TO_METERS);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((sprite.getWidth()/2-20)/ Constants.PIXELS_TO_METERS, (sprite.getHeight()
				/2-20)/ Constants.PIXELS_TO_METERS);		
		fixtureDef=new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = DENSITY;
		fixtureDef.restitution=RESTITUTION;
		fixtureDef.friction=FRICTION;
		stateTime=0f;
		fixtureDef.filter.categoryBits = Constants.PHYSICS_ENTITY;
		fixtureDef.filter.maskBits = Constants.PHYSICS_ENTITY|Constants.WORLD_ENTITY;
		box=fixtureDef;
	}

	public void jump()
	{
		if (!isJumping)
		{
			body.setLinearVelocity(body.getLinearVelocity().x, YVELOCITY+body.getLinearVelocity().y);	
			isJumping=true;
		}
	}

	public void fallFast()
	{
		body.setLinearVelocity(body.getLinearVelocity().x, -FALLSPEED+body.getLinearVelocity().y);
	}

	public void moveRight()
	{
		if (!isJumping)
		{
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		}
		else
			currentFrame=attackFrames[JUMP_SPRITE_INDEX];
		if (kick)
			currentFrame=attackFrames[5];
		sprite.setRegion(currentFrame);
		facingLeft=false;
		body.setLinearVelocity(XVELOCITY,body.getLinearVelocity().y);
		sprite.setX((int) (body.getPosition().x*Constants.PIXELS_TO_METERS));
		sprite.setY((int) (body.getPosition().y*Constants.PIXELS_TO_METERS));
		sprite.setPosition((body.getPosition().x * Constants.PIXELS_TO_METERS) - sprite.getWidth()/2-5,(body.getPosition().y * Constants.PIXELS_TO_METERS) -sprite.getHeight()/2+10);
	}

	public void moveLeft()
	{
		if (!isJumping)
		{
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		}
		else
			currentFrame=attackFrames[JUMP_SPRITE_INDEX];
		if (kick)
			currentFrame=attackFrames[5];
		sprite.setRegion(currentFrame);
		facingLeft=true;
		body.setLinearVelocity(-XVELOCITY,body.getLinearVelocity().y);
		sprite.setPosition((body.getPosition().x * Constants.PIXELS_TO_METERS) - sprite.getWidth()/2-5,(body.getPosition().y * Constants.PIXELS_TO_METERS) -sprite.getHeight()/2+10);
	}

	public void stayStationary()
	{
		body.setLinearVelocity((float)(body.getLinearVelocity().x/1.1),body.getLinearVelocity().y);
		currentFrame=walkFrames[INITIAL_SPRITE_INDEX];
		if (kick)
			currentFrame=attackFrames[5];
		if (calculatorAttack)
			currentFrame=attackFrames[12];
		sprite.setPosition((body.getPosition().x * Constants.PIXELS_TO_METERS) - sprite.getWidth()/2-5,(body.getPosition().y * Constants.PIXELS_TO_METERS) -sprite.getHeight()/2+10);
		sprite.setRegion(currentFrame);
	}

	public void updatePosition()
	{
		sprite.setPosition((body.getPosition().x * Constants.PIXELS_TO_METERS) - sprite.getWidth()/2,(body.getPosition().y * Constants.PIXELS_TO_METERS) -sprite.getHeight()/2 );
	}

	public BodyDef getBodyDef()
	{
		return bodyDef;
	}

	public FixtureDef getFixtureDef()
	{
		return fixtureDef;
	}

	public void setContactFixture()
	{
		PolygonShape shape=new PolygonShape();
		shape.setAsBox((sprite.getWidth()/2)/Constants.PIXELS_TO_METERS/2-0.15f, 0.01f,new Vector2(body.getLocalCenter().x,body.getLocalCenter().y+(sprite.getHeight()
				/2+20)/2/Constants.PIXELS_TO_METERS+0.01f),0f);
		fixtureDef.shape=shape;
		fixtureDef.filter.categoryBits = Constants.WORLD_ENTITY;
		fixtureDef.filter.maskBits = Constants.PHYSICS_ENTITY;
		fixtureDef.friction=FRICTION;
	}

	public void getKicked(boolean goLeft,float kickPower,boolean kickDamage)
	{
		body.setLinearVelocity(new Vector2(0f,0f));
		if (goLeft)
			body.applyLinearImpulse(-kickPower,0.4f,body.getPosition().x,body.getPosition().y,true);
		else
			body.applyLinearImpulse(kickPower,0.4f,body.getPosition().x,body.getPosition().y,true);
		if (kickDamage)
		{
			health-=5f;
			kickColor=true;
			if (health<0)
				health=0;
		}
		isKicked=true;
	}

}
