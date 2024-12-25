package com.shrubyway.game.item.Food;

import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.effect.BerryExplosion;
import com.shrubyway.game.visibleobject.entity.Entity;

public class ExplerryEatingAct extends Food {
    public ExplerryEatingAct(float eatingTime, float heling) {
        super(eatingTime, heling);
    }
    @Override public void afterActing(Entity owner) {
        Game.objectsList.add(new BerryExplosion(owner.positionCenter().x, owner.positionCenter().y));
    }
}
