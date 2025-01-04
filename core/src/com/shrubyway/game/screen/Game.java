package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.CameraEffects;
import com.shrubyway.game.CollisionChecker;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Food.Food;
import com.shrubyway.game.item.Harmonica;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.item.Potion.Potion;
import com.shrubyway.game.layout.Layout;
import com.shrubyway.game.layout.SettingsLayout;
import com.shrubyway.game.linemaker.ActionLogicManager;
import com.shrubyway.game.linemaker.LineMaker;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.map.ScreenGrid;
import com.shrubyway.game.myinterface.*;
import com.shrubyway.game.overlay.Overlay;
import com.shrubyway.game.saver.GameSaver;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.effect.DamageDisplay;
import com.shrubyway.game.visibleobject.effect.VisibleEffect;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.EntityManager;
import com.shrubyway.game.visibleobject.entity.Shruby;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;
import com.shrubyway.game.waiters.Narrator.NarratorStart;
import com.shrubyway.game.waiters.WaiterManager;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Screen implements java.io.Serializable {
    static public OrthographicCamera localCamera;
    Vector2 mousePosition = new Vector2(0,0);
    static public ScreenGrid screenGrid;

    public static CollisionChecker collisionChecker = new CollisionChecker();
    Event event;

    public static ObjectsList objectsList = new ObjectsList();
    public static Inventory inventory = new Inventory();
    public static int lastEatenItemID = -1;
    static GameSaver gameSaver = new GameSaver();
    static public boolean gameSaved = false;
    public static Map map;
    static public Shruby player;
    static private Layout layout;
    static ElementPumping elementPumping = new ElementPumping();

    static public LineMaker LineMaker = new LineMaker();

    static public LineRenderer lineRenderer = new LineRenderer();

    static public Overlay overlay = null;

    Button continueButton, settingsButton, menuButton;

    public Game() {
       init();
    }

    private void init() {
        event.clear();
        objectsList.getList().clear();
        GlobalBatch.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                GlobalBatch.screenWidth, GlobalBatch.screenHeight));
        soundClick = ShrubyWay.assetManager.get("sounds/EFFECTS/Click.ogg", Sound.class);

        float bWidht = ShrubyWay.assetManager.get("interface/c1.png", Texture.class).getWidth(),
                bHeight = ShrubyWay.assetManager.get("interface/c1.png", Texture.class).getHeight();

        continueButton= new Button(ShrubyWay.assetManager.get("interface/c1.png", Texture.class),
                ShrubyWay.assetManager.get("interface/c2.png", Texture.class), GlobalBatch.centerX() -
                bWidht / 2, GlobalBatch.centerY() - bHeight / 2 + bHeight + 50);
        settingsButton = new Button(ShrubyWay.assetManager.get("interface/s1.png", Texture.class),
                ShrubyWay.assetManager.get("interface/s2.png", Texture.class), GlobalBatch.centerX() -
                bWidht / 2, GlobalBatch.centerY() - bHeight / 2);
        menuButton = new Button(ShrubyWay.assetManager.get("interface/m1.png", Texture.class),
                ShrubyWay.assetManager.get("interface/m2.png", Texture.class), GlobalBatch.centerX() -
                bWidht / 2, GlobalBatch.centerY() - bHeight / 2 - bHeight - 50);
        inventory = new Inventory();
        lastEatenItemID = -1;
        screenGrid = new ScreenGrid();
        EntityManager.init();
        DecorationsManager.init();
        ItemManager.init();
        MobsManager.init();
        elementPumping.init();
        lineRenderer.init();
        TutorialHints.finish();
        overlay = null;

        objectsList = new ObjectsList();
        event = new Event();
        map = new Map(1);
        if(!GameSaver.checkSaveFile()) player = new Shruby(17300, 18600);
        else player = new Shruby(17300, 18600, false);

        localCamera = new OrthographicCamera(GlobalBatch.screenWidth, GlobalBatch.screenHeight);
        localCamera.position.set(player.positionCenter().x, player.positionCenter().y, 0);
        localCamera.update();

        objectsList.add(player);
        AnimationGlobalTime.clear();
        if(GameSaver.checkSaveFile()) {
            gameSaved = true;
           loadGame();
        } else {
            gameSaved = false;
            loadGameDefault();
        }
        if(!Event.happened("narrator_start_triggered")) {
            WaiterManager.addWaiter(new NarratorStart());
        }
        SoundSettings.changeMusic("music/Forest Theme.mp3");
    }

    Vector2 tempVector = new Vector2(0,0);

    public void correctPosition() {
        tempVector.set(player.position());
        if (tempVector.x < 0) {
            tempVector.x += MapSettings.MAPSIZE;
            localCamera.position.x += MapSettings.MAPSIZE;
        }
        if (tempVector.y < 0) {
            tempVector.y += MapSettings.MAPSIZE;
            localCamera.position.y += MapSettings.MAPSIZE;
        }
        if (tempVector.x >= MapSettings.MAPSIZE) {
            tempVector.x -= MapSettings.MAPSIZE;
            localCamera.position.x -= MapSettings.MAPSIZE;
        }
        if (tempVector.y >= MapSettings.MAPSIZE) {
            tempVector.y -= MapSettings.MAPSIZE;
            localCamera.position.y -= MapSettings.MAPSIZE;
        }
        player.changePosition(tempVector);
    }

    boolean leftClick = false, rightClick = false;
    Vector2 mouseScreenPos = new Vector2(0,0);

    public void playerInputWorking(float delta) {
        mousePosition.set(ShrubyWay.inputProcessor.mousePosition().x
                + localCamera.position.x - GlobalBatch.screenWidth / 2,
                ShrubyWay.inputProcessor.mousePosition().y
                        + localCamera.position.y - GlobalBatch.screenHeight / 2);
        mouseScreenPos.set((int)(ShrubyWay.inputProcessor.mousePosition().x / GlobalBatch.getScale()),
                (int)(ShrubyWay.inputProcessor.mousePosition().y)/ GlobalBatch.getScale());

        if(ShrubyWay.inputProcessor.isXPressed()) {
            LineMaker.skip();
        }


      if(ShrubyWay.inputProcessor.isCPressed()) {
         /*for(int i = 7; i < 8; i++) {
              objectsList.add(MobsManager.newOf(i, player.positionCenter().x + 200,
                      player.positionCenter().y + 200 * i));
          } */
        }
        if(ShrubyWay.inputProcessor.isLPressed()) {
            /*for(int i = 0; i < ItemManager.itemNumber; i++) {
                objectsList.add(new VisibleItem(new Item(i), player.positionCenter().x,
                        player.positionCenter().y));
            }
            player.money += 100;*/
            ElementPumping.addExp(100);
        }


        boolean spacePressed = ShrubyWay.inputProcessor.isSpacePressed();
        if (spacePressed && player.canAct()) {
            int temp = inventory.selectedItem();
            if(temp != -1) {
                Boolean f = true;
                if((ItemManager.itemActing[temp] instanceof Food && player.health.getHealth()
                        == player.health.getMaxHealth())) f = false;
                if(ItemManager.itemActing[temp] == null) f = false;

                if(f) {
                    ItemManager.itemActing[temp].Acting();
                    if (ItemManager.itemActing[temp].checkAct()) {
                        if (ItemManager.itemActing[temp] instanceof Food) {
                            Item temp2 = inventory.take();
                            float heling = ((Food) ItemManager.itemActing[temp2.id]).heling;
                            heling += ElementPumping.getEatingHealAddition(ElementPumping.waterLevel);
                            ((Food)ItemManager.itemActing[temp]).afterActing(player);
                            player.health.heal(heling);
                            lastEatenItemID = temp2.id;
                            ShrubyWay.inputProcessor.setSpacePressed(false);
                        }
                        if (ItemManager.itemActing[temp] instanceof Potion) {
                            inventory.take();
                            ((Potion)ItemManager.itemActing[temp]).afterActing(player);
                            ShrubyWay.inputProcessor.setSpacePressed(false);
                        }
                        if (ItemManager.itemActing[temp] instanceof Harmonica) {
                            ShrubyWay.inputProcessor.setSpacePressed(false);
                        }
                    }
                    player.updateAnimation(ItemManager.itemActing[temp].actingAnimation);
                } else {
                    if(ItemManager.itemActing[temp] != null) ItemManager.itemActing[temp].stopActing();
                }
            }

        } else {
            int temp = inventory.selectedItem();
            if(temp != -1) {
                if(ItemManager.itemActing[temp] != null) {
                    ItemManager.itemActing[temp].stopActing();
                }
            }
        }
        leftClick = ShrubyWay.inputProcessor.isMouseLeft();
        rightClick = ShrubyWay.inputProcessor.isMouseRight();

        if(inventory.selectedItem() == -1 || ItemManager.itemActing[inventory.selectedItem()] == null
        || !ItemManager.itemActing[inventory.selectedItem()].stillActing) {
            player.running(ShrubyWay.inputProcessor.isRuning());
            Vector2 movingVector = ShrubyWay.inputProcessor.getMovementDirection();
            if (movingVector.len() != 0) {
                if (inventory.selectedItem() != -1) {
                    if (ItemManager.itemActing[inventory.selectedItem()] != null) {
                        ItemManager.itemActing[inventory.selectedItem()].stopActing();
                    }
                }
            }
            player.tryMoveTo(movingVector);
            correctPosition();
        }
        if (ShrubyWay.inputProcessor.isEnterPressed()) {

            for (VisibleObject obj : objectsList.getList()) {
                if(obj instanceof Decoration) {
                    Decoration dec = (Decoration) obj;
                    if(player.interactionBox().overlaps(dec.interactionBox())) {
                        dec.interact();
                    }
                }
            }
        }



        if ((leftClick || rightClick) &&
                !inventory.checkClick(mouseScreenPos)
        && !elementPumping.leftClick(ShrubyWay.inputProcessor.mousePosition())) {
            if (leftClick) {
                player.attack(mousePosition, player.positionCenter());
            }
            if (rightClick) {
                if (player.canThrow()) {
                    int selected = inventory.selectedItem();
                    if (selected != -1 && ItemManager.throwingDamage[selected] != 0) {
                        Item temp = inventory.take();
                        player.throwItem(mousePosition, temp, true);
                    }
                }
            }


            leftClick = false;
            rightClick = false;
        }
    }

    public float lastDrop = -1000000000f;
    Sound soundClick;


    public void menuInputWorking() {
        if (ShrubyWay.inputProcessor.isEscPressed()) {
            if (gamePaused) {
                resume();
            } else {
                pause();
            }
        }

        if(gamePaused) {
            if(layout != null) {
                layout.update(ShrubyWay.inputProcessor.mousePosition());
                if(layout.isClosed()) layout = null;
                return;
            }
            continueButton.update();
            menuButton.update();
            settingsButton.update();
            if(!ShrubyWay.inputProcessor.isMouseLeft()) return;
            if(continueButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                soundClick.play(SoundSettings.soundVolume);
                resume();
            }
            if(menuButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                soundClick.play(SoundSettings.soundVolume);
                resume();
                map.pauseSounds();
                Menu();
            }
            if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                soundClick.play(SoundSettings.soundVolume);
                layout = new SettingsLayout();
            }
        } else {
            if(layout != null) {
                layout = null;
            }
        }
    }

    public void interfaceInputWorking(float delta) {
        if (ShrubyWay.inputProcessor.isEPressed()) {
            inventory.changeOpened();
        }
        int x = ShrubyWay.inputProcessor.numberPressed();
        if (x > 0) {
            inventory.changeSelected(x);
        }
        x = ShrubyWay.inputProcessor.getScroll();
        inventory.addSelected(x);

        if (ShrubyWay.inputProcessor.isQPressed() && (AnimationGlobalTime.time() -
                lastDrop) > 0.1f) {
            lastDrop = AnimationGlobalTime.time();
            inventory.dropItem(player.faceDirection, player.positionItemDrop());
        }
        if (leftClick) {
            leftClick = false;
            inventory.leftClick(mouseScreenPos);
        }
        if (rightClick) {
            rightClick = false;
            inventory.rightClick(mouseScreenPos);
            ShrubyWay.inputProcessor.setMouseRight(false);
        }

        if (!inventory.opened) {
            inventory.dropItemHand(player.faceDirection, player.positionItemDrop());
        }


    }


    void gameOver() {
        com.shrubyway.game.linemaker.LineMaker.skip();
        ShrubyWay.screen = new GameOver();
    }

    void Menu() {
        com.shrubyway.game.linemaker.LineMaker.skip();
        ShrubyWay.screen = new Menu();
    }

    float lastMobUpdate = 5f;
    static public float lastRegen = 5f;

    static public float RegenCooldown = 15f;


    public void globalProcessing(float delta) {
       TutorialHints.update();
       ActionLogicManager.update(delta);
       WaiterManager.update(delta);
       LineMaker.update(delta);
       lineRenderer.update(delta);

       List<VisibleObject> temp = new CopyOnWriteArrayList<>(objectsList.getList());
       MiniMap.mobs.clear();
       for(VisibleObject obj : temp) {
          if(obj instanceof Mob) {
                MiniMap.mobs.add(((Mob) obj).position);
          }
       }
        map.update(player.positionLegs());
        if(player.dead()) {
              LineMaker.skip();
              SoundSettings.stopMusic();
        }

       for (VisibleObject obj : temp) {
            if (!objectsList.getList().contains(obj)) continue;
            if(obj instanceof VisibleEffect) {
                VisibleEffect visibleEffect = (VisibleEffect) obj;
                visibleEffect.update(delta);

                if (!visibleEffect.interactive) {
                    continue;
                }
                for(VisibleObject obj2 : temp) {
                    visibleEffect.apply(obj2);
                }
                visibleEffect.applied = true;
            } else
            if (obj instanceof Bullet) {
                Bullet bul = (Bullet) obj;
                bul.processBullet(delta);
            } else if (obj instanceof Entity) {
                Entity ent = (Entity) obj;
                ent.liquidStatus(map.checkLiquid(ent.positionLegs()));
                if (ent.makingStep(map.checkTile(ent.positionLegs()))) {
                    map.makeStep(ent.positionLegs(), player.positionLegs());
                }
                if (obj instanceof Mob) {
                    Mob mob = (Mob) obj;
                    mob.ai(player.positionLegs(), delta);
                }
            } else if (obj instanceof VisibleItem) {
                VisibleItem visobj = (VisibleItem) obj;
                visobj.moveToPlayer(player.positionItemDrop(), inventory, delta);
            }
        }

        for (VisibleObject from1 : temp)
            if(from1 instanceof InteractiveObject) {
            InteractiveObject from = (InteractiveObject) from1;
            if (from.attackBox() != null && from.attackBox().topLeftCorner.x < from.attackBox().bottomRightCorner.x) {
                for (VisibleObject to1 : temp)
                    if(to1 instanceof InteractiveObject) {
                    InteractiveObject to = (InteractiveObject) to1;
                    if (from == to) continue;
                    if (to.hitBox() != null) {
                        if (from.attackBox().overlaps(to.hitBox())) {
                            if (from instanceof Bullet) {
                                Bullet bul = (Bullet) from;
                                if (bul.whoThrow == to) continue;
                            }
                            if (to instanceof Decoration) {
                                Decoration dec = (Decoration) to;
                                if (from instanceof Bullet) {
                                    dec.hitWithProjectile(from.damage(), from.attackBox().overlapCenter(to.hitBox()));
                                } else
                                dec.hitWithMelee(from.damage(), from.attackBox().overlapCenter(to.hitBox()));
                            } else
                                if (to instanceof Entity) {
                                Entity ent = (Entity) to;
                                if(ent.health.getHealth() <= 0) continue;
                                ent.getDamage(from.damage(),
                                        from.positionCenter(), from.attackBox().overlapCenter(to.hitBox()));
                              if(ent.health.getHealth() <= 0) {
                                if(ent instanceof Mob) {
                                    Mob mob = (Mob) ent;
                                       if(from instanceof Bullet) {
                                           Bullet bul = (Bullet) from;
                                           if(bul.whoThrow == player) {
                                               elementPumping.addExp(MobsManager.getExp(mob));
                                           }
                                       }
                                       if(from == player) {
                                             elementPumping.addExp(MobsManager.getExp(mob));
                                       }
                                    } else {
                                }
                                }
                            }
                            if (from instanceof Bullet) {
                                Bullet bul = (Bullet) from;
                                bul.die();
                            }
                        }
                    }
                }
            }
        }
        for (VisibleObject obj : objectsList.getList()) {
            if (obj instanceof Entity) {
                Entity ent = (Entity) obj;
                ent.update(delta);
            }
        }
        objectsList.sort();
        if(!objectsList.contains(player)) {
            map.pauseSounds();
            gameOver();
        }
        GlobalSoundManager.update(player.positionCenter());

        for(VisibleObject to: temp) {
           if(to instanceof InteractiveObject) {
                InteractiveObject inter = (InteractiveObject) to;
               if(inter.collisionBox() != null
                       && inter.collisionBox().topLeftCorner.x < inter.collisionBox().bottomRightCorner.x) {
                  Body body = inter.getCollisionBody();
                  if(inter instanceof Entity) {
                      Entity ent = (Entity) inter;
                      ent.nextMovement.scl(60);
                      collisionChecker.setLinearVelocity(body, ent.nextMovement);
                  }
               }
           }
        }
        collisionChecker.process(delta);

        for(VisibleObject obj : temp) {
            if(obj instanceof Entity) {
                Entity ent = (Entity) obj;
                Rectangle col = ent.collisionBox();
                float x_center = (col.topLeftCorner.x + col.bottomRightCorner.x) / 2f,
                        y_center = (col.topLeftCorner.y + col.bottomRightCorner.y) / 2f;
                ent.updatePosition(collisionChecker.getMovement(ent.getCollisionBodyToChange(), x_center, y_center));

            }
        }
        temp.clear();
    }
    public static Vector2 cameraFollowingPosition = null;
    public void cameraUpdate(float delta) {
        localCamera.position.add(new Vector3(-CameraEffects.getAddPositionExplosion().x,
                -CameraEffects.getAddPositionExplosion().y, 0));
        if(cameraFollowingPosition != null) {
           localCamera.position.lerp(new Vector3(cameraFollowingPosition,0), 0.1f * delta * 60f);
        } else {
            localCamera.position.lerp(new Vector3(player.positionCenter(),0), 0.1f * delta * 60f);
        }
        CameraEffects.update(delta);
        localCamera.position.add(new Vector3(CameraEffects.getAddPositionExplosion(), 0));
        localCamera.update();
    }


    public void gameTick(float delta) {
        delta = Math.min(delta, 1f);
        lastDelta = delta;

        if(overlay != null) {
            if(ShrubyWay.inputProcessor.isMouseLeft()) {
                overlay.leftClick(ShrubyWay.inputProcessor.mousePosition());
            }
            overlay.update(delta);
            if(overlay.isClosed()) {
                SoundSettings.changeMusic("music/Forest Theme.mp3");
                overlay = null;
            }
        } else {
            playerInputWorking(delta);
            interfaceInputWorking(delta);
            globalProcessing(delta);
            cameraUpdate(delta);
        }
    }


    public void renderShadows() {
        for(VisibleObject obj : objectsList.getList()) {
            if(obj instanceof Entity) {
                Entity ent = (Entity) obj;
                if(!ent.liquid()) {
                    float x = 1;
                    if(ent instanceof Mob) {
                        Mob mb = (Mob) ent;
                        x = mb.alpha;
                    }
                    GlobalBatch.batch.setColor(1,1,1,0.3f * x);
                    ent.renderShadow();
                    GlobalBatch.batch.setColor(1,1,1,1);
                }
            }
        }
    }

    public void renderDamage() {
        for(VisibleObject obj : objectsList.getList()) {
            if(obj instanceof DamageDisplay) {
                DamageDisplay dam = (DamageDisplay) obj;
                dam.render();
            }
        }
    }

    public void renderObjects() {
        for (VisibleObject obj : objectsList.getList()) {
            if(!(obj instanceof DamageDisplay)) obj.render();
        }
    }
    float lastDelta = 0;

    public void renderInterface() {
        player.interactionBox().render();
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                GlobalBatch.screenWidth, GlobalBatch.screenHeight));

        HealthBar.render(player.health);
        inventory.render(mouseScreenPos);
        MiniMap.render(map.lvl, player.positionLegs().x, player.positionLegs().y);
        elementPumping.render(ShrubyWay.inputProcessor.mousePosition(), mouseScreenPos);
        TutorialHints.render();
        lineRenderer.render();


        //TextDrawer.drawCenterBlack("Mobs: " + MobsManager.MobCount, 50, 50, 1);
        //TextDrawer.drawBlack("" +Gdx.graphics.getFramesPerSecond(), 50, 50, 1);
        //TextDrawer.drawBlack("" + mouseScreenPos, 50, 100, 1);

        GlobalBatch.batch.setColor(1,1,1,1 - CameraEffects.getSaveEffect());
        GlobalBatch.render(ShrubyWay.assetManager.get("interface/screen.png", Texture.class),
                0, 0);
        GlobalBatch.batch.setColor(1,1,1,1);
    }

    float lastScaleX = -1, lastScaleY = -1;
    public void renderPause() {
        if (gamePaused) {
            GlobalBatch.render(ShrubyWay.assetManager.get("interface/shadow.png", Texture.class),
                    0, 0, GlobalBatch.topRightCorner().x, GlobalBatch.topRightCorner().y);
            if(lastScaleX != GlobalBatch.scaleX || lastScaleY != GlobalBatch.scaleY) {
                lastScaleX = GlobalBatch.scaleX;
                lastScaleY = GlobalBatch.scaleY;
                float bWidht = ShrubyWay.assetManager.get("interface/c1.png", Texture.class).getWidth(),
                bHeight = ShrubyWay.assetManager.get("interface/c1.png", Texture.class).getHeight();
                continueButton.set(GlobalBatch.centerX() - bWidht / 2, GlobalBatch.centerY() - bHeight / 2 + bHeight + 50);
                settingsButton.set(GlobalBatch.centerX() - bWidht / 2, GlobalBatch.centerY() - bHeight / 2);
                menuButton.set(GlobalBatch.centerX() -
                        bWidht / 2, GlobalBatch.centerY() - bHeight / 2 - bHeight - 50);
                continueButton.update();
                settingsButton.update();
                menuButton.update();
            }

            if(menuButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null) {
                menuButton.renderSellected();
            } else menuButton.render();
            if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null) {
                settingsButton.renderSellected();
            } else settingsButton.render();
            if(continueButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null) {
                continueButton.renderSellected();
            } else continueButton.render();

            if(layout != null) layout.render(ShrubyWay.inputProcessor.mousePosition());
        }
    }


    public void pause() {
        gamePaused = true;
        LineMaker.pause();
        AnimationGlobalTime.pause();
        if(overlay != null) overlay.pause();
        map.pauseSounds();
    }

    public void resume() {
        gamePaused = false;
        LineMaker.resume();
        AnimationGlobalTime.resume();
        if(overlay != null) overlay.resume();
        map.resumeSounds();
    }


    public void renderFrame() {

        ScreenUtils.clear(0, 0, 0, 1);

        if(overlay != null) {
            if(gamePaused) overlay.render(null);
            else
            overlay.render(ShrubyWay.inputProcessor.mousePosition());
        } else {
            localCamera.viewportWidth = GlobalBatch.screenWidth;
            localCamera.viewportHeight = GlobalBatch.screenHeight;
            GlobalBatch.setProjectionMatrix(localCamera);
            map.render(new Vector2(localCamera.position.x, localCamera.position.y));
            renderShadows();
            renderObjects();
            renderDamage();
            renderInterface();
        }
        //batch.begin();
        renderPause();
       // batch.end();
    }

    Boolean gamePaused = false;
    Boolean gameInitialized = false;
    @Override
    public void updateScreen() {
        menuInputWorking();
        if (!gamePaused) gameTick(Gdx.graphics.getDeltaTime());

    }

    @Override
    public void renderScreen() {
        renderFrame();
    }

    static public void saveGame(Boolean effect) {
        gameSaved = true;
        com.shrubyway.game.linemaker.LineMaker.skip();
        if(effect) {
            ShrubyWay.assetManager.get("sounds/EFFECTS/save.ogg", Sound.class).play(SoundSettings.soundVolume);
            CameraEffects.save();
        }
        gameSaver.saveGameFiles();
        String userHome = System.getenv("APPDATA");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "SAVE.txt";

        File shrubyDirectory = new File(userHome, "ShrubyWay");
        if (!shrubyDirectory.exists()) {
            shrubyDirectory.mkdirs();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(gameSaver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadGame() {
        String userHome = System.getenv("APPDATA");
        String filePath = userHome + File.separator + "ShrubyWay" + File.separator + "SAVE.txt";

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            gameSaver = (GameSaver) objectInputStream.readObject();
            gameSaver.loadGameFiles();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    void loadGameDefault() {
        gameSaver.loadDefaultSettings();
    }

    @Override public void dispose() {

    }


}
