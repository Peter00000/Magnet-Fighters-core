package com.mygdx.MagnetFighters;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;

public class FloatingPlatform
{
	float friction;
	Texture image;
	Sprite sprite;
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
    float posX,posY,width,height;
    Fixture contactFixture;
    PolygonShape shape;
    boolean isBox;
    float[] vertices;

	public FloatingPlatform(Texture im, float x, float y, float w, float h)
	{
		image=im;
	    posX=x;
	    posY=y;
	    width=w;
	    height=h;
		World temp=new World(new Vector2(0f,0f),false);
		temp.dispose();
		shape=new PolygonShape();
		shape.setAsBox(width/Constants.PIXELS_TO_METERS/2, height/Constants.PIXELS_TO_METERS/2);
		isBox=true;
	}
	
	public FloatingPlatform(Texture im, float x, float y, float[]v)
	{
		image=im;
	    posX=x;
	    posY=y;
		World temp=new World(new Vector2(0f,0f),false);
		temp.dispose();
		shape=new PolygonShape();
		shape.set(v);
		isBox=false;
		vertices=v;
	}
	
	public void createFloatingPlatform()
	{
		World temp=new World(new Vector2(0f,0f),false);
		temp.dispose();
		sprite=new Sprite(image,(int)width,(int)height);
		bodyDef=new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(posX/Constants.PIXELS_TO_METERS,posY/Constants.PIXELS_TO_METERS));
		fixtureDef = new FixtureDef();
		fixtureDef.shape=shape;
		if (Constants.stage==1)
			friction=Constants.COMPLAB_FRICTION;
		if (Constants.stage==2)
			friction=Constants.TESTTUBE_FRICTION;
		if (Constants.stage==3)
			friction=Constants.BANDSAW_FRICTION;
		fixtureDef.friction=friction;
		sprite.setPosition(bodyDef.position.x*Constants.PIXELS_TO_METERS-sprite.getWidth()/2, bodyDef.position.y*Constants.PIXELS_TO_METERS-sprite.getHeight()/2);
	}
	
	public void setContactFixture()
	{
		PolygonShape s=new PolygonShape();
		if (isBox)
			s.setAsBox(width/Constants.PIXELS_TO_METERS/2-0.05f, 0.01f,new Vector2(body.getLocalCenter().x,body.getLocalCenter().y+height/2/Constants.PIXELS_TO_METERS+0.01f),0f);
		else
			s.set(new float[]
					{
					vertices[0],vertices[1]+0.05f,vertices[2],vertices[3]+0.05f,vertices[2],vertices[3],vertices[0],vertices[1]
					});
		fixtureDef.shape=s;
        fixtureDef.filter.categoryBits = Constants.WORLD_ENTITY;
        fixtureDef.filter.maskBits = Constants.PHYSICS_ENTITY;
		fixtureDef.friction=friction;
	}
}