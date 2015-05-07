package com.mygdx.MagnetFighters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MagnetFighters extends Game
{
	public SoundEngine sounds;
	public SpriteBatch batch;
	public BitmapFont font;
	public IntroUI intro;
	public UI UI;
	public GameController playGame;
	public SelectPlayer select;
	public Profile	profile;
	public EndGame end;
	public int gameState = 1; 

	public void create() {
		batch 		= new SpriteBatch();
		intro 		= new IntroUI(this);
		UI 			= new UI(this);
		playGame 	= new GameController(this);
		select 		= new SelectPlayer(this);
		profile 	= new Profile(this);
		end 		= new EndGame(this);
		this.setScreen(UI);
		sounds=new SoundEngine();
		sounds.setMusicVolume(0.3f);
		sounds.playMusic(6);
		sounds.setSoundVolume(0.8f);
	}

	/*Call when the game state changes*/
	public void changeScreen(int state){
		gameState = state;
		switch(gameState){
		case 0:	this.setScreen(intro);	 break;//0 - intro screens
		case 1: this.setScreen(UI);		 break;//1 - Main User interface
		case 2: this.setScreen(select);  break;//2 - Character creation
		case 3: this.setScreen(profile); break;//3 - Player selection
		case 4: 							   //4 - Tutorial game
			Assets.stage=Assets.TUTORIAL_LEVEL; 
			playGame.resetGame();
			this.setScreen(playGame);
			sounds.stopMusic(); 
			sounds.playMusic(0); break;
		case 5: 							   //5 - Play game
			Assets.stage=2;
			playGame.resetGame();
			this.setScreen(playGame);
			sounds.stopMusic(); 
			sounds.playMusic((int)(Math.random()*6)); break;
		case 6: this.setScreen(end);	 break;//6 - End game
		}//end switch
	}//end changeScreen

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}


}