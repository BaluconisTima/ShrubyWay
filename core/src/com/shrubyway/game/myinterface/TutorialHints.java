package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;

public class TutorialHints {

    static boolean finished = false;
    public static float progress = 0;
    public static int currentHint = -1;

    static public void changeHint(int hintNumber) {
        finished = false;
        currentHint = hintNumber;
        progress = 0.02f;
    }


    static public void finish() {
        finished = true;
    }

    static public void update() {
        if(currentHint == -1) return;
        if(!finished) {
            progress *= 1.2f;
            if(progress > 1) {
                progress = 1;
            }
        } else {
            progress *= 0.9f;
            if(progress < 0.01f) {
                progress = 0;
               // currentHint = -1;
               // finished = false;
            }
        }
    }


    static public void render() {
        if(currentHint == -1) return;
        Texture hint = ShrubyWay.assetManager.get("interface/tutorial000" + currentHint + ".png", Texture.class);
        GlobalBatch.batch.setColor(1,1,1, progress);
        GlobalBatch.render(hint,1900 - hint.getWidth(), 1080/2 - hint.getHeight()/2 + 70);
        GlobalBatch.batch.setColor(1,1,1, 1);
    }
}
