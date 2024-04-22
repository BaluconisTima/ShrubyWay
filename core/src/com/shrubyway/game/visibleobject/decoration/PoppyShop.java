package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.shapes.Rectangle;
public class PoppyShop extends Decoration {
    public PoppyShop() {
        id = 22;
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 300,
                    position.y + 15, 600, 300);
        else collisionBox.change(position.x + halfTextureWidth - 300, position.y + 15, 600, 300);
    }
    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, 60, 50);
    }
    @Override public void hit(float damage, Vector2 hitPosition) {
       // lastHitTime = AnimationGlobalTime.time();
    }
    @Override public void setInteractionBox() {
        if(interactionBox == null) interactionBox = new Rectangle(0,0,-1,-1);
        interactionBox.change(position.x + halfTextureWidth - 40,
                position.y + 40, 80, 80);
    }
}
