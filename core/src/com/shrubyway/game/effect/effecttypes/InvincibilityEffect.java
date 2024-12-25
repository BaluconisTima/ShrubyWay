package com.shrubyway.game.effect.effecttypes;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.effect.Effect;
import com.shrubyway.game.visibleobject.entity.Entity;

public class InvincibilityEffect extends Effect {
    public InvincibilityEffect(float time, Color color, Entity owner) {
        super(time, color, owner);
        apply();
    }

    @Override public void merge(Effect effect) {
        if(effect instanceof InvincibilityEffect) {
            InvincibilityEffect invincibilityEffect = (InvincibilityEffect) effect;
            time_left = Math.max(invincibilityEffect.time_left, time_left);
        }
    }
}
