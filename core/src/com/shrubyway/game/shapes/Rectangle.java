package com.shrubyway.game.shapes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Rectangle {
    public final Vector2 topLeftCorner = new Vector2();
    public final Vector2 bottomRightCorner = new Vector2();

    public Rectangle(float x, float y, float width, float height) {
        topLeftCorner.set(x, y);
        bottomRightCorner.set(x + width, y + height);
    }
    public void change(float x, float y, float width, float height) {
        topLeftCorner.set(x, y);
        bottomRightCorner.set(x + width, y + height);
    }
    public boolean overlaps(Rectangle temp) {
        if(temp == null) return false;
        if(topLeftCorner.x >= bottomRightCorner.x ||
                topLeftCorner.y >= bottomRightCorner.y ||
                temp.topLeftCorner.x >= temp.bottomRightCorner.x ||
                temp.topLeftCorner.y >= temp.bottomRightCorner.y) {
            return false;
        }

        return (topLeftCorner.x < temp.bottomRightCorner.x &&
                bottomRightCorner.x > temp.topLeftCorner.x &&
                topLeftCorner.y < temp.bottomRightCorner.y &&
                bottomRightCorner.y > temp.topLeftCorner.y);
    }
    Vector2 closestPoint = new Vector2();
    public boolean overlaps(Circle x) {
        closestPoint.x = Math.max(topLeftCorner.x, Math.min(x.centerPosition.x, bottomRightCorner.x));
        closestPoint.y = Math.max(topLeftCorner.y, Math.min(x.centerPosition.y, bottomRightCorner.y));
        return (x.centerPosition.dst2(closestPoint) < x.radius * x.radius);
    }

    public boolean checkPoint(Vector2 point) {
        return (point.x >= topLeftCorner.x && point.x <= bottomRightCorner.x &&
                point.y >= topLeftCorner.y && point.y <= bottomRightCorner.y);
    }

    public void render(Batch batch) {
        //batch.draw(texture,topLeftCorner.x, topLeftCorner.y, bottomRightCorner.x - topLeftCorner.x, bottomRightCorner.y - topLeftCorner.y);
    }

}
