package com.shrubyway.game.layout;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.myinterface.Slider;
import com.shrubyway.game.saver.GameSaver;
import com.shrubyway.game.saver.SettingsSaver;
import com.shrubyway.game.sound.SoundSettings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SettingsLayout extends Layout {
    static private Texture settingsText;
    static private Button doneButton;

    Slider soundSlider, musicSlider;

    public SettingsLayout() {
        super();
        soundSlider = new Slider(GlobalBatch.centerX(), GlobalBatch.centerY() + 85, SoundSettings.soundVolume, "Sound Volume");
        musicSlider = new Slider(GlobalBatch.centerX(), GlobalBatch.centerY() - 165, SoundSettings.musicVolume, "Music Volume");

        if (settingsText == null) settingsText = ShrubyWay.assetManager.get("interface/settings_text.png", Texture.class);
        if (doneButton == null)
            doneButton = new Button(ShrubyWay.assetManager.get("interface/done.png", Texture.class),
                    ShrubyWay.assetManager.get("interface/done2.png", Texture.class),
                    GlobalBatch.centerX() - ShrubyWay.assetManager.get("interface/done.png", Texture.class).getWidth()/2,
                    GlobalBatch.centerY() - 390);
    }

    private void done() {
        String filePath = GameSaver.saveLocation() + "SETTINGS.txt";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            SettingsSaver soundSaver = new SettingsSaver();
            soundSaver.saveSettingsFiles();
            objectOutputStream.writeObject(soundSaver);
        } catch (IOException e) {
            e.printStackTrace();
        }

        close();
    }

    float localScaleX = -1, localScaleY = -1;

    @Override public void update(Vector2 mousePos) {
        if(localScaleX != GlobalBatch.scaleX || localScaleY != GlobalBatch.scaleY) {
            soundSlider.set(GlobalBatch.centerX(), GlobalBatch.centerY() + 85);
            musicSlider.set(GlobalBatch.centerX(), GlobalBatch.centerY() - 165);
            doneButton.set(GlobalBatch.centerX() - ShrubyWay.assetManager.get("interface/done.png", Texture.class).getWidth()/2,
                    GlobalBatch.centerY() - 390);
            localScaleX = GlobalBatch.scaleX;
            localScaleY = GlobalBatch.scaleY;
        }
        super.update(mousePos);
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if (doneButton.rectangle.checkPoint(mousePos)) done();
        }
        if(ShrubyWay.inputProcessor.isMouseLeftPressed()) {
            soundSlider.click(mousePos.x, mousePos.y);
            musicSlider.click(mousePos.x, mousePos.y);
            SoundSettings.musicVolume = musicSlider.getValue();
            SoundSettings.soundVolume = soundSlider.getValue();
        }
    }

    @Override public void render(Vector2 mousePos) {
        super.render(mousePos);
        GlobalBatch.render(settingsText, GlobalBatch.centerX() - settingsText.getWidth()/2,
                GlobalBatch.centerY() - settingsText.getHeight()/2 + 350);
        soundSlider.render();
        musicSlider.render();

        if(doneButton.rectangle.checkPoint(mousePos)) doneButton.renderSellected();
        else doneButton.render();
    }

}
