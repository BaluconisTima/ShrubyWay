package com.shrubyway.game.item;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.sound.SoundSettings;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Harmonica extends ItemActing {

    int soundCount = 6;
    Sound sound = null;
    Boolean soundPlaying = false;
    static float soundCooldown[] = {2.5f, 1.5f, 3.5f, 4.5f, 5.5f, 3.5f};

    public Harmonica() {
        actingAnimation = 5;
        int x = (int)Math.floor(Math.random() * soundCount) + 1;
        sound = GlobalAssetManager.get("sounds/EFFECTS/Harmonica/"
                        + x + ".ogg",
                Sound.class);
        actingTime = soundCooldown[x - 1];
    }

    @Override public void Acting() {
          super.Acting();
          if(sound != null && !soundPlaying) {
                soundPlaying = true;
              sound.play(SoundSettings.soundVolume);
          }
    }

    @Override public void stopActing() {
        super.stopActing();
        if(sound != null) {
            sound.stop();
        }
        int x = (int)Math.floor(Math.random() * soundCount) + 1;
            sound = GlobalAssetManager.get("sounds/EFFECTS/Harmonica/" + x  + ".ogg",
                    Sound.class);
            actingTime = soundCooldown[x - 1];
            soundPlaying = false;

    }

}
