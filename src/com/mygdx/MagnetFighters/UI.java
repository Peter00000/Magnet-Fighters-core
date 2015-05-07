package com.mygdx.MagnetFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.mygdx.MagnetFighters.MagnetFighters;
import com.mygdx.MagnetFighters.SoundEngine;

public class UI implements Screen {

	//Import variables
	private MagnetFighters game;
	private SpriteBatch batch;
	private Skin skin;
    private Stage stage;
    private int mouseX = 0, mouseY = 0; 	//tracks the postition of the mouse
    private SoundEngine sounds;
    private TextButton[] buttons;
    private boolean playerSetupReady = true;
    private Texture[] texture;
    private Image[] image;
    private int slides = 0;
    private final int slideNo = 4;
    private long keyTimer = System.currentTimeMillis(); //ms
    private final long keyTimeout = 200; //ms
  
	public UI(final MagnetFighters gam) {
		//Initialize skin and stage
		game = gam;
		batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
       
        //Initialize sounds
//        sounds = new SoundEngine();
//        sounds.setMusicVolume(0.2f);
//        sounds.playMusic(1);
        
        //Import textures
        texture = new Texture[slideNo];
        image = new Image[slideNo];
        for(int i = 0; i<slideNo; i++){
        	texture[i] = new Texture(Gdx.files.internal("Main"+i+".png"));
        	image[i] = new Image(texture[i]);
        }
        stage.addActor(new Image(new Texture(Gdx.files.internal("Intro0.png"))));
       

        //Configure the buttons
//        buttonInitX = Gdx.graphics.getWidth()/2;
//        buttons = new TextButton[buttonNames.length];
//        for(int i = 0; i < buttons.length; i++){
//        	buttons[i] = new TextButton(buttonNames[i],skin,"default");
//        	buttons[i].setColor(Color.BLACK);
//        	buttons[i].setScale(2.0f, 2.0f);
//        	if(buttonNames[i].equals("Play")){
//    		buttons[i].setWidth(buttonW2);
//        	buttons[i].setHeight(buttonH2);
//        	buttons[i].setPosition(buttonInitX-buttonW2/2, playY);
//        	} else{
//        	buttons[i].setWidth(buttonW1);
//        	buttons[i].setHeight(buttonH1);
//        	buttons[i].setPosition(buttonInitX+buttonSpacingX*i-buttonW1/2,
//        			buttonInitY-buttonSpacingY*i);
//        	}
//        	
//        	buttons[i].addListener(new ClickListener(){
//        		public void clicked(InputEvent event, float x, float y){
//                    System.out.println("Button clicked:"+this.getPressedButton());
//                    game.changeScreen(2);
//                    if(System.currentTimeMillis()-keyTimer>500)
//                    {keyTimer = System.currentTimeMillis(); /*sounds.playSound(0,0);*/}
//            }});
//        	stage.addActor(buttons[i]);
//        }
        
        //Configure the background
        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
      //Mouse tracking
        mouseX = Gdx.input.getX();
        mouseY = Gdx.input.getY();
        
        //Turn the play green when setup is ready
//        if(mouseX < buttonInitX + buttonW2/2 &&
//            	mouseX > buttonInitX - buttonW2/2 &&
//            	mouseY < Gdx.graphics.getHeight()-playY &&
//            	mouseY > Gdx.graphics.getHeight()-playY - buttonH2){
//        	Color buttonColor = Color.GREEN;
//        	if(!playerSetupReady)	buttonColor = Color.RED;
//        	buttons[buttons.length-1].setColor(buttonColor);
//        	buttons[buttons.length-1].setVisible(false);}
//        else buttons[buttons.length-1].setColor(Color.BLACK);
        
      //Switch scene based on the key input
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))	stage.addActor(image[slides]);
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && keyTimer<System.currentTimeMillis())
        {
        	keyTimer = System.currentTimeMillis()+keyTimeout;
        	slides--;
        	if(slides<0) slides = slideNo-1;
        	stage.addActor(image[slides]);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && keyTimer<System.currentTimeMillis())
        {
        	keyTimer = System.currentTimeMillis()+keyTimeout;
        	slides++;
        	if (slides>slideNo-1) slides=0;
        	stage.addActor(image[slides]);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && keyTimer<System.currentTimeMillis())
        {
        	keyTimer = System.currentTimeMillis()+keyTimeout;
        	if(slides == 0);						//setting mapping
        	if(slides == 1) game.changeScreen(2);	//player mapping
        	if(slides == 2) game.changeScreen(0);	//tutorial mapping
        	if(slides == 3)	if(playerSetupReady)game.changeScreen(5);
        											//play game mapping
        }//end if
        
        batch.begin();
        stage.draw();
        batch.end();
	}

	public void pause() {}
	public void resume() {}
	public void dispose() {
		batch.dispose();
        sounds.terminate();
	}

	public void show() {}
	public void hide() {}

}