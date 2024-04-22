package com.shrubyway.game.item.Food;

import com.shrubyway.game.item.Item;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class FriedMushroomsAct extends Food {
    public FriedMushroomsAct(float eatingTime, float heling) {
        super(eatingTime, heling);
    }
    @Override public void afterActing(Entity owner) {
        Game.objectsList.add(new VisibleItem(new Item(1), Game.player.positionLegs()));
    }
}
