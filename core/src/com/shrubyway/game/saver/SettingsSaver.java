package com.shrubyway.game.saver;

import com.shrubyway.game.sound.SoundSettings;

import java.io.File;
import java.io.Serializable;

public class SettingsSaver implements Serializable {

    float soundVolume = 1, musicVolume = 1;

     public void saveSettingsFiles() {
       soundVolume = SoundSettings.soundVolume;
       musicVolume = SoundSettings.musicVolume;
    }

     public void loadSettingsFiles() {
        SoundSettings.soundVolume = soundVolume;
        SoundSettings.musicVolume = musicVolume;
    }

    static public boolean checkFile() {
        String userHome = System.getenv("APPDATA");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "SETTINGS.txt";
        File file = new File(filePath);
        return file.exists();
    }


}
