package com.shrubyway.game.item.Potion;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.effect.effecttypes.InvincibilityEffect;
import com.shrubyway.game.visibleobject.entity.Entity;

public class InvincibilityPotion extends Potion {
    public InvincibilityPotion(float duration) {
        super(duration);
        color = new Color(1f, 0, 0.1f, 0.8f);
    }

    @Override
    public void ApplyEffect(Entity owner) {
        new InvincibilityEffect(duration, color, owner);
    }
}
