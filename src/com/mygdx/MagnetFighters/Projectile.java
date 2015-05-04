package com.mygdx.MagnetFighters;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile extends Item
{
	final float ANGULAR_DAMPING=10f;
	final boolean regenerate=false;

	public Projectile(Texture tex,float x,float y,float w,float h,int id) 
	{
		super(tex, x, y, w, h,id);
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
		bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/Constants.PIXELS_TO_METERS,(sprite.getY()) / Constants.PIXELS_TO_METERS);
		bodyDef.angularDamping=ANGULAR_DAMPING;
		fixtureDef=new FixtureDef();
		CircleShape shape=new CircleShape();
		shape.setRadius(sprite.getWidth()/2/Constants.PIXELS_TO_METERS);
		fixtureDef.shape=shape;
		fixtureDef.density = DENSITY;
		fixtureDef.restitution=RESTITUTION;
		fixtureDef.friction=FRICTION;
		fixtureDef.filter.categoryBits = Constants.PHYSICS_ENTITY;
		fixtureDef.filter.maskBits = Constants.PHYSICS_ENTITY|Constants.WORLD_ENTITY;
	}
	
	public void launch(boolean direction,float launchVelocity)
	{
		if (direction)
			body.applyLinearImpulse(launchVelocity, 0f, body.getPosition().x, body.getPosition().y, true);
		else
			body.applyLinearImpulse(-launchVelocity, 0f, body.getPosition().x, body.getPosition().y, true);
	}
	
}