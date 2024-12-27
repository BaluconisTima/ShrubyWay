package com.shrubyway.game.linemaker.narrator;

import com.shrubyway.game.event.Event;
import com.shrubyway.game.linemaker.ActionLogic;
import com.shrubyway.game.screen.Game;

public class NarratorActionLogic extends ActionLogic {
    public float delay = 0.3f, localDelay = 0f;
    String eventPrefix = "NAR_";
    boolean waitLine(int id, int stage_id, float delayAfter) {
        if(!Event.happened("Line_NAR_" + id + "_Casted")) {
            Game.LineMaker.castLine(0, id);
        } else {
            if(Event.happened("Line_NAR_" + id + "_Finished")) {
                delay = delayAfter;
                Event.cast(eventPrefix + stage_id);
                return true;
            }
        }
        return false;
    }

    boolean checkLine(int id) {
        return Event.happened("Line_NAR_" + id + "_Finished");
    }

    protected int getStatus() {
        int i = 0;
        while(Event.happened(eventPrefix + i)) {
            i++;
        }
        return i;
    }

}
