package com.shrubyway.game.item.Food;

import com.shrubyway.game.effect.effecttypes.AgaricEffect;
import com.shrubyway.game.visibleobject.entity.Entity;

public class AgaricAct extends Food {
    float agaricTime = 20;
    public AgaricAct(float eatingTime, float heling, float agaricTime) {
        super(eatingTime, heling);
    }
    @Override public void afterActing(Entity owner) {
        new AgaricEffect(agaricTime, owner);
    }
}
