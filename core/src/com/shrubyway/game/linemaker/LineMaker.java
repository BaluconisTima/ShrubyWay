package com.shrubyway.game.linemaker;

import com.badlogic.gdx.audio.Music;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.sound.SoundSettings;

import java.io.Serializable;
import java.util.HashMap;

public class LineMaker implements Serializable {

    static private final int VOICES = 3;
    static HashMap<Integer, String> lineText[] = new HashMap[VOICES];

    static String names[] = new String[VOICES];
    static String displayedName[] = new String[VOICES];
    static Music lastLine[] = new Music[VOICES];
    static int lastLineID[] = new int[VOICES];

    static {
        for(int i = 0; i < VOICES; i++) {
            lineText[i] = new HashMap<Integer, String>();
            lastLine[i] = null;
        }
        names[0] = "NAR"; displayedName[0] = "Narrator";
        names[1] = "POP"; displayedName[1] = "Poppy";
        names[2] = "BEA"; displayedName[2] = "Beau";




        loadText();

    }

    static public void update(float delta) {
        for(int i = 0; i < VOICES; i++) {
            if(lastLine[i] != null) {
                lastLine[i].setVolume(SoundSettings.soundVolume);
            }
            if(lastLine[i] != null && !lastLine[i].isPlaying()) {
                Game.lineRenderer.clearText();
                Event.cast("Line_" + names[i] + "_" + lastLineID[i] + "_Finished");
                lastLine[i] = null;
            }
        }
    }

    static public void skip() {
        for(int i = 0; i < VOICES; i++) {
            stopLine(i);
        }
    }


    static public void pause() {
        for(int i = 0; i < VOICES; i++) {
            if(lastLine[i] != null && lastLine[i].isPlaying()) {
                lastLine[i].pause();
            }
        }
    }

    static public void resume() {
        for(int i = 0; i < VOICES; i++) {
            if(lastLine[i] != null && !lastLine[i].isPlaying()) {
                lastLine[i].play();
            }
        }
    }

    static public void stopLine(int ID) {
        if(lastLine[ID] != null && lastLine[ID].isPlaying()) {
            Game.lineRenderer.clearText();
            Event.cast("Line_" + names[ID] + "_" + lastLineID[ID] + "_Finished");
            lastLine[ID].stop();
            lastLine[ID] = null;
        }
    }
    static public void castLine(int ID, int lineID) {

        if(lastLine[ID] != null && lastLine[ID].isPlaying()) {
            Game.lineRenderer.clearText();
            Event.cast("Line_" + names[ID] + "_" + lastLineID[ID] + "_Finished");
            lastLine[ID].stop();
            lastLine[ID] = null;
        }

        lastLine[ID] = ShrubyWay.assetManager.get("sounds/LINES/" + names[ID] + "/" + lineID + ".mp3", Music.class);
        Event.cast("Line_" + names[ID] + "_" + lineID + "_Casted");
        Game.lineRenderer.setText(lineText[ID].get(lineID), displayedName[ID]);
        lastLineID[ID] = lineID;
        lastLine[ID].play();
        lastLine[ID].setVolume(SoundSettings.soundVolume);
    }

    static void loadText() {
        lineText[0].put(0, "Here is one of the worlds engulfed in darkness, needing our salvation.");
        lineText[0].put(1, "You'll need a map to get oriented in this world.");
        lineText[0].put(2, "Look around to see if there are any minions of darkness nearby.");
        lineText[0].put(3, "I said to look around, as in walking, not just standing and looking.");
        lineText[0].put(4, "You do understand English, right?");
        lineText[0].put(5, "El traductor no está disponible");
        lineText[0].put(6, "Great! Keep moving.");
        lineText[0].put(7, "Shruby, feel the power of fire awakening in your hands as you fight!");
        lineText[0].put(8, "Here is the first minion!");
        lineText[0].put(9, "Well done! The power of fire will always aid you in battles, enveloping your fists and speeding up your strikes.");
        lineText[0].put(10, "But always remember to defend yourself; that's when the power of stone will help you.");
        lineText[0].put(11, "The forces of darkness won't inflict much damage on you when this magic protects you!");
        lineText[0].put(12, "So, continue searching for minions.");
        lineText[0].put(13, "Do you see that? That's another minion.");
        lineText[0].put(14, "But they're too far for you to attack with your hands...");
        lineText[0].put(15, "It's time to harness the power of wind!");
        lineText[0].put(16, "Grab something and throw it right at them.");
        lineText[0].put(17, "Yes, that's perfect for the throw!");
        lineText[0].put(18, "Try to throw a little... More accurately.");
        lineText[0].put(19, "Excellent! The power of wind helps you throw objects far and deal significant damage.");
        lineText[0].put(20, "However, the forces of darkness are more dangerous than you might think.");
        lineText[0].put(21, "But the power of water will aid you in recovery after battles.");
        lineText[0].put(22, "If you eat some food, you'll experience regeneration in your body.");
        lineText[0].put(23, "This looks like food! You can eat it.");
        lineText[0].put(24, "I see something very wrong with this...");
        lineText[0].put(25, "While battling the forces of evil, remember to check your surroundings for items that will assist you.");
        lineText[0].put(26, "Even in ordinary trees, you might find something for future use.");
        lineText[0].put(27, "Here's another group of darkness minions. Show them, Shruby!");
        lineText[0].put(28, "The more darkness you defeat, the faster you awaken your abilities. Choose the element you want to awaken now.");
        lineText[0].put(29, "Excellent! Oh, mighty hero, now you will face more battles with darkness!");
        lineText[0].put(30, "But you'll have to work on your powers for a long time to free this world.");
        lineText[0].put(31, "I can only help you sometimes, but I'll watch your progress.");
        lineText[0].put(32, "Keep destroying the minions of darkness and saving the light, and I'll let you know when you're ready to move on.");
        lineText[0].put(33, "Good luck, Shruby!");
        lineText[0].put(34, "You can do that, but you better try wind power too.");
        lineText[0].put(35, "Uh, okay. I guess you'll figure it out sooner or later.");
        lineText[0].put(36, "Don't get ahead of yourself, I'll introduce you to this power later!");
        lineText[0].put(37, "Okay...");
        lineText[0].put(38, "Okay..?");
        lineText[0].put(39, "Okaaaay");
        lineText[0].put(40, "Okay, now you're just being weird.");
    }

}
