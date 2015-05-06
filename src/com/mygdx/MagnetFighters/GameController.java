package com.mygdx.MagnetFighters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GameController implements Screen{

	/*Game global variables*/
	SpriteBatch batch;
	World world;
	BitmapFont font;
	OrthographicCamera camera;
	Matrix4 DebugMatrix;
	MagnetFighters game;

	/*Game indicator booleans*/
	boolean gameState=true;

	/*Renderers*/
	Box2DDebugRenderer boxDebugRender;
	ShapeRenderer shapeRenderer;

	/*Game constants*/
	final int ADJUSTED_WIDTH=Gdx.graphics.getWidth()/1920;
	final int ADJUSTED_HEIGHT=Gdx.graphics.getWidth()/1080;

	/*Player and platform fields*/
	Player player1,player2;
	EdgePlatform floor,leftedge,rightedge;
	FloatingPlatform[] platforms;
	ArrayList<Projectile> projectiles;
	ArrayList<Item>items;


	/*Texture, sound, background and other retributes*/
	Texture background;
	Texture winScreen;

	/*The default constructor*/
	public GameController(final MagnetFighters g) 
	{
		super();
		game=g;
		importAssets();

	}
	public void importAssets()
	{
		/*Game initialization*/
		batch = new SpriteBatch();
		world = new World(new Vector2(0, Constants.GRAVITY), true);
		
		TextureRegion[][] tmp = TextureRegion.split(Assets.calcButtonSheet, Assets.calcButtonSheet.getWidth()
				/2, Assets.calcButtonSheet.getHeight()/2);
		Assets.calcButtons=new TextureRegion[4];
		int index=0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) 
			{
				Assets.calcButtons[index] = tmp[i][j];
				index++;
			}
		}

		if (Assets.stage==1)
			background=Assets.computerlab;
		if (Assets.stage==2)
			background=Assets.testtubes;
		if (Assets.stage==3)
			background=Assets.bandsaws;
		winScreen=Assets.winscreen;
		
		Texture walkSheet=Assets.player1walk;
		Texture attackSheet=Assets.player1attack;
		player1=new Player(walkSheet,attackSheet,Constants.FRAMEROWS,Constants.FRAMECOLS);
		player1=preparePlayer(player1);
		Texture walkSheet2=Assets.player2walk;
		Texture attackSheet2=Assets.player2attack;
		player2=new Player(walkSheet2,attackSheet2,Constants.FRAMEROWS,Constants.FRAMECOLS);
		player2=preparePlayer(player2);
		
		if (Assets.floor_enabled)
		{
			floor=Assets.floor;
			floor=preparePlatform(floor);
		}
		if (Assets.leftedge_enabled)
		{
			leftedge=Assets.leftedge;
			leftedge=preparePlatform(leftedge);
		}
		if (Assets.rightedge_enabled)
		{
			rightedge=Assets.rightedge;
			rightedge=preparePlatform(rightedge);
		}
		
		if (Assets.stage==1)
			platforms=Assets.complabPlatforms;
		if (Assets.stage==2)
			platforms=Assets.testtubePlatforms;
		if (Assets.stage==3)
			platforms=Assets.bandsawPlatforms;	
		for (int i=0;i<platforms.length;i++)
			platforms[i]=prepareFloatingPlatform(platforms[i]);
		
		projectiles=new ArrayList<Projectile>();
		items=new ArrayList<Item>();
		
		for (int i=0;i<Assets.initItems;i++)
		{
			spawnItem(1);
			spawnItem(2);
		}
		
		/*Windows and world setting*/
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		CollisionHandler c=new CollisionHandler();
		Player[]players={player1,player2};
		EdgePlatform[]edges={floor,leftedge,rightedge};
		c.edges=edges; 
		c.platforms=platforms; 
		c.players=players;
		c.projectiles=projectiles;
		c.items=items;
		world.setContactListener(c);
		boxDebugRender = new Box2DDebugRenderer();
		shapeRenderer=new ShapeRenderer();
	} 

	@Override
	public void render (float delta) 
	{

		//Key 9-8-7 combo to reset the game
		if (Gdx.input.isKeyPressed(Keys.NUM_9))
			if (Gdx.input.isKeyPressed(Keys.NUM_8))
				if (Gdx.input.isKeyPressed(Keys.NUM_7))
				{
					game.sounds.stopMusic();
					game.ui.gameState=1;
					game.setScreen(game.ui);
					game.sounds.playMusic(6);
					resetGame();
				}

		//Key 6-5 to reset the game with random music
		if (Gdx.input.isKeyPressed(Keys.NUM_6))
			if (Gdx.input.isKeyPressed(Keys.NUM_5))
			{
				game.sounds.stopMusic();
				game.sounds.playMusic((int)(Math.random()*6));
				resetGame();
			}

		if (player1.health==0||player2.health==0)
		{
			if (gameState)
			{ 
				game.sounds.stopSound();
				try 
				{
					Thread.sleep(2000);
					gameState=false;
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				game.sounds.stopMusic();
				game.sounds.playMusic(7);
			}
			if (player1.health>0)
				winMessage(player1);
			else
				winMessage(player2);
			return;
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.8f, 0f);
		world.step(Constants.STEP, 6, 2);
		player1.updatePosition();
		batch.setProjectionMatrix(camera.combined);
		DebugMatrix = batch.getProjectionMatrix().cpy().scale(Constants.PIXELS_TO_METERS, Constants.PIXELS_TO_METERS, 0);
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(background,-Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		controlPlayer(player1,Keys.UP,Keys.DOWN,Keys.RIGHT,Keys.LEFT,Keys.SHIFT_RIGHT,Keys.SLASH);
		controlPlayer(player2,Keys.W,Keys.S,Keys.D,Keys.A,Keys.NUM_1,Keys.NUM_2);
		controlProjectiles();
		controlItems();
		batch.end();
		if (Constants.DEBUG)
			boxDebugRender.render(world, DebugMatrix);
		renderHealthBars(player1);
		renderHealthBars(player2);
		controlCamera();
	}


	//Set the player attributes
	public Player preparePlayer(Player player)
	{
		player.createPlayer();
		player.body = world.createBody(player.getBodyDef());
		player.body.setUserData(player.sprite);
		player.body.createFixture(player.getFixtureDef());
		MassData m=new MassData();
		m.mass=player.MASS;
		player.body.setMassData(m);
		player.setContactFixture();
		player.contactFixture=player.body.createFixture(player.fixtureDef);
		return player;
	}

	public void controlProjectiles()
	{
		if (projectiles.isEmpty())
			return;
		for (int i=0;i<projectiles.size();i++)
		{
			if (projectiles.get(i).dead)
				continue;
			if (projectiles.get(i).body.getLinearVelocity().x==0)
				projectiles.get(i).destroy=true;
			if (projectiles.get(i).destroy)
			{
				destroyItem(projectiles.get(i));
				game.sounds.playSound(4, 0,Constants.IMPACT_SOUND_LENGTH);
				continue;
			}
			projectiles.get(i).sprite.setPosition((projectiles.get(i).body.getPosition().x * Constants.PIXELS_TO_METERS) - projectiles.get(i).sprite.getWidth()/2,(projectiles.get(i).body.getPosition().y * Constants.PIXELS_TO_METERS) -projectiles.get(i).sprite.getHeight()/2);
			projectiles.get(i).sprite.setOrigin(projectiles.get(i).sprite.getWidth()/2, projectiles.get(i).sprite.getHeight()/2);
		//	projectiles.get(i).sprite.setRotation((projectiles.get(i).body.getAngle() * MathUtils.radiansToDegrees)%360);
			projectiles.get(i).sprite.setOriginCenter();
			projectiles.get(i).sprite.draw(batch);
		}
	}

	public void controlItems()
	{
		if (items.size()<=2)
		{
			double rand=Math.random();
			if (rand<0.001)
			{
				spawnItem(1);
			}
			if (rand>0.9995)
			{
				spawnItem(2);
			}
		}
		for (int i=0;i<items.size();i++)
		{
			if (items.get(i).dead)
				continue;
			if (items.get(i).destroy)
			{
				items.get(i).dead=true;
				world.destroyBody(items.get(i).body);
				if (items.get(i).ID==1)
					game.sounds.playSound(3, 0,Constants.ITEM_SOUND_LENGTH);
				if (items.get(i).ID==2)
					game.sounds.playSound(5, 0,Constants.LIFE_SOUND_LENGTH);
				items.remove(i);
				continue;
			}
			items.get(i).sprite.setPosition((items.get(i).body.getPosition().x * Constants.PIXELS_TO_METERS) - items.get(i).sprite.getWidth()/2,(items.get(i).body.getPosition().y * Constants.PIXELS_TO_METERS) -items.get(i).sprite.getHeight()/2);
			items.get(i).sprite.setOrigin(items.get(i).sprite.getWidth()/2, items.get(i).sprite.getHeight()/2);
			items.get(i).sprite.setRotation((items.get(i).body.getAngle() * MathUtils.radiansToDegrees)%360);
			items.get(i).sprite.setOriginCenter();
			items.get(i).sprite.draw(batch);
		}
	}

	public void spawnItem(int id)
	{
		Item i = null;
		if (Assets.stage==1)
		{
			if (id==1)
				i=(new Item(Assets.calculatorTexture,(float) (Math.random()*Assets.labSpawn),Gdx.graphics.getHeight()*3/4,30,70,1));
			if (id==2)
				i=(new Item(Assets.gradeTexture,(float) (Math.random()*Assets.labSpawn),Gdx.graphics.getHeight()*3/4,40,40,2));
		}
		if (Assets.stage==2)
		{
			if (id==1)
				i=(new Item(Assets.calculatorTexture,Assets.tubeSpawn[(int)(Math.random()*4)],Gdx.graphics.getHeight()*3/4,30,70,1));
			if (id==2)
				i=(new Item(Assets.gradeTexture,Assets.tubeSpawn[(int)(Math.random()*4)],Gdx.graphics.getHeight()*3/4,40,40,2));
		}
		if (Assets.stage==3)
		{
			int rand=(int)(Math.random()*2);
			if (id==1)
			{	
				if (rand==0)
					i=(new Item(Assets.calculatorTexture,(float)Math.random()*(Gdx.graphics.getWidth()/2+Assets.bandsawSpawn[0])-Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()*3/4,30,70,1));
				else
					i=(new Item(Assets.calculatorTexture,(float)Math.random()*(Gdx.graphics.getWidth()/2-Assets.bandsawSpawn[1])+Assets.bandsawSpawn[1],Gdx.graphics.getHeight()*3/4,30,70,1));
			}
			if (id==2)
			{
				if (rand==0)
					i=(new Item(Assets.gradeTexture,(float)Math.random()*(Gdx.graphics.getWidth()/2+Assets.bandsawSpawn[0])-Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()*3/4,40,40,2));
				else
					i=(new Item(Assets.gradeTexture,(float)Math.random()*(Gdx.graphics.getWidth()/2-Assets.bandsawSpawn[1])+Assets.bandsawSpawn[1],Gdx.graphics.getHeight()*3/4,40,40,2));
			}
		}
		i=prepareItem(i);
		items.add(i);
	}


	public Projectile prepareProjectile(Projectile p)
	{
		p.create();
		p.body=world.createBody(p.bodyDef);
		p.body.setUserData(p.sprite);
		MassData m=new MassData();
		m.mass=1;
		p.body.setMassData(m);
		p.body.createFixture(p.fixtureDef);
		return p;
	}

	public Projectile defaultProjectile()
	{
		return new Projectile(Assets.weapon,(float)(-Math.random()*Gdx.graphics.getWidth()/2),400f,Assets.weaponSize,Assets.weaponSize,2);
	}

	public void createItem(int num)
	{
		if (num==1)
		{
			if (Assets.stage==1)
				items.add(new Item(Assets.calculatorTexture,(float) (Math.random()*Assets.labSpawn),Gdx.graphics.getHeight()*3/4,30,70,1));
			if (Assets.stage==2)
				items.add(new Item(Assets.calculatorTexture,Assets.tubeSpawn[(int)(Math.random()*4)],Gdx.graphics.getHeight()*3/4,30,70,1));
		}
	}

	public Item prepareItem(Item i)
	{
		i.create();
		i.body=world.createBody(i.bodyDef);
		i.body.setUserData(i.sprite);
		MassData m=new MassData();
		m.mass=50000;
		i.body.setMassData(m);
		i.body.createFixture(i.fixtureDef);
		return i;
	}

	public EdgePlatform preparePlatform(EdgePlatform platform)
	{
		platform.createPlatform();
		platform.body = world.createBody(platform.bodyDef);
		platform.body.createFixture(platform.fixtureDef);
		return platform;
	}

	public FloatingPlatform prepareFloatingPlatform(FloatingPlatform platform)
	{
		platform.createFloatingPlatform();
		platform.body = world.createBody(platform.bodyDef);
		platform.body.createFixture(platform.fixtureDef);
		platform.setContactFixture();
		platform.contactFixture=platform.body.createFixture(platform.fixtureDef);
		return platform;
	}
	public void dispose()
	{
		batch.dispose();
		world.dispose();
	}

	public void renderHealthBars(Player player)
	{
		float length=player.health;
		shapeRenderer.begin(ShapeType.Filled);
		if (player.health>70)
			shapeRenderer.setColor(Color.GREEN);
		else if (player.health>30)
			shapeRenderer.setColor(Color.YELLOW);
		else
			shapeRenderer.setColor(Color.RED);

		if (player.kickColor)
			shapeRenderer.setColor(Color.BLUE);
		if (player.collectHealth)
			shapeRenderer.setColor(Color.PINK);

		if (player.calculatorAttack)
		{
			shapeRenderer.setColor(Color.LIGHT_GRAY);
			length=player.calculatorDuration;
		}
		if (player.facingLeft)
			shapeRenderer.rect(player.sprite.getX()+length+Gdx.graphics.getWidth()/2, player.sprite.getY()+Gdx.graphics.getHeight()/2+100f, -length, 10);
		else
			shapeRenderer.rect(player.sprite.getX()+Gdx.graphics.getWidth()/2, player.sprite.getY()+Gdx.graphics.getHeight()/2+100f, length, 10);
		shapeRenderer.end();
		batch.begin();
		if (player.calculatorEquipped&&!player.calculatorAttack)
		{
			batch.draw(Assets.calculatorTexture, player.sprite.getX(),player.sprite.getY()+player.sprite.getHeight()-10f,15,25);
		}
		batch.end();
	}

	public void kickPlayers()
	{
		if (player1.kickAttack&&player2.kickAttack)
		{
			int rand=(int)(Math.random()*2);
			if (rand==0)
				player1.kickAttack=false;
			else
				player2.kickAttack=false;

		}
		if (player2.kickAttack)
		{
			if (!player1.isLaunched)
			{
				player1.getKicked(player2.sprite.getX()>player1.sprite.getX(),1f,5f);
				player2.getKicked(player2.sprite.getX()<player1.sprite.getX(),0.2f,0f);
				player2.kickAttack=false;
				game.sounds.playSound(0, 0,Constants.PUNCH_SOUND_LENGTH);
			}
		}

		else if (player1.kickAttack)
		{
			if (!player2.isLaunched)
			{
				player2.getKicked(player1.sprite.getX()>player2.sprite.getX(),1f,5f);
				player1.getKicked(player1.sprite.getX()<player2.sprite.getX(),0.2f,0f);
				player1.kickAttack=false;
				game.sounds.playSound(0, 0,Constants.PUNCH_SOUND_LENGTH);
			}
		}
	}
	public void controlPlayer(Player player, int up,int down,int right,int left,int kick,int special)
	{
		if (player1.contact&&player2.contact)
		{
			kickPlayers();
		}
		if (Gdx.input.isKeyPressed(special)&&player.calculatorEquipped&&!player.isLaunched)
		{
			player.calculatorAttack=true;
			player.calculatorDuration-=0.5f;
			player.stayStationary();
			drawPlayer(player);
			if (player.calculatorDuration<=0f)
			{
				player.calculatorEquipped=false;
			}
			if (player.calculatorDuration%20==5)
			{
				projectiles.add(defaultProjectile());
				if (player.facingLeft)
				{
					projectiles.get(projectiles.size()-1).setPosition(player.sprite.getX()-player.sprite.getWidth(), player.sprite.getY()+player.sprite.getHeight()/2);
					prepareProjectile(projectiles.get(projectiles.size()-1));
					projectiles.get(projectiles.size()-1).launch(!player.facingLeft, 1f);
					game.sounds.playSound(2, 0, 0.3f);
				}
				else
				{
					projectiles.get(projectiles.size()-1).setPosition(player.sprite.getX()+player.sprite.getWidth(), player.sprite.getY()+player.sprite.getHeight()/2);
					prepareProjectile(projectiles.get(projectiles.size()-1));
					projectiles.get(projectiles.size()-1).launch(!player.facingLeft, 1f);
					game.sounds.playSound(2, 0, 0.2f);
				}
			}
			return;
		}
		else
			player.calculatorAttack=false;
		if (player.isLaunched||player.collectHealth)
			player.invincibility++;
		if (player.invincibility>20)
		{
			player.collectHealth=false;
			player.isLaunched=false;
			player.kickColor=false;
			player.invincibility=0;
		}
		if (player.sprite.getY()<(-Gdx.graphics.getHeight()/2-100)||player.sprite.getX()<(-Gdx.graphics.getWidth()/2-300))
			player.health=0;
		batch.setColor(player.color);
		if (Gdx.input.isKeyPressed(up)&&!player.isLaunched&&!player.lockPlayerRight)
		{
			if (!player.isJumping)
			{
				game.sounds.playSound(1,0,Constants.JUMP_SOUND_LENGTH);
			}
			player.jump();
		}
		if (Gdx.input.isKeyPressed(down)&&!player.isLaunched)
		{
			player.fallFast();
		}
		if (player.kick)
			player.kick_iterator++;
		if (player.kick_iterator>5)
		{
			player.kickAttack=false;
			if (player.kick_iterator>20)
			{
				player.kick=false;
				player.kick_iterator=0;
			}
		}
		if (player.kick_iterator>0&&player.kick_iterator<20)
		{
			if (player.kick_iterator<5)
				player.kickAttack=true;
			player.kick_iterator++;
			player.kick=true;
		}
		if(Gdx.input.isKeyJustPressed(kick))
		{
			player.kick=true;
			player.kickAttack=true;
		}
		if (Gdx.input.isKeyPressed(right)&&!player.isLaunched&&!player.lockPlayerRight)
		{
			player.moveRight();
		}
		else if (Gdx.input.isKeyPressed(left)&&!player.isLaunched&&!player.lockPlayerLeft)
		{
			player.moveLeft();
		}	
		else 
		{
			player.stayStationary();
		}
		drawPlayer(player);
	}

	public void drawPlayer(Player player)
	{
		player.sprite.rotate(90);
		if (player.facingLeft)
			batch.draw(player.sprite,player.sprite.getX()+100,player.sprite.getY(),-100,100);
		else
			batch.draw(player.sprite,player.sprite.getX(),player.sprite.getY(),100,100);
	}

	public void destroyItem(Item i)
	{
		i.dead=true;
		world.destroyBody(i.body);
	}

	public void winMessage(Player winner)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.8f, 0f);
		batch.begin();
		batch.draw(winScreen,-Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(winner.walkFrames[winner.INITIAL_SPRITE_INDEX],0,-Gdx.graphics.getHeight()/2+67,1000,1000);
		batch.end();
	}

	public void controlCamera()
	{
		camera.update();
	}

	public void resetGame()
	{		
		this.dispose();
		importAssets();
		gameState=true;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
	}
}