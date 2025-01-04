package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.shrubyway.game.adapters.MyInputAdapter;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.saver.SettingsSaver;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.screen.LoadingScreen;
import com.shrubyway.game.screen.Screen;
import com.shrubyway.game.sound.SoundSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ShrubyWay extends ApplicationAdapter {
    static public Screen screen;
    static public MyInputAdapter inputProcessor = new MyInputAdapter();

    static public GlobalAssetManager assetManager = new GlobalAssetManager();

    static public VideoPlayer videoPlayer = null;

    @Override public void resize(int width, int height) {
        if(width == 0 || height == 0) {
            if(screen instanceof Game) {
                ((Game) screen).pause();
            }
            return;
        }
        GlobalBatch.changeScale(width, height);
    }

    @Override public void create() {
        GlobalBatch.changeScale((int)GlobalBatch.screenWidth, (int)GlobalBatch.screenHeight);
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(videoPlayer == null) {
            videoPlayer = VideoPlayerCreator.createVideoPlayer();
            videoPlayer.setLooping(false);
            videoPlayer.setVolume(0);
            try {
            videoPlayer.play(Gdx.files.internal("SCENES/SHOP/TEST1/0.webm"));
            videoPlayer.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Texture texture = videoPlayer.getTexture();
        GlobalBatch.create();
        Gdx.graphics.setVSync(true);
        screen = new LoadingScreen();
        Gdx.input.setInputProcessor(inputProcessor);

        SettingsSaver soundSaver = new SettingsSaver();
        if(SettingsSaver.checkFile()) {
            String userHome = System.getenv("APPDATA");
            String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "SETTINGS.txt";
            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                soundSaver = (SettingsSaver) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            soundSaver.loadSettingsFiles();
        }
    }

    @Override public void render() {
        SoundSettings.update();
        AnimationGlobalTime.update();
            screen.updateScreen();
            GlobalBatch.begin();
            screen.renderScreen();
            GlobalBatch.end();
    }

    @Override public void dispose() {
        GlobalBatch.dispose();
        screen.dispose();
        inputProcessor.dispose();
        assetManager.dispose();
    }

    @Override public void pause() {
        screen.resumeScreen();
    }
    @Override public void resume() {
        screen.resumeScreen();
    }
}
