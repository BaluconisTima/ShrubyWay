package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;

public class Reeds extends Decoration{
    {
        id = 16;
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

        collisionBox.change(position.x + halfTextureWidth - 45,
                position.y + 10, -90, -10);
    }

    @Override public void render() {
        GlobalBatch.render(DecorationsManager.texture[id].getKeyFrame(AnimationGlobalTime.time()
                        - lastHitTime), Math.round(position.x - 20),
                Math.round(position.y - 15));
        collisionBox().render();
        hitBox().render();
    }
}
