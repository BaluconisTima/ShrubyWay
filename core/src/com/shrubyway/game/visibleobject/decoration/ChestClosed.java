package com.shrubyway.game.visibleobject.decoration;

import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.util.ArrayList;

public class ChestClosed extends Decoration {
    public ArrayList<Item> items;
    public ArrayList<Float> chances, c;

    {
         id = 19;
         items = new ArrayList<Item>();
         chances = new ArrayList<Float>();
         c = new ArrayList<Float>();

         items.add(new Item(1)); chances.add(0.95f); c.add(0.6f);
         items.add(new Item(13)); chances.add(0.9f); c.add(0.5f);

         items.add(new Item(0)); chances.add(0.7f); c.add(0.5f);
         items.add(new Item(8)); chances.add(0.4f); c.add(0.75f);

         items.add(new Item(9)); chances.add(0.01f); c.add(0.0f);
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
       lastHitTime = AnimationGlobalTime.time();
    }

    @Override public void setInteractionBox() {
        if(interactionBox == null) interactionBox = new Rectangle(0,0,-1,-1);
        interactionBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, 60, 60);
    }

    @Override
    public Rectangle interactionBox() {
        return super.interactionBox();
    }

    @Override public void interact() {
        GlobalAssetManager.get("sounds/EFFECTS/chest.ogg", com.badlogic.gdx.audio.Sound.class).play(
                SoundSettings.soundVolume);
        Decoration temp = DecorationsManager.newOf(20);
        temp.change(decorationI * MapSettings.TYLESIZE,
                decorationJ * MapSettings.TYLESIZE,
                decorationI, decorationJ);

        ArrayList<Item> dropItems = new ArrayList<Item>();
        for(int i = 0; i < this.items.size(); i++){
           float p = this.chances.get(i);
           while(Math.random() <= p) {
                dropItems.add(this.items.get(i));
                p *= c.get(i);
           }
        }

        for(Item item : dropItems){
            System.out.println(item.id);
            float x = (float) (Math.random() * 30 - 15);
            VisibleItem temp2 = new VisibleItem(item, position.x + 100 + x,
                    position.y - 10);
            Game.objectsList.add(temp2);
        }


        Game.objectsList.add(temp);
        Game.objectsList.del(this);
    }
}
