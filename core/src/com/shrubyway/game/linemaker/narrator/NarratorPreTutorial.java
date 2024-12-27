package com.shrubyway.game.linemaker.narrator;

import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.linemaker.ActionLogicManager;
import com.shrubyway.game.myinterface.TutorialHints;
import com.shrubyway.game.screen.Game;

public class NarratorPreTutorial extends NarratorActionLogic {

    {
        eventPrefix = "NAR_PRE_TUT_";
    }


    private static void hintsDeps() {
        if(TutorialHints.currentHint == 1 && TutorialHints.progress < 0.01f) TutorialHints.changeHint(2);
        if(TutorialHints.currentHint == 2 && ShrubyWay.inputProcessor.isRuning()) TutorialHints.finish();
    }
    Vector2 positionCenter = new Vector2(17300, 20600);
    Vector2 positionCamera = new Vector2(17500, 20000);
    boolean wasClose = false;
    Vector2 lastPlayerPosition = Game.player.position;

    int[] AngryLines = {4, 7, 41, 42};

    @Override public void update(float delta) {
        hintsDeps();

        if(delay > 0) {
            delay -= delta;
            return;
        }
        int stage = getStatus();
        switch (stage) {
            case 0:
                Game.player.stopMovment();
                waitLine(0, 0, 0.3f);
                break;
            case 1:
                waitLine(1, 1, 0.3f);
                break;
            case 2:
                Game.cameraFollowingPosition = positionCamera;
                if (waitLine(2, 2, 0.25f)) {
                    Game.player.AllowMovment();
                    Game.cameraFollowingPosition = null;
                    Game.player.moved = false;
                    TutorialHints.changeHint(1);
                    lastPlayerPosition = new Vector2(Game.player.position);
                    localDelay = 4f;
                }
                ;
                break;
            case 3:
                if(Game.player.position.dst(positionCenter) < 1000) {
                    wasClose = true;
                    localDelay = Math.min(localDelay, 0.3f);
                }
                if(Game.player.position.dst(positionCenter) < 700) {
                    ActionLogicManager.remove(this);
                    ActionLogicManager.add(new NarratorTutorial());
                    return;
                }
                if (localDelay > 0) {
                    localDelay -= delta;
                    return;
                }
                if(!Game.player.moved) {
                    if(!checkLine(3)) {
                        waitLine(3, 3, 0.3f);
                        localDelay = 5f;
                    }
                    return;
                }

                boolean triggerAngryLine = false;

                if(lastPlayerPosition.dst(positionCenter) < Game.player.position.dst(positionCenter) && Game.player.position.dst(positionCenter) > 2500) {
                    if(!checkLine(6)) {
                        waitLine(6, 0, 0.3f);
                        localDelay = 5f;
                    } else triggerAngryLine = true;
                } else
                    if (wasClose && Game.player.position.dst(positionCenter) > 1150) {
                        wasClose = false;
                        if(!checkLine(5)) {
                            waitLine(5, 0, 0.3f);
                            localDelay = 6f;
                        } else triggerAngryLine = true;
                    }



                if (triggerAngryLine) {
                    for(int i = 0; i < AngryLines.length; i++) {
                        if(!checkLine(AngryLines[i])) {
                            waitLine(AngryLines[i], 0, 0.3f);
                            localDelay = 5f;
                            break;
                        }
                    }
                }
                lastPlayerPosition = new Vector2(Game.player.position);
                localDelay = Math.max(localDelay, 0.1f);
                break;
        }


    }
}
