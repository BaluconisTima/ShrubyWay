package com.shrubyway.game.visibleobject.decoration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.shapes.Rectangle;

public class Pine extends Decoration {
    static Animation<TextureRegion> texture
            = animator.toAnimation(new Texture("Decorations/PINE.png"), 8, 0, 0);
    static float halfTextureWidth = texture.getKeyFrame(0f).getRegionWidth() / 2f;


    @Override public void change(float x, float y, int i, int j) {
        texture.setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + 150/2, y);
        decorationI = i;
        decorationI = j;
        decorationType = '2';
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 45,
                    position.y + 10, 90, 10);

            collisionBox.change(position.x + halfTextureWidth - 45,
                    position.y + 10, 90, 10);
        return collisionBox;
    }
    @Override public Pine newTemp() {
        return new Pine();
    }
    @Override public void render(Batch batch){
        batch.draw(texture.getKeyFrame(AnimationGlobalTime.x - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        if(collisionBox != null) collisionBox.render(batch);
    };

}
