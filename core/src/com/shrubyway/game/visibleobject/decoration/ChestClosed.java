package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;

public class ChestClosed extends Decoration {
    {
        id = 19;
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 60,
                    position.y + 15, 120, 50);
        else collisionBox.change(position.x + halfTextureWidth - 60, position.y + 15, 120, 50);
    }

    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, 60, 50);
    }

    @Override public void hit() {
        Decoration temp = DecorationsManager.newOf(20);
        temp.change(decorationI * MapSettings.TYLESIZE,
                decorationJ * MapSettings.TYLESIZE,
                decorationI, decorationJ);


        Game.objectsList.add(temp);
        Game.objectsList.del(this);
    }
}
