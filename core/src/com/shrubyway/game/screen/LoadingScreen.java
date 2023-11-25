package com.shrubyway.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.TextDrawer;

import java.util.ArrayList;

public class LoadingScreen extends Screen{
    static Texture background;
    static Texture loadingBar;
    static Texture loadStatus;
    static Texture logo;
    static Texture phrase;
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

    public LoadingScreen() {
        if(background == null) ShrubyWay.assetManager.addAsset("interface/SWbackgound.png", Texture.class);
        if(loadingBar == null) ShrubyWay.assetManager.addAsset("interface/loading.png", Texture.class);
        if(loadStatus == null) ShrubyWay.assetManager.addAsset("interface/loadingStatus.png", Texture.class);
        if(logo == null) ShrubyWay.assetManager.addAsset("interface/SWlogo.png", Texture.class);
        if(phrase == null) ShrubyWay.assetManager.addAsset("interface/phrase.png", Texture.class);

        ShrubyWay.assetManager.load("interface/SWbackgound.png", Texture.class);
        ShrubyWay.assetManager.load("interface/loading.png", Texture.class);
        ShrubyWay.assetManager.load("interface/loadingStatus.png", Texture.class);
        ShrubyWay.assetManager.load("interface/SWlogo.png", Texture.class);
        ShrubyWay.assetManager.load("interface/phrase.png", Texture.class);


        background = ShrubyWay.assetManager.get("interface/SWbackgound.png", Texture.class);
        loadingBar = ShrubyWay.assetManager.get("interface/loading.png", Texture.class);
        loadStatus = ShrubyWay.assetManager.get("interface/loadingStatus.png", Texture.class);
        logo = ShrubyWay.assetManager.get("interface/SWlogo.png", Texture.class);
        phrase = ShrubyWay.assetManager.get("interface/phrase.png", Texture.class);
        startLoading();
    }

    static public void updateStatus(int status) {
        maxStatus = status;
    }

    private void goToMenu() {
        ShrubyWay.screen = new Menu();
    }

    static public void startLoading() {
        ShrubyWay.assetManager.loadAll();
        maxStatus = 0;
        loadingStatus = 0;
        phraseOfday = (int) (Math.random() * phrases.size());
    }


     @Override public void updateScreen() {
         if (ShrubyWay.assetManager.update()) {
             goToMenu();
         }
        updateStatus((int)(ShrubyWay.assetManager.getProgress() * 100));
        loadingStatus = maxStatus;
    }

    @Override public void renderScreen() {
        float centerX = GlobalBatch.centerX(), centerY = GlobalBatch.centerY();

        GlobalBatch.render(background, 0, 0);
        GlobalBatch.render(logo, centerX - logo.getWidth()/2, centerY + 80);
        GlobalBatch.render(loadingBar, centerX - loadingBar.getWidth()/2, centerY - loadingBar.getHeight()/2);
        GlobalBatch.render(new TextureRegion(loadStatus, 0, 0, loadingStatus * loadStatus.getWidth() / 100,
                loadStatus.getHeight()), centerX - loadingBar.getWidth()/2 + 36, centerY + 30 - loadingBar.getHeight()/2);
        GlobalBatch.render(phrase, 454, centerY - 270);
        TextDrawer.drawCenterWhite(phrases.get(phraseOfday), 1920 / 2, centerY - 270 + 80, 0.7f);
    }



}
