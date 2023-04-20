package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.visibleobject.RenderingList;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class Inventory {
    static Texture base = new Texture("interface/mainInv.png"),
            full = new Texture("interface/fullInv.png"),
            select = new Texture("interface/selectInv.png");
    static Boolean opened = false;
    static public BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/font2.fnt"));


    static Sound click = Gdx.audio.newSound(Gdx.files.internal("sounds/Effects/Click.ogg"));
    static Rectangle buttons[][] = new Rectangle[5][9];
    static Item items[][] = new Item[5][9];
    static Integer numberOfItem[][] = new Integer[5][9];

    static Integer selected = 0;


    static Item itemInHand = null;
    static Integer numberOfItemInHand = 0;
    static {
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(0.5f);

        for(int i = 0; i < 9; i++) {
            buttons[0][i] = new Rectangle(27 + 79.1f * i,1080 - 70 - 28,70,70);
        }
        for(int j = 1; j < 5; j++) {
            for(int i = 0; i < 9; i++) {
                buttons[j][i] = new Rectangle(27 + 79.1f * i,1080 - 70 - 55 - 79.1f * j,70,70);
            }
        }


        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 9; j++) {
                items[i][j] = null;
                numberOfItem[i][j] = 0;
            }
    }


    static public void render(Batch batch, Vector2 mousePosition) {
       batch.draw(base,0,0);
       if(opened) batch.draw(full,0,0);
        for(int i = 0; i < 9; i++) {
            buttons[0][i].render(batch);
            if(items[0][i] != null) {
                batch.draw(ItemManager.itemTexture[items[0][i].id],
                        27 + 79.1f * i, 1080 - 70 - 28, 70, 70);
                if(numberOfItem[0][i] > 1)
                    font.draw(batch, numberOfItem[0][i].toString(),
                        27 + 79.1f * i + 50 - (numberOfItem[0][i].toString().length()-1) * 17,
                        1080 - 70);
            }
        }
        if(opened)
            for (int i = 1; i < 5; i++)
                for (int j = 0; j < 9; j++) {
                    buttons[i][j].render(batch);
                    if (items[i][j] != null) {
                        batch.draw(ItemManager.itemTexture[items[i][j].id],
                                27 + 79.1f * j,
                                1080 - 70 - 55 - 79.1f * i, 70, 70);
                        if (numberOfItem[i][j] > 1)
                            font.draw(batch, numberOfItem[i][j].toString(),
                                    27 + 79.1f * j + 50 - (numberOfItem[i][j].toString().length() - 1) * 17,
                                    1080 - 70 - 28
                                            - 79.1f * i);
                    }
                }

        batch.draw(select, 12 + 79.1f * selected, 1080 - 111.55f);
        if(opened) {
            if(itemInHand != null) {
                batch.draw(ItemManager.itemTexture[itemInHand.id],
                        mousePosition.x - 35, mousePosition.y - 35, 70, 70);
                if(numberOfItemInHand > 1) {
                    font.draw(batch, numberOfItemInHand.toString(),
                            mousePosition.x + 15 - (numberOfItemInHand.toString().length()-1) * 17,
                            mousePosition.y -10);
                }
            }
        }

    }

    static public boolean checkClick(Vector2 point) {
        if(!opened) return false;
        if(point.x <= 750 && point.y >= 620) return true;
        changeOpenned();
        return false;
    }

    static public void leftClick(Vector2 point) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                if(buttons[i][j].checkPoint(point)) {
                    Item temp = items[i][j]; Integer temp2 = numberOfItem[i][j];
                    items[i][j] = itemInHand; numberOfItem[i][j] = numberOfItemInHand;
                    itemInHand = temp; numberOfItemInHand = temp2;
                    return;
                }
            }
        }

    }
    static public void changeOpenned() {
        if(opened == true) {

        }
        click.play(SoundSettings.soundVolume);
        opened = !opened;
    }
    static public void dropItem(int faceDirection, Vector2 playerPosition) {
        Item temp = null;
        if(items[0][selected] != null) {
            if(numberOfItem[0][selected] > 1) {
                temp = items[0][selected];
                numberOfItem[0][selected]--;
            } else {
                temp = items[0][selected];
                items[0][selected] = null;
                numberOfItem[0][selected] = 0;
            }
        }
        if(temp != null) {
            switch (faceDirection) {
                case 0:
                    RenderingList.add(new VisibleItem(temp, playerPosition.x,
                            playerPosition.y, new Vector2(0,-1)));
                    break;
                case 1:
                    RenderingList.add(new VisibleItem(temp, playerPosition.x,
                            playerPosition.y, new Vector2(0,1)));
                    break;
                case 2:
                    RenderingList.add(new VisibleItem(temp, playerPosition.x,
                            playerPosition.y, new Vector2(-1,0)));
                    break;
                case 3:
                    RenderingList.add(new VisibleItem(temp, playerPosition.x,
                            playerPosition.y, new Vector2(1,0)));
                    break;
            }
        }
    }
    static public void dropItemHand(int faceDirection, Vector2 playerPosition) {
        Item temp = null;
        while(itemInHand != null) {
            if(numberOfItemInHand> 1) {
                temp = itemInHand;
                numberOfItemInHand--;
            } else {
                temp = itemInHand;
                itemInHand = null;
                numberOfItemInHand = 0;
            }
            if(temp != null) {
                switch (faceDirection) {
                    case 0:
                        RenderingList.add(new VisibleItem(temp, playerPosition.x,
                                playerPosition.y, new Vector2(0,-1)));
                        break;
                    case 1:
                        RenderingList.add(new VisibleItem(temp, playerPosition.x,
                                playerPosition.y, new Vector2(0,1)));
                        break;
                    case 2:
                        RenderingList.add(new VisibleItem(temp, playerPosition.x,
                                playerPosition.y, new Vector2(-1,0)));
                        break;
                    case 3:
                        RenderingList.add(new VisibleItem(temp, playerPosition.x,
                                playerPosition.y, new Vector2(1,0)));
                        break;
                }
            }
        }
    }
    static public void changeSelected(int i) {
        selected = i - 1;
    }
    static public void addSelected(int i) {
        selected += i + 999999;
        selected %= 9;
    }

    static public boolean addItem(Item item) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 9; j++) {
                if(items[i][j] == null) {
                    items[i][j] = item;
                    numberOfItem[i][j] = 1;
                    return true;
                }
                if(items[i][j].id == item.id) {
                    numberOfItem[i][j]++;
                    return true;
                }
            }
        return false;
    }

}
