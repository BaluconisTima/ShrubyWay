package com.shrubyway.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.map.MapSettings;

public class SoundAtPosition {
    static Sound sound;
    long id;
    private Vector2 position;
    public SoundAtPosition(Sound s, Vector2 position) {
        sound = s;
        id = sound.play(0);
        this.position = position;
    }

    void update(Vector2 playerPosition) {
        if(Math.abs(position.x - playerPosition.x) > Math.abs(position.x - playerPosition.x + MapSettings.MAPSIZE)) {
            position.x += MapSettings.MAPSIZE;
        }
        if(Math.abs(position.x - playerPosition.x) > Math.abs(position.x - playerPosition.x - MapSettings.MAPSIZE)) {
            position.x -= MapSettings.MAPSIZE;
        }
        if(Math.abs(position.y - playerPosition.y) > Math.abs(position.y - playerPosition.y + MapSettings.MAPSIZE)) {
            position.y += MapSettings.MAPSIZE;
        }
        if(Math.abs(position.y - playerPosition.y) > Math.abs(position.y - playerPosition.y - MapSettings.MAPSIZE)) {
            position.y -= MapSettings.MAPSIZE;
        }

        float distance = position.dst(playerPosition);
        float volume = 1 - distance / 1500;
        if(volume < 0) volume = 0;

        volume *= SoundSettings.soundVolume;
        sound.setVolume(id, volume);
    }

    void pause() {
        sound.pause(id);
    }

    void resume() {
        sound.resume(id);
    }

}
