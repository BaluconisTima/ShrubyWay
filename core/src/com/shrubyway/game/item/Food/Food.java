package com.shrubyway.game.item.Food;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.ItemActing;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.entity.Entity;

public class Food extends ItemActing {
    public float heling = 0;
    static Sound eatingSound = null;

    static float soundCooldownBase = 0.45f;
    float lastSoundTime = 0;
    long soundID = -1;

    static {
        eatingSound = ShrubyWay.assetManager.get("sounds/EFFECTS/Eating.ogg", Sound.class);
    }
    public Food(float eatingTime, float heling) {
            actingAnimation = 6;
            this.actingTime = eatingTime;
            this.heling = heling;
     }

     public void afterActing(Entity owner) {
     }

    @Override public void Acting() {
        stillActing = true;
        startActingTime = Math.min(startActingTime, AnimationGlobalTime.time());
        if(AnimationGlobalTime.time() - startActingTime > actingTime * Math.pow(0.96, ElementPumping.waterLevel)) {
            acted = true;
        }
         float soundCooldown = (float)(soundCooldownBase * Math.max(0.45f, Math.pow(0.98, ElementPumping.waterLevel)));
         if(soundID == -1 || AnimationGlobalTime.time() - lastSoundTime > soundCooldown) {
             lastSoundTime = AnimationGlobalTime.time();
             soundID = eatingSound.play(SoundSettings.soundVolume);
         }
     }
}
