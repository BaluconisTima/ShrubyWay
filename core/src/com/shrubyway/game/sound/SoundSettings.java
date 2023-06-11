package com.shrubyway.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.GlobalAssetManager;

public class SoundSettings {
    public static float soundVolume = 1;
    public static float musicVolume = 1;

    static Sound music;
    static String oldMusic = null, newMusic = null;
    static long musicID = -1;
    static float musicLocalVolume = 1;
    public static void changeMusic(String way) {
        if(way == oldMusic) return;
        newMusic = way;
    }

    public static void update() {
        if(newMusic != null) {
            musicLocalVolume *= 0.5;
            if(musicLocalVolume < 0.01) {
                musicLocalVolume = 1;
                if(music != null) music.stop();
                music = GlobalAssetManager.get(newMusic, Sound.class);
                musicID = music.play(musicVolume * musicLocalVolume);
                music.setLooping(musicID, true);
                oldMusic = newMusic;
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
