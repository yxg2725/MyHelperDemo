package com.example.myhelper.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2019/1/11.
 */

public class InputMethodUtils {
    /**
     *View  获取焦点的View
     */

    public static void hintKeyBoard(Context context, View view) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && view != null) {
            //拿到view的token 不为空
            if (view.getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
