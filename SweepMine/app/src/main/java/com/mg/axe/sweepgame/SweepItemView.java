package com.mg.axe.sweepgame;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Author Zaifeng
 * @Create 2017/8/9 0009
 * @Description Content
 */

public class SweepItemView extends FrameLayout {

    private TextView number;
    private ImageView imageView;

    public SweepItemView(@NonNull Context context) {
        this(context, null);
    }

    public SweepItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.sweep_item_view, this);
        number = (TextView) view.findViewById(R.id.tvNum);
        imageView = (ImageView) view.findViewById(R.id.ivMine);
    }

    private int viewWidth = 0;
    private int viewHeight = 0;

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewWidth,viewWidth);
    }

    /**
     * 展示数字
     *
     * @param count
     */
    public void showNumber(int count) {
        imageView.setVisibility(View.GONE);
        number.setVisibility(View.VISIBLE);
        number.setText(count + "");
    }

    /**
     * 展示地雷
     */
    public void showMine() {
        imageView.setVisibility(View.VISIBLE);
        number.setVisibility(View.GONE);
    }

    /**
     * 展示空白
     */
    public void showEmpty() {
        imageView.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
    }


}
