package com.shrubyway.game.item.Food;

import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.effect.FruitExplosion;
import com.shrubyway.game.visibleobject.entity.Entity;

public class ExplruitEatingAct extends Food {
    public ExplruitEatingAct(float eatingTime, float heling) {
        super(eatingTime, heling);
    }
    @Override public void afterActing(Entity owner) {
        Game.objectsList.add(new FruitExplosion(owner.positionCenter().x, owner.positionCenter().y));
    }
}
