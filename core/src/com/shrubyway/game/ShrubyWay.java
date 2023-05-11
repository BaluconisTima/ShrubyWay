package com.shrubyway.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.shrubyway.game.adapters.MyInputAdapter;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.*;
import com.shrubyway.game.sound.SoundSettings;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShrubyWay extends ApplicationAdapter {
    static Screen screen;
    static Screen menu;
    static Screen gameOver;

    static Screen errorInformationScreen;

    //static Screen loadScreen = new loadScreen();

    static public MyInputAdapter inputProcessor = new MyInputAdapter();

    @Override public void create() {
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setVSync(true);
        menu = new Menu();
        gameOver = new GameOver();
        //errorInformationScreen = new ErrorInformationScreen("!");

      /*  LocalDate today = LocalDate.now();
        LocalDate targetDate = LocalDate.of(2023, 5, 10);
        if(today.isBefore(targetDate)) {
            screen = new ErrorInformationScreen("It's too soon! The game will be open on 10.05.2023");
            Gdx.input.setInputProcessor(inputProcessor);
            return;
        } else if(today.isAfter(targetDate)) {
            screen = new ErrorInformationScreen("It's too late! The game was open on 10.05.2023");
            Gdx.input.setInputProcessor(inputProcessor);
            return;
        } */
        screen = menu;
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override public void render() {
        SoundSettings.update();
        GlobalAssetManager.assetManager.finishLoading();

        if(Screen.loadingStatus.get() == 100) {
            screen.updateScreen();
            if(screen instanceof Menu && ((Menu)screen).goToGame) {
                Menu.goToGame = false;
                screen = new Game();
                render();
            } else if(screen instanceof Game) {
                if(((Game)screen).gameOver) {
                    ((Game)screen).gameOver = false;
                    screen = gameOver;
                    render();
                } else if(((Game)screen).menu) {
                    ((Game)screen).menu = false;
                    screen = menu;
                    render();
                }
            } else if(screen instanceof GameOver) {
                   if(((GameOver)screen).tryingAgain) {
                       ((GameOver)screen).tryingAgain = false;
                       screen = new Game();
                       render();
                   } else if(((GameOver)screen).exit) {
                       ((GameOver)screen).exit = false;
                       screen = menu;
                       render();
                   }
            }
            screen.renderScreen();
        } else {
            Gdx.gl.glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }
}
