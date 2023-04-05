package com.shrubyway.game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
public class Shraby {
    private final Vector2 position = new Vector2();
    private final byte FaceDirection = 0;
    private final int Speed = 2;
    private final Texture texture;

    public Shraby(float x, float y) {
      texture = new Texture("Test.png");
      position.set(x, y);
    }

    public void render(Batch batch) {
           batch.draw(texture, position.x, position.y);
    }
    public void moveTo(Vector2 direction) {
        position.add(direction.scl(Speed));
    }

    public void dispose() {
        texture.dispose();
    }

}