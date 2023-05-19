package com.shrubyway.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    static private void loadFromJar() {
        JarFile jarFile;
        try {
            String jarPath = GlobalAssetManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            jarPath = java.net.URLDecoder.decode(jarPath, "UTF-8");
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            return;
        }

        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryPath = entry.getName();
            if (!entry.isDirectory()) {
                FileHandle fileHandle = Gdx.files.internal(entryPath);
                String extension = fileHandle.extension();
                if (extension.equals("png")) {
                    addAsset(fileHandle.path(), com.badlogic.gdx.graphics.Texture.class);
                } else if (extension.equals("wav") || extension.equals("mp3") || extension.equals("ogg")) {
                    addAsset(fileHandle.path(), com.badlogic.gdx.audio.Sound.class);
                }
            }
        }

        try {
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void loadAll() {
            FileHandle assetsFolder = Gdx.files.local("");
            loadFromJar();
            loadRec(assetsFolder);
    }
}
