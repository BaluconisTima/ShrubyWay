package com.shrubyway.game.visibleobject.visibleitem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.Inventory;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;

public class VisibleItem extends VisibleObject {
    public Item item;
    private float dropTime;
    private Vector2 direction;
    private Vector2 globalDir;
    static Sound pop = Gdx.audio.newSound(Gdx.files.internal("sounds/Effects/Pop.ogg"));

    public VisibleItem(Item item, float x, float y) {
        globalDir = new Vector2(0,0);
        pop.play(SoundSettings.soundVolume);
        this.item = item;
        position.set(x + (float)Math.random() * 10f - 5, y + (float)Math.random() * 10f - 5);
        dropTime = AnimationGlobalTime.x;
    }
    public VisibleItem(Item item, float x, float y, Vector2 globalDir) {
        this.globalDir = globalDir;
        this.globalDir.nor();
        globalDir.scl(50);
        pop.play(SoundSettings.soundVolume);
        this.item = item;
        position.set(x + (float)Math.random() * 10f - 5, y + (float)Math.random() * 10f - 5);
        dropTime = AnimationGlobalTime.x;
    }

    @Override
    public void render(Batch batch) {
        batch.draw(ItemManager.itemTexture[item.id],
                Math.round(position.x - ItemManager.itemTexture[item.id].getWidth() / 2),
                Math.round(position.y + 16 + 8 * Math.sin(5 * (AnimationGlobalTime.x - dropTime))),
                Math.round(ItemManager.itemTexture[item.id].getWidth() * 0.5f),
                Math.round(ItemManager.itemTexture[item.id].getHeight() * 0.5f));
    }

    public void delete() {
        pop.play(SoundSettings.soundVolume);
        ObjectsList.del(this);
    }

    public void moveToPlayer(Vector2 playerPosition) {
        if(AnimationGlobalTime.x - dropTime < 0.3f)  {
            globalDir.scl(0.8f);
            position.add(globalDir);
            return;
        }
        if(AnimationGlobalTime.x - dropTime < 0.5f) return;

        Vector2 dir = new Vector2(playerPosition.x - position.x, playerPosition.y - position.y);
        if(dir.len() <= 20 && Inventory.addItem(item)) {
            this.delete();
        } else
            if(dir.len() < 150) {
            dir.nor();
            position.add(dir.scl(20));
        }


    }

}