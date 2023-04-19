package com.shrubyway.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.visibleobject.RenderingList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.decoration.Bush;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.Pine;
import com.shrubyway.game.visibleobject.decoration.Rock;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class Map {
    Background background;
    TreeSet<VisibleObject>[][] chunks = new TreeSet[16][16];
    long timeChecking = 0;
    boolean lastCheked[][] = new boolean[16][16];
    char decorations[][] = new char[256][256];
    public List<Decoration> decorationsList = new ArrayList<Decoration>();
    public List<VisibleObject> tempList;
    private final int renderDistanceX = 13, renderDistanceY = 10;

    private void decorationsLoad(int level) {

        String fileName = "maps/" + level + "/Decorations.txt";
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                for (int q = 0; q < 256; q++) {
                    decorations[j][q] = temp.charAt(q);
                }
                j++;
            }
        } catch (FileNotFoundException e) {
        }
        ;
    }


    public Map(int level) {
        tempList = new ArrayList<>();
        background = new Background(level);
        decorationsList.add(new Bush());
        decorationsList.add(new Pine());
        decorationsList.add(new Rock());
        decorationsLoad(level);

        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 16; j++) {
                chunks[i][j] = new TreeSet<>();
            }

        for (int i = 0; i < 256; i++)
            for (int j = 0; j < 256; j++) {
                if (decorations[i][j] != '0') {
                    int type = decorations[i][j] - '1';
                    Decoration temp = decorationsList.get(type);
                    decorationsList.set(type, temp.newTemp());
                    temp.change(i * 150, j * 150, i, j);
                    chunks[i / 16][j / 16].add(temp);
                }
            }
    }

    public void updateChunk(int i2, int j2, Vector2 playerPosition) {
        if(!lastCheked[i2/16][j2/16]) {
            lastCheked[i2/16][j2/16] = true;
            tempList.clear();
            for(VisibleObject check: chunks[i2 / 16][j2 /16]) {
                boolean fx = false, fy = false;
                if(check.position.x > playerPosition.x - 150 * renderDistanceX
                        && check.position.x < playerPosition.x + 150 * renderDistanceX) fx = true;
                else
                if(check.position.x - 38400 > playerPosition.x - 150 * renderDistanceX
                        && check.position.x - 38400 < playerPosition.x + 150 * renderDistanceX) {
                    check.position.x -= 38400;
                    fx = true;
                } else
                if(check.position.x + 38400 > playerPosition.x - 150 * renderDistanceX
                        && check.position.x + 38400 < playerPosition.x + 150 * renderDistanceX) {
                    check.position.x += 38400;
                    fx = true;
                }

                if(check.position.y > playerPosition.y - 150 * renderDistanceY
                        && check.position.y < playerPosition.y + 150 * renderDistanceY) fy = true;
                else
                if(check.position.y - 38400 > playerPosition.y - 150 * renderDistanceY
                        && check.position.y - 38400 < playerPosition.y + 150 * renderDistanceY) {
                    check.position.y -= 38400;
                    fy = true;
                } else
                if(check.position.y + 38400 > playerPosition.y - 150 * renderDistanceY
                        && check.position.y + 38400 < playerPosition.y + 150 * renderDistanceY) {
                    check.position.y += 38400;
                    fy = true;
                }

                if(fx && fy)
                    if(check.position.x > playerPosition.x - 150 * renderDistanceX
                            && check.position.x < playerPosition.x + 150 * renderDistanceX &&
                            check.position.y > playerPosition.y - 150 * renderDistanceY
                            && check.position.y < playerPosition.y + 150 * renderDistanceY) {
                        RenderingList.list.add(check);
                        tempList.add(check);
                    }
            }
            chunks[i2 / 16][j2 / 16].removeAll(tempList);
            tempList.clear();
        }
    }
    public void updateRenderingObjects(Vector2 playerPosition) {
        timeChecking++;
        int x , y;
        for(VisibleObject check: RenderingList.list) {
                if(Math.abs(check.position.x - playerPosition.x) > 150 * renderDistanceX ||
                   Math.abs(check.position.y - playerPosition.y) > 150 * renderDistanceY) {
                    x = (int)check.position.x; while(x < 0) x += 38400; while(x >= 38400) x -= 38400; x /= 150; x /= 16;
                    y = (int)check.position.y; while(y < 0) y += 38400; while(y >= 38400) y -= 38400; y /= 150; y /= 16;
                    chunks[x][y].add(check);
                    tempList.add(check);
                }
            }
        RenderingList.list.removeAll(tempList);
        tempList.clear();
        x = (int)playerPosition.x;
        y = (int)playerPosition.y;
        x /= 150; y /= 150;
        boolean fx = false, fy = false;

       for (int i = x - renderDistanceX; i < x + renderDistanceX; i++) {
            for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
                updateChunk(i2, j2, playerPosition);
            }
        }
        for (int i = x - renderDistanceX; i < x + renderDistanceX; i++) {
            for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
                lastCheked[i2/16][j2/16] = false;
            }
        }


    }
    public void addVisibleObject(VisibleObject visibleObject) {
        int x = (int)visibleObject.position.x; if(x < 0) x += 38400; if(x >= 38400) x -= 38400; x /= 150; x /= 16;
        int y = (int)visibleObject.position.y; if(y < 0) y += 38400; if(y >= 38400) y -= 38400; y /= 150; y /= 16;
        chunks[x][y].add(visibleObject);
    }

    public void render(SpriteBatch batch, Vector2 playerPosition) {
        background.render(batch, playerPosition);

    }
    public void makeStep(Vector2 step, Vector2 playerPosition) {
        background.makeStep(step, playerPosition);
    }

    public boolean checkLiquid(Vector2 position) {
        return background.checkLiquid(position);
    }

    public char checkTile(Vector2 position) {
        return background.checkTile(position);
    }

}