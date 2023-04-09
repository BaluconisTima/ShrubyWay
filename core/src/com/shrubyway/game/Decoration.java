package com.shrubyway.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Decoration extends VisibleObject{
    public int Decoration_i = 0, Decoration_j = 0;
    public char Decoration_type = 0;

    public void change(float x, float y, int i, int j) {
        position.set(x, y);
        Decoration_i = i;
        Decoration_j = j;
    }

    public Decoration newTemp() {
        return new Decoration();
    }

}
