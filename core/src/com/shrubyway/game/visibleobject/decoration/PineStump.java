package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.shapes.Rectangle;

public class PineStump extends Decoration{
    {
        id = 12;
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,-1,-1);
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 45,
                    position.y + 30, 50, 10);

        collisionBox.change(position.x + halfTextureWidth - 45,
                position.y + 10, 90, 10);
    }
}
