package com.shrubyway.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.animation.Animator;
import com.shrubyway.game.event.Event;
import com.shrubyway.game.item.Item;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.myinterface.TextDrawer;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.shapes.Rectangle;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;

public class PoppyShop extends Overlay {

    int localClipNumber = 0;
    int lastNonIdle = -1;
    static final int itemsInShop = 13;
    static int[] itemID = new int[itemsInShop];
    static Integer[] itemPrice = new Integer[itemsInShop];

    static String[] itemDescription = new String[itemsInShop];
    static Rectangle[] itemRect = new Rectangle[itemsInShop];

    static Rectangle[] RealItemRect = new Rectangle[itemsInShop];
    static Button sell, exit;
    static Animation<TextureRegion> open, close;

    static Vector2 mouseScreenPos = new Vector2(0, 0);


    private void playClip(int clipNumber) {
        while(true) {
            clipNumber = Math.max(0, Math.min(clipNumber, 15));
            if (clipNumber > 1) lastNonIdle = clipNumber;
            localClipNumber = clipNumber;
            try {
                FileHandle clip = Gdx.files.internal("SCENES/SHOP/TEST1/" + clipNumber + ".webm");
                ShrubyWay.videoPlayer.play(clip);
            } catch (Exception e) {
                continue;
            }
                break;
            }
    }

    private void sell(){
        playClip(5 + (int)(Math.random() * 2));
    }
    boolean closed = false;

