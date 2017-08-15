package com.mg.axe.sweepgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Axe on 2017/8/14.
 */

public class ItemView extends View {

    public static int ITEM_TYPE_EMPTY = 0;
    public static int ITEM_TYPE_NUMBER = 1;
    public static int ITEM_TYPE_MINE = 2;

    private int type = 0;

    private int viewWidth = 0;
    private int number;

    private Paint paint;

    private RectF rectF = new RectF();
    private Rect mTextRect = new Rect();
    private Bitmap bitmap;

    private boolean isOpen = false;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public ItemView(Context context) {
        this(context, null);
    }

    public void setOpen(boolean open) {
        isOpen = open;
        invalidate();
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(50);
        paint.setStrokeWidth(6);
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = viewWidth;
        rectF.bottom = viewWidth;
    }

    public void setNumber(int number) {
        type = ITEM_TYPE_NUMBER;
        this.number = number;
        invalidate();
    }

    public void setMine() {
        type = ITEM_TYPE_MINE;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (type == ITEM_TYPE_EMPTY) {
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.empty);
            }
            canvas.drawBitmap(bitmap, null, rectF, paint);
        } else if (type == ITEM_TYPE_NUMBER) {
            setPaintColor();
            String numberStr = String.valueOf(number);
            paint.getTextBounds(numberStr, 0, numberStr.length(), mTextRect);
            canvas.drawText(numberStr, viewWidth / 2 - mTextRect.width() / 2,
                    viewWidth / 2 + mTextRect.height() / 2, paint);
        } else {
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boom);
            }
            canvas.drawBitmap(bitmap, null, rectF, paint);
        }
//        if (!isOpen) {
//            paint.setColor(Color.WHITE);
//            canvas.drawRect(rectF, paint);
//        }
    }

    private void setPaintColor() {
        switch (number) {
            case 1:
                paint.setColor(Color.BLACK);
                break;
            case 2:
                paint.setColor(Color.GREEN);
                break;
            case 3:
                paint.setColor(Color.BLUE);
                break;
            case 4:
                paint.setColor(Color.parseColor("#DDCEED"));
                break;
            case 5:
                paint.setColor(Color.parseColor("#DDCEED"));
                break;
            case 6:
                paint.setColor(Color.parseColor("#CDC7F5"));
                break;
            case 7:
                paint.setColor(Color.parseColor("#F9DEC3"));
                break;
            case 8:
                paint.setColor(Color.RED);
                break;
        }
    }
}
