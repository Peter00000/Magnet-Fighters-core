package com.mygdx.MagnetFighters;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;

public class FloatingPlatform
{
	final float PIXELS_TO_METERS = 100f;
	float friction=0.1f;
	Texture image;
	Sprite sprite;
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
    final short PHYSICS_ENTITY = 0x1;    // 0001
    final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
    float posX,posY,width,height;
    Fixture contactFixture;

	public FloatingPlatform(Texture im, float x, float y, float w, float h)
	{
		image=im;
	    posX=x;
	    posY=y;
	    width=w;
	    height=h;
	}
	
	public void createFloatingPlatform()
	{
		World temp=new World(new Vector2(0f,0f),false);
		temp.dispose();
		sprite=new Sprite(image,(int)width,(int)height);
		bodyDef=new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(posX/PIXELS_TO_METERS,posY/PIXELS_TO_METERS));
		fixtureDef = new FixtureDef();
		PolygonShape shape=new PolygonShape();
		shape.setAsBox(width/PIXELS_TO_METERS/2, height/PIXELS_TO_METERS/2);
		fixtureDef.shape=shape;
		fixtureDef.friction=0f;
		sprite.setPosition(bodyDef.position.x*PIXELS_TO_METERS-sprite.getWidth()/2, bodyDef.position.y*PIXELS_TO_METERS-sprite.getHeight()/2);
	}
	
	public void setContactFixture()
	{
		PolygonShape shape=new PolygonShape();
		shape.setAsBox(width/PIXELS_TO_METERS/2-0.05f, 0.01f,new Vector2(body.getLocalCenter().x,body.getLocalCenter().y+height/2/PIXELS_TO_METERS+0.01f),0f);
		fixtureDef.shape=shape;
        fixtureDef.filter.categoryBits = WORLD_ENTITY;
        fixtureDef.filter.maskBits = PHYSICS_ENTITY;
		fixtureDef.friction=friction;
	}
}