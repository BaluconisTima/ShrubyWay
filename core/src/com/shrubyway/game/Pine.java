package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Pine extends Decoration {
    static Texture texture = new Texture(Gdx.files.internal("Decorations/PINE.png"));
    Rectangle collisionBox = null;
    static float HalfTextureWidth = texture.getWidth() / 2;

    @Override public void change(float x, float y, int i, int j) {
        position.set(x - HalfTextureWidth + 150/2, y);
        Decoration_i = i;
        Decoration_j = j;
        Decoration_type = '2';
    }
    @Override public Rectangle collisionBox() {
        if(collisionBox == null)
            collisionBox = new Rectangle(position.x + HalfTextureWidth - 45,
                    position.y, 90, 10);
        return collisionBox;
    }
    @Override public Pine newTemp() {
        return new Pine();
    }
    @Override public void Render(Batch batch){
        batch.draw(texture, position.x, position.y);
        if(collisionBox != null) collisionBox.render(batch);
    };

}