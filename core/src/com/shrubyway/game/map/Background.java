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
    static final int TILENUMBER = 256;
    char backgroundMap[][] = new char[TILENUMBER][TILENUMBER];
private final int renderDistanceX = 13, renderDistanceY = 10;
    private final float soundDistanceX = 1200, soundDistanceY = 1200;
private int level;
 static final int TILETYPES = 4;
 static final int TYLESIZE = 150;

    Animation<TextureRegion> tile[][];
    long tileSound[] = new long[TILETYPES];
    Sound stepSound[][] = new Sound[TILETYPES][2];
    int tileWithSound[] = {0};

    float nearestTile[] = new float[TILETYPES];
    Sound sound;
    static SoundSettings soundSettings;


   private void animationsLoader() {
       tile = new Animation[TILETYPES][2];
       for(int i = 0; i < TILETYPES; i++)
           for(int j = 0; j < 2; j++){
               String way = "TILES/" + i + "/" + j + "/";
               File folder = new File(way);
               File[] files = folder.listFiles();
               Arrays.sort(files);
               TextureRegion[] animationFrames = new TextureRegion[files.length];
               for(int q = 0; q < files.length; q++) {
                   animationFrames[q] = new TextureRegion(new Texture(way + files[q].getName()));
               }
               tile[i][j] = new Animation<>(1/24f, animationFrames);
           }
   }
   private void tileMapLoader() {
           String fileName = "maps/" + level + "/basicMap.txt";
           try (Scanner scanner = new Scanner(new File(fileName))) {
               int j = 0;
               while (scanner.hasNextLine()) {
                   String temp = scanner.nextLine();
                   for(int q = 0; q < TILENUMBER; q++) {
                       backgroundMap[j][q] = temp.charAt(q);
                   }
                   j++;
               }
           } catch (FileNotFoundException e){};
   }

   private void soundLoader() {
       for(int i = 0; i < TILETYPES; i++) {
           tileSound[i] = -1;
           stepSound[i][0] = Gdx.audio.newSound(Gdx.files.internal("sounds/STEPS/" + i + "_0.ogg"));
           stepSound[i][1] = Gdx.audio.newSound(Gdx.files.internal("sounds/STEPS/" + i + "_1.ogg"));
       }
       for(int to: tileWithSound) {
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
          for(int i = 0; i < TILETYPES; i++) {
                  nearestTile[i] = 1;
          }
          int x = 0; x += playerPosition.x; x /= TYLESIZE;
          int y = 0; y += playerPosition.y; y /= TYLESIZE;
          for(int d = 0; d < 2; d++)
              for (int i = x - renderDistanceX; i < x + renderDistanceX; i++)
                  for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                      if (Math.abs(i + j) % 2 != d) continue;
                      int i2 = (i + TILENUMBER) % TILENUMBER, j2 = (j + TILENUMBER) % TILENUMBER;
                      int tile = backgroundMap[i2][j2] - '0';
                      nearestTile[tile] = Math.min(nearestTile[tile],
                              Math.max(Math.abs(i * TYLESIZE + TYLESIZE / 2 - playerPosition.x) / soundDistanceX,
                                       Math.abs(j * TYLESIZE + TYLESIZE / 2 - playerPosition.y) / soundDistanceY));
                      TextureRegion tempTexture = this.tile[tile][d].getKeyFrame(AnimationGlobalTime.x, true);
                      batch.draw(tempTexture, (i * TYLESIZE) - 25,
                              (j * TYLESIZE) - 25,
                              200,
                              200);
                  }
          for(int i: tileWithSound) {
              sound.setVolume(tileSound[i], Math.max(0,  0.7f * (1 - nearestTile[i])* soundSettings.soundVolume));
          }
    }
    public boolean checkLiquid(Vector2 playerPosition) {
        int xr = 0; xr += (playerPosition.x + 10) / TYLESIZE;
        int xl = 0; xl += (playerPosition.x - 10) / TYLESIZE;
        int yr = 0; yr += (playerPosition.y + 10) / TYLESIZE;
        int yl = 0; yl += (playerPosition.y - 10) / TYLESIZE;
        xr = (xr + TILENUMBER) % TILENUMBER; xl = (xl + TILENUMBER) % TILENUMBER;
        yr = (yr + TILENUMBER) % TILENUMBER; yl = (yl + TILENUMBER) % TILENUMBER;

        return (backgroundMap[xr][yr] == '0'
              && backgroundMap[xr][yl] == '0' &&
                backgroundMap[xl][yl] == '0'
                && backgroundMap[xl][yr] == '0');
    }

    public char checkTile(Vector2 position) {
        int x = 0; x += (position.x) / TYLESIZE;
        int y = 0; y += (position.y) / TYLESIZE;
        x = (x + TILENUMBER) % TILENUMBER;
        y = (y + TILENUMBER) % TILENUMBER;
        return backgroundMap[x][y];
    }

    void makeStep(Vector2 step, Vector2 playerPosition) {
        int x = 0; x += (step.x) / TYLESIZE;
        int y = 0; y += (step.y) / TYLESIZE;
        x = (x + TILENUMBER) % TILENUMBER; y = (y + TILENUMBER) % TILENUMBER;

           long temp = stepSound[backgroundMap[x][y] - '0'][(int)(Math.random() * 1.999)].play(1f * soundSettings.soundVolume);
           sound.setPitch(temp, 1 + (float)Math.random() * 0.2f - 0.1f);
           sound.setVolume(temp, 1 - Math.max(Math.abs(step.x - playerPosition.x) / soundDistanceX,
                   Math.abs(step.y - playerPosition.y) / soundDistanceY));

    }

}
