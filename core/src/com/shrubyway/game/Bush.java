package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bush extends Decoration {
    static Texture texture = new Texture(Gdx.files.internal("Decorations/BUSH.png"));

    static float halfTextureWidth = texture.getWidth() / 2;

    @Override public void change(float x, float y, int i, int j) {
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
        batch.draw(texture, position.x, position.y);
        if(collisionBox != null) collisionBox.render(batch);
    };


}
