package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.shapes.Rectangle;

public class OakStump extends Decoration{
    {
        id = 11;
    }
    @Override public void hit(float damage, Vector2 hitPosition) {
        super.hit(damage, hitPosition);
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 28,
                    position.y + 15, 70, 15);

        collisionBox.change(position.x + halfTextureWidth - 28,
                position.y + 15, 70, 15);
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,-1,-1);
    }
}
