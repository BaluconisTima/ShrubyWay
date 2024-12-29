package com.shrubyway.game.item;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.sound.SoundSettings;

public class Harmonica extends ItemActing {

    int soundCount = 6;
    Sound sound = null;
    Boolean soundPlaying = false;
    static float soundCooldown[] = {2.1f, 1.61f, 3.8f, 4.6f, 5.2f, 3.52f};

    public Harmonica() {
        actingAnimation = 5;
        int x = (int)Math.floor(Math.random() * soundCount) + 1;
        sound = ShrubyWay.assetManager.get("sounds/EFFECTS/Harmonica/"
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
            sound = ShrubyWay.assetManager.get("sounds/EFFECTS/Harmonica/" + x  + ".ogg",
                    Sound.class);
            actingTime = soundCooldown[x - 1];
            soundPlaying = false;

    }

}
