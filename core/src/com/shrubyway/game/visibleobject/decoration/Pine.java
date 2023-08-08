package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Pine extends Decoration {
    float halfTextureWidth;

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
                position.y + 10, 90, 10);
    }
    @Override public void change(float x, float y, int i, int j) {
        id = 1;
        halfTextureWidth = DecorationsManager.texture[id].getKeyFrame(0f).getRegionWidth() / 2f;
        DecorationsManager.texture[id].setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + MapSettings.TYLESIZE/2, y + 30);
        decorationI = i;
        decorationI = j;

    }

    @Override public void interact() {
        super.interact();
        if(Math.random() < 0.2) {
            ObjectsList.add(new VisibleItem(ItemManager.newItem(2),
                    position().x + halfTextureWidth  + ((float) Math.random() * 200f - 100f),
                    position().y + 50, new Vector2(0, -0.2f)));
        }
        if(Math.random() < 0.02) {
            ObjectsList.add(MobsManager.newOf(1, position().x + halfTextureWidth  + ((float) Math.random() * 200f - 100f),
                    position().y + 50));
        }
    }

}
