package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Background {
char Background_map[][][] = new char[2][256][256];
Texture Tile[][] = new Texture[4][2];

   public Background() {

       for(int i = 0; i < 4; i++)
           for(int j = 0; j < 2; j++){
              String s = "TILES/";
              switch (i) {
                 case 0:
                    s += "WATER";
                    break;
                 case 1:
                    s += "GRASS";
                    break;
                 case 2:
                    s += "SAND";
                    break;
                  case 3:
                    s += "DIRT";
                    break;
              }
              s += (j) % 2 + ".png";
              Tile[i][j] = new Texture(Gdx.files.internal(s));
       }

       for(int i = 0; i < 2; i++) {
           String fileName = "map" + i + ".txt";
           try (Scanner scanner = new Scanner(new File(fileName))) {
               int j = 0;
               while (scanner.hasNextLine()) {
                   String temp = scanner.nextLine();
                   for(int q = 0; q < 256; q++) {
                       Background_map[i][j][q] = temp.charAt(q);
                   }
                   j++;
               }
           } catch (FileNotFoundException e){};
       }
    }
    public void render(Batch batch, int level, Vector2 playerPosition) {

          int x = 0; x += playerPosition.x; x /= 150;
          int y = 0; y += playerPosition.y; y /= 150;
          for(int d = 0; d < 2; d++)
              for (int i = x - 9; i < x + 10; i++)
                  for (int j = y - 5; j < y + 6; j++) {
                      if (Math.abs(i + j) % 2 != d) continue;
                      int i2 = (i + 256) % 256, j2 = (j + 256) % 256;

                      int tile = Background_map[level][i2][j2] - '0';
                      batch.draw(Tile[tile][d],
                            (i * 150)- 25,
                            (j * 150) - 175,
                            Tile[level][d].getWidth(),
                            Tile[level][d].getHeight());

                  }
    }
    public boolean checkLiquid(int level, Vector2 playerPosition) {
        int x = 0; x += playerPosition.x / 150;
        int y = 0; y += playerPosition.y / 150;
        return (Background_map[level][(x + 256) % 256][(y + 256) % 256] - '0' == 0);
    }
}
