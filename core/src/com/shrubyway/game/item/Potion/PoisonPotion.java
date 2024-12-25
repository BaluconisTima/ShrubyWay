package com.shrubyway.game.item.Potion;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.effect.effecttypes.PoisonEffect;
import com.shrubyway.game.visibleobject.entity.Entity;

public class PoisonPotion extends Potion {

    float damage;
    public PoisonPotion(float duration, float damage) {
        super(duration);
        this.damage = damage;
        color = Color.GREEN;
    }

    @Override
    public void ApplyEffect(Entity owner) {
        new PoisonEffect(duration, owner, color, damage);
    }
}
