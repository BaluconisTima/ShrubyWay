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
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.CameraEffects;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Food;
import com.shrubyway.game.item.Harmonica;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.map.ScreenGrid;
import com.shrubyway.game.myinterface.*;
import com.shrubyway.game.saver.GameSaver;
import com.shrubyway.game.sound.GlobalSoundManager;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.effect.Explosion;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.EntityManager;
import com.shrubyway.game.visibleobject.entity.Shruby;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Screen implements java.io.Serializable {
    static public OrthographicCamera localCamera;
    Vector2 mousePosition;
    static public ScreenGrid screenGrid;
    Event event;

    public static ObjectsList objectsList = new ObjectsList();
    public static Inventory inventory = new Inventory();
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    static GameSaver gameSaver = new GameSaver();
    public static Map map;
    static public Shruby player;
    static ElementPumping elementPumping = new ElementPumping();

    Button continueButton, settingsButton, menuButton;


    public Game() {
       init();
    }

    private void init() {
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
          //  GameSaver.loadDefaultSettings();
        }

    }

    public void correctPosition() {
        Vector2 temp = new Vector2(player.position());
        if (temp.x < 0) {
            temp.add(new Vector2(MapSettings.MAPSIZE, 0));
            localCamera.position.add(new Vector3(MapSettings.MAPSIZE, 0, 0));
        }
        if (temp.y < 0) {
            temp.add(new Vector2(0, MapSettings.MAPSIZE));
            localCamera.position.add(new Vector3(0, MapSettings.MAPSIZE, 0));
        }
        if (temp.x >= MapSettings.MAPSIZE) {
            temp.add(new Vector2(-MapSettings.MAPSIZE, 0));
            localCamera.position.add(new Vector3(-MapSettings.MAPSIZE, 0, 0));
        }
        if (temp.y >= MapSettings.MAPSIZE) {
            temp.add(new Vector2(0, -MapSettings.MAPSIZE));
            localCamera.position.add(new Vector3(0, -MapSettings.MAPSIZE, 0));
        }
        player.changePosition(temp);
    }

    boolean leftClick = false, rightClick = false;

    public void playerInputWorking(float delta) {
        mousePosition = new Vector2(ShrubyWay.inputProcessor.mousePosition().x
                + localCamera.position.x - Gdx.graphics.getWidth() / 2,
                ShrubyWay.inputProcessor.mousePosition().y
                        + localCamera.position.y - Gdx.graphics.getHeight() / 2);

        if(ShrubyWay.inputProcessor.isCPressed()) {
          objectsList.add(MobsManager.newOf(3, mousePosition.x, mousePosition.y));
        }
        /*if(ShrubyWay.inputProcessor.isLPressed()) {
          loadGame();
        } */


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
                            float waterLevel = ElementPumping.waterLevel;
                            waterLevel /= 12;
                            while(Math.random() < waterLevel) {
                                heling *= 1.3;
                                waterLevel /= 4;
                            }
                            player.health.heal(heling);
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
            player.tryMoveTo(movingVector, delta);
            correctPosition();
        }
        if (ShrubyWay.inputProcessor.isZPressed()) {

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
                // settings = true;
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
        ShrubyWay.screen = new GameOver();
    }

    void Menu() {
        ShrubyWay.screen = new Menu();
    }

    CopyOnWriteArrayList<VisibleObject> temp = new CopyOnWriteArrayList<VisibleObject>();
    CopyOnWriteArrayList<InteractiveObject> temp2 = new CopyOnWriteArrayList<>();

    float lastMobUpdate = 5f;

    public void globalProcessing(float delta) {

        if (AnimationGlobalTime.time() - lastMobUpdate > 1f) {
            lastMobUpdate = AnimationGlobalTime.time();
            MobsManager.playerAddUpdate(1);
           // MobsManager.tryGenerateMob(player.positionLegs());
        }

  /*      screenGrid.build(objectsList.getList());*/

        temp.clear();
        temp2.clear();

        for (VisibleObject obj : objectsList.getList()) {
            temp.add(obj);
        }

        map.update(player.positionLegs());
        if(player.dead()) {
            SoundSettings.changeMusic(null);
        }

        for (VisibleObject obj : temp) {
            if (!objectsList.getList().contains(obj)) continue;
            if(obj instanceof Explosion exp) {

                for(VisibleObject obj2 : temp) {
                    if(obj2 instanceof Entity ent2) {
                        ((Entity) obj2).addMomentum(exp.getMomentum(ent2.positionCenter()));
                        if(!exp.damaged) {
                            ent2.getDamage(exp.getDamage(ent2.positionCenter()));
                        }
                    }
                    if(obj2 instanceof Bullet bul) {

                    }
                    if(obj2 instanceof Decoration dec) {
                        if(!exp.damaged) {
                            if(exp.getDamage(dec.positionCenter()) != 0) dec.hit();
                        }
                    }
                }
                exp.damaged = true;
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

        for (VisibleObject obj : objectsList.getList()) {
            if (obj instanceof InteractiveObject io) temp2.add(io);
        }

        for (InteractiveObject from : temp2) {
            if (!objectsList.getList().contains(from)) continue;

            if (from.attackBox() != null && from.attackBox().topLeftCorner.x < from.attackBox().bottomRightCorner.x) {
                for (InteractiveObject to : temp2) {
                    if (!objectsList.getList().contains(to)) continue;
                    if (from == to) continue;

                    if (to.hitBox() != null) {
                        if (from.attackBox().overlaps(to.hitBox())) {

                            if (from instanceof Bullet bul) {
                                if (bul.whoThrow == to) continue;
                            }
                            if (to instanceof Decoration dec) {
                                dec.hit();
                            } else
                                if (to instanceof Entity ent) {


                                if(ent.health.getHealth() <= 0) continue;
                                ent.getDamage(from.damage(),
                                        from.positionCenter());

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

    public void renderObjects() {
        for (VisibleObject obj : objectsList.getList()) {
            obj.render();
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
        TextDrawer.drawBlack("" +Gdx.graphics.getFramesPerSecond(), 50, 50, 1);

        long memory = Gdx.app.getJavaHeap() / 1024 / 1024;
        TextDrawer.drawBlack("" + memory, 50, 100, 1);
        int i = 0;
        for(VisibleObject obj : objectsList.getList()) {
            i++;
            TextDrawer.drawBlack("" + obj.getClass().getSimpleName(), 50, 1000 - 50 * i, 0.5f);
        }

        if (gamePaused) {
             GlobalBatch.render(ShrubyWay.assetManager.get("interface/shadow.png", Texture.class),
                    0, 0);
            if(menuButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                menuButton.renderSellected();
            } else menuButton.render();
            if(settingsButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                settingsButton.renderSellected();
            } else settingsButton.render();
            if(continueButton.rectangle.checkPoint(ShrubyWay.inputProcessor.mousePosition())) {
                continueButton.renderSellected();
            } else continueButton.render();
        }
        GlobalBatch.batch.setColor(1,1,1,1 - CameraEffects.getSaveEffect());
        GlobalBatch.render(ShrubyWay.assetManager.get("interface/screen.png", Texture.class),
                0, 0);
        GlobalBatch.batch.setColor(1,1,1,1);
    }


    public void pause() {
        gamePaused = true;
        AnimationGlobalTime.pause();
        map.pauseSounds();
    }

    public void resume() {
        gamePaused = false;
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
        renderInterface();
       // batch.end();
    }

    Boolean gamePaused = false;
    Boolean gameInitialized = false;
    @Override
    public void updateScreen() {
        SoundSettings.changeMusic("music/Forest Theme.mp3");
        menuInputWorking();
        if (!gamePaused) gameTick(Gdx.graphics.getDeltaTime());

    }

    @Override
    public void renderScreen() {
        renderFrame();
    }

    static public void saveGame(Boolean effect) {
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

    @Override public void dispose() {

    }


}
