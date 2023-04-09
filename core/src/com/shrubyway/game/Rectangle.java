package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;

public class Rectangle {
    public final Vector2 topLeftCorner = new Vector2();
    public final Vector2 bottomRightCorner = new Vector2();

    private ShapeRenderer shapeRenderer;
    private Texture texture;
    public Rectangle(float x, float y, float width, float height) {
        topLeftCorner.set(x, y);
        bottomRightCorner.set(x + width, y + height);
        texture = new Texture("TEST.png");
    }
    public void change(float x, float y, float width, float height) {
        topLeftCorner.set(x, y);
        bottomRightCorner.set(x + width, y + height);
    }
    public boolean overlaps(Rectangle x) {
        return (topLeftCorner.x < x.bottomRightCorner.x && bottomRightCorner.x > x.topLeftCorner.x &&
                topLeftCorner.y < x.bottomRightCorner.y && bottomRightCorner.y > x.topLeftCorner.y);
    }
    public boolean overlaps(circle x) {
        Vector2 closestPoint = new Vector2();
        closestPoint.x = Math.max(topLeftCorner.x, Math.min(x.centerPosition.x, bottomRightCorner.x));
        closestPoint.y = Math.max(topLeftCorner.y, Math.min(x.centerPosition.y, bottomRightCorner.y));
        return (x.centerPosition.dst2(closestPoint) < x.Radius * x.Radius);
    }

    public void render(Batch batch) {
         batch.draw(texture,topLeftCorner.x, topLeftCorner.y, bottomRightCorner.x - topLeftCorner.x, bottomRightCorner.y - topLeftCorner.y);
    }

}
