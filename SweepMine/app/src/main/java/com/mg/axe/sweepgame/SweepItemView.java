package com.mg.axe.sweepgame;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @Author Zaifeng
 * @Create 2017/8/9 0009
 * @Description Content
 */

public class SweepItemView extends FrameLayout {


    public SweepItemView(@NonNull Context context) {
        this(context, null);
    }

    public SweepItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
