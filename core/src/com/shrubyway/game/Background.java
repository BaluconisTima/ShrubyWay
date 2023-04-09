package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
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
private int level;
final int TilesTypes = 4;


    private String LoadAnimation(int i, int j) {
        String s = "TILES/";
        s += i + "/" + (j % 2);
        s += (j) % 2 + ".png";
        return s;
    }
    Animation<TextureRegion> Tile[][];



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
               Tile[i][j] = new Animation<TextureRegion>(1/24f, animation_frames);
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

public Background(int Level) {
       level = Level;
       animationsLoader();
       TileMapLoader();
    }


    public void render(Batch batch, Vector2 playerPosition) {
          int x = 0; x += playerPosition.x; x /= 150;
          int y = 0; y += playerPosition.y; y /= 150;
          for(int d = 0; d < 2; d++)
              for (int i = x - renderDistanceX; i < x + renderDistanceX; i++)
                  for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                      if (Math.abs(i + j) % 2 != d) continue;
                      int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
                      int tile = Background_map[i2][j2] - '0';
                      TextureRegion tempTexture = Tile[tile][d].getKeyFrame(AnimationGlobalTime.x, true);
                      batch.draw(tempTexture, (i * 150) - 25,
                              (j * 150) - 25,
                              200,
                              200);
                  }
    }
    public boolean checkLiquid(Vector2 playerPosition) {
        int xr = 0; xr += (playerPosition.x + 10) / 150;
        int xl = 0; xl += (playerPosition.x - 10) / 150;
        int yr = 0; yr += (playerPosition.y + 10) / 150;
        int yl = 0; yl += (playerPosition.y - 10) / 150;
        return (Background_map[(xr + 256) % 256][(yr + 256) % 256] == '0'
              && Background_map[(xl + 256) % 256][(yr + 256) % 256] == '0' &&
                Background_map[(xr + 256) % 256][(yl + 256) % 256] == '0'
                && Background_map[(xl + 256) % 256][(yl + 256) % 256] == '0');
    }

}
