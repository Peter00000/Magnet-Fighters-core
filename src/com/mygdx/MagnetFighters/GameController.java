package com.mygdx.MagnetFighters;




import sun.font.TrueTypeFont;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameController extends ApplicationAdapter {

	SpriteBatch batch;
	private static int frameRows=2;
	private static int frameCols=8;
	World world;
	BitmapFont font;
	final float PIXELS_TO_METERS = 100f;
	OrthographicCamera camera;
	Box2DDebugRenderer boxDebugRender;
	Matrix4 DebugMatrix;
	private Box2DDebugRenderer debugRenderer;
	Player player1,player2;
	EdgePlatform floor,platform1,platform2;
	FloatingPlatform platform3;
	final boolean DEBUG=false;
	ShapeRenderer shapeRenderer;
	final float GRAVITY=-9.8f;
	final float STEP=1/40f;
	Texture background;
	FloatingPlatform[] platforms;
	final boolean FLOOR=false;
	Texture winScreen;
	SoundEngine sound;
	SoundEngine sound2;
	boolean gameState=true;
	boolean test=true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, GRAVITY), true);
		Texture walkSheet=new Texture(Gdx.files.internal("human.png"));
		Texture walkSheet2=new Texture(Gdx.files.internal("human2.png"));
		Texture attackSheet=new Texture(Gdx.files.internal("human_kick_calc.png"));
		Texture attackSheet2=new Texture(Gdx.files.internal("human_kick_calc2.png"));
		background=new Texture(Gdx.files.internal("background2.jpg"));
		Texture platformTexture=new Texture(Gdx.files.internal("platformcomp.jpg"));
		winScreen=new Texture(Gdx.files.internal("You Win.png"));
		player1=new Player(walkSheet,attackSheet,frameRows,frameCols);
		player1.attackSheet=attackSheet;
		player1=preparePlayer(player1);
		player1.color=Color.WHITE;
		player2=new Player(walkSheet2,attackSheet2,frameRows,frameCols);
		player2=preparePlayer(player2);
		player2.color=Color.WHITE;
		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = (Gdx.graphics.getHeight()/2)/PIXELS_TO_METERS;
		floor=new EdgePlatform(-w*100,-h,w*100,-h,0,0,0.05f);
		if (FLOOR)
			floor=preparePlatform(floor);
		platform1=new EdgePlatform(-w/2,-h*100,-w/2,h*100,0,0,0f);
		//	platform1=preparePlatform(platform1);
		platform2=new EdgePlatform(w/2,-h*100,w/2,h*100,0,0,0f);
		//	platform2=preparePlatform(platform2);
		platform3=new FloatingPlatform(platformTexture,-650*Gdx.graphics.getWidth()/1920,-175*Gdx.graphics.getHeight()/1080,2000*Gdx.graphics.getWidth()/1920,80*Gdx.graphics.getHeight()/1080);
		platform3=prepareFloatingPlatform(platform3);
		EdgePlatform[]edges={floor};
		Player[]players={player1,player2};
		FloatingPlatform[] temp={platform3};
		platforms=temp;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		CollisionDetector c=new CollisionDetector();
		c.edges=edges; c.platforms=platforms; c.players=players;
		world.setContactListener(c);
		boxDebugRender = new Box2DDebugRenderer();
		shapeRenderer=new ShapeRenderer();
		font=new BitmapFont();
		font.setScale(10f);
		sound=new SoundEngine();
		sound2=new SoundEngine();
		sound.setMusicVolume(0.5f);
		sound.setSoundVolume(1f);
		sound.playMusic((int)(Math.random()*3));
		sound2.setMusicVolume(0.8f);
	}

	@Override
	public void render () 
	{
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			this.dispose();
			System.exit(0);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_9))
			if (Gdx.input.isKeyPressed(Keys.NUM_8))
				if (Gdx.input.isKeyPressed(Keys.NUM_7))
						resetGame();

		if (player1.health==0||player2.health==0)
		{
			sound.stopMusic();
			sound.stopSound();
			if (gameState)
			{
				try 
				{
					Thread.sleep(2000);
					gameState=false;
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			sound2.playMusic(3);
			if (player1.health>0)
				winMessage(player1);
			else
				winMessage(player2);
			return;
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.8f, 0f);
		world.step(STEP, 6, 2);
		player1.updatePosition();
		batch.setProjectionMatrix(camera.combined);
		DebugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(background,-Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (player1.checkKick&&player2.checkKick)
		{
			kickPlayers();
		}
		controlPlayer(player1,Keys.UP,Keys.DOWN,Keys.RIGHT,Keys.LEFT,Keys.SHIFT_RIGHT);
		controlPlayer(player2,Keys.W,Keys.S,Keys.D,Keys.A,Keys.T );
		batch.end();
		if (DEBUG)
			boxDebugRender.render(world, DebugMatrix);
		renderHealthBars(player1);
		renderHealthBars(player2);
		camera.update();
	}

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
		shapeRenderer.begin(ShapeType.Filled);
		if (player.health>70)
			shapeRenderer.setColor(Color.GREEN);
		else if (player.health>30)
			shapeRenderer.setColor(Color.YELLOW);
		else
			shapeRenderer.setColor(Color.RED);

		if (player.kickColor)
			shapeRenderer.setColor(Color.BLUE);
		if (player.flipped)
			shapeRenderer.rect(player.sprite.getX()+player.health+Gdx.graphics.getWidth()/2, player.sprite.getY()+Gdx.graphics.getHeight()/2+100f, -player.health, 10);
		else
			shapeRenderer.rect(player.sprite.getX()+Gdx.graphics.getWidth()/2, player.sprite.getY()+Gdx.graphics.getHeight()/2+100f, player.health, 10);
		shapeRenderer.end();
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
			if (!player1.isKicked)
			{
				player1.getKicked(player2.sprite.getX()>player1.sprite.getX(),1f,true);
				player2.getKicked(player2.sprite.getX()<player1.sprite.getX(),0.2f,false);
				sound.playSound(0, 0);
			}
		}

		else if (player1.kickAttack)
		{
			if (!player2.isKicked)
			{
				player2.getKicked(player1.sprite.getX()>player2.sprite.getX(),1f,true);
				player1.getKicked(player1.sprite.getX()<player2.sprite.getX(),0.2f,false);
				sound.playSound(0, 0);
			}
		}
	}
	public void controlPlayer(Player player, int up,int down,int right,int left,int kick)
	{
		if (player.isKicked)
			player.invincibility++;
		if (player.invincibility>20)
		{
			player.isKicked=false;
			player.kickColor=false;
			player.invincibility=0;
		}
		if (player.sprite.getY()<(-Gdx.graphics.getHeight()/2-100)||player.sprite.getX()<(-Gdx.graphics.getWidth()/2-300))
			player.health=0;
		batch.setColor(player.color);
		if (Gdx.input.isKeyPressed(up)&&!player.isKicked)
		{
			if (!player.isJumping)
			{
				sound.playSound(1,0);
			}
			player.jump();
		}
		if (Gdx.input.isKeyPressed(down)&&!player.isKicked)
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
		if (Gdx.input.isKeyPressed(right)&&!player.isKicked)
		{
			player.moveRight();
		}
		else if (Gdx.input.isKeyPressed(left)&&!player.isKicked)
		{
			player.moveLeft();
		}
		else
		{
			player.stayStationary();
		}
		if (player.flipped)
			batch.draw(player.sprite,player.sprite.getX()+100,player.sprite.getY(),-100,100);
		else
			batch.draw(player.sprite,player.sprite.getX(),player.sprite.getY(),100,100);
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

	public void resetGame()
	{		
		player1.body.setTransform((float)(-Math.random()*Gdx.graphics.getWidth()/2)/PIXELS_TO_METERS, Gdx.graphics.getHeight()/PIXELS_TO_METERS, 0);
		player2.body.setTransform((float)(-Math.random()*Gdx.graphics.getWidth()/2)/PIXELS_TO_METERS, Gdx.graphics.getHeight()/PIXELS_TO_METERS, 0);
		player1.body.setLinearVelocity(0.001f,player1.body.getLinearVelocity().x);
		player2.body.setLinearVelocity(0.001f,player2.body.getLinearVelocity().x);
		player1.isKicked=false;player2.isKicked=false;player1.kickColor=false;player2.kickColor=false;
		player1.health=100f;
		player2.health=100f;
		sound2.terminate();
		sound.stopMusic();
		sound.playMusic((int)(Math.random()*3));
		gameState=true;
	}
}