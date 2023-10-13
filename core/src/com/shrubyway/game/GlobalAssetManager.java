package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.net.URLDecoder;
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
        if(!assetManager.isLoaded(fileName)) {
            assetManager.load(fileName, type);
            assetManager.finishLoadingAsset(fileName);
        }
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
    private static void loadFromJar() {
        String jarPath = ShrubyWay.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try (JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryPath = entry.getName();
               // System.out.println(entryPath);
                if (!entry.isDirectory()) {
                    FileHandle fileHandle = Gdx.files.internal(entryPath);
                    String extension = fileHandle.extension();
                    if (extension.equals("png")) {
                        addAsset(fileHandle.path(), com.badlogic.gdx.graphics.Texture.class);
                    } else if (extension.equals("wav") || extension.equals("ogg") || extension.equals("mp3")) {
                        addAsset(fileHandle.path(), com.badlogic.gdx.audio.Sound.class);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void loadAll() {
       // loadFromJar();
        //ONLY FOR LOCAL TEST! DELETE THIS LINE IF YOU WANT TO BUILD JAR FILE!
       FileHandle assetsFolder = Gdx.files.local("");
       loadRec(assetsFolder);
    }

}
