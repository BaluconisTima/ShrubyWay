package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.shrubyway.game.adapters.MyInputAdapter;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.screen.*;
import com.shrubyway.game.sound.SoundSettings;

public class ShrubyWay extends ApplicationAdapter {
    static Screen screen;
    static public MyInputAdapter inputProcessor = new MyInputAdapter();

    @Override public void resize(int width, int height) {
        GlobalBatch.changeScale(width, height);
    }
    @Override public void create() {
        GlobalBatch.changeScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GlobalBatch.create();
        Gdx.graphics.setVSync(true);
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
        screen = new Menu();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override public void render() {
        SoundSettings.update();
        AnimationGlobalTime.update();
            screen.updateScreen();
            if(screen instanceof Menu men && men.goToGame) {
                Menu.goToGame = false;
                screen = new Game();
                render();
            } else if(screen instanceof Game game) {
                if(game.gameOver) {
                    game.gameOver = false;
                    screen = new GameOver();
                    render();
                } else if(game.menu) {
                    game.menu = false;
                    screen = new Menu();
                    render();
                }
            } else if(screen instanceof GameOver gameover) {
                   if(gameover.tryingAgain) {
                       gameover.tryingAgain = false;
                       screen = new Game();
                       render();
                   } else if(gameover.exit) {
                       gameover.exit = false;
                       screen = new Menu();
                       render();
                   }
            }
            GlobalBatch.begin();
            screen.renderScreen();
            GlobalBatch.end();
    }
}
