package com.shrubyway.game.item;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.sound.SoundSettings;

public class Food extends ItemActing {
    public float heling = 0;
    static Sound eatingSound = null;

    static float soundCooldown = 0.45f;
    float lastSoundTime = 0;
    long soundID = -1;

    static {
        eatingSound = GlobalAssetManager.get("sounds/EFFECTS/Eating.ogg", Sound.class);
    }
    public Food(float eatingTime, float heling) {
            actingAnimation = 6;
            this.actingTime = eatingTime;
            this.heling = heling;

     }

    @Override public void Acting() {
         super.Acting();
         if(soundID == -1 || AnimationGlobalTime.time() - lastSoundTime > soundCooldown) {
             lastSoundTime = AnimationGlobalTime.time();
             soundID = eatingSound.play(SoundSettings.soundVolume);
         }
     }
}
