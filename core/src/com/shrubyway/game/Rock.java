package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;


public class Rock extends Decoration {
    static Texture texture = new Texture(Gdx.files.internal("Decorations/ROCK.png"));
    static float HalfTextureWidth = texture.getWidth() / 2;

    @Override public void change(float x, float y, int i, int j) {
        position.set(x - HalfTextureWidth + 150/2, y + 10);
        decorationI = i;
        decorationJ = j;
        decorationType = '1';
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + HalfTextureWidth - 60,
                    position.y + 20, 120, 40);
        else collisionBox.change(position.x + HalfTextureWidth - 60, position.y + 20, 120, 40);
        return collisionBox;
    }
    @Override public Rock newTemp() {
        return new Rock();
    }
    @Override public void render(Batch batch){
        batch.draw(texture, position.x, position.y);
        if(collisionBox != null) collisionBox.render(batch);
    };


}
