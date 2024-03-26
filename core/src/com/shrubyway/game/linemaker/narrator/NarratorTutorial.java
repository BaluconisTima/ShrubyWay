package com.shrubyway.game.linemaker.narrator;

import com.badlogic.gdx.audio.Sound;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.linemaker.actionLogic;
import com.shrubyway.game.myinterface.ElementPumping;
import com.shrubyway.game.myinterface.TutorialHints;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;


/***
 It's bad code, but there's no choice right now.
 It will most likely be redone.
 If you see this line in the full version of the game, then I've decided it's not so bad.
 ***/

public class NarratorTutorial extends actionLogic {

    private static int getTutorialStatus() {
        int i = 0;
        while(Event.happened("Forest_Tutorial_" + i)) {
            i++;
        }
        return i;
    }

    static float delay = 0.3f, localDelay = 0f;

    static private void clear(int id) {
        // Clear old events
    }

    static boolean notMoved = false;

    static private void spawnStones() {
        Game.objectsList.add(new VisibleItem(new Item(0),
                Game.player.positionCenter().x + 150, Game.player.positionCenter().y));
        Game.objectsList.add(new VisibleItem(new Item(0),
                Game.player.positionCenter().x - 150, Game.player.positionCenter().y));
        Game.objectsList.add(new VisibleItem(new Item(0),
                Game.player.positionCenter().x , Game.player.positionCenter().y + 150));
        Game.objectsList.add(new VisibleItem(new Item(0),
                Game.player.positionCenter().x , Game.player.positionCenter().y - 150));
        Game.player.throwTime = 0;
    }


    static boolean waitLine(int id, int stage_id, float delayAfter) {
        if(!Event.happened("Line_NAR_" + id + "_Casted")) {
            Game.LineMaker.castLine(0, id);
        } else {
            if(Event.happened("Line_NAR_" + id + "_Finished")) {
                delay = delayAfter;
                Event.cast("Forest_Tutorial_" + stage_id);
                return true;
            }
        }
        return false;
    }


