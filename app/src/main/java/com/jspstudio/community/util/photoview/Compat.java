package com.jspstudio.community.util.photoview;

import android.view.View;

public class Compat {

    public static void postOnAnimation(View view, Runnable runnable) {
        view.postOnAnimation(runnable);
    }



}
