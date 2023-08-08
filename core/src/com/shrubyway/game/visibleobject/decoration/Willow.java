package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Willow extends Decoration {
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
        id = 3;
        halfTextureWidth = DecorationsManager.texture[id].getKeyFrame(0f).getRegionWidth() / 2f;
        DecorationsManager.texture[id].setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + MapSettings.TYLESIZE/2, y + 30);
        decorationI = i;
        decorationI = j;

    }

    @Override public void interact() {
        super.interact();
        if(Event.happened(Event.getEvent("harmonicaDroped"))) return;
        if(Math.random() * 100 < 10) {
            VisibleItem item = new VisibleItem(ItemManager.newItem(4), position.x + halfTextureWidth - 60,
                    position.y + 50);
            ObjectsList.add(item);
            Event.cast(Event.getEvent("harmonicaDroped"));
        }
    }


}
