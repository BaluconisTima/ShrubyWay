package com.shrubyway.game.overlay;

import com.badlogic.gdx.math.Vector2;

abstract public class Overlay {
    public void pause() {

    }
    public void resume() {

    }
    public void update(float delta) {

    }

    public void rightClick(Vector2 clickPosition) {

    }
    public void leftClick(Vector2 clickPosition) {

    }
    public void scipButton() {

    }
    public void render(Vector2 mousePosition) {

    }
    public boolean isClosed() {
        return false;
    }

}
