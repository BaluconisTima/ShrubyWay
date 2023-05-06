package com.shrubyway.game.item;

import com.shrubyway.game.animation.AnimationGlobalTime;

public class ItemActing {
    float actingTime = 0.5f;
    float lastActingTime;
    float startActingTime = 1000000000;
    boolean acted = false;


    public void Acting() {
        if(AnimationGlobalTime.time() - lastActingTime > 0.5f) {
            startActingTime = 1000000000;
        }
        startActingTime = Math.min(startActingTime, AnimationGlobalTime.time());
        lastActingTime = AnimationGlobalTime.time();
        if(AnimationGlobalTime.time() - startActingTime > actingTime) {
           acted = true;
        }
    }

    public boolean checkAct() {
        if(acted) {
            startActingTime = 1000000000;
            acted = false;
            return true;
        }
        return false;
    }

    public void stopActing() {
        startActingTime = 1000000000;
    }



}
