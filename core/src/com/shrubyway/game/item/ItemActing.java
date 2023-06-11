package com.shrubyway.game.item;

import com.shrubyway.game.animation.AnimationGlobalTime;

public abstract class ItemActing {
    float actingTime = 0.5f;
    float startActingTime = 1000000000;
    boolean acted = false;

    public int actingAnimation;
    public boolean stillActing = false;


    public void Acting() {
        stillActing = true;
        startActingTime = Math.min(startActingTime, AnimationGlobalTime.time());
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
        stillActing = false;
        startActingTime = 1000000000;
    }



}
