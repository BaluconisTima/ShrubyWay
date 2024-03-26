package com.shrubyway.game.animation;

import com.badlogic.gdx.Gdx;

public class AnimationGlobalTime {
    public static float lastTime = 0;
    public static boolean stop = false;


    static public void clear() {
        lastTime = 0;
    }

    static public void setTime(float time) {
        lastTime = time;
    }
    static public float time(){
        return lastTime;
    }

    static public void update() {
        if(!stop) lastTime += Gdx.graphics.getDeltaTime();
    }

    static public void pause() {
        stop = true;
    }
    static public void resume() {
        stop = false;
    }
}
