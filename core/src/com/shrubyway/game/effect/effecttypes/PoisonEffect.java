package com.shrubyway.game.effect.effecttypes;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.effect.Effect;
import com.shrubyway.game.visibleobject.entity.Entity;

public class PoisonEffect extends Effect {
    public float damage;
    public float last_hit;

    private static float cooldown = 1.2f;
    public PoisonEffect(float time, Entity owner, Color color, float damage) {
        super(time, color, owner);
        this.damage = damage;
        this.last_hit = time;
        apply();
    }

    @Override public void update(float delta) {
        super.update(delta);
        if(last_hit - time_left > cooldown) {
            last_hit = time_left;
            owner.getDamageWithoutMomentum(damage, owner.positionCenter());
        }
    }

    @Override public void merge(Effect effect) {
        if(effect instanceof PoisonEffect) {
            PoisonEffect poisonEffect = (PoisonEffect) effect;
            if(poisonEffect.damage > damage) {
                time_left = poisonEffect.time_left + time_left * (poisonEffect.damage - damage);
                damage = poisonEffect.damage;
            }
        }
    }
}
