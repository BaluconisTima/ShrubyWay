package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.shapes.Rectangle;

public class Button {
    public Texture textureNotSellected, textureSellected;
    Vector2 position;
    public Rectangle rectangle;
    float lastScale = 1;


    public Button(Texture textureNotSellected, Texture textureSellected, float x, float y) {
        this.textureNotSellected = textureNotSellected;
        this.textureSellected = textureSellected;
        this.position = new Vector2(x, y);
        rectangle = new Rectangle(position.x, position.y,
                textureNotSellected.getWidth(), textureNotSellected.getHeight());
    }

    public void set(float x, float y) {
        this.position = new Vector2(x, y);
        lastScale = -1;
        rectangle = new Rectangle(position.x, position.y,
                textureNotSellected.getWidth(), textureNotSellected.getHeight());
    }

    public void update() {
        if(lastScale != GlobalBatch.getScale()) {
            lastScale = GlobalBatch.getScale();
            rectangle = new Rectangle(position.x * lastScale, position.y * lastScale,
                    textureNotSellected.getWidth() * lastScale, textureNotSellected.getHeight() * lastScale);
        }
    }

    public void render() {
        update();
        GlobalBatch.render(textureNotSellected, position.x, position.y);
    }

    public void render(Vector2 mousePosition) {
        update();
        if(rectangle.checkPoint(mousePosition)) {
            GlobalBatch.render(textureSellected, position.x, position.y);
        } else {
            GlobalBatch.render(textureNotSellected, position.x, position.y);
        }
    }

    public void renderSellected() {
        update();
        GlobalBatch.render(textureSellected, position.x, position.y);
    }
}
