package com.shrubyway.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
    long lastCheked[][] = new long[16][16];
    char Decorations[][] = new char[256][256];
    public List<Decoration> decorationsList = new ArrayList<Decoration>();
    public List<VisibleObject> tempList = new ArrayList<VisibleObject>();
    private final int renderDistanceX = 13, renderDistanceY = 10;

    private void DecorationsLoad(int level) {
        String fileName = "maps/" + level + "/Decorations.txt";
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int j = 0;
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                for (int q = 0; q < 256; q++) {
                    Decorations[j][q] = temp.charAt(q);
                }
                j++;
            }
        } catch (FileNotFoundException e) {
        }
        ;
    }


    public Map(int level) {
        background = new Background(level);
        decorationsList.add(new Bush());
        decorationsList.add(new Pine());
        decorationsList.add(new Rock());
        DecorationsLoad(level);

        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 16; j++) {
                chunks[i][j] = new TreeSet<VisibleObject>();
            }

        for (int i = 0; i < 256; i++)
            for (int j = 0; j < 256; j++) {
                if (Decorations[i][j] != '0') {
                    int type = Decorations[i][j] - '1';
                    Decoration temp = decorationsList.get(type);
                    decorationsList.set(type, temp.newTemp());
                    temp.change(i * 150, j * 150, i, j);
                    chunks[i / 16][j / 16].add(temp);
                }
            }
    }
    public void ChangeDecoration(Vector2 position, char type) {
     /*   int x = 0, y = 0;
        x += position.x;
        y += position.y;
        if (x < 0) x += 38400;
        if (y < 0) y += 38400;
        if (x >= 38400) x -= 38400;
        if (y >= 38400) y -= 38400;
        x /= 150;
        y /= 150;
        if(Decorations[x][y] - '0' != 0) {
            Decoration temp = decorationsList.get(Decorations[x][y] - '1').newTemp();
            temp.change(x * 150, y * 150, x, y);
            chunks[x / 16][y / 16].remove(temp);
        }
        Decorations[x][y] = type;
        if(type != '0') {
            Decoration temp = decorationsList.get(Decorations[x][y] - '1').newTemp();
            temp.change(x * 150, y * 150, x, y);
            chunks[x / 16][y / 16].add(decorationsList.get(type - '1'));
        } */
    }
    public void UpdateRenderingObjects(Vector2 playerPosition, TreeSet<VisibleObject> renderingList) {
        timeChecking++;
        int x = 0, y = 0;
        tempList.clear();
        for(VisibleObject check: renderingList) {
                if(Math.abs(check.position.x - playerPosition.x) > 150 * renderDistanceX ||
                   Math.abs(check.position.y - playerPosition.y) > 150 * renderDistanceY) {
                    x = (int)check.position.x; if(x < 0) x += 38400; if(x >= 38400) x -= 38400; x /= 150; x /= 16;
                    y = (int)check.position.y; if(y < 0) y += 38400; if(y >= 38400) y -= 38400; y /= 150; y /= 16;
                    chunks[x][y].add(check);
                    tempList.add(check);
                }
            }
        renderingList.removeAll(tempList);
        x = (int)playerPosition.x;
        y = (int)playerPosition.y;
        x /= 150; y /= 150;
        boolean fx = false, fy = false;

       for (int i = x - renderDistanceX; i < x + renderDistanceX; i++) {
            for (int j = y - renderDistanceY; j < y + renderDistanceY; j++) {
                int i2 = (i + 256) % 256, j2 = (j + 256) % 256;
              if(lastCheked[i2/16][j2/16] != timeChecking) {
                     lastCheked[i2/16][j2/16] = timeChecking;
                     tempList.clear();
                    for(VisibleObject check: chunks[i2 / 16][j2 /16]) {
                        fx = false; fy = false;
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
                            renderingList.add(check);
                            tempList.add(check);
                        }
                    }
                    chunks[i2 / 16][j2 / 16].removeAll(tempList);
                }
            }
        }

    }
    public void addEntity(Entity entity) {
        int x = (int)entity.position.x; if(x < 0) x += 38400; if(x >= 38400) x -= 38400; x /= 150; x /= 16;
        int y = (int)entity.position.y; if(y < 0) y += 38400; if(y >= 38400) y -= 38400; y /= 150; y /= 16;
        chunks[x][y].add(entity);
    }

    public void render(SpriteBatch batch, Vector2 playerPosition) {
        background.render(batch, playerPosition);

    }
    void makeStep(Vector2 step, Vector2 playerPosition) {
        background.makeStep(step, playerPosition);
    }

    public boolean checkLiquid(Vector2 position) {
        return background.checkLiquid(position);
    }


}