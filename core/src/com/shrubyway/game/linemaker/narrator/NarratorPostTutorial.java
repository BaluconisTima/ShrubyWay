package com.shrubyway.game.linemaker.narrator;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.linemaker.ActionLogicManager;
import com.shrubyway.game.sound.SoundSettings;

public class NarratorPostTutorial extends NarratorActionLogic {
    {
        eventPrefix = "NAR_POST_TUT_";
        delay = 7f;
    }

    @Override public void update(float delta) {
        if (delay > 0) {
            delay -= delta;
            return;
        }
        int stage = getStatus();
        switch (stage) {
            case 0:
                if(waitLine(36, 0, 0.3f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/paper.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Map_Opened");
                }
                break;
            case 1:
                waitLine(38, 2, 0.3f);
                break;
            case 2:
                ActionLogicManager.remove(this);
                ActionLogicManager.add(new NarratorInGame());
                break;
        }
    }
}
