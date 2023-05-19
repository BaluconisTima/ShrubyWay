package com.shrubyway.game.visibleobject.decoration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Willow extends Decoration {
    static Animation<TextureRegion> texture;
    static String way = "Decorations/WILLOW.png";

    static {
        texture = animator.toAnimation((Texture)GlobalAssetManager.get(way, Texture.class), 8, 0, 0);
    }
    static float halfTextureWidth = texture.getKeyFrame(0f).getRegionWidth() / 2f;

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
        texture.setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + 150/2, y + 30);
        decorationI = i;
        decorationI = j;
        decorationType = '4';
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

    @Override public void render(Batch batch){
        batch.draw(texture.getKeyFrame(AnimationGlobalTime.time() - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        collisionBox().render(batch);
        hitBox().render(batch);
    };

}