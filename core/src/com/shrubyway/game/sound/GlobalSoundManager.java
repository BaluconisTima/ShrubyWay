package com.shrubyway.game.sound;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GlobalSoundManager {
    static ArrayList<SoundAtPosition> sounds = new ArrayList<>(), sounds2 = new ArrayList<>();

    static public void addSound(SoundAtPosition sound) {
        sounds.add(sound);
    }
    static public void pause() {
        for(SoundAtPosition sound : sounds) {
            sound.pause();
        }
    }

    static public void resume() {
        for(SoundAtPosition sound : sounds) {
            sound.resume();
        }
    }

    static public void update(Vector2 playerPosition) {
        for(SoundAtPosition sound : sounds) {
            sound.update(playerPosition);
        }
        if(sounds.size() > 500){
            int i = 0;
            for(SoundAtPosition sound : sounds2) {
                if(i > 250) sounds2.add(sound);
                i++;
            }
           sounds = sounds2;
            sounds2 = new ArrayList<>();
        }
    }

}
