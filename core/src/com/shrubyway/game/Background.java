package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Background {
char Background_map[][][] = new char[2][256][256];
char Decorations[][][] = new char[2][256][256];
private int renderDistanceX = 10, renderDistanceY = 10;

    Bush tmp;
    private String TileInterpretator(int i, int j) {
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
        return s;
    }
Texture Tile[][] = new Texture[4][2];

public List<Decoration> decorationsList = new ArrayList<Decoration>();

   public Background() {

       for(int i = 0; i < 4; i++)
           for(int j = 0; j < 2; j++){
              Tile[i][j] = new Texture(Gdx.files.internal(TileInterpretator(i, j)));
       }
       decorationsList.add(new Bush());
       decorationsList.add(new Pine());

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

    int x = 0, y = 0;


    private TreeSet<VisibleObject> temp_del = new TreeSet<VisibleObject>();
    private TreeSet<VisibleObject> temp_entitys = new TreeSet<VisibleObject>();
    private TreeSet<VisibleObject> temp_effects = new TreeSet<VisibleObject>();
    private TreeSet<Decoration> temp_decorations = new TreeSet<Decoration>();

   public void updateDecorations(int level, Vector2 playerPosition, TreeSet<Decoration> DecorObj) {
       for (int i = x - renderDistanceX; i < x + renderDistanceX; i++) {
           for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
               int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
               if(Decorations[level][i2][j2] == '0') continue;

                   Decoration temp = decorationsList.get(Decorations[level][i2][j2] - '1');
                   temp.change(i * 150, j * 150, i2, j2);
                   if(!DecorObj.contains(temp)) {
                       DecorObj.add(temp);
                       decorationsList.set(Decorations[level][i2][j2] - '1', temp.newTemp());
                   }

           }
       }
   }
    public void update(int level, Vector2 playerPosition, TreeSet<VisibleObject> VisObj) {

        x = 0; x += playerPosition.x; x /= 150;
        y = 0; y += playerPosition.y; y /= 150;
        temp_del.clear();
        temp_entitys.clear();
        temp_effects.clear();
        temp_decorations.clear();
        for(VisibleObject to: VisObj) {
            if(Math.abs(playerPosition.x - (to.positionBottom()).x)/150 > renderDistanceX ||
            Math.abs(playerPosition.y - (to.positionBottom()).y)/150 > renderDistanceY) {
                temp_del.add(to);
            }
        }
        for(VisibleObject to: temp_del) {
            VisObj.remove(to);
        }


        for(VisibleObject to: VisObj) {
           /* if(to instanceof Entity) {
                temp_entitys.add(to);
            }
            if(to instanceof Effect) {
                temp_effects.add(to);
            } */
            if(to instanceof Decoration) {
                temp_decorations.add((Decoration)to);
            }
        }

        updateDecorations(level, playerPosition, temp_decorations);

        for(Decoration to: temp_decorations) {
            VisObj.add(to);
        }

    }


    public void addDecoration(int level, Vector2 position, char type) {
        x = 0; y = 0;x += position.x; y += position.y;
        if(x < 0) x += 38400;
        if(y < 0) y += 38400;
        if(x >= 38400) x -= 38400;
        if(y >= 38400) y -= 38400;
        x /= 150;
        y /= 150;
        Decorations[level][x][y] = type;
    }

    public void render(Batch batch, int level, Vector2 playerPosition) {
          int x = 0; x += playerPosition.x; x /= 150;
          int y = 0; y += playerPosition.y; y /= 150;

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
