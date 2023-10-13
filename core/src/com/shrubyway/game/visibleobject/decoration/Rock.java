package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.shapes.Rectangle;


public class Rock extends Decoration {
    {
        id = 1;
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


}
