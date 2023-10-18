package com.shrubyway.game.sound;

import com.badlogic.gdx.audio.Music;
import com.shrubyway.game.GlobalAssetManager;

public class SoundSettings {
    public static float soundVolume = 1;
    public static float musicVolume = 1;

    static Music music;
    static String currentMusic = null;
    static float musicLocalVolume = 1;
    public static void changeMusic(String way) {
        if(currentMusic == way) return;
       if(currentMusic != null) music.stop();
        currentMusic = way;
        if(way == null) return;
        music = GlobalAssetManager.get(way, Music.class);
        music.play();
        music.setVolume(musicVolume);
        music.setLooping(true);
    }

    public static void update() {
        if(currentMusic != null) music.setVolume(musicVolume);

    }


}
