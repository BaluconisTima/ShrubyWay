package com.shrubyway.game.layout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.shrubyway.game.myinterface.Button;
import com.shrubyway.game.shapes.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.video.VideoPlayer;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.sound.SoundSettings;

public class PoppyShop {

    int localClipNumber = 0;
    int lastNonIdle = -1;
    static final int itemsInShop = 13;
    static int[] itemID = new int[itemsInShop];
    static Integer[] itemPrice = new Integer[itemsInShop];

    static String[] itemDescription = new String[itemsInShop];
    static Rectangle[] itemRect = new Rectangle[itemsInShop];

    static Button sell, exit;

    static {

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
                "Duration: 10 seconds";
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

    private void playClip(int clipNumber) {
        clipNumber = Math.max(0, Math.min(clipNumber, 15));
        if(clipNumber > 1) lastNonIdle = clipNumber;
        localClipNumber = clipNumber;
        try {
            ShrubyWay.videoPlayer.play(Gdx.files.internal("SCENES/SHOP/vp9/" + clipNumber + ".webm"));
        } catch (Exception e) {
            e.printStackTrace();
            playClip(clipNumber);
        }
    }

    public PoppyShop() {
        SoundSettings.changeMusic("music/Shop.mp3");

        ShrubyWay.videoPlayer.setLooping(false);
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
        playClip(0);
    }
    public void close() {

    }
    public void pause() {
        ShrubyWay.videoPlayer.pause();
    }
    public void resume() {
        ShrubyWay.videoPlayer.resume();
    }
    public void render(Vector2 mousePos) {
        Texture frame = ShrubyWay.videoPlayer.getTexture();
        if(frame != null) GlobalBatch.render(frame, 0, 0);

        //GlobalBatch.render(ShrubyWay.assetManager.get("interface/shoppanel.png", Texture.class), 260, 26);

    }
    public void update(Vector2 mousePos) {
        ShrubyWay.videoPlayer.update();
    }
    public boolean isClosed() {
        return false;
    }

}
