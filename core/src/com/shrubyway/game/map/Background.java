package com.shrubyway.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.sound.SoundSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Background {

    char backgroundMap[][] = new char[MapSettings.TILENUMBER][MapSettings.TILENUMBER];
    private int level;


    Animation<TextureRegion> tile[][];
    long tileSound[] = new long[MapSettings.TILETYPES];
    Sound stepSound[][] = new Sound[MapSettings.TILETYPES][2];
    int tileWithSound[] = {0};

    float nearestTile[] = new float[MapSettings.TILETYPES];
    Sound sound;
    static SoundSettings soundSettings;


    private void animationsLoader() {
        tile = new Animation[MapSettings.TILETYPES][2];
        for (int i = 0; i < MapSettings.TILETYPES; i++)
            for (int j = 0; j < 2; j++) {
                String way = "TILES/" + i + "/" + j + "/";
                File folder = new File(way);
                File[] files = folder.listFiles();
                Arrays.sort(files);
                TextureRegion[] animationFrames = new TextureRegion[files.length];
                for (int q = 0; q < files.length; q++) {
                    animationFrames[q] = new TextureRegion(new Texture(way + files[q].getName()));
                }
                tile[i][j] = new Animation<>(1 / 24f, animationFrames);
            }
    }

    private void tileMapLoader() {
        String fileName = "maps/" + level + "/basicMap.txt";
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                for (int q = 0; q < MapSettings.TILENUMBER; q++) {
                    backgroundMap[j][q] = temp.charAt(q);
                }
                j++;
            }
        } catch (FileNotFoundException e) {
        }
        ;
    }

    private void soundLoader() {
        for (int i = 0; i < MapSettings.TILETYPES; i++) {
            tileSound[i] = -1;
            stepSound[i][0] = Gdx.audio.newSound(Gdx.files.internal("sounds/STEPS/" + i + "_0.ogg"));
            stepSound[i][1] = Gdx.audio.newSound(Gdx.files.internal("sounds/STEPS/" + i + "_1.ogg"));
        }
        for (int to : tileWithSound) {
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/TILES/" + to + ".ogg"));
            tileSound[to] = sound.play();
            sound.setLooping(tileSound[to], true);
            sound.setVolume(tileSound[to], 0);
        }
    }

    public Background(int levelNew) {
        level = levelNew;
        animationsLoader();
        tileMapLoader();
        soundLoader();
    }


    public void render(Batch batch, Vector2 playerPosition) {
        for (int i = 0; i < MapSettings.TILETYPES; i++) {
            nearestTile[i] = 1;
        }
        int x = 0;
        x += playerPosition.x;
        x /= MapSettings.TYLESIZE;
        int y = 0;
        y += playerPosition.y;
        y /= MapSettings.TYLESIZE;
        for (int d = 0; d < 2; d++)
            for (int i = x - MapSettings.visibleDistanceX; i < x + MapSettings.visibleDistanceX; i++)
                for (int j = y - MapSettings.visibleDistanceY; j < y + MapSettings.visibleDistanceY; j++) {
                    if (Math.abs(i + j) % 2 != d) continue;
                    int i2 = (i + MapSettings.TILENUMBER) % MapSettings.TILENUMBER,
                            j2 = (j + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
                    int tile = backgroundMap[i2][j2] - '0';
                    tempDistance.set(i * MapSettings.TYLESIZE
                            + MapSettings.TYLESIZE / 2,
                            j * MapSettings.TYLESIZE
                                    + MapSettings.TYLESIZE / 2);
                    tempDistance.sub(playerPosition);
                    float temp1 = tempDistance.len(); temp1 = temp1 / MapSettings.soundDistance;
                    temp1 = Math.min(temp1, 1);
                    nearestTile[tile] = Math.min(nearestTile[tile], temp1);

                    TextureRegion tempTexture = this.tile[tile][d].getKeyFrame(AnimationGlobalTime.x, true);
                    batch.draw(tempTexture, (i * MapSettings.TYLESIZE) - 25,
                            (j * MapSettings.TYLESIZE) - 25,
                            200,
                            200);
                }
        for (int i : tileWithSound) {
            sound.setVolume(tileSound[i], Math.max(0, 0.7f * (1 - nearestTile[i]) * soundSettings.soundVolume));
        }
    }

    public boolean checkLiquid(Vector2 playerPosition) {
        int xr = 0;
        xr += (playerPosition.x + 10) / MapSettings.TYLESIZE;
        int xl = 0;
        xl += (playerPosition.x - 10) / MapSettings.TYLESIZE;
        int yr = 0;
        yr += (playerPosition.y + 10) / MapSettings.TYLESIZE;
        int yl = 0;
        yl += (playerPosition.y - 10) / MapSettings.TYLESIZE;
        xr = (xr + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
        xl = (xl + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
        yr = (yr + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
        yl = (yl + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;

        return (backgroundMap[xr][yr] == '0'
                && backgroundMap[xr][yl] == '0' &&
                backgroundMap[xl][yl] == '0'
                && backgroundMap[xl][yr] == '0');
    }

    public char checkTile(Vector2 position) {
        int x = 0;
        x += (position.x) / MapSettings.TYLESIZE;
        int y = 0;
        y += (position.y) / MapSettings.TYLESIZE;
        x = (x + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
        y = (y + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
        return backgroundMap[x][y];
    }
   Vector2 tempDistance = new Vector2(0,0);
    void makeStep(Vector2 step, Vector2 playerPosition) {
        int x = 0;
        x += (step.x) / MapSettings.TYLESIZE;
        int y = 0;
        y += (step.y) / MapSettings.TYLESIZE;
        x = (x + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;
        y = (y + MapSettings.TILENUMBER) % MapSettings.TILENUMBER;

        long temp = stepSound[backgroundMap[x][y] - '0']
                [(int) (Math.random() * 1.999)].play();

        sound.setPitch(temp, 1 + (float) Math.random() * 0.2f - 0.1f);
        tempDistance.set(step.x, step.y);
        tempDistance.sub(playerPosition);
        float temp1 = tempDistance.len(); temp1 = temp1 / MapSettings.soundDistance;
        temp1 = Math.min(temp1, 1);
        sound.setVolume(temp,  (1 - temp1) * soundSettings.soundVolume);

    }

}
