package com.mygdx.MagnetFighters;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.MagnetFighters.MagnetFighters;

public class SelectPlayer implements Screen{

	//Import variables
	private MagnetFighters game;
	private SpriteBatch batch;
    private Stage stage;
    private Texture[] texture;
    private Image[] image;
    private int slides = 0;
    private final int slideNo = 4;
	    
	public SelectPlayer(final MagnetFighters gam) {
		//Initialize skin and stage
		game = gam;
		batch = new SpriteBatch();
        stage = new Stage();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
