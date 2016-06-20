package com.larvalabs.betweenus.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.larvalabs.betweenus.base.OnCompleteListener;
import com.larvalabs.betweenus.core.Constants;

public class AnimUtil {

    /**
     * Use Animator to do a "setTimeout"
     */
    public static ValueAnimator animateDummy(int duration) {
        ValueAnimator a = ValueAnimator.ofFloat(0, 1);
        a.setDuration(duration);
        a.start();
        return a;
    }

    public static ValueAnimator animateDummy(int duration, final OnCompleteListener listnr) {
        return animateDummy(duration, false, listnr);
    }

    public static ValueAnimator animateDummy(int duration, boolean dontStart, final OnCompleteListener listnr) {
        ValueAnimator a = animateDummy(duration);
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listnr != null) {
                    listnr.onComplete();
                }
            }
        });
        if (!dontStart) {
            a.start();
        }
        return a;
    }

    public static ObjectAnimator fadeIn(View view) {
        return fadeIn(view, Constants.baseDuration, 0, null);
    }

    public static ObjectAnimator fadeIn(View view, int duration, int delay, final OnCompleteListener listener) {
        view.setVisibility(View.VISIBLE);

        ObjectAnimator a = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), 1.0f);
        a.setDuration(duration);
        a.setInterpolator(new DecelerateInterpolator());
        a.setStartDelay(delay);
        a.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
        a.start();
        return a;
    }
}
