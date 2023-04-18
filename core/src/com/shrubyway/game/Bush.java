package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bush extends Decoration {
    static Animation<TextureRegion> texture
            = animator.toAnimation(new Texture("Decorations/BUSH.png"), 8, 0, 0);
    static float halfTextureWidth = texture.getKeyFrame(0f).getRegionWidth() / 2f;

    @Override public void change(float x, float y, int i, int j) {
        texture.setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + 150/2, y);
        decorationI = i;
        decorationJ = j;
        decorationType = '1';
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 10,
                    position.y, 20, 10);
        else collisionBox.change(position.x + halfTextureWidth - 10,
                position.y, 20, 10);
        return collisionBox;
    }
    @Override public Bush newTemp() {
        return new Bush();
    }
    @Override public void render(Batch batch){
        batch.draw(texture.getKeyFrame(AnimationGlobalTime.x - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        if(collisionBox != null) collisionBox.render(batch);
    };


}
