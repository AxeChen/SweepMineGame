package com.mg.axe.sweepgame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.Set;

/**
 * Created by Axe on 2017/8/6.
 */

public class SweepView extends ViewGroup {

    /**
     * 默认10行10列
     */
    private final int DEFUALT_ROWS = 10;
    private final int DEFUALT_LINES = 10;

    /**
     * 默认10个地雷
     */
    private final int DEFULAT_MINES = 10;

    /**
     * 地雷的个数
     */
    private int mines;

    /**
     * 所有的集合
     */
    private SweepBean[][] sweepBeen;

    /**
     * 地雷保存的下标 （用set防止下标相同）
     */
    private Set<Integer> mineList;

    public SweepView(Context context) {
        super(context);
    }

    public SweepView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SweepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMines();
    }

    /**
     * 初始化地雷
     **/
    private void initMines() {

    }

    private void initAll() {
        //初始化所有的方块
        sweepBeen = new SweepBean[10][10];

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
