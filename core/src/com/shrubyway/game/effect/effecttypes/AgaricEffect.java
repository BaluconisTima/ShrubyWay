package com.shrubyway.game.effect.effecttypes;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.entity.Entity;
public class AgaricEffect extends SpeedEffect {
    public AgaricEffect(float time, Entity owner) {
        super(time, owner, new Color(0, 218f/256, 175f/256, 1), -0.7f);
        SoundSettings.setReverse(true);
    }

    @Override public void update(float delta) {
        super.update(delta);
        if(time_left <= 0) {
            owner.removeEffect(this);
            SoundSettings.setReverse(false);
        }
    }
}
