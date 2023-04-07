package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bullet extends VisibleObject {
    static Texture texture = new Texture(Gdx.files.internal("TEXT.png"));
    public Bullet(float x, float y) {
        position.set(x - texture.getWidth() / 2, y - texture.getWidth() / 2);
    }
    @Override public Rectangle collisionBox() {
        return new Rectangle(position.x,
                position.y, -1, -1);
    }
    @Override public void Render(Batch batch) {
        batch.draw(texture, position.x,
                position.y);
        collisionBox().render(batch);
    }
}
