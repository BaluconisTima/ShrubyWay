package com.shrubyway.game.visibleobject.decoration;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundAtPosition;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Willow extends Decoration {
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

    {
        id = 10;
    }

    @Override public void hit() {
        super.hit();
        GlobalSoundManager.addSound(new SoundAtPosition(
                ShrubyWay.assetManager.get("sounds/EFFECTS/bush.ogg", Sound.class), position));

        if(Event.happened(Event.getEvent("harmonicaDroped"))) return;
        if(Math.random() * 100 < 10) {
            VisibleItem item = new VisibleItem(ItemManager.newItem(7), position.x + halfTextureWidth - 60,
                    position.y + 50);
            Game.objectsList.add(item);
            Event.cast(Event.getEvent("harmonicaDroped"));
        }
    }


}
