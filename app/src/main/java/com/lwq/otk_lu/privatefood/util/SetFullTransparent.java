/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.util;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SetFullTransparent {
    public SetFullTransparent(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
