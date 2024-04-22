package com.shrubyway.game.visibleobject.visibleitem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.myinterface.Inventory;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.VisibleObject;

public class VisibleItem extends VisibleObject {
    public Item item;
    private float dropTime;
    private Vector2 direction;
    private Vector2 globalDir;
    private Vector2 tempVector = new Vector2();
    static Sound pop = Gdx.audio.newSound(Gdx.files.internal("sounds/EFFECTS/Pop.ogg"));

    public  VisibleItem(Item item, Vector2 pos) {
        globalDir = new Vector2(0,0);
        pop.play(SoundSettings.soundVolume);
        this.item = item;
        position.set(pos);
        dropTime = AnimationGlobalTime.time();
    }
    public VisibleItem(Item item, float x, float y) {
        globalDir = new Vector2(0,0);
        pop.play(SoundSettings.soundVolume);
        this.item = item;
        position.set(x + (float)Math.random() * 10f - 5, y + (float)Math.random() * 10f - 5);
        dropTime = AnimationGlobalTime.time();
    }
    public VisibleItem(Item item, float x, float y, Vector2 globalDir) {
        this.globalDir = globalDir;
        globalDir.scl(50);
        pop.play(SoundSettings.soundVolume);
        this.item = item;
        position.set(x + (float)Math.random() * 10f - 5, y + (float)Math.random() * 10f - 5);
        dropTime = AnimationGlobalTime.time();
    }

    @Override
    public void render() {
        GlobalBatch.render(ItemManager.itemTexture[item.id],
                Math.round(position.x - ItemManager.itemTexture[item.id].getRegionWidth() / 4),
                Math.round(position.y - 16 * Math.sin(5 * (AnimationGlobalTime.time() - dropTime))),
                Math.round(ItemManager.itemTexture[item.id].getRegionWidth() * 0.5f),
                Math.round(ItemManager.itemTexture[item.id].getRegionHeight() * 0.5f));

    }

    public void delete() {
        pop.play(SoundSettings.soundVolume);
        Game.objectsList.del(this);
    }
    Vector2 dir = new Vector2(0,0);
    public void moveToPlayer(Vector2 playerPosition, Inventory inventory, float delta) {
        if(!inventory.havePlaceFor(item.id)) return;
        if(AnimationGlobalTime.time() - dropTime < 0.7f)  {
            globalDir.scl(0.82f);
            position.add(globalDir);
            return;
        }
        if(AnimationGlobalTime.time() - dropTime < 0.5f) return;

        dir.set(playerPosition.x - positionCenter().x, playerPosition.y - positionCenter().y);
        if(dir.len() <= 20) {
            if(ItemManager.checkIfSpecial(item.id)) {
                Sound coin = ShrubyWay.assetManager.get("sounds/EFFECTS/coin.ogg", Sound.class);
                coin.play(SoundSettings.soundVolume);
                ItemManager.addMoney(item.id);
                this.delete();
                return;
            }
            if(inventory.addItem(item)) this.delete();
        } else
            if(dir.len() < 150) {
            dir.nor();
            position.add(dir.scl(20 * delta / (1.f / 60.f)));
        }


    }

    public Vector2 positionCenter() {
        tempVector.set(position.x,
                 position.y + ItemManager.itemTexture[item.id].getRegionHeight() / 4);
        return tempVector;
    }

}