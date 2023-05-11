package com.shrubyway.game.item;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.sound.SoundSettings;

public class Food extends ItemActing {
    public float heling = 0;
    static Sound eatingSound = null;

    static float soundCooldown = 0.6f;
    float lastSoundTime = 0;
    long soundID = -1;

    static {
        GlobalAssetManager.assetManager.load("sounds/EFFECTS/Eating.ogg", Sound.class);
        GlobalAssetManager.assetManager.finishLoading();
        eatingSound = GlobalAssetManager.assetManager.get("sounds/EFFECTS/Eating.ogg", Sound.class);
    }
    public Food(float eatingTime, float heling) {
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