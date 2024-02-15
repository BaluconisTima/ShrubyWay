package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GlobalAssetManager {
    private AssetManager assetManager = new AssetManager();

    public void addAsset(String path, Class type) {
        assetManager.load(path, type);
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public boolean update() {
        return assetManager.update();
    }

    public void load(String path, Class type) {
        assetManager.load(path, type);
    }


    public <T> T get(String fileName, Class<T> type) {
        if(!assetManager.isLoaded(fileName)) {
            assetManager.load(fileName, type);
            assetManager.finishLoadingAsset(fileName);
        }
        if (type == Texture.class) {
            Texture texture = assetManager.get(fileName, Texture.class);
            Texture.TextureFilter minFilter = Texture.TextureFilter.Linear;
            Texture.TextureFilter magFilter = Texture.TextureFilter.Linear;
            texture.setFilter(minFilter, magFilter);
            texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
            return (T) texture;
        }

        return assetManager.get(fileName, type);
    }

    private void loadRec(FileHandle folder) {
        FileHandle[] files = folder.list();
        for (FileHandle file : files) {
            if (file.isDirectory()) {
                loadRec(file);
            } else {
                String extension = file.extension();
                if (extension.equals("png")) {
                    addAsset(file.path(), Texture.class);
                } else if (extension.equals("wav") || extension.equals("ogg")) {
                    addAsset(file.path(), com.badlogic.gdx.audio.Sound.class);
                } else if (extension.equals("mp3")) {
                    addAsset(file.path(), com.badlogic.gdx.audio.Music.class);
                }
            }
        }
    }
    private void loadFromJar() {
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
                    } else if (extension.equals("wav") || extension.equals("ogg")) {
                        addAsset(fileHandle.path(), com.badlogic.gdx.audio.Sound.class);
                    } else if (extension.equals("mp3")) {
                        addAsset(fileHandle.path(), com.badlogic.gdx.audio.Music.class);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        //loadFromJar();
        //ONLY FOR LOCAL TEST! DELETE THIS LINE IF YOU WANT TO BUILD JAR FILE!
       FileHandle assetsFolder = Gdx.files.local("");
       loadRec(assetsFolder);
    }

    public void dispose() {
        assetManager.dispose();
    }

}
