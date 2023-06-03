package com.shrubyway.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundSettings {
    public static float soundVolume = 1;
    public static float musicVolume = 1;

    static Sound music;
    static String newMusic = null;
    static long musicID = -1;
    static float musicLocalVolume = 1;
    public static void changeMusic(String way) {
        newMusic = way;
    }

    public static void update() {
        if(newMusic != null) {
            musicLocalVolume *= 0.5;
            if(musicLocalVolume < 0.01) {
                musicLocalVolume = 1;
                if(music != null) music.stop();
                music = Gdx.audio.newSound(Gdx.files.internal(newMusic));
                musicID = music.play(musicVolume * musicLocalVolume);
                music.setLooping(musicID, true);
                newMusic = null;
            }
        } else {
            if(musicLocalVolume < 1) musicLocalVolume *= 2;
            if(musicLocalVolume > 1) musicLocalVolume = 1;
        }
        if(musicID != -1)
            music.setVolume(musicID, musicVolume * musicLocalVolume);
    }


}
