package com.shrubyway.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.shrubyway.game.shapes.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
    Texture textureNotSellected, textureSellected;
    Vector2 position;
    public Rectangle rectangle;

    public Button(Texture textureNotSellected, Texture textureSellected, float x, float y) {
        this.textureNotSellected = textureNotSellected;
        this.textureSellected = textureSellected;
        this.position = new Vector2(x, y);
        rectangle = new Rectangle(position.x, position.y,
                textureNotSellected.getWidth(), textureNotSellected.getHeight());
    }

    public void render(Batch batch) {
        batch.draw(textureNotSellected, position.x, position.y);
    }

    public void renderSellected(Batch batch) {
        batch.draw(textureSellected, position.x, position.y);
    }
}
