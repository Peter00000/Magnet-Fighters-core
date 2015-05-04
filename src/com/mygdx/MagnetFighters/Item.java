package com.mygdx.MagnetFighters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Item
{
	Sprite sprite;
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	Texture texture;
	float INITX;
	float INITY;
	final float DENSITY=0.5f;
	final float RESTITUTION=0f;
	final float FRICTION=1f;
	float width;
	float height;
	boolean contact=false;
	boolean destroy=false;
	boolean dead=false;
	int ID;
	
	public Item(Texture tex,float x,float y,float w, float h,int i)
	{
		texture=tex;
		INITX=x;
		INITY=y;
		width=w;
		height=h;
		ID=i;
	}
	
	public void create()
	{
		World temp=new World(new Vector2(0f,0f),false);
		temp.dispose();
		sprite=new Sprite(texture);
		sprite.setPosition(INITX, INITY);
		sprite.setSize(width,height);
		bodyDef=new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.fixedRotation=true;
		bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/Constants.PIXELS_TO_METERS,(sprite.getY()) / Constants.PIXELS_TO_METERS);
		fixtureDef=new FixtureDef();
		PolygonShape shape=new PolygonShape();
		shape.setAsBox(width/2/Constants.PIXELS_TO_METERS, height/2/Constants.PIXELS_TO_METERS);
		fixtureDef.shape=shape;
		fixtureDef.density = DENSITY;
		fixtureDef.restitution=RESTITUTION;
		fixtureDef.friction=FRICTION;
		fixtureDef.filter.categoryBits = Constants.PHYSICS_ENTITY;
		fixtureDef.filter.maskBits = Constants.PHYSICS_ENTITY|Constants.WORLD_ENTITY;
	}
	public void setPosition(float x,float y)
	{
		INITX=x;
		INITY=y;
	}
}