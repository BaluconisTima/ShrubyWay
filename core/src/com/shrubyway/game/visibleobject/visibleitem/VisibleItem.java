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
import com.shrubyway.game.shapes.Rectangle;

public class VisibleItem extends VisibleObject {
    public Item item;
    private float dropTime;
    private Vector2 direction;
    private Vector2 globalDir;
    private Vector2 tempVector = new Vector2();
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
        globalDir.scl(50);
        pop.play(SoundSettings.soundVolume);
        this.item = item;
        position.set(x + (float)Math.random() * 10f - 5, y + (float)Math.random() * 10f - 5);
        dropTime = AnimationGlobalTime.x;
    }

    @Override
    public void render(Batch batch) {
        batch.draw(ItemManager.itemTexture[item.id],
                Math.round(position.x - ItemManager.itemTexture[item.id].getRegionWidth() / 4),
                Math.round(position.y - 16 * Math.sin(5 * (AnimationGlobalTime.x - dropTime))),
                Math.round(ItemManager.itemTexture[item.id].getRegionWidth() * 0.5f),
                Math.round(ItemManager.itemTexture[item.id].getRegionHeight() * 0.5f));

    }

    public void delete() {
        pop.play(SoundSettings.soundVolume);
        ObjectsList.del(this);
    }
    Vector2 dir = new Vector2(0,0);
    public void moveToPlayer(Vector2 playerPosition) {
        if(AnimationGlobalTime.x - dropTime < 0.7f)  {
            globalDir.scl(0.82f);
            position.add(globalDir);
            return;
        }
        if(AnimationGlobalTime.x - dropTime < 0.5f) return;

        dir.set(playerPosition.x - positionCenter().x, playerPosition.y - positionCenter().y);
        if(dir.len() <= 20) {
            if(Inventory.addItem(item)) this.delete();
        } else
            if(dir.len() < 150) {
            dir.nor();
            position.add(dir.scl(20));
        }


    }

    public Vector2 positionCenter() {
        tempVector.set(position.x,
                 position.y + ItemManager.itemTexture[item.id].getRegionHeight() / 4);
        return tempVector;
    }

}