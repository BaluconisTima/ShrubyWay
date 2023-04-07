package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Bush extends Decoration {
    static Texture texture = new Texture(Gdx.files.internal("Decorations/BUSH.png"));
    Rectangle collisionBox;
    static float HalfTextureWidth = texture.getWidth() / 2;

    public Bush(float x, float y, int i, int j, char dt) {
        position.set(x - 250/2 + 150/2, y);
        collisionBox = new Rectangle(position.x + HalfTextureWidth - 10,
                position.y, 20, 10);
        Decoration_i = i;
        Decoration_j = j;
        Decoration_type = dt;
    }
    @Override public Rectangle collisionBox() {
        return collisionBox;
    }
    @Override public void Render(Batch batch) {
        batch.draw(texture, position.x,
                position.y);
        collisionBox().render(batch);
    }

}
