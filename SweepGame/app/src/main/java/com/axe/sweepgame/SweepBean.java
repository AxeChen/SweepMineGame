package com.axe.sweepgame;

import android.widget.TextView;

/**
 * Created by Axe on 2017/8/6.
 */

public class SweepBean {

    /**
     * 空的空格（周围没有地雷）
     */
    public static final int TYPE_EMPTY = 0;

    /**
     * 数字（周围可能有一个以上的雷）
     */
    public static final int TYPE_NUMBER = 1;

    /**
     * 有地雷
     */
    public static final int TYPE_MINE = 2;

    public int type;

    public int value;

    public boolean isOpen;

    private ItemView itemView;

    public ItemView getItemView() {
        return itemView;
    }

    public void setItemView(ItemView itemView) {
        this.itemView = itemView;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
