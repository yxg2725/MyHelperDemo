package com.example.myhelper.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Administrator on 2019/1/8.
 */

public class AnimationUtils {

    private static ObjectAnimator animator;
    private static HashMap<Integer,ObjectAnimator> cacheAnimator = new HashMap<>();

    public static void rotation(View view, int position,float start, float end, final OnAnimationEndListener listener){
        if (cacheAnimator.get(position) == null){
            animator = ObjectAnimator.ofFloat(view, "rotation", start, end);
            cacheAnimator.put(position,animator);
        }else {
            animator = cacheAnimator.get(position);
        }


        animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null){
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    public  static void reset(int position){
        if (cacheAnimator.size()>=position){
            ObjectAnimator objectAnimator = cacheAnimator.get(position);
            if (objectAnimator != null){
                animator.cancel();
            }
        }


    }

    public interface OnAnimationEndListener{
        void onAnimationEnd();
    }

}
