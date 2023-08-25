package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.shapes.Rectangle;

public class MapleStump extends Decoration{
    {
        id = 13;
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 28,
                    position.y + 15, 70, 15);

        collisionBox.change(position.x + halfTextureWidth - 24,
                position.y + 15, 50, 15);
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,-1,-1);

    }
}
