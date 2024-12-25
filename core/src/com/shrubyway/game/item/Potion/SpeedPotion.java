package com.shrubyway.game.item.Potion;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.effect.effecttypes.SpeedEffect;
import com.shrubyway.game.visibleobject.entity.Entity;

public class SpeedPotion extends Potion {
    float speed;
    public SpeedPotion(float duration, float speed) {
        super(duration);
        this.speed = speed;
        color = new Color(1, 0.91f, 0,1);
        if (speed < 1) {
            color = new Color(0, 0.80f, 1f, 1);
        }
    }

    @Override
    public void ApplyEffect(Entity owner) {
        new SpeedEffect(duration, owner, color, speed);
    }
}
