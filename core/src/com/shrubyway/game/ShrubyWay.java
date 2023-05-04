package com.shrubyway.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.shrubyway.game.adapters.MyInputAdapter;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.Screen;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.screen.Menu;
import com.shrubyway.game.sound.SoundSettings;

import java.util.concurrent.CountDownLatch;

public class ShrubyWay extends ApplicationAdapter {
    static Screen screen;
    static public MyInputAdapter inputProcessor = new MyInputAdapter();
    @Override public void create() {
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setVSync(true);
        screen = new Menu();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override public void render() {
        SoundSettings.update();
        if(screen.loaded) screen.updateScreen();
        if(screen instanceof Menu) {
            if(((Menu) screen).goToGame) {
                screen = new Game();
                render();
            }
        } else
            if(screen instanceof Game) {

        }
        if(screen.loaded) screen.renderScreen();
    }
}
