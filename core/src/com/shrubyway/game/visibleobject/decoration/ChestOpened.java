package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;

public class ChestOpened extends Decoration {
    {
        id = 20;
    }
    @Override public void change(float x, float y, int i, int j) {
        halfTextureWidth = DecorationsManager.texture[id].getKeyFrame(0f).getRegionWidth() / 2f;
        DecorationsManager.texture[id].setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + MapSettings.TYLESIZE/2, y + 30);
        decorationI = i;
        decorationJ = j;
        //System.out.println(decorationI + " " + decorationJ);
        lastHitTime = AnimationGlobalTime.time();
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

    }
}