    private void exit() {
        ShrubyWay.videoPlayer.stop();
        int clipNumber = 13 + (int)(Math.random() * 2);
        playClip(clipNumber);
        if(clipNumber == 13) {
            closeTime = -0.7f;
        } else {
            closeTime = -2.2f;
        }

        ShrubyWay.videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                closed = true;
                playClip(0);
            }
        });

    }

    private void classicCompletionListener() {
        ShrubyWay.videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                if(localClipNumber == 0 && (Math.random() < 1/7f)) {
                    playClip(1);
                } else {
                    playClip(0);
                }
            }
        });
    }
    long last_bad_click = 0;
    @Override public void leftClick(Vector2 mousePos) {
        if(sell.rectangle.checkPoint(mousePos)) sell();
        if(exit.rectangle.checkPoint(mousePos)) exit();

        for(int i = 0; i < itemsInShop; i++) {
            if(RealItemRect[i].checkPoint(mouseScreenPos)) {
                if (itemPrice[i] != null) {
                    if (Game.player.money >= itemPrice[i]) {
                        last_bad_click = -1;
                        Sound sold = ShrubyWay.assetManager.get("sounds/EFFECTS/sold.ogg", Sound.class);
                        sold.play(SoundSettings.soundVolume);
                        Game.player.money -= itemPrice[i];
                        if (Game.inventory.havePlaceFor(itemID[i])) {
                            Game.inventory.addItem(new Item(itemID[i]));
                            playClip(2 + (int) (Math.random() * 2));
                        } else {
                            playClip(4);
                            Game.objectsList.add(new VisibleItem(new Item(itemID[i]),
                                    Game.player.positionLegs().x, Game.player.positionLegs().y));
                        }
                    } else {
                        if (last_bad_click == i) {
                            playClip(10);
                            last_bad_click = -1;
                        } else {
                            playClip(7 + (int) (Math.random() * 3));
                            last_bad_click = i;
                        }
                    }
                } else {
                    last_bad_click = -1;
                    if (!Event.happened("Poppy_Shop_Willow_touched")) {
                        Event.cast("Poppy_Shop_Willow_touched");
                        SoundSettings.pauseMusic();
                        ShrubyWay.videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
                            @Override
                            public void onCompletionListener(FileHandle file) {
                              SoundSettings.resumeMusic();
                              playClip(0);
                                classicCompletionListener();
                            }
                        });
                        playClip(15);

                    } else {
                        playClip(11 + (int) (Math.random() * 2));
                    }
                }
            }
        }
    }



    public PoppyShop() {
        closeTime = -inf;
        openTime = 0;
        ShrubyWay.assetManager.get("sounds/EFFECTS/shopPine.ogg", Sound.class).play(SoundSettings.soundVolume);
        SoundSettings.changeMusic("music/Shop.mp3");
        ShrubyWay.videoPlayer.stop();
        classicCompletionListener();
        playClip(0);
    }

    @Override public void pause() {
        ShrubyWay.videoPlayer.pause();
    }
    @Override public void resume() {
        ShrubyWay.videoPlayer.resume();
    }

    static final float inf = 1000000000;
    static float openTime = -inf, closeTime = -inf;
    Vector2 centerOffset = new Vector2(0, 0);
    float scaleX = 0, scaleY = 0;
    @Override public void render(Vector2 mousePos) {
        if(ShrubyWay.videoPlayer == null) ShrubyWay.videoPlayer= VideoPlayerCreator.createVideoPlayer();

        if(scaleX != GlobalBatch.scaleX || scaleY != GlobalBatch.scaleY) {
            scaleX = GlobalBatch.scaleX;
            scaleY = GlobalBatch.scaleY;
            centerOffset.set(GlobalBatch.screenWidth / 2f - 960 * GlobalBatch.getScale(), GlobalBatch.screenHeight / 2f - 540 * GlobalBatch.getScale());
            centerOffset.scl(1/GlobalBatch.getScale());

            for(int i = 0; i < itemsInShop; i++) {
                if(RealItemRect[i] == null) {
                    RealItemRect[i] = new Rectangle(0,0,0,0);
                }
                RealItemRect[i].set(itemRect[i].topLeftCorner.x + centerOffset.x,
                        itemRect[i].topLeftCorner.y + centerOffset.y,
                        itemRect[i].bottomRightCorner.x - itemRect[i].topLeftCorner.x,
                        itemRect[i].bottomRightCorner.y - itemRect[i].topLeftCorner.y);
            }

        }

        Texture frame = ShrubyWay.videoPlayer.getTexture();

        if(frame != null) GlobalBatch.render(frame, centerOffset.x, centerOffset.y);

        GlobalBatch.render(ShrubyWay.assetManager.get("interface/shoppanel.png", Texture.class), 260, 26);
        TextDrawer.drawCenterWhite(Game.player.money + " M.", 715, 100, 1);
        sell.render(mousePos);
        exit.render(mousePos);

        for(int i = 0; i < itemsInShop; i++) {
            if (RealItemRect[i].checkPoint(mouseScreenPos)) {
                TextDrawer.drawWithShadow(ItemManager.itemName[itemID[i]], mouseScreenPos.x, mouseScreenPos.y - 50, 0.7f);
                TextDrawer.drawWithShadowColor(itemDescription[i], mouseScreenPos.x, mouseScreenPos.y - 100, 0.4f, new Color(119f/256,136f/256,153f/256, 1));
            }
            RealItemRect[i].render();
        }

        if(openTime >= 0) {
            TextureRegion frameOpen = open.getKeyFrame(openTime);
            GlobalBatch.render(frameOpen, centerOffset.x, centerOffset.y);
            if(open.isAnimationFinished(openTime)) {
                openTime = -inf;
            }
        }
        if(closeTime >= 0) {
            TextureRegion frameClose = close.getKeyFrame(closeTime);
            GlobalBatch.render(frameClose, centerOffset.x, centerOffset.y);
        }

    }
    @Override public void update(float delta) {
        ShrubyWay.videoPlayer.setVolume(SoundSettings.soundVolume);
        ShrubyWay.videoPlayer.update();
        mouseScreenPos.set((int)(ShrubyWay.inputProcessor.mousePosition().x / GlobalBatch.getScale()),
                (int)(ShrubyWay.inputProcessor.mousePosition().y)/ GlobalBatch.getScale());

        if(openTime > -inf) {
            openTime += delta;
        }
        if(closeTime > -inf) {
            if(closeTime <= 0 && closeTime + delta > 0) {
                ShrubyWay.assetManager.get("sounds/EFFECTS/shopPine.ogg", Sound.class).play(SoundSettings.soundVolume);
            }
            closeTime += delta;
        }
    }
    @Override public boolean isClosed() {
        return closed;
    }

    static {
        open = Animator.toAnimation("interface/ShopOpen/",  8);
        close = Animator.toAnimation("interface/ShopClose/",  8);
        open.setPlayMode(Animation.PlayMode.NORMAL);
        close.setPlayMode(Animation.PlayMode.NORMAL);

        sell = new Button(ShrubyWay.assetManager.get("interface/shopsell.png", Texture.class),
        ShrubyWay.assetManager.get("interface/shopsellsel.png", Texture.class),
        272, 50);

        exit = new Button(ShrubyWay.assetManager.get("interface/shopexit.png", Texture.class),
        ShrubyWay.assetManager.get("interface/shopexitsel.png", Texture.class),
        867, 50);

        itemID[0] = 15;
        itemPrice[0] = 50;
        itemDescription[0] = "A little snack. \n" +
                "Restores 7 HP.";
        itemRect[0] = new Rectangle(361, 216, 129, 104, true);

        itemID[1] = 16;
        itemPrice[1] = 100;
        itemDescription[1] = "Well fried. \n" +
                "Restores 10 HP, a stick remains.";
        itemRect[1] = new Rectangle(516, 170, 307, 148, true);

        itemID[2] = 17;
        itemPrice[2] = 200;
        itemDescription[2] = "This is... Clearly nutritious. \n" +
                "Restores 15 HP.";
        itemRect[2] = new Rectangle(841, 214, 214, 106, true);

        itemID[3] = 23;
        itemPrice[3] = 100;
        itemDescription[3] =  "I think my poison was poisoned!\n" +
                "Duration: 15 seconds";
        itemRect[3] = new Rectangle(362, 391, 150, 173, true);

        itemID[4] = 24;
        itemPrice[4] = 150;
        itemDescription[4] = "You might as well give up. \n" +
                "Duration: 5 seconds";
        itemRect[4] = new Rectangle(544, 391, 150, 173, true);

        itemID[5] = 25;
        itemPrice[5] = 100;
        itemDescription[5] = "Time to slow down.\n" +
                "Duration: 30 seconds";
        itemRect[5] = new Rectangle(731, 391, 150, 173, true);

        itemID[6] = 26;
        itemPrice[6] = 150;
        itemDescription[6] = "Time to speed up!\n" +
                "Duration: 60 seconds";
        itemRect[6] = new Rectangle(908, 391, 150, 173, true);

        itemID[7] = 14;
        itemPrice[7] = 100;
        itemDescription[7] = "Creates a dome of wind that can \n" +
                "save in times of trouble. ";
        itemRect[7] = new Rectangle(323, 623, 207, 190, true);

        itemID[8] = 18;
        itemPrice[8] = 200;
        itemDescription[8] = "Throwing it is easier than it seems. \n" +
                "Deals 10 damage.";
        itemRect[8] = new Rectangle(541, 627, 111, 190, true);

        itemID[9] = 19;
        itemPrice[9] = 100;
        itemDescription[9] = "Avoid touching the needles. \n" +
                "Deals 5 damage.";
        itemRect[9] = new Rectangle(666, 628, 160, 190, true);

        itemID[10] = 20;
        itemPrice[10] = 25;
        itemDescription[10] = "FAQ: not skippable in water. \n" +
                "Deals 2 damage.";
        itemRect[10] = new Rectangle(833, 737, 93, 74, true);

        itemID[11] = 10;
        itemPrice[11] = 250;
        itemDescription[11] = "A distant ancestor of the explerry.\n";
        itemRect[11] = new Rectangle(931, 632, 166, 190, true);

        itemID[12] = 31;
        itemPrice[12] = null;
        itemDescription[12] = "It looks like children's drawings \n" +
                "of old willow and burnt sticks that look\n" +
                "like pencils.\n";
        itemRect[12] = new Rectangle(749, 881, 353, 27, true);
    }

}
