package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.shrubyway.game.layout.Layout;
import com.shrubyway.game.linemaker.LineMaker;
import com.shrubyway.game.layout.SettingsLayout;
import com.shrubyway.game.linemaker.narrator.Narrator;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.map.ScreenGrid;
import com.shrubyway.game.myinterface.*;
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
import com.shrubyway.game.visibleobject.effect.Explosion;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.EntityManager;
import com.shrubyway.game.visibleobject.entity.Shruby;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

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
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    static GameSaver gameSaver = new GameSaver();
    public static Map map;
    static public Shruby player;
    static private Layout layout;
    static ElementPumping elementPumping = new ElementPumping();

    static public LineMaker LineMaker = new LineMaker();

    static public LineRenderer lineRenderer = new LineRenderer();

    Button continueButton, settingsButton, menuButton;

    public static boolean mobDiedByThrow = false, playerEating = false;


    public Game() {
       init();
    }

    private void init() {
        event.clear();
        objectsList.getList().clear();
        GlobalBatch.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
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
        screenGrid = new ScreenGrid();
        EntityManager.init();
        DecorationsManager.init();
        ItemManager.init();
        MobsManager.init();
        elementPumping.init();
        lineRenderer.init();
        TutorialHints.finish();
        Narrator.logic = null;

        objectsList = new ObjectsList();
        event = new Event();
        map = new Map(1);
        if(!GameSaver.checkSaveFile()) player = new Shruby(31599,31599);
        else player = new Shruby(31599, 31599, false);

        localCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        localCamera.position.set(player.positionCenter().x, player.positionCenter().y, 0);
        localCamera.update();

        objectsList.add(player);
        AnimationGlobalTime.clear();
        if(GameSaver.checkSaveFile()) {
           loadGame();
        } else {
            loadGameDefault();
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

    public void playerInputWorking(float delta) {
        mousePosition.set(ShrubyWay.inputProcessor.mousePosition().x
                + localCamera.position.x - Gdx.graphics.getWidth() / 2,
                ShrubyWay.inputProcessor.mousePosition().y
                        + localCamera.position.y - Gdx.graphics.getHeight() / 2);

        if(ShrubyWay.inputProcessor.isXPressed()) {
            LineMaker.skip();
        }


      if(ShrubyWay.inputProcessor.isCPressed()) {
          MobsManager.addMobNear(player.positionLegs(), 4);
          /*ElementPumping.waterLevel++;
          ElementPumping.fireLevel++;
          ElementPumping.earthLevel++;
          ElementPumping.airLevel++; */
        }
        if(ShrubyWay.inputProcessor.isLPressed()) {
            /*ElementPumping.waterLevel--;
            ElementPumping.fireLevel--;
            ElementPumping.earthLevel--;
            ElementPumping.airLevel--; */
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
                            playerEating = true;
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
                if(obj instanceof Decoration dec) {
                    if(player.interactionBox().overlaps(dec.interactionBox())) {
                        dec.interact();
                    }
                }
            }
        }



        if ((leftClick || rightClick) &&
                !inventory.checkClick(ShrubyWay.inputProcessor.mousePosition())
        && !elementPumping.leftClick(ShrubyWay.inputProcessor.mousePosition())) {
            if (leftClick) {
                player.attack(mousePosition);
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
            inventory.leftClick(ShrubyWay.inputProcessor.mousePosition());
        }
        if (rightClick) {
            rightClick = false;
            inventory.rightClick(ShrubyWay.inputProcessor.mousePosition());
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
       Narrator.update(delta);
       LineMaker.update(delta);
       lineRenderer.update(delta);

       if (AnimationGlobalTime.time() - lastMobUpdate > 1f && Event.happened("Forest_Tutorial_Finished")) {
            lastMobUpdate = AnimationGlobalTime.time();
            MobsManager.playerAddUpdate(1);

                MobsManager.tryGenerateMob(player.positionLegs());
        }
       if(AnimationGlobalTime.time() - lastRegen > RegenCooldown * ElementPumping.getPassiveHealTimeMultiplier(ElementPumping.waterLevel)) {
           lastRegen = AnimationGlobalTime.time();
           player.health.heal(1f);
       }

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
            if(obj instanceof Explosion exp) {

                for(VisibleObject obj2 : temp) {
                    if(obj2 instanceof Entity ent2) {
                        ((Entity) obj2).addMomentum(exp.getMomentum(ent2.positionCenter()));
                        if(!exp.damaged) {
                            ent2.getDamageWithoutMomentum(exp.getDamage(ent2.positionCenter()), ent2.positionCenter());
                        }
                    }
                    if(obj2 instanceof Bullet bul) {

                    }
                    if(obj2 instanceof Decoration dec) {
                        if(!exp.damaged) {
                            if(exp.getDamage(dec.positionCenter()) != 0) dec.hit(exp.getDamage(dec.positionCenter()), dec.positionCenter());
                        }
                    }
                }
                exp.damaged = true;
            } else
              if(obj instanceof DamageDisplay dam) {
                    dam.update(delta);
             } else
            if (obj instanceof Bullet bul) {
                bul.processBullet(delta);
            } else if (obj instanceof Entity ent) {
                ent.liquidStatus(map.checkLiquid(ent.positionLegs()));
                if (ent.makingStep(map.checkTile(ent.positionLegs()))) {
                    map.makeStep(ent.positionLegs(), player.positionLegs());
                }
                if (obj instanceof Mob mob) {
                    mob.ai(player.positionLegs(), delta);
                }
            } else if (obj instanceof VisibleItem visobj) {
                visobj.moveToPlayer(player.positionItemDrop(), inventory, delta);
            }
        }

        for (VisibleObject from1 : temp)
            if(from1 instanceof InteractiveObject from) {
            if (from.attackBox() != null && from.attackBox().topLeftCorner.x < from.attackBox().bottomRightCorner.x) {
                for (VisibleObject to1 : temp)
                    if(to1 instanceof InteractiveObject to) {
                    if (from == to) continue;
                    if (to.hitBox() != null) {
                        if (from.attackBox().overlaps(to.hitBox())) {
                            if (from instanceof Bullet bul) {
                                if (bul.whoThrow == to) continue;
                            }
                            if (to instanceof Decoration dec) {
                                dec.hit(from.damage(), from.attackBox().overlapCenter(to.hitBox()));
                            } else
                                if (to instanceof Entity ent) {
                                if(ent.health.getHealth() <= 0) continue;

                                ent.getDamage(from.damage(),
                                        from.positionCenter(), from.attackBox().overlapCenter(to.hitBox()));

                                if(ent instanceof Mob && from instanceof Bullet bul && ent.health.getHealth() <= 0) {
                                   mobDiedByThrow = true;
                                }

                              if(ent.health.getHealth() <= 0) {
                                if(ent instanceof Mob mob) {
                                       if(from instanceof Bullet bul) {
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
                            if (from instanceof Bullet bul) {
                                bul.die();
                            }
                        }
                    }
                }
            }
        }
        for (VisibleObject obj : objectsList.getList()) {
            if (obj instanceof Entity ent) {
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
           if(to instanceof InteractiveObject inter) {
               if(inter.collisionBox() != null
                       && inter.collisionBox().topLeftCorner.x < inter.collisionBox().bottomRightCorner.x) {
                  Body body = inter.getCollisionBody();
                  if(inter instanceof Entity ent) {
                      ent.nextMovement.scl(60);
                      collisionChecker.setLinearVelocity(body, ent.nextMovement);
                  }
               }
           }
        }
        collisionChecker.process(delta);

        for(VisibleObject obj : temp) {
            if(obj instanceof Entity ent) {
                Rectangle col = ent.collisionBox();
                float x_center = (col.topLeftCorner.x + col.bottomRightCorner.x) / 2f,
                        y_center = (col.topLeftCorner.y + col.bottomRightCorner.y) / 2f;
                ent.updatePosition(collisionChecker.getMovement(ent.getCollisionBodyToChange(), x_center, y_center));

            }
        }
        temp.clear();
    }

    public void cameraUpdate(float delta) {
        localCamera.position.add(new Vector3(-CameraEffects.getAddPositionExplosion().x,
                -CameraEffects.getAddPositionExplosion().y, 0));
        localCamera.position.lerp(new Vector3(player.positionCenter(),0), 0.1f * delta * 60f);
        CameraEffects.update(delta);
        localCamera.position.add(new Vector3(CameraEffects.getAddPositionExplosion(), 0));
        localCamera.update();
    }


    public void gameTick(float delta) {
        delta = Math.min(delta, 1f);
        lastDelta = delta;
        playerInputWorking(delta);
        interfaceInputWorking(delta);
        globalProcessing(delta);
        cameraUpdate(delta);
    }


    public void renderShadows() {
        for(VisibleObject obj : objectsList.getList()) {
            if(obj instanceof Entity ent) {
                if(!ent.liquid()) {
                    float x = 1;
                    if(ent instanceof Mob mb) {
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
            if(obj instanceof DamageDisplay dam) {
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
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        HealthBar.render(player.health);
        Vector2 mousePos = new Vector2(ShrubyWay.inputProcessor.mousePosition().x, ShrubyWay.inputProcessor.mousePosition().y);
        mousePos.x /= GlobalBatch.scale; mousePos.y /= GlobalBatch.scale;
        inventory.render(mousePos);
        MiniMap.render(map.lvl, player.positionLegs().x, player.positionLegs().y);
        elementPumping.render(ShrubyWay.inputProcessor.mousePosition());
        TutorialHints.render();
        lineRenderer.render();


       // TextDrawer.drawCenterBlack("Mobs: " + MobsManager.MobCount, 50, 50, 1);
       /* TextDrawer.drawBlack("" +Gdx.graphics.getFramesPerSecond(), 50, 50, 1);
        float memory = Gdx.app.getJavaHeap() / 1024f / 1024f;
        TextDrawer.drawBlack("" + memory, 50, 100, 1); */


        if (gamePaused) {
             GlobalBatch.render(ShrubyWay.assetManager.get("interface/shadow.png", Texture.class),
                    0, 0);
            if(menuButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition()) && layout == null) {
                menuButton.renderSellected();
            } else menuButton.render();
            if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())&& layout == null) {
                settingsButton.renderSellected();
            } else settingsButton.render();
            if(continueButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())&& layout == null) {
                continueButton.renderSellected();
            } else continueButton.render();

            if(layout != null) layout.render(ShrubyWay.inputProcessor.mousePosition());
        }
        GlobalBatch.batch.setColor(1,1,1,1 - CameraEffects.getSaveEffect());
        GlobalBatch.render(ShrubyWay.assetManager.get("interface/screen.png", Texture.class),
                0, 0);
        GlobalBatch.batch.setColor(1,1,1,1);
    }


    public void pause() {
        gamePaused = true;
        LineMaker.pause();
        AnimationGlobalTime.pause();
        map.pauseSounds();
    }

    public void resume() {
        gamePaused = false;
        LineMaker.resume();
        AnimationGlobalTime.resume();
        map.resumeSounds();
    }


    public void renderFrame() {
        GlobalBatch.setProjectionMatrix(localCamera);
        ScreenUtils.clear(1, 1, 1, 1);
        //batch.begin();
        map.render(player.position());
        renderShadows();
        renderObjects();
        renderDamage();
        renderInterface();
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
