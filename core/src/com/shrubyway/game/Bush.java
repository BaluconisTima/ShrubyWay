package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Bush extends VisibleObject{
    private Texture texture = new Texture(Gdx.files.internal("Decorations/BUSH.png"));
    public Bush(float x, float y) {
        position.set(x, y);
    }
    @Override public void Render(Batch batch) {
        batch.draw(texture, position.x - 250/2 + 150/2,
                position.y);
    }

}
