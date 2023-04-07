package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Background {
char Background_map[][][] = new char[2][256][256];
char Decorations[][][] = new char[2][256][256];

private int renderDistanceX = 10, renderDistanceY = 10;
    Bush tmp;
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

       for(int i = 1; i < 2; i++) {
           String fileName = "maps/" + i + "/basicMap.txt";
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

           fileName = "maps/" + i + "/Decorations.txt";
           try (Scanner scanner = new Scanner(new File(fileName))) {
               int j = 0;
               while (scanner.hasNextLine()) {
                   String temp = scanner.nextLine();
                   for(int q = 0; q < 256; q++) {
                       Decorations[i][j][q] = temp.charAt(q);
                   }
                   j++;
               }
           } catch (FileNotFoundException e){};
       }
    }

    public List<Decoration> updateDecorations(int level, Vector2 playerPosition, List<Decoration> decorations) {
        int x = 0; x += playerPosition.x;
        x /= 150;
        int y = 0; y += playerPosition.y;
        y /= 150;
        List<Decoration> temp_del = new ArrayList<Decoration>();
        for(Decoration to: decorations) {
            Decoration temp = to;
            if(Math.abs(playerPosition.x - (to.positionBottom()).x) > renderDistanceX &&
            Math.abs(playerPosition.y - (to.positionBottom()).y) > renderDistanceY) {
                Decorations[level][temp.Decoration_i][temp.Decoration_j] = temp.Decoration_type;
                temp_del.add(to);
            }
        }
        for(Decoration to: temp_del) {
            decorations.remove(to);
        }

        for (int i = x - renderDistanceX; i < x + renderDistanceX; i++)
            for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
                if(Decorations[level][i2][j2] == '1') {
                    Bush temp = new Bush(i * 150, j * 150, i2, j2, '1');
                    Decorations[level][i2][j2] = '0';
                    decorations.add(temp);
                }
            }

       return decorations;
    }

    public void addDecoration(int level, Vector2 position, char type) {
       int x = 0; x += position.x; int y = 0; y += position.y;
       if(x < 0) x += 38400;
       if(y < 0) y += 38400;
       if(x >= 38400) x -= 38400;
       if(y >= 38400) y -= 38400;
        x /= 150;
        y /= 150;
        Decorations[level][x][y] = type;
    }

    public void render(Batch batch, int level, Vector2 playerPosition) {

          int x = 0; x += playerPosition.x;
          x /= 150;
          int y = 0; y += playerPosition.y;
          y /= 150;
          for(int d = 0; d < 2; d++)
              for (int i = x - renderDistanceX; i < x + renderDistanceX; i++)
                  for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                      if (Math.abs(i + j) % 2 != d) continue;
                      int i2 = (i + 256) % 256, j2 = (j + 256) % 256;


                      int tile = Background_map[level][i2][j2] - '0';
                      batch.draw(Tile[tile][d],
                            (i * 150) - 25,
                            (j * 150) - 25,
                            Tile[level][d].getWidth(),
                            Tile[level][d].getHeight());
                  }
    }
    public boolean checkLiquid(int level, Vector2 playerPosition) {
        int xr = 0; xr += (playerPosition.x + 10) / 150;
        int xl = 0; xl += (playerPosition.x - 10) / 150;
        int yr = 0; yr += (playerPosition.y + 10) / 150;
        int yl = 0; yl += (playerPosition.y - 10) / 150;
        return (Background_map[level][(xr + 256) % 256][(yr + 256) % 256] == '0'
              && Background_map[level][(xl + 256) % 256][(yr + 256) % 256] == '0' &&
                Background_map[level][(xr + 256) % 256][(yl + 256) % 256] == '0'
                && Background_map[level][(xl + 256) % 256][(yl + 256) % 256] == '0');
    }

}
