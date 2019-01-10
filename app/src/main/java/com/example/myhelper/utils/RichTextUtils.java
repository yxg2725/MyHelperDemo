package com.example.myhelper.utils;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by Administrator on 2019/1/10.
 */

public class RichTextUtils {

    public static void settext(TextView textView,String html){
//        String html="<font color='green'>已完成</font> <br>";
        CharSequence charSequence= Html.fromHtml(html);
        textView.setText(charSequence);
        //该语句在设置后必加，不然没有任何效果
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
