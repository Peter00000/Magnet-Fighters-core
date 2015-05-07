package com.mygdx.MagnetFighters;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class EndGame implements Screen{

	private MagnetFighters game;
	private SpriteBatch batch;
    private Stage stage;
	
	public EndGame(final MagnetFighters gam){
		game = gam;
		batch = new SpriteBatch();
        stage = new Stage();
	}
	

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	public void show() {}
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void hide() {}
	public void dispose() {batch.dispose();}

}
