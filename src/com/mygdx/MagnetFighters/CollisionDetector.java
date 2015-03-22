package com.mygdx.MagnetFighters;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionDetector implements ContactListener
{
	Player[] players;
	FloatingPlatform[] platforms;
	EdgePlatform[] edges;
	
	@Override
	public void beginContact(Contact contact) 
	{
		for (int i=0;i<players.length;i++)
			for (int j=0;j<platforms.length;j++)
			{
				if((contact.getFixtureA() == platforms[j].contactFixture &&
						contact.getFixtureB().getBody() == players[i].body)
						||
						(contact.getFixtureA().getBody() == players[i].body &&
						contact.getFixtureB() == platforms[j].contactFixture)) 
				{

					players[i].isJumping=false;
				}
			}
		for (int i=0;i<players.length;i++)
			for (int j=0;j<edges.length;j++)
			{
				if((contact.getFixtureA().getBody() == edges[j].body &&
						contact.getFixtureB().getBody() == players[i].body)
						||
						(contact.getFixtureA().getBody() == players[i].body &&
						contact.getFixtureB().getBody()==edges[j].body)) 
				{

					players[i].isJumping=false;
				}
			}
		if((contact.getFixtureA() == players[0].contactFixture &&
				contact.getFixtureB().getBody()==players[1].body)
				||
				(contact.getFixtureA().getBody()==players[1].body &&
				contact.getFixtureB() == players[0].contactFixture))  
		{
			if (players[0].isJumping==false)
				players[1].isJumping=false;
		}
		
		if((contact.getFixtureA() == players[1].contactFixture &&
				contact.getFixtureB().getBody()==players[0].body)
				||
				(contact.getFixtureA().getBody()==players[0].body &&
				contact.getFixtureB() == players[1].contactFixture)) 
		{
			if (players[1].isJumping==false)
				players[0].isJumping=false;
		}
		
		if((contact.getFixtureA().getBody() == players[1].body &&
				contact.getFixtureB().getBody()==players[0].body)
				||
				(contact.getFixtureA().getBody()==players[0].body &&
				contact.getFixtureB().getBody() == players[1].body)) 
		{
			players[0].checkKick=true;
			players[1].checkKick=true;
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		if((contact.getFixtureA().getBody() == players[1].body &&
				contact.getFixtureB().getBody()==players[0].body)
				||
				(contact.getFixtureA().getBody()==players[0].body &&
				contact.getFixtureB().getBody() == players[1].body)) 
		{
			players[0].checkKick=false;
			players[1].checkKick=false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		// TODO Auto-generated method stub
		
	}
	
}