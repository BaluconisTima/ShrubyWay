package com.shrubyway.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

import java.util.TreeSet;

public class Bullet extends Entity {
   TextureRegion BulletTexture = new TextureRegion(new Texture("Entities/WIND.png"));
   protected Vector2 finishPosition, Direction;
    public Bullet(Vector2 StartPosition, Vector2 FinishPosition) {
        position.set(StartPosition);
        Speed = 30f;
       Direction =
               new Vector2(FinishPosition.x -position.x, FinishPosition.y - position.y).nor();
       Direction.scl(getSpeed());
       finishPosition = new Vector2(FinishPosition);

    }

    public void TryMoveTo() {
        position.add(Direction);
    }
    @Override public void Render(Batch batch) {
        collisionBox.change(position.x, position.y, -10, -10);
        collisionBox().render(batch);
        batch.draw(BulletTexture, position.x, position.y, 0, 0,
                BulletTexture.getRegionWidth(), BulletTexture.getRegionHeight(), 1, 1, Direction.angleDeg() - 90);

    }
}

