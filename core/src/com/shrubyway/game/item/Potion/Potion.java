package com.shrubyway.game.item.Potion;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemActing;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Potion extends ItemActing {
    static Sound drinkingSound = null;

    public Color color = Color.WHITE;
    float duration = 0;

    static {
        drinkingSound = ShrubyWay.assetManager.get("sounds/EFFECTS/drink.ogg", Sound.class);
    }

    public void ApplyEffect(Entity owner){}

    public Potion(float duration) {
        this.duration = duration;
        this.actingTime = 0.1f;
    }

    public void afterActing(Entity owner) {
        drinkingSound.play(SoundSettings.soundVolume);
        ApplyEffect(owner);
        Game.objectsList.add(new VisibleItem(new Item(21), owner.positionLegs()));
    }


}
