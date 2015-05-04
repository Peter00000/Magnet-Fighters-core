package com.mygdx.MagnetFighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;


public class SoundEngine {

	private String[] musicFiles = {
			"smash1.mp3",
			"smash2.mp3",
			"smash3.mp3",
			"sonic1.mp3",
			"sonic2.mp3",
			"sonic3.mp3",
			"menu.mp3",
			"victory.mp3"

	};
	private String[] soundFiles = {
			"kickSound.wav","jump.wav","shot.wav","laser.wav","impact.wav"
	};
	private Music[] musics = new Music[musicFiles.length];
	private Sound[] sounds= new Sound[soundFiles.length];
	private float musicVolume = 1;
	private float soundVolume = 1;
	long soundID;

	public SoundEngine() {
		for(int i=0; i<musicFiles.length; i++)
			musics[i] = Gdx.audio.newMusic(Gdx.files.internal(musicFiles[i]));

		for(int i=0; i<soundFiles.length; i++)
			sounds[i] = Gdx.audio.newSound(Gdx.files.internal(soundFiles[i]));

	}


	//The method plays the music at certain index when called
	//Loop back the music
	public void playMusic(int index){
		if(index >= musicFiles.length)	return;
		musics[index].play();
		musics[index].setVolume(musicVolume);
		musics[index].setLooping(true);
	}


	//The method autoplay music in loops
	public void PlayMusic(){
		int index = (int)(Math.random()*musicFiles.length);
		musics[index].play();
		musics[index].setVolume(musicVolume);
		musics[index].setOnCompletionListener(new Music.OnCompletionListener(){
			public void onCompletion(Music music){
				PlayMusic();
			}});
	}


	//The method plays the designated sound effect
	public void playSound(final int index, float pan, float soundDuration){
		if(index < sounds.length)
			soundID = sounds[index].loop(soundVolume, 1f, 0);
		else return;
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				sounds[index].stop();
				}}
		, soundDuration);

	}

	public void stopMusic()
	{
		for(int i=0; i<musics.length; i++)
			musics[i].stop();
	}
	
	public void stopSound()
	{
		for(int i=0; i<sounds.length; i++)
			sounds[i].stop();
	}


	//The setters
	public void setMusicVolume(float volume)	{musicVolume = volume;}
	public void setSoundVolume(float volume)	{soundVolume = volume;}


	//The method dispose all of the sound files
	public void terminate(){
		for(int i=0; i<musicFiles.length; i++)
			musics[i].dispose();
		for(int i=0; i<soundFiles.length; i++)
			sounds[i].dispose();
	}

}