package com.shrubyway.game.sound;

import com.badlogic.gdx.audio.Music;
import com.shrubyway.game.ShrubyWay;

public class SoundSettings {
    public static float soundVolume = 1;
    public static float musicVolume = 1;
    static Music music;
    static String currentMusic = null;
    public static void changeMusic(String way) {
        if(currentMusic == way) return;
        if(music != null && music.isPlaying()) music.stop();
       if(way == null) {
           currentMusic = null;
           return;
       }
        currentMusic = way;
        music = ShrubyWay.assetManager.get(way, Music.class);
        music.play();
        music.setVolume(musicVolume);
        music.setLooping(true);
    }

    public static void stopMusic() {
        if(music != null && music.isPlaying()) {
            music.stop();
            music = null;
        }
        if(currentMusic != null) currentMusic = null;
    }

    public static void update() {
        if(currentMusic != null) music.setVolume(musicVolume);

    }


}
