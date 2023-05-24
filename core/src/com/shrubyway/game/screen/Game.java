package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.*;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.Food;
import com.shrubyway.game.item.Harmonica;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.map.MapSettings;
import com.shrubyway.game.myinterface.HealthBar;
import com.shrubyway.game.myinterface.Inventory;
import com.shrubyway.game.myinterface.MiniMap;
import com.shrubyway.game.myinterface.TextDrawer;
import com.shrubyway.game.visibleobject.InteractiveObject;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.decoration.DecorationsManager;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.Shruby;
import com.shrubyway.game.visibleobject.entity.mob.Mob;
import com.shrubyway.game.visibleobject.entity.mob.MobsManager;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Screen {
    Shruby player;
    Map map;
    OrthographicCamera localCamera;
    Vector2 mousePosition;

    public boolean gameOver = false, menu = false;
    static LoadingScreen loadingScreen;


    public Game() {
       GlobalAssetManager.loadAll();
       if(loadingScreen == null)
           loadingScreen = new LoadingScreen();
       loadingScreen.startLoading();
    }

    private void init() {
        ObjectsList.getList().clear();
        GlobalBatch.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        DecorationsManager.init();
        ItemManager.init();
        MobsManager.init();
        map = new Map(1);
        player = new Shruby(50, 50);
        localCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        localCamera.position.set(player.positionCenter().x, player.positionCenter().y, 0);
        localCamera.update();

        ObjectsList.add(player);
        AnimationGlobalTime.clear();
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

    public void playerInputWorking() {
        mousePosition = new Vector2(ShrubyWay.inputProcessor.mousePosition().x
                + localCamera.position.x - Gdx.graphics.getWidth() / 2,
                ShrubyWay.inputProcessor.mousePosition().y
                        + localCamera.position.y - Gdx.graphics.getHeight() / 2);
        boolean spacePressed = ShrubyWay.inputProcessor.isSpacePressed();
        if (spacePressed && player.canAct()) {
            int temp = Inventory.selectedItem();
            if(temp != -1) {
                Boolean f = true;
                if((ItemManager.itemActing[temp] instanceof Food && player.health.getHealth()
                        == player.health.getMaxHealth())) f = false;
                if(ItemManager.itemActing[temp] == null) f = false;

                if(f) {
                    ItemManager.itemActing[temp].Acting();
                    if (ItemManager.itemActing[temp].checkAct()) {
                        if (ItemManager.itemActing[temp] instanceof Food) {

                            Item temp2 = Inventory.take();
                            player.health.heal(
                                    ((Food) ItemManager.itemActing[temp2.id]).heling);
                        }
                        if (ItemManager.itemActing[temp] instanceof Harmonica) {
                            ShrubyWay.inputProcessor.setSpacePressed(false);
                        }
                    }
                    player.updateAnimation(ItemManager.itemActing[temp].actingAnimation);
                } else {
                    ItemManager.itemActing[temp].stopActing();
                }
            }

        } else {
            int temp = Inventory.selectedItem();
            if(temp != -1) {
                if(ItemManager.itemActing[temp] != null) {
                    ItemManager.itemActing[temp].stopActing();
                }
            }
        }
        leftClick = ShrubyWay.inputProcessor.isMouseLeft();
        rightClick = ShrubyWay.inputProcessor.isMouseRight();

        if(Inventory.selectedItem() == -1 || ItemManager.itemActing[Inventory.selectedItem()] == null
        || !ItemManager.itemActing[Inventory.selectedItem()].stillActing) {
            player.running(ShrubyWay.inputProcessor.isRuning());
            Vector2 movingVector = ShrubyWay.inputProcessor.getMovementDirection();
            if (movingVector.len() != 0) {
                if (Inventory.selectedItem() != -1) {
                    if (ItemManager.itemActing[Inventory.selectedItem()] != null) {
                        ItemManager.itemActing[Inventory.selectedItem()].stopActing();
                    }
                }
            }
            player.tryMoveTo(movingVector);
            correctPosition();
        }



        if ((leftClick || rightClick) &&
                !Inventory.checkClick(ShrubyWay.inputProcessor.mousePosition())) {
            if (leftClick) {
                player.attack(mousePosition);
            }
            if (rightClick) {
                if (player.canThrow()) {
                    int selected = Inventory.selectedItem();
                    if (selected != -1 && ItemManager.throwingDamage[selected] != 0) {
                        Item temp = Inventory.take();
                        player.throwItem(mousePosition, temp, true);
                    }
                }
            }

            leftClick = false;
            rightClick = false;
        }
    }

    public float lastDrop = -1000000000f;

    public void menuInputWorking() {
        if (ShrubyWay.inputProcessor.isEscPressed()) {
            if (gamePaused) {
                resume();
            } else {
                pause();
            }
        }
    }

    public void interfaceInputWorking() {
        if (ShrubyWay.inputProcessor.isEPressed()) {
            Inventory.changeOpenned();
        }
        int x = ShrubyWay.inputProcessor.numberPressed();
        if (x > 0) {
            Inventory.changeSelected(x);
        }
        x = ShrubyWay.inputProcessor.getScroll();
        Inventory.addSelected(x);

        if (ShrubyWay.inputProcessor.isQPressed() && (AnimationGlobalTime.time() -
                lastDrop) > 0.1f) {
            lastDrop = AnimationGlobalTime.time();
            Inventory.dropItem(player.faceDirection, player.positionItemDrop());
        }
        if (leftClick) {
            leftClick = false;
            Inventory.leftClick(ShrubyWay.inputProcessor.mousePosition());
        }
        if (rightClick) {
            rightClick = false;
            Inventory.rightClick(ShrubyWay.inputProcessor.mousePosition());
            ShrubyWay.inputProcessor.setMouseRight(false);
        }

        if (!Inventory.opened) {
            Inventory.dropItemHand(player.faceDirection, player.positionItemDrop());
        }

    }


    CopyOnWriteArrayList<VisibleObject> temp = new CopyOnWriteArrayList<VisibleObject>();
    CopyOnWriteArrayList<InteractiveObject> temp2 = new CopyOnWriteArrayList<InteractiveObject>();

    float lastMobUpdate = 5f;

    public void globalProcessing() {

        if (AnimationGlobalTime.time() - lastMobUpdate > 1f) {
            lastMobUpdate = AnimationGlobalTime.time();
            MobsManager.playerAddUpdate(1);
            MobsManager.tryGenerateMob(player.positionLegs());
        }
        temp.clear();
        temp2.clear();
        for (VisibleObject obj : ObjectsList.getList()) {
            temp.add(obj);
        }
        map.update(player.positionLegs());

        for (VisibleObject obj : temp) {
            if (!ObjectsList.getList().contains(obj)) continue;
            if (obj instanceof Bullet) {
                ((Bullet) obj).processBullet(player.positionCenter());

            } else if (obj instanceof Entity) {
                Entity temp = (Entity) obj;
                temp.liquidStatus(map.checkLiquid(((Entity) obj).positionLegs()));
                if (obj instanceof Mob) {
                    ((Mob) obj).ai(player.positionLegs());
                }
                if (((Entity) obj).makingStep(map.checkTile(((Entity) obj).positionLegs()))) {

                    map.makeStep(((Entity) obj).positionLegs(), player.positionLegs());
                }
            } else if (obj instanceof VisibleItem) {
                ((VisibleItem) obj).moveToPlayer(player.positionItemDrop());
            }
        }

        for (VisibleObject obj : ObjectsList.getList()) {
            if (obj instanceof InteractiveObject) temp2.add((InteractiveObject) obj);
        }

        for (InteractiveObject obj : temp2) {
            if (!ObjectsList.getList().contains(obj)) continue;

            if (obj.attackBox() != null && obj.attackBox().topLeftCorner.x < obj.attackBox().bottomRightCorner.x) {
                for (InteractiveObject obj2 : temp2) {
                    if (!ObjectsList.getList().contains(obj2)) continue;
                    if (obj == obj2) continue;

                    if (obj2.hitBox() != null) {
                        if (obj.attackBox().overlaps(obj2.hitBox())) {
                            if (obj instanceof Bullet) {
                                if (((Bullet) obj).whoThrow == obj2) continue;
                            }
                            if (obj2 instanceof Decoration) {
                                ((Decoration) obj2).interact();
                            } else if (obj2 instanceof Entity) {
                                ((Entity) obj2).getDamage(obj.damage,
                                        obj.positionCenter());
                            }
                            if (obj instanceof Bullet) {
                                ((Bullet) obj).die();
                            }
                        }
                    }
                }
            }
        }

        for (VisibleObject obj : ObjectsList.getList()) {
            if (obj instanceof Entity) {
                ((Entity) obj).update();
            }
        }
        ObjectsList.sort();
        if(!ObjectsList.contains(player)) {
            gameOver = true;
            map.pauseSounds();
        }
    }

    public void cameraUpdate() {
        localCamera.position.lerp(new Vector3(player.positionCenter(),0), 0.1f);
        localCamera.update();
    }

    public void gameTick() {
        playerInputWorking();
        interfaceInputWorking();
        globalProcessing();
        cameraUpdate();
    }

    public void renderObjects() {
        for (VisibleObject obj : ObjectsList.getList()) {
            obj.render();
        }
    }

    public void renderInterface() {
        GlobalBatch.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        HealthBar.render(player.health);
        Inventory.render(ShrubyWay.inputProcessor.mousePosition());
       // TextDrawer.drawWithShadow("" + localCamera.position, 100, 100, 1);
        MiniMap.render(map.lvl, player.positionLegs().x, player.positionLegs().y);
        if (gamePaused) {

        }
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
        renderObjects();
        renderInterface();
       // batch.end();
    }

    Boolean gamePaused = false;
    Boolean gameInitialized = false;
    @Override
    public void updateScreen() {
        if(!gameInitialized) {
            for(int i = 0; i < 5; i++) {
                if (GlobalAssetManager.update()) {
                    gameInitialized = true;
                    init();
                    updateScreen();
                    break;
                }

            }
            loadingScreen.updateStatus((int)(GlobalAssetManager.getProgress() * 100));
            loadingScreen.updateScreen();
        } else {
            menuInputWorking();
            if (!gamePaused) gameTick();
        }

    }

    @Override
    public void renderScreen() {
        if(!gameInitialized) {
            loadingScreen.renderScreen();
        }else {
            renderFrame();
        }
    }

}
