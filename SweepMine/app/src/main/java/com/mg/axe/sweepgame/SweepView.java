package com.mg.axe.sweepgame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by Axe on 2017/8/6.
 */

public class SweepView extends ViewGroup implements OnClickListener {

    /**
     * 默认10行10列
     */
    private final int DEFAULT_ROWS = 10;
    private final int DEFAULT_LINES = 10;

    private int screenWidth = 0;
    private int screenheight = 0;

    private int squareMagin = 4;

    /**
     * 默认10个地雷
     */
    private final int DEFAULT_MINES = 10;

    /**
     * 地雷的个数
     */
    private int mines;

    /**
     * 所有的集合
     */
    private SweepBean[][] sweepBeens;

    /**
     * 地雷保存的下标 （用set防止下标相同）
     */
    private Set<Integer> mineList;

    private int squareWidth = 0;

    private int lines = DEFAULT_LINES;
    private int rows = DEFAULT_ROWS;

    private LayoutParams layoutParams = null;

    public SweepView(Context context) {
        this(context, null);
    }

    public SweepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = Utils.getScreenWidth(context)[0];
        screenheight = Utils.getScreenWidth(context)[1];
        //计算小方块的宽度（根据手机屏幕的宽度来算）
        squareWidth = (screenWidth - (squareMagin * (rows - 1))) / rows;
        layoutParams = new LayoutParams(squareWidth, squareWidth);
        initMines();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(screenWidth, screenWidth);
        //
    }

    private void initMines() {
        sweepBeens = new SweepBean[lines][rows];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                SweepBean sweepBean = new SweepBean();
                TextView textView = new TextView(getContext());
                textView.setOnClickListener(this);
                int left = j * squareWidth + ((j + 1) - 1) * squareMagin;
                int top = i * squareWidth + ((i + 1) - 1) * squareMagin;
                int right = left + squareWidth;
                int bottom = top + squareWidth;
                textView.setLeft(left);
                textView.setTop(top);
                textView.setRight(right);
                textView.setBottom(bottom);
                textView.setBackgroundResource(R.color.colorAccent);
                textView.setLayoutParams(layoutParams);
                sweepBean.setTextView(textView);
                addView(textView);
                sweepBeens[i][j] = sweepBean;
            }
        }
    }

    private void initAll() {
        //初始化所有的方块
        sweepBeens = new SweepBean[10][10];

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (sweepBeens == null) {
            return;
        }
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                SweepBean sweepBean = sweepBeens[i][j];
                TextView textView = sweepBean.getTextView();
                textView.setText("10");
                textView.layout(textView.getLeft(), textView.getTop(), textView.getRight(), textView.getBottom());
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
