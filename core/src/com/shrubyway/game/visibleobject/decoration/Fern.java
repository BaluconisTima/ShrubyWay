package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.shapes.Rectangle;

public class Fern extends Decoration{
    {
        id = 17;
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 60,
                position.y + 50, 120, 100);
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 45,
                    position.y + 10, 90, 10);

        collisionBox.change(position.x + halfTextureWidth - 20,
                position.y + 10, 40, 10);
    }
}
