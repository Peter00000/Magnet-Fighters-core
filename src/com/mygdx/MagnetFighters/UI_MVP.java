package com.mygdx.MagnetFighters;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UI_MVP implements Screen {

	//Import variables
	private MagnetFighters game;
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private int mouseX = 0, mouseY = 0; 	//tracks the postition of the mouse
	private SoundEngine sounds;
	private long keyTimer = System.currentTimeMillis();
	private TextButton[] buttons;
	private String[] buttonNames = {"Introduction","Create Character",
			"Select Player","Settings","Play"};
	private final float buttonW1 = 400f,
			buttonW2 = 600f,
			buttonH1 = 75f,
			buttonH2 = 100f;
	private float 		buttonInitX,
	buttonInitY = 500f,
	buttonSpacingX = 0f,
	buttonSpacingY = 100f,
	playY = 50f;
	private boolean playerSetupReady = true;
	private Texture background;
	private Image backgroundImg;
	public int gameState = 1; 
	//0 - intro screens
	//1 - Main User interface
	//2 - Character creation
	//3 - Player selection
	//4 - Game count down
	//5 - Play game
	//6 - End game
	//7 - Setting screen

	public UI_MVP(final MagnetFighters gam) {
		//Initialize skin and stage
		game = gam;
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();

		//Initialize sounds
		//Import textures
		background = new Texture(Gdx.files.internal("Logo Screen.png"));
		backgroundImg = new Image(background);
		stage.addActor(backgroundImg);

		//Configure the buttons
		buttonInitX = Gdx.graphics.getWidth()/2;
		buttons = new TextButton[buttonNames.length];
		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new TextButton(buttonNames[i],skin,"default");
			buttons[i].setColor(Color.CLEAR);
			buttons[i].setScale(2.0f, 2.0f);
			if(buttonNames[i].equals("Play")){
				buttons[i].setWidth(buttonW2);
				buttons[i].setHeight(buttonH2);
				buttons[i].setPosition(buttonInitX-buttonW2/2, playY);
			} else{
				buttons[i].setWidth(buttonW1);
				buttons[i].setHeight(buttonH1);
				buttons[i].setPosition(buttonInitX+buttonSpacingX*i-buttonW1/2,
						buttonInitY-buttonSpacingY*i);
			}

			buttons[i].addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y){
					System.out.println("Button clicked:"+this.getPressedButton());
					gameState = 0;
					if(System.currentTimeMillis()-keyTimer>500)
					{keyTimer = System.currentTimeMillis(); /*sounds.playSound(0,0);*/}
				}});
			stage.addActor(buttons[i]);
		}

		//Configure the background

		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void resize(int width, int height) {


	}

	@Override
	public void render(float delta) {
		Color buttonColor;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
			gameState=0;
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			game.sounds.terminate();
			System.exit(0);
		}
		//Mouse tracking
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();

		//Turn the play green when setup is ready
		if(mouseX < buttonInitX + buttonW2/2 &&
				mouseX > buttonInitX - buttonW2/2 &&
				mouseY < Gdx.graphics.getHeight()-playY &&
				mouseY > Gdx.graphics.getHeight()-playY - buttonH2){
			buttonColor = Color.GREEN;
			if(!playerSetupReady)	buttonColor = Color.RED;
			buttons[buttons.length-1].setColor(buttonColor);}
		else buttons[buttons.length-1].setColor(Color.BLACK);

		//when game state is zero, call play game
		if(gameState == 0)	
		{ 
			game.sounds.stopMusic(); 
			game.sounds.playMusic((int)(Math.random()*6)); 
			game.setScreen(game.g); 
			game.g.resetGame();
			this.hide();
		}

		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void pause() {


	}

	@Override
	public void resume() {


	}

	@Override
	public void dispose() 
	{
		batch.dispose();
		sounds.terminate();
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() 
	{
		
	}

}
