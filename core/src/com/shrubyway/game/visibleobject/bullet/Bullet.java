package com.shrubyway.game.visibleobject.bullet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.shrubyway.game.visibleobject.VisibleObject;


public class Bullet extends VisibleObject {
    static TextureRegion bulletTexture = new TextureRegion(new Texture("Entities/WIND.png"));
    public Vector2 direction;
    protected float speed;

    public Bullet(Vector2 startPosition, Vector2 finishPosition) {
        position.set(startPosition);
        speed = 3f;
        direction =
                new Vector2(finishPosition.x - position.x, finishPosition.y - position.y);
        direction.scl(1 / direction.len());
        direction.scl(speed);
    }

    public void tryMoveTo() {
        position.add(direction);
    }

    @Override
    public void render(Batch batch) {
        batch.draw(bulletTexture, Math.round(position.x), Math.round(position.y), 0, 0,
                bulletTexture.getRegionWidth(), bulletTexture.getRegionHeight(), 1, 1, Math.round(direction.angleDeg() - 90));

    }


}

