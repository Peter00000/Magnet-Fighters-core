package com.mygdx.MagnetFighters;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

public class EdgePlatform
{
	final float PIXELS_TO_METERS = 100f;
	float friction=0.5f;
	Body body;
	Vector2 v1;
	Vector2 v2;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	float posX;
	float posY;
    final short PHYSICS_ENTITY = 0x1;    // 0001
    final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
	
	public EdgePlatform(float v1x, float v1y, float v2x, float v2y, float x, float y, float f)
	{
		v1=new Vector2(v1x, v1y);
		v2=new Vector2(v2x, v2y);
		posX=x;
		posY=y;
		friction=f;
	}
	
	public void createPlatform()
	{
		World temp=new World(new Vector2(0,0), false);
		temp.dispose();
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(posX,posY));
		fixtureDef = new FixtureDef();
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(v1,v2);
		fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = WORLD_ENTITY;
        fixtureDef.filter.maskBits = PHYSICS_ENTITY;
		fixtureDef.friction=friction;
	}
}