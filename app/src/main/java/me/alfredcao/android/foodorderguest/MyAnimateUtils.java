package me.alfredcao.android.foodorderguest;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by cyssn on 2015-12-08.
 */
public class MyAnimateUtils {

    private static final float TRANSLATE_Y_FLOAT = 80.0f;
    private static final long ANIMATION_DURATION = 800;
    private static final long ANIMATION_DELAY = 300;

    public static void fadeOutView(final View view){

        AnimationSet animationSet = new AnimationSet(false);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(ANIMATION_DURATION);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        TranslateAnimation translateAnimation = new TranslateAnimation(
                view.getX(),view.getX(),view.getY(),view.getY() + TRANSLATE_Y_FLOAT*3);
        translateAnimation.setDuration(ANIMATION_DURATION);
        translateAnimation.setFillAfter(true);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);
        //tv.setVisibility(View.GONE);
        //tv.setHeight(0);
    }

    public static void showView(View view){
        view.setVisibility(View.VISIBLE);
    }

    public static void fadeInView(final View view){

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setStartOffset(ANIMATION_DELAY);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(ANIMATION_DURATION);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        TranslateAnimation translateAnimation = new TranslateAnimation(
                view.getTranslationX(),view.getTranslationX(),
                view.getY() + TRANSLATE_Y_FLOAT, view.getY());
        translateAnimation.setDuration(ANIMATION_DURATION);
        translateAnimation.setFillAfter(true);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);
        //tv.setVisibility(View.GONE);
        //tv.setHeight(0);
    }
}
