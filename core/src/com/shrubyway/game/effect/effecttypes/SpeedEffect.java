package com.shrubyway.game.effect.effecttypes;
import com.shrubyway.game.effect.Effect;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.badlogic.gdx.graphics.Color;
public class SpeedEffect extends Effect {

    public float speed;
        public SpeedEffect(float time, Entity owner, float speed, Color color) {
            super(time, Color.BLUE, owner);
            this.speed = speed;
            this.color = color;
        }

        @Override public void merge(Effect effect) {
            if(effect instanceof SpeedEffect) {
                SpeedEffect speedEffect = (SpeedEffect) effect;
                if(Math.abs(speedEffect.speed) > Math.abs(speed)) {
                    time_left = speedEffect.time_left + time_left * (speedEffect.speed - speed);
                    speed = speedEffect.speed;
                }
            }
        }
}
