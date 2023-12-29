package com.shrubyway.game.linemaker.narrator;

import com.shrubyway.game.event.Event;
import com.shrubyway.game.linemaker.actionLogic;
import com.shrubyway.game.screen.Game;

public class Narrator {

    static public actionLogic logic;
    static public void update(float delta) {
        if(Event.happened("Forest_Shruby_jumped_out")) {
           if(!Event.happened("Forest_Tutorial_Finished")) {
               logic = new NarratorTutorial();
           }
        } else {
            if(Game.player.canAct()) {
                Event.cast("Forest_Shruby_jumped_out");
            }
        }
        if(logic != null) {
            logic.update(delta);
        }
    }



}
