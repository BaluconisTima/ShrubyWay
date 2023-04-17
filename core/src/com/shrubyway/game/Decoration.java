package com.shrubyway.game;

abstract class Decoration extends VisibleObject{
    public int decorationI = 0, decorationJ = 0;
    public char decorationType;

    public void change(float x, float y, int i, int j) {
        position.set(x, y);
        decorationI = i;
        decorationJ = j;
    }
     public Decoration newTemp() {
        return null;
    }


}
