package com.shrubyway.game.effect;
import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.visibleobject.entity.Entity;

public abstract class Effect {
    protected float time_left;
    protected Entity owner;
    public Color color;
    public Effect(float time, Color color, Entity owner) {
        this.time_left = time;
        this.color = color;
        this.owner = owner;
    }

    public double colorStrength() {
        if(time_left > 3.14f) {
            return 1;
        } else {
           return Math.abs(Math.sin(time_left * 5));
        }
    }

    public void apply() {
        if (owner != null) owner.addEffect(this);
    }
    public void update(float delta) {
        time_left -= delta;
        if(time_left <= 0 && owner != null) {
            owner.removeEffect(this);
        }
    }

    public void merge(Effect effect) {
        time_left = Math.max(time_left, effect.time_left);
    }
}
