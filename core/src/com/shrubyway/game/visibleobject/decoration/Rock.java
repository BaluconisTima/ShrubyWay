package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;


public class Rock extends Decoration {
    float halfTextureWidth;

    @Override public void change(float x, float y, int i, int j) {
        id = 2;
        halfTextureWidth = DecorationsManager.texture[id].getKeyFrame(0f).getRegionWidth() / 2f;
        DecorationsManager.texture[id].setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + MapSettings.TYLESIZE/2, y + 10);
        decorationI = i;
        decorationJ = j;

    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 60,
                    position.y + 20, 120, 40);
        else collisionBox.change(position.x + halfTextureWidth - 60, position.y + 20, 120, 40);
    }

    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, 60, 50);
    }


}
