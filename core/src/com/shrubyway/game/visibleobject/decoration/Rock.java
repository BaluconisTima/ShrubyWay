package com.shrubyway.game.visibleobject.decoration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalAssetManager;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.shapes.Rectangle;


public class Rock extends Decoration {
    static Animation<TextureRegion> texture;
    static String way = "Decorations/ROCK.png";

    static {
        texture = animator.toAnimation((Texture)GlobalAssetManager.get(way, Texture.class), 8, 0, 0);
    }
    static float halfTextureWidth = texture.getKeyFrame(0f).getRegionWidth() / 2f;

    @Override public void change(float x, float y, int i, int j) {
        texture.setPlayMode(Animation.PlayMode.NORMAL);
        position.set(x - halfTextureWidth + MapSettings.TYLESIZE/2, y + 10);
        decorationI = i;
        decorationJ = j;
        decorationType = '3';
    }
    @Override public void setCollisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + halfTextureWidth - 60,
                    position.y + 20, 120, 40);
        else collisionBox.change(position.x + halfTextureWidth - 60, position.y + 20, 120, 40);
    }

    @Override public void setHitbox() {
        if(hitBox == null) hitBox = new Rectangle(0,0,0,0);
        hitBox.change(position.x + halfTextureWidth - 30,
                position.y + 30, 60, 50);
    }

    @Override public void render(){
        GlobalBatch.render(texture.getKeyFrame(AnimationGlobalTime.time() - lastInteraction), Math.round(position.x),
                Math.round(position.y));
        collisionBox().render();
        hitBox().render();
    };


}
