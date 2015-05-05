package com.mygdx.MagnetFighters;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionDetector implements ContactListener
{
	Player[] players;
	FloatingPlatform[] platforms;
	EdgePlatform[] edges;
	ArrayList<Projectile> projectiles;
	ArrayList<Item> items;

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
					players[i].lockPlayerLeft=false;
					players[i].lockPlayerRight=false;
				}

				else if((contact.getFixtureA() == platforms[j].body.getFixtureList().get(0) &&
						contact.getFixtureB().getBody() == players[i].body)
						||
						(contact.getFixtureA().getBody() == players[i].body &&
						contact.getFixtureB() == platforms[j].body.getFixtureList().get(0))) 
				{
					if (players[i].facingLeft)
						players[i].lockPlayerLeft=true;
					else
						players[i].lockPlayerRight=true;
				}
			}
		if (edges[0]!=null)
		{
			for (int i=0;i<players.length;i++)
				for (int j=0;j<edges.length;j++)
				{
					if (edges[j]==null)
						continue;
					if((contact.getFixtureA().getBody() == edges[j].body &&
							contact.getFixtureB().getBody() == players[i].body)
							||
							(contact.getFixtureA().getBody() == players[i].body &&
							contact.getFixtureB().getBody()==edges[j].body)) 
					{

						players[i].isJumping=false;
					}
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
			players[0].contact=true;
			players[1].contact=true;
		}
		if (!projectiles.isEmpty())
		{
			for (int i=0;i<projectiles.size();i++)
			{
				for (int j=0;j<players.length;j++)
				{
					if((contact.getFixtureA().getBody() == players[j].body &&
							contact.getFixtureB().getBody()==projectiles.get(i).body)
							||
							(contact.getFixtureA().getBody()==projectiles.get(i).body &&
							contact.getFixtureB().getBody() == players[j].body)) 
					{
						if (!projectiles.get(i).dead)
						{
							projectiles.get(i).contact=true;
							if (players[j].facingLeft)
								players[j].getKicked(true,0.8f,10f);
							else
								players[j].getKicked(false,0.8f,10f);
							projectiles.get(i).contact=false;
							projectiles.get(i).destroy=true;
						}
					}
				}
			}
		}
		if (items!=null)
		{
			for (int i=0;i<items.size();i++)
			{
				for (int j=0;j<players.length;j++)
				{
					if((contact.getFixtureA().getBody() == players[j].body &&
							contact.getFixtureB().getBody()==items.get(i).body)
							||
							(contact.getFixtureA().getBody()==items.get(i).body &&
							contact.getFixtureB().getBody() == players[j].body)) 
					{
						items.get(i).contact=true;
						items.get(i).destroy=true;
						if (items.get(i).ID==1)
						{
							players[j].calculatorEquipped=true;
							players[j].calculatorDuration=100f;
						}
						if (items.get(i).ID==2)
						{
							players[j].collectHealth=true;
							players[j].health+=20f;
							if (players[j].health>100f)
								players[j].health=100f;
						}
					}
				}
			}
		}
		if (items!=null&&projectiles!=null)
		{
			for (int i=0;i<items.size();i++)
				for (int j=0;j<projectiles.size();j++)
				{
					if((contact.getFixtureA().getBody() == projectiles.get(j).body &&
							contact.getFixtureB().getBody()==items.get(i).body)
							||
							(contact.getFixtureA().getBody()==items.get(i).body &&
							contact.getFixtureB().getBody() == projectiles.get(j).body)) 
					{
						projectiles.get(j).destroy=true;
					}
				}
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
			players[0].contact=false;
			players[1].contact=false;
		}
		for (int i=0;i<projectiles.size();i++)
		{
			for (int j=0;j<players.length;j++)
			{
				if((contact.getFixtureA().getBody() == players[j].body &&
						contact.getFixtureB().getBody()==projectiles.get(i).body)
						||
						(contact.getFixtureA().getBody()==projectiles.get(i).body &&
						contact.getFixtureB().getBody() == players[j].body)) 
				{
					projectiles.get(i).contact=false;
					projectiles.get(i).destroy=true;
				}
			}
		}
		for (int i=0;i<items.size();i++)
		{
			for (int j=0;j<players.length;j++)
			{
				if((contact.getFixtureA().getBody() == players[j].body &&
						contact.getFixtureB().getBody()==items.get(i).body)
						||
						(contact.getFixtureA().getBody()==items.get(i).body &&
						contact.getFixtureB().getBody() == players[j].body)) 
				{
					items.get(i).contact=false;
				}
			}
		}
		for (int i=0;i<projectiles.size();i++)
			for (int j=0;j<projectiles.size();j++)
			{
				if((contact.getFixtureA().getBody() == projectiles.get(i).body &&
						contact.getFixtureB().getBody()==projectiles.get(j).body)
						||
						(contact.getFixtureA().getBody()==projectiles.get(j).body &&
						contact.getFixtureB().getBody() == projectiles.get(i).body)) 
				{
					if (i!=j)
					{
						projectiles.get(i).destroy=true;
						projectiles.get(j).destroy=true;
					}
				}
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