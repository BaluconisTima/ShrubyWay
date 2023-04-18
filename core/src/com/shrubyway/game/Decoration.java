package com.shrubyway.game;

import com.badlogic.gdx.Gdx;

abstract class Decoration extends VisibleObject{
    public int decorationI = 0, decorationJ = 0;
    public char decorationType;
    static Animator animator = new Animator();
    protected float lastInteraction = 0f;

    public void change(float x, float y, int i, int j) {
        position.set(x, y);
        decorationI = i;
        decorationJ = j;
    }

    public void interact() {
        lastInteraction = AnimationGlobalTime.x;
    }
     public Decoration newTemp() {
        return null;
    }


}
