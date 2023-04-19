package com.shrubyway.game.visibleobject.decoration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.RenderingList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Pine extends Decoration {
    static Animation<TextureRegion> texture
            = animator.toAnimation(new Texture("Decorations/PINE.png"), 8, 0, 0);
    static float halfTextureWidth = texture.getKeyFrame(0f).getRegionWidth() / 2f;

    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 60,
                position.y + 50, 120, 50);
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
        position.set(x - halfTextureWidth + 150/2, y);
        decorationI = i;
        decorationI = j;
        decorationType = '2';
    }

    @Override public void interact() {
        super.interact();
        if(Math.random() < 0.2) {
            RenderingList.addTemp(new VisibleItem(ItemManager.newItem(2),
                    position().x + halfTextureWidth  + ((float) Math.random() * 200f - 100f),
                    position().y + 3));
        }
    }

    @Override public Pine newTemp() {
        return new Pine();
    }
    @Override public void render(Batch batch){
        batch.draw(texture.getKeyFrame(AnimationGlobalTime.x - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        collisionBox().render(batch);
        hitBox().render(batch);
    };

}
