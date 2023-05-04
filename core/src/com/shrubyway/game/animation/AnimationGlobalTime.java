package com.shrubyway.game.animation;

import com.badlogic.gdx.utils.TimeUtils;

public class AnimationGlobalTime {
    public static float lastTime = 0;
    public static float stopedTime = -1;


    static public void clear() {
        lastTime = TimeUtils.nanoTime() / 1000000000.0f;
    }
    static public float time(){

        if(stopedTime == -1) return  TimeUtils.nanoTime() / 1000000000.0f - lastTime;
        else return stopedTime - lastTime;
    }

    static public void pause() {
        stopedTime = TimeUtils.nanoTime() / 1000000000.0f;
    }
    static public void resume() {
        lastTime += TimeUtils.nanoTime() / 1000000000.0f - stopedTime;
        stopedTime = -1;
    }
}
