package com.shrubyway.game.linemaker.narrator;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Food.Food;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ThrowableItem;
import com.shrubyway.game.linemaker.ActionLogicManager;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.myinterface.TutorialHints;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.decoration.*;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class NarratorTutorial extends NarratorActionLogic {
    {
        eventPrefix = "NAR_TUT_";
    }

    /* This action is NOT save-safe. That means that if you save the game during this action, it can be broken. */
    /* Parts that can be broken are: dummyes, chest */
    static Decoration[] dummyes = null;
    static Decoration chest = null;

    Vector2 positionCenter = new Vector2(17300, 20600);

    static Decoration saveSpot = null;
    boolean foodInInventory = false;
    int[] throwItems = {20, 1, 0};
    int[] missThrowLines = {43, 44, 45, 46, 40};
    int throwMissCounter = 0;
    int localDummy = 0;
    int stageDamageStatus = -1;

    int ateLeaf = -1;

    public NarratorTutorial() {
        dummyes = new Decoration[]{DecorationsManager.getDecorationOnPosition(116, 135),
                DecorationsManager.getDecorationOnPosition(118, 138),
                DecorationsManager.getDecorationOnPosition(114, 138)};
    }

    static private void spawnItems(int id, int number) {
        for (int i = 0; i < number; i++) {
            float angle = (float) (2 * Math.PI * i / number);
            float x = Game.player.positionCenter().x + 150 * (float) Math.cos(angle);
            float y = Game.player.positionCenter().y + 150 * (float) Math.sin(angle);
            Game.objectsList.add(new VisibleItem(new Item(id), x, y));
        }
    }

    static private void attackPlayerWithJunk() {
        for (int i = 0; i < 16; i++) {
            float angle = (float) (2 * Math.PI * i / 7);
            float x = Game.player.positionCenter().x + 700 * (float) Math.cos(angle);
            float y = Game.player.positionCenter().y + 700 * (float) Math.sin(angle);

            ThrowableItem item = new ThrowableItem(new Vector2(x, y), Game.player.positionCenter(), new Item(1), null, true);

            Game.objectsList.add(item);
        }
    }

    private void FinishTutorialEarly() {
        Event.cast("Forest_Tutorial_Finished");
        Event.cast("Pumping_Opened");
        if(!Event.happened("Fire_opened")) {
            Event.cast("Fire_opened");
        }
        if(!Event.happened("Air_opened")) {
            Event.cast("Air_opened");
        }
        if(!Event.happened("Earth_opened")) {
            Event.cast("Earth_opened");
        }
        if(!Event.happened("Water_opened")) {
            Event.cast("Water_opened");
        }
        TutorialHints.finish();
        ActionLogicManager.remove(this);
        ActionLogicManager.add(new NarratorPostTutorial());
    }

    boolean notifiedAboutDistance = false;

    @Override
    public void update(float delta) {
        if (delay > 0) {
            delay -= delta;
            return;
        }
        int stage = getStatus();
        if(stage <= 16) {
            if(positionCenter.dst(Game.player.positionCenter()) > 1200) {
                FinishTutorialEarly();
                waitLine(47, -1, 0.3f);
                return;
            }

            if(positionCenter.dst(Game.player.positionCenter()) > 1000 && !notifiedAboutDistance) {
                Game.player.stopMovment();
                if(waitLine(35, -1, 0.3f)) {
                    notifiedAboutDistance = true;
                    Game.player.AllowMovment();
                }
                return;
            }
        }
        switch (stage) {
            case 0:
                waitLine(8, 0, 0.3f);
                break;
            case 1:
                if (waitLine(9, 1, 1.3f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/fire.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Fire_opened");
                }
                break;
            case 2:
                waitLine(10, 2, 0.6f);
                break;
            case 3:
                if(waitLine(11, 3, 0.3f)) {
                    for (Decoration dummy : dummyes) {
                        ((Dummy) dummy).hitCounter = 0;
                    }
                    dummyes[0].pointWithArrow = true;
                    Game.player.attacked = false;
                    TutorialHints.changeHint(5);
                    localDummy = 0;
                };
                break;
            case 4:
                if(Game.player.attacked) {
                    TutorialHints.finish();
                }
                if(localDummy == 3) {
                    if(waitLine(12, 4, 0.6f)) {
                        ShrubyWay.assetManager.get("sounds/EFFECTS/air.ogg", Sound.class).play(SoundSettings.soundVolume);
                        Event.cast("Air_opened");
                    };
                    break;
                }
                if(((Dummy)dummyes[localDummy]).hitCounter >= 5) {
                    dummyes[localDummy].pointWithArrow = false;
                    localDummy++;
                    if(localDummy < 3) {
                        dummyes[localDummy].pointWithArrow = true;
                        ((Dummy) dummyes[localDummy]).hitCounter = 0;
                    }
                }
                break;
            case 5:
                waitLine(13, 5, 0.3f);
                break;
            case 6:
                if(waitLine(14, 6, 0.3f)) {
                    for (Decoration dummy : dummyes) {
                        ((Dummy) dummy).throwCounter = 0;
                    }
                    dummyes[0].pointWithArrow = true;
                    Game.player.throwed = false;
                    TutorialHints.changeHint(6);
                    localDummy = 0;
                    spawnItems(throwItems[localDummy], 3);
                    throwMissCounter = 0;
                    Game.player.throwCount = 0;
                }
                break;
            case 7:
                if(Game.player.throwed) {
                    TutorialHints.finish();
                }
                if(localDummy == 3) {
                    waitLine(15, 7, 0.3f);
                    break;
                }
                if(((Dummy)dummyes[localDummy]).throwCounter >= 3) {
                    dummyes[localDummy].pointWithArrow = false;
                    localDummy++;
                    Game.player.throwCount = 0;
                    if(localDummy < 3) {
                        dummyes[localDummy].pointWithArrow = true;
                        ((Dummy) dummyes[localDummy]).throwCounter = 0;
                        spawnItems(throwItems[localDummy], 3);
                    }
                    break;
                }
                if(Game.player.throwCount >= 3 && AnimationGlobalTime.time() - Game.player.lastThrowTime > 1f) {
                    boolean giveMoreItems = false;
                    if(throwMissCounter != 5) {
                        if(waitLine(missThrowLines[throwMissCounter], 0, 0.3f)) {
                            throwMissCounter++;
                            giveMoreItems = true;
                        }
                    } else giveMoreItems = true;

                    if(giveMoreItems) {
                        Game.player.throwCount = 0;
                        spawnItems(throwItems[localDummy], 3);
                    }
                }
                break;
            case 8:
                if(waitLine(16, 8, 0.6f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/earth.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Earth_opened");
                }
                break;
            case 9:
                if(waitLine(17, 9, 1.5f)) {
                   Game.player.tookDamage = false;
                   attackPlayerWithJunk();

                }
                break;
            case 10:
                if(stageDamageStatus == -1) {
                    if(Game.player.tookDamage) stageDamageStatus = 0;
                    else stageDamageStatus = 1;
                }

                if(stageDamageStatus == 0) waitLine(18, 10, 0.3f);
                else waitLine(19, 10, 0.3f);
                break;
            case 11:
                if(waitLine(20, 11, 0.6f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/water.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Water_opened");
                }
                break;
            case 12:
                waitLine(21, 12, 0.3f);
                break;
            case 13:
                if (chest == null) {
                    chest = DecorationsManager.getDecorationOnPosition(116, 141);
                    if(chest instanceof ChestClosed) {
                        chest.pointWithArrow = true;
                    }
                    foodInInventory = Game.inventory.haveItemsWithType(Food.class);
                }
                if(chest instanceof ChestClosed) {
                    if(TutorialHints.currentHint != 4) {
                        TutorialHints.changeHint(4);
                    }
                    if(waitLine(22, 0, 0.3f)) {
                        Decoration tempChest = DecorationsManager.getDecorationOnPosition(116, 141);
                        if(tempChest instanceof ChestOpened) {
                            TutorialHints.finish();
                            if(waitLine(24, 13, 0.3f)) {
                                Game.player.health.healed = false;
                                TutorialHints.changeHint(3);
                            };
                        }
                    }
                } else {
                    if(foodInInventory) {
                        if(waitLine(23, 13, 0.3f)) {
                            Game.player.health.healed = false;
                            TutorialHints.changeHint(3);
                        };
                    } else {
                        if(waitLine(24, 13, 0.3f)) {
                            spawnItems(13, 1);
                            Game.player.health.healed = false;
                            TutorialHints.changeHint(3);
                        };
                    }
                }
                break;
            case 14:
                if(!Game.player.health.healed) {
                    if(Game.player.health.getHealth() == Game.player.health.getMaxHealth()) {
                        Game.player.health.changeHealth(Game.player.health.getMaxHealth() - 0.01f);
                    }
                }
                if(Game.player.health.healed) {
                    TutorialHints.finish();
                    if(ateLeaf == -1) {
                        if(Game.lastEatenItemID == 3 || Game.lastEatenItemID == 4 || Game.lastEatenItemID == 5) {
                            ateLeaf = 1;
                        } else {
                            ateLeaf = 0;
                        }
                    }
                    if(ateLeaf == 0) waitLine(26, 14, 0.3f);
                    else waitLine(25, 14, 0.3f);
                }
                break;
            case 15:
                if(waitLine(27, 15, 0.3f)) {
                    saveSpot = DecorationsManager.newOf(0);
                    saveSpot.change(116 * MapSettings.TYLESIZE,
                            137 * MapSettings.TYLESIZE,
                            116, 137);
                    saveSpot.pointWithArrow = true;
                    Game.gameSaved = false;
                    Game.objectsList.add(saveSpot);
                }
                break;
            case 16:
                if(Game.gameSaved) {
                    if(saveSpot == null) {
                        saveSpot = DecorationsManager.getDecorationOnPosition(116, 137);
                    }
                    saveSpot.pointWithArrow = false;
                    MobsManager.MobDeathCounter = 0;
                    if(waitLine(28, 16, 0.3f)) {
                        MobsManager.addMobNear(Game.player.positionCenter(), 0);
                        MobsManager.addMobNear(Game.player.positionCenter(), 1);
                        MobsManager.addMobNear(Game.player.positionCenter(), 0);
                    };
                }
                break;
            case 17:
                if(MobsManager.MobDeathCounter >= 3) {
                    waitLine(29, 17, 0.3f);
                }
                break;
            case 18:
                if(waitLine(30, 18, 0.3f)) {
                    ElementPumping.setLocalExp(ElementPumping.getNextLevelCost());
                    Event.cast("Pumping_Opened");
                }
                break;
            case 19:
                waitLine(31, 19, 0.3f);
                break;
            case 20:
                if(ElementPumping.getLVL() == 0) break;
                waitLine(32, 20, 0.3f);
                break;
            case 21:
                waitLine(33, 21, 0.3f);
                break;
            case 22:
                if(waitLine(34, 22, 0.3f)) {
                    Event.cast("Forest_Tutorial_Finished");
                    ActionLogicManager.remove(this);
                    ActionLogicManager.add(new NarratorPostTutorial());
                }
                break;


        }
    }
}
