package com.mg.axe.sweepgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;
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
    private Set<Integer> mineList = new HashSet<>();

    private int squareWidth = 0;

    private int lines = DEFAULT_LINES;
    private int rows = DEFAULT_ROWS;

    private Random random = new Random();

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
        randomMine();
        initMines();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(screenWidth, screenWidth);
        //
    }

    private void randomMine() {
        int maxMu = lines * rows;
        while (mineList.size() < 15) {
            mineList.add(random.nextInt(maxMu));
        }
        Log.i("mine count", mineList.size() + "");
    }

    private void initMines() {
        sweepBeens = new SweepBean[lines][rows];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                SweepBean sweepBean = new SweepBean();
                TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
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
                //设置tag，方便点击时取值
                textView.setTag(i + "&" + j);
                sweepBean.setTextView(textView);
                if (mineList.contains(i * rows + j)) {
                    textView.setText("雷");
                    sweepBean.setType(SweepBean.TYPE_MINE);
                } else {
                    sweepBean.setType(SweepBean.TYPE_EMPTY);
                }
                addView(textView);
                sweepBeens[i][j] = sweepBean;
            }
        }
        setSquareNum();
    }

    public void setSquareNum() {
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                SweepBean sweepBean = sweepBeens[i][j];
                if (sweepBean.getType() != SweepBean.TYPE_MINE) {
                    setSquareNum(sweepBean, i, j);
                }
            }
        }
    }

    public void setSquareNum(SweepBean bean, int lines, int rows) {
        int count = checkRoundMineCount(lines, rows + 1)
                + checkRoundMineCount(lines, rows - 1)
                + checkRoundMineCount(lines - 1, rows)
                + checkRoundMineCount(lines - 1, rows + 1)
                + checkRoundMineCount(lines - 1, rows - 1)
                + checkRoundMineCount(lines + 1, rows)
                + checkRoundMineCount(lines + 1, rows + 1)
                + checkRoundMineCount(lines + 1, rows - 1);
        bean.setValue(count);
        if (count > 0) {
            bean.setType(SweepBean.TYPE_NUMBER);
            bean.getTextView().setText(count + "");
        }
    }

    private int checkRoundMineCount(int lines, int rows) {
        SweepBean sweepBean = null;
        try {
            sweepBean = sweepBeens[lines][rows];
            if (sweepBean.getType() == SweepBean.TYPE_MINE) {
                return 1;
            } else {
                return 0;
            }
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
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
                textView.layout(textView.getLeft(), textView.getTop(), textView.getRight(), textView.getBottom());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView textView = (TextView) v;
            String tag = (String) v.getTag();
            String[] split = tag.split("&");
            int lines = Integer.parseInt(split[0]);
            int rows = Integer.parseInt(split[1]);
            SweepBean sweepBean = sweepBeens[lines][rows];
            if (sweepBean != null) {
                if (sweepBean.getType() == SweepBean.TYPE_MINE) {
                    Toast.makeText(getContext(), "你点中了雷", Toast.LENGTH_SHORT).show();
                } else if (sweepBean.getType() == SweepBean.TYPE_EMPTY) {
                    Toast.makeText(getContext(), "你点中了空格", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getContext(), "你点中了" + sweepBean.getValue(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
