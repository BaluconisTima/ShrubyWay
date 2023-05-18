package com.shrubyway.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.TextDrawer;

import java.util.ArrayList;

public class LoadingScreen extends Screen{
    static Texture background = new Texture("interface/SWbackgound.png");
    static Texture loadingBar = new Texture("interface/loading.png");
    static Texture loadStatus = new Texture("interface/loadingStatus.png");
    static Texture logo = new Texture("interface/SWlogo.png");
    static Texture phrase = new Texture("interface/phrase.png");
    static Batch batch = new SpriteBatch();
    static int maxStatus = 0;
    static ArrayList<String> phrases = new ArrayList<>();
    static {
        phrases.add("Shruby is not a leek");
        phrases.add("Shruby is not a grass");
        phrases.add("Shruby is not a tree");
        phrases.add("Shruby is not a bush");
        phrases.add("Shruby is probably not a shrub(?)");
        phrases.add("Shruby is not a stem");

        phrases.add("Music can bring back strange memories");
        phrases.add("Don't eat fly agarics!");
        phrases.add("Pump the elements evenly");
        phrases.add("If something seems unreal, it probably is");
        phrases.add("Remember to save!");
        phrases.add("Explosive berries are not the best food.");
        phrases.add("The chests contain many interesting things!");
        phrases.add("You are playing an early version of the game");
        phrases.add("QBA'G ORYVRIR GUR ANEENGBE");
        phrases.add("The old willow tree holds treasures.");
        phrases.add("You are not the first hero");

    }
    static int phraseOfday = 0;
    static public void updateStatus(int status) {
        maxStatus = status;
    }

    static public void startLoading() {
        maxStatus = 0;
        loadingStatus = 0;
        phraseOfday = (int) (Math.random() * phrases.size());
    }
     float last = 1;
     @Override public void updateScreen() {
        if(loadingStatus < maxStatus) {
            last *= 1.01;
            last = Math.min(last, 100);
            loadingStatus += Math.min(last, Math.floor((maxStatus - loadingStatus) / 100f) + 1);
            loadingStatus = Math.min(loadingStatus, maxStatus);
        } else {
            last = 1;

        }
    }
    @Override public void renderScreen() {
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(logo, 564, 450);
        batch.draw(loadingBar, 342, 50);
        batch.draw(new TextureRegion(loadStatus, 0, 0, loadingStatus * loadStatus.getWidth() / 100,
                loadStatus.getHeight()), 378, 80);
        batch.draw(phrase, 454, 230);
        batch.end();

        batch.begin();
        TextDrawer.drawCenterWhite(batch, phrases.get(phraseOfday), 0, 325, 0.7f);
        batch.end();
    }



}