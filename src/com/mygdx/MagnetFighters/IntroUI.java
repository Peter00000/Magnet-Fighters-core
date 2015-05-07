package com.mygdx.MagnetFighters;

import com.badlogic.gdx.ApplicationListener;
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

public class IntroUI implements Screen {

	//Import variables
	private MagnetFighters game;
	private SpriteBatch batch;
    private Stage stage;
    private Texture[] texture;
    private Image[] image;
    private int slides = 0;
    private final int slideNo = 3;
    private long keyTimer = System.currentTimeMillis(); //ms
    private final long keyTimeout = 200; //ms
    
	public IntroUI(final MagnetFighters gam) {
		//Initialize skin and stage
		game = gam;
		batch = new SpriteBatch();
        stage = new Stage();
       
        //Import textures
        texture = new Texture[slideNo];
        image = new Image[slideNo];
        for(int i = 0; i<slideNo; i++){
        	texture[i] = new Texture(Gdx.files.internal("Intro"+i+".png"));
        	image[i] = new Image(texture[i]);
        }
        stage.addActor(image[slides]);
        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Checks if the sceneNo is changed is past and switch scene
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && keyTimer<System.currentTimeMillis()){
        	keyTimer = System.currentTimeMillis()+keyTimeout;
        	if(slides == slideNo-1){
        		slides = 0;
        		game.changeScreen(4);
        	}
        	else{
        		slides++;
        		stage.addActor(image[slides]);
        	} //end else
        }//end if
        
        batch.begin();
        stage.draw();
        batch.end();
	}

	public void show() {}
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void hide() {}
	public void dispose() {batch.dispose();}

}