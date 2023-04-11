package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Background {
char Background_map[][] = new char[256][256];
private final int renderDistanceX = 13, renderDistanceY = 10;
    private final float soundDistanceX = 1200, soundDistanceY = 1200;
private int level;
final int TilesTypes = 4;

    Animation<TextureRegion> Tile[][];
    long TileSound[] = new long[TilesTypes];
    Sound StepSound[][] = new Sound[TilesTypes][2];
    int TileWithSound[] = {0};

    float NearestTile[] = new float[TilesTypes];
    Sound sound;


   private void animationsLoader() {
       Tile = new Animation[TilesTypes][2];
       for(int i = 0; i < TilesTypes; i++)
           for(int j = 0; j < 2; j++){
               String way = "TILES/" + i + "/" + j + "/";
               File folder = new File(way);
               File[] files = folder.listFiles();
               Arrays.sort(files);
               TextureRegion[] animation_frames = new TextureRegion[files.length];
               for(int q = 0; q < files.length; q++) {
                   animation_frames[q] = new TextureRegion(new Texture(way + files[q].getName()));
               }
               Tile[i][j] = new Animation<>(1/24f, animation_frames);
           }
   }
   private void TileMapLoader() {
           String fileName = "maps/" + level + "/basicMap.txt";
           try (Scanner scanner = new Scanner(new File(fileName))) {
               int j = 0;
               while (scanner.hasNextLine()) {
                   String temp = scanner.nextLine();
                   for(int q = 0; q < 256; q++) {
                       Background_map[j][q] = temp.charAt(q);
                   }
                   j++;
               }
           } catch (FileNotFoundException e){};
   }

   private void soundLoader() {
       for(int i = 0; i < TilesTypes; i++) {
           TileSound[i] = -1;
           StepSound[i][0] = Gdx.audio.newSound(Gdx.files.internal("sounds/STEPS/" + i + "_0.ogg"));
           StepSound[i][1] = Gdx.audio.newSound(Gdx.files.internal("sounds/STEPS/" + i + "_1.ogg"));
       }
       for(int to: TileWithSound) {
           sound = Gdx.audio.newSound(Gdx.files.internal("sounds/TILES/" + to + ".ogg"));
           TileSound[to] = sound.play();
           sound.setLooping(TileSound[to], true);
           sound.setVolume(TileSound[to], 0);
       }
   }

public Background(int Level) {
       level = Level;
       animationsLoader();
       TileMapLoader();
       soundLoader();
    }


    public void render(Batch batch, Vector2 playerPosition) {
          for(int i = 0; i < TilesTypes; i++) {
                  NearestTile[i] = 1;
          }
          int x = 0; x += playerPosition.x; x /= 150;
          int y = 0; y += playerPosition.y; y /= 150;
          for(int d = 0; d < 2; d++)
              for (int i = x - renderDistanceX; i < x + renderDistanceX; i++)
                  for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                      if (Math.abs(i + j) % 2 != d) continue;
                      int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
                      int tile = Background_map[i2][j2] - '0';
                      NearestTile[tile] = Math.min(NearestTile[tile],
                              Math.max(Math.abs(i * 150 + 75 - playerPosition.x) / soundDistanceX,
                                       Math.abs(j * 150 + 75 - playerPosition.y) / soundDistanceY));
                      TextureRegion tempTexture = Tile[tile][d].getKeyFrame(AnimationGlobalTime.x, true);
                      batch.draw(tempTexture, (i * 150) - 25,
                              (j * 150) - 25,
                              200,
                              200);
                  }
          for(int i: TileWithSound) {
              sound.setVolume(TileSound[i], Math.max(0, 2 * (1 - NearestTile[i])));
          }
    }
    public boolean checkLiquid(Vector2 playerPosition) {
        int xr = 0; xr += (playerPosition.x + 10) / 150;
        int xl = 0; xl += (playerPosition.x - 10) / 150;
        int yr = 0; yr += (playerPosition.y + 10) / 150;
        int yl = 0; yl += (playerPosition.y - 10) / 150;
        xr = (xr + 256) % 256; xl = (xl + 256) % 256;
        yr = (yr + 256) % 256; yl = (yl + 256) % 256;

        return (Background_map[xr][yr] == '0'
              && Background_map[xr][yl] == '0' &&
                Background_map[xl][yl] == '0'
                && Background_map[xl][yr] == '0');
    }

    public char checkTile(Vector2 position) {
        int x = 0; x += (position.x) / 150;
        int y = 0; y += (position.y) / 150;
        x = (x + 256) % 256;
        y = (y + 256) % 256;
        return Background_map[x][y];
    }

    void makeStep(Vector2 step, Vector2 playerPosition) {
        int x = 0; x += (step.x) / 150;
        int y = 0; y += (step.y) / 150;
        x = (x + 256) % 256; y = (y + 256) % 256;
           long temp = StepSound[Background_map[x][y] - '0'][(int)(Math.random() * 2)].play(1f);
           sound.setPitch(temp, 1 + (float)Math.random() * 0.2f - 0.1f);
           sound.setVolume(temp, 1 - Math.max(Math.abs(step.x - playerPosition.x) / soundDistanceX,
                   Math.abs(step.y - playerPosition.y) / soundDistanceY));

    }

}