    @Override public void update(float delta) {
        if(TutorialHints.currentHint == 1 && TutorialHints.progress < 0.01f) TutorialHints.changeHint(2);
        if(TutorialHints.currentHint == 2 && ShrubyWay.inputProcessor.isRuning()) TutorialHints.finish();

        if (delay > 0) {
            delay -= delta;
            return;
        }
        int status = getTutorialStatus();


        switch (status) {
            case 0:
                waitLine(0, 0, 0.3f);
                break;
            case 1:
                if (waitLine(1, 1, 0.4f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/paper.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Map_Opened");
                }
                ;
                break;
            case 2:
                if (waitLine(2, 2, 0)) {
                    TutorialHints.changeHint(1);
                    Game.player.moved = false;
                    localDelay = 10f;
                }
                break;
            case 3: {
                if (Game.player.moved) {
                    TutorialHints.finish();
                    if (notMoved) {
                        waitLine(6, 3, 15f);
                    } else {
                        Event.cast("Forest_Tutorial_3");
                        delay = 20f;
                    }
                    break;
                }
                if (localDelay > 0) {
                    localDelay -= delta;
                    break;
                }
                notMoved = true;

                if (waitLine(3, -1, 0.3f)) {
                    if (!Event.happened("Forest_Tutorial_LINE_3")) {
                        Event.cast("Forest_Tutorial_LINE_3");
                        localDelay = 8f;
                        break;
                    }
                } else break;
                if (waitLine(4, -1, 0.3f)) {
                    if (!Event.happened("Forest_Tutorial_LINE_4")) {
                        Event.cast("Forest_Tutorial_LINE_4");
                        localDelay = 8f;
                        break;
                    }
                } else break;
                if (waitLine(5, -1, 0.3f)) {
                    if (!Event.happened("Forest_Tutorial_LINE_5")) {
                        Event.cast("Forest_Tutorial_LINE_5");
                        localDelay = 8f;
                        break;
                    }
                } else break;
                break;
            }
            case 4: {
                if (!Event.happened("Line_NAR_8_Casted")) MobsManager.addMobNear(Game.player.position, 0);
                if (waitLine(8, 4, 0)) {
                    localDelay = 10f;
                }
                break;
            }
            case 5: {
                if (waitLine(7, 5, 0)) {
                    Game.player.throwed = false;
                    localDelay = 10f;
                    ShrubyWay.assetManager.get("sounds/EFFECTS/fire.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Fire_opened");
                    Game.player.throwed = false;
                    Game.player.attacked = false;
                    TutorialHints.changeHint(5);
                }
                break;
            }
            case 6: {
                if(Game.player.attacked) {
                    TutorialHints.finish();
                }
                if (MobsManager.MobCount == 0) {
                    TutorialHints.finish();
                    waitLine(9, 6, 0);
                } else if (Game.player.throwed) {
                    waitLine(36, -1, 0);
                }
                break;

            }
            case 7: {
                if (waitLine(10, 7, 0.6f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/earth.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Earth_opened");
                }
                ;
                break;
            }
            case 8: {
                waitLine(11, 8, 0.3f);
                break;
            }
            case 9: {
                if (waitLine(12, 9, 0.3f)) {
                    localDelay = 15f;
                }
                ;
                break;
            }
            case 10: {
                if (localDelay > 0) {
                    localDelay -= delta;
                    break;
                }
                if (!Event.happened("Line_NAR_13_Casted")) MobsManager.addMobNear(Game.player.position, 1);
                waitLine(13, 10, 0);
                break;
            }
            case 11: {
                waitLine(14, 11, 0);
                break;
            }
            case 12: {
                if (waitLine(15, 12, 0.6f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/air.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Air_opened");
                }
                ;
                break;
            }
            case 13: {
                if (waitLine(16, 13, 1f)) {
                    spawnStones();
                }
                TutorialHints.changeHint(6);
                Game.player.throwed = false;
                Game.player.throwTime = 0;
                Game.mobDiedByThrow = false;
                Game.player.attacked = false;
                break;
            }
            case 14: {
                if (MobsManager.MobCount == 0) {
                    TutorialHints.finish();
                    if (Game.mobDiedByThrow)
                        waitLine(19, 14, 0);
                    else waitLine(35, 14, 0);
                    break;
                }
                if (Game.player.throwed && !Event.happened("Line_NAR_17_Casted")) {
                    TutorialHints.finish();
                    waitLine(17, -1, 0);
                    break;
                }

                if (Game.player.attacked && !Event.happened("Line_NAR_34_Casted")) {
                    waitLine(34, -1, 0);
                    Game.player.attacked = false;
                    break;
                }

                if (Game.player.throwTime >= 4) {
                    if (waitLine(18, -1, 0)) {
                        if (!Event.happened("Forest_LINE_18_CASTED")) {
                            Event.cast("Forest_LINE_18_CASTED");
                            spawnStones();
                            break;
                        }
                    } else break;

                    if (waitLine(39, -1, 0)) {
                        if (!Event.happened("Forest_LINE_39_CASTED")) {
                            Event.cast("Forest_LINE_39_CASTED");
                            spawnStones();
                            break;
                        }
                    } else break;

                    if (waitLine(38, -1, 0)) {
                        if (!Event.happened("Forest_LINE_38_CASTED")) {
                            Event.cast("Forest_LINE_38_CASTED");
                            spawnStones();
                            break;
                        }
                    } else break;

                    if (waitLine(40, -1, 0)) {
                        if (!Event.happened("Forest_LINE_40_CASTED")) {
                            Event.cast("Forest_LINE_40_CASTED");
                            spawnStones();
                            break;
                        }
                    } else break;

                    spawnStones();
                }

                break;
            }
            case 15: {
                waitLine(20, 15, 0);
                break;
            }
            case 16: {
                if (waitLine(21, 16, 0.6f)) {
                    ShrubyWay.assetManager.get("sounds/EFFECTS/water.ogg", Sound.class).play(SoundSettings.soundVolume);
                    Event.cast("Water_opened");
                }
                break;
            }
            case 17: {
                waitLine(22, 17, 0.2f);
                break;
            }
            case 18: {
                if(localDelay > 0) {
                    localDelay -= delta;
                    break;
                }
                if(!Event.happened("Forest_FREE_FOOD_PEOPLE"))  {
                    TutorialHints.changeHint(3);
                    Event.cast("Forest_FREE_FOOD_PEOPLE");
                    Game.objectsList.add(new VisibleItem(new Item(13), Game.player.positionCenter().x + 150, Game.player.positionCenter().y));
                }
                waitLine(23, 18, 0);
                Game.playerEating = false;
                break;
            }
            case 19: {
                if(Game.player.health.getHealth() == Game.player.health.getMaxHealth()) {
                    Game.player.health.changeHealth(Game.player.health.getMaxHealth() - 0.01f);
                }
                if(Game.playerEating) {
                    TutorialHints.finish();
                    Event.cast("Forest_Tutorial_19");
                }
                break;
            }
            case 20: {
                waitLine(25, 20, 0);
                break;
            }
            case 21: {
                if(waitLine(26, 21, 0)) {
                    localDelay = 10f;
                }
                break;
            }
            case 22: {
                if(localDelay > 0) {
                    localDelay -= delta;
                    break;
                }
                if(!Event.happened("Forest_Tutorial_More_Mobs"))  {
                    Event.cast("Forest_Tutorial_More_Mobs");
                    MobsManager.addMobNear(Game.player.position, 0);
                    MobsManager.addMobNear(Game.player.position, 0);
                    MobsManager.addMobNear(Game.player.position, 1);
                }
                waitLine(27, 22, 0);
                break;
            }
            case 23: {
                if(MobsManager.MobCount != 0) break;
                if(!Event.happened("Pumping_Opened")) {
                    ElementPumping.setLocalExp(ElementPumping.getNextLevelCost());
                    Event.cast("Pumping_Opened");
                }
                waitLine(28, 23, 0);

                break;
            }
            case 24:{
               if(ElementPumping.getLVL() == 0) break;
                waitLine(29, 24, 0);
                break;
            }
            case 25:{ waitLine(30, 25, 0); break; }
            case 26:{ waitLine(31, 26, 0); break; }
            case 27:{ waitLine(32, 27, 0); break; }
            case 28:{
                if(waitLine(33, 28, 0)) {
                    Event.cast("Forest_Tutorial_Finished");
                 }
                break;
            }





        }
    }

}
