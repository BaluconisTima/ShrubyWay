package com.shrubyway.game.waiters.Narrator;

import com.shrubyway.game.event.Event;
import com.shrubyway.game.linemaker.ActionLogicManager;
import com.shrubyway.game.linemaker.narrator.NarratorPreTutorial;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.waiters.Waiter;

public class NarratorStart extends Waiter {
    @Override
    public boolean checkCondition() {
        if(Game.player == null) {
            return false;
        }
        return Game.player.canAct();
    }

    @Override
    public void action() {
        Event.cast("narrator_start_triggered");
        ActionLogicManager.add(new NarratorPreTutorial());
    }
}
