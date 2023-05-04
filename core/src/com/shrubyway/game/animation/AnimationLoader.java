package com.shrubyway.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shrubyway.game.GlobalAssetManager;

import java.util.ArrayList;

public class AnimationLoader {
       static Animator animator = new Animator();
       static Texture animationList;
       static public ArrayList<ArrayList<Animation<TextureRegion>[]>>

       load(String s, String actions[], ArrayList<String>[] actionsTypes, int frameCount[]){

              ArrayList<ArrayList<Animation<TextureRegion>[]>> currentAnimation = new ArrayList<>();

              for(int i = 0; i < actions.length; i++) {
                        currentAnimation.add(new ArrayList<Animation<TextureRegion>[]>());

                        int x = actionsTypes[i].toArray().length;
                     for(int j = 0; j < x; j++) {
                            String tempAnimation = s + "/" + actions[i] + "/" + actionsTypes[i].get(j) + ".png";
                            GlobalAssetManager.assetManager.load(tempAnimation, Texture.class);
                            GlobalAssetManager.assetManager.finishLoadingAsset(tempAnimation);
                            animationList = GlobalAssetManager.assetManager.get(tempAnimation, Texture.class);
                            Animation temp[]
                                    = {animator.toAnimation(animationList, frameCount[i], 0, 0),
                                       animator.toAnimation(animationList, frameCount[i], 0, 130)};
                            currentAnimation.get(i).add(temp);
                     }
              }
              return currentAnimation;
       }
}
