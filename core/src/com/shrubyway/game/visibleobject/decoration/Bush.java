package com.shrubyway.game.visibleobject.decoration;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.io.Serializable;

public class Bush extends Decoration  {
    static Animation<TextureRegion> texture;
    static String way = "Decorations/BUSH.png";

    static {
        GlobalAssetManager.assetManager.load(way, Texture.class);
        GlobalAssetManager.assetManager.finishLoading();
        texture = animator.toAnimation((Texture)GlobalAssetManager.assetManager.get(way), 8, 0, 0);
    }

    @Override public void interact() {
        super.interact();
        if(Math.random() < 0.2) {
            ObjectsList.add(new VisibleItem(ItemManager.newItem(3),
                    position().x + halfTextureWidth  + ((float) Math.random() * 200f - 100f),
                    position().y + 50, new Vector2(0, -0.2f)));
        }
    }
    static float halfTextureWidth = texture.getKeyFrame(0f).getRegionWidth() / 2f;
    @Override
    public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 60,
                position.y + 30, 120, 75);
    }
    @Override
    public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 10,
                    position.y, 20, 10);
        else collisionBox.change(position.x + halfTextureWidth - 10,
                position.y, 20, 10);
    }

    @Override
    public void change(float x, float y, int i, int j) {
        texture.setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + 150/2, y);
        decorationI = i;
        decorationJ = j;
        decorationType = '1';
    }
    @Override
    public void render(Batch batch){
        batch.draw(texture.getKeyFrame(AnimationGlobalTime.time() - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        collisionBox().render(batch);
        hitBox().render(batch);
    };


}
