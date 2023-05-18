package com.shrubyway.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

public class GlobalAssetManager {
    private static AssetManager assetManager = new AssetManager();

    static public void addAsset(String path, Class type) {
        assetManager.load(path, type);
    }

    static public float getProgress() {
        return assetManager.getProgress();
    }

    static public boolean update() {
        return assetManager.update();
    }

    static public <T> T get(String fileName, Class<T> type) {
        return assetManager.get(fileName, type);
    }

    static private void loadRec(FileHandle folder) {
        FileHandle[] files = folder.list();

        for (FileHandle file : files) {
            if (file.isDirectory()) {
                loadRec(file);
            } else {
                String extension = file.extension();

                if (extension.equals("png")) {
                    addAsset(file.path(), com.badlogic.gdx.graphics.Texture.class);
                } else if (extension.equals("wav") || extension.equals("mp3") || extension.equals("ogg")) {
                    addAsset(file.path(), com.badlogic.gdx.audio.Sound.class);
                }
            }
        }
    }

    static public void loadAll() {
        FileHandle assetsFolder;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            assetsFolder = Gdx.files.local("");
        } else {
            assetsFolder = Gdx.files.internal("");
        }

        loadRec(assetsFolder);
    }
}
