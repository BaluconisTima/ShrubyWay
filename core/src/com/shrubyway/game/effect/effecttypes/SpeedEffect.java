package com.shrubyway.game.effect.effecttypes;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.effect.Effect;
import com.shrubyway.game.visibleobject.entity.Entity;
public class SpeedEffect extends Effect {

    public float speed;
        public SpeedEffect(float time, Entity owner, Color color, float speed) {
            super(time, color, owner);
            this.speed = speed;
            apply();
        }

        @Override public void merge(Effect effect) {
            if(effect instanceof SpeedEffect) {
                SpeedEffect speedEffect = (SpeedEffect) effect;

                time_left = (time_left + speedEffect.time_left) / 2;
                speed *= speedEffect.speed;
                color = new Color((color.r + speedEffect.color.r) / 2,
                            (color.g + speedEffect.color.g) / 2,
                            (color.b + speedEffect.color.b) / 2,
                            (color.a + speedEffect.color.a) / 2);
            }
        }
}
