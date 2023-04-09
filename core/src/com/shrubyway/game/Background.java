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
char Background_map[][][] = new char[2][256][256];
List<VisibleObject> [][]chunks = new ArrayList[16][16];
char Decorations[][][] = new char[2][256][256];
private int renderDistanceX = 13, renderDistanceY = 10;

    Bush tmp;
    private String LoadAnimation(int i, int j) {
        String s = "TILES/";
        s += i + "/" + (j % 2);
        s += (j) % 2 + ".png";
        return s;
    }
    Animation<TextureRegion> Tile[][];

public List<Decoration> decorationsList = new ArrayList<Decoration>();

   public Background() {
       int TilesTypes = 4;
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

       decorationsList.add(new Bush());
       decorationsList.add(new Pine());
       decorationsList.add(new Rock());

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
    private TreeSet<Entity> temp_entitys = new TreeSet<Entity>();
    private TreeSet<VisibleObject> temp_effects = new TreeSet<VisibleObject>();
    private TreeSet<Decoration> temp_decorations = new TreeSet<Decoration>();

   public void updateDecorations(int level, Vector2 playerPosition, TreeSet<Decoration> DecorObj) {
       for (int i = x - renderDistanceX; i < x + renderDistanceX; i++) {
           for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
               int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
              // if(Decorations[level][i2][j2] == '0') continue;
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
            if(Math.abs(playerPosition.x - (to.position()).x) > renderDistanceX * 150 ||
            Math.abs(playerPosition.y - (to.position()).y) > renderDistanceY * 150) {
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
        //updateEntities(level, playerPosition, temp_decorations);

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
                      TextureRegion tempTexture = Tile[tile][d].getKeyFrame(AnimationGlobalTime.x, true);
                      batch.draw(tempTexture, (i * 150) - 25,
                              (j * 150) - 25,
                              200,
                              200);
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
