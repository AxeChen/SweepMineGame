package com.axe.sweepgame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private final int DEFAULT_MINES = 1;

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
    }

    private void randomMine() {
        int maxMu = lines * rows;
        while (mineList.size() < 10) {
            mineList.add(random.nextInt(maxMu));
        }
        Log.i("mine count", mineList.size() + "");
    }

    private void initMines() {
        sweepBeens = new SweepBean[lines][rows];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                SweepBean sweepBean = new SweepBean();
                ItemView sweepItemView = new ItemView(getContext());
                sweepItemView.setOnClickListener(this);
                sweepItemView.setViewWidth(squareWidth);

                int left = j * squareWidth + ((j + 1) - 1) * squareMagin;
                int top = i * squareWidth + ((i + 1) - 1) * squareMagin;
                int right = left + squareWidth;
                int bottom = top + squareWidth;
                sweepItemView.setLeft(left);
                sweepItemView.setTop(top);
                sweepItemView.setRight(right);
                sweepItemView.setBottom(bottom);
                sweepItemView.setLayoutParams(layoutParams);
                //设置tag，方便点击时取值
                sweepItemView.setTag(i + "&" + j);
                sweepBean.setItemView(sweepItemView);
                if (mineList.contains(i * rows + j)) {
                    sweepItemView.setMine();
                    sweepBean.setType(SweepBean.TYPE_MINE);
                } else {
                    sweepBean.setType(SweepBean.TYPE_EMPTY);
                }
                sweepItemView.setViewWidth(squareWidth);
                addView(sweepItemView);
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
            bean.getItemView().setNumber(count);
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
                ItemView sweepItemView = sweepBean.getItemView();
                sweepItemView.layout(sweepItemView.getLeft(), sweepItemView.getTop(), sweepItemView.getRight(), sweepItemView.getBottom());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ItemView) {
            String tag = (String) v.getTag();
            String[] split = tag.split("&");
            int lines = Integer.parseInt(split[0]);
            int rows = Integer.parseInt(split[1]);
            SweepBean sweepBean = sweepBeens[lines][rows];
            if (sweepBean != null) {
                if (sweepBean.getType() == SweepBean.TYPE_MINE) {
                    //游戏结束
                    lightAllBoom();

                } else if (sweepBean.getType() == SweepBean.TYPE_EMPTY) {
                    sweepBean.getItemView().setOpen(true);

                    checkEmpty(lines, rows - 1);
                    checkEmpty(lines, rows + 1);
                    checkEmpty(lines + 1, rows);
                    checkEmpty(lines - 1, rows);

                    //算对角线
                    checkEmpty(lines - 1, rows - 1);
                    checkEmpty(lines - 1, rows + 1);
                    checkEmpty(lines + 1, rows + 1);
                    checkEmpty(lines + 1, rows - 1);

                } else {
                    sweepBean.getItemView().setOpen(true);
                }
            }

        }
    }

    /**
     * 检查空格
     *
     * @param lines
     * @param rows
     */
    private SweepBean checkEmpty(int lines, int rows) {
        SweepBean checkBean = null;
        try {
            checkBean = sweepBeens[lines][rows];
            if (checkBean != null && !checkBean.isOpen && checkBean.type!=SweepBean.TYPE_MINE ) {
                if (checkBean.getType() == SweepBean.TYPE_EMPTY) {
                    if (!checkBean.isOpen) {
                        checkBean.isOpen = true;
                        checkBean.getItemView().setOpen(true);
                    }
                } else if (checkBean.getType() == SweepBean.TYPE_NUMBER) {
                    if (!checkBean.isOpen) {
                        checkBean.isOpen = true;
                        checkBean.getItemView().setOpen(true);
                    }
                }

                // 如果是数字就不需要去判断了
                if(checkBean.getType() != SweepBean.TYPE_NUMBER){
                    String tag = (String) checkBean.getItemView().getTag();
                    String[] split = tag.split("&");
                    int line = Integer.parseInt(split[0]);
                    int row = Integer.parseInt(split[1]);
                    checkEmpty(line, row - 1);
                    checkEmpty(line, row + 1);
                    checkEmpty(lines + 1, rows);
                    checkEmpty(lines - 1, rows);

                    checkEmpty(lines - 1, rows - 1);
                    checkEmpty(lines - 1, rows + 1);
                    checkEmpty(lines + 1, rows + 1);
                    checkEmpty(lines + 1, rows - 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return checkBean;


    }

    /**
     * 点燃所有的雷
     */
    private void lightAllBoom() {
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                SweepBean sweepbean = sweepBeens[i][j];
                //去掉所有的点击事件
                sweepbean.getItemView().setOnClickListener(null);
                if (sweepbean.getItemView().getType() == SweepBean.TYPE_MINE) {
                    sweepbean.getItemView().setOpen(true);
                }
            }
        }
    }

    private void checkDuijiaoxian(int line, int row) {
        SweepBean bottomBean = null;
        try {
            bottomBean = sweepBeens[line][row];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bottomBean != null) {
            if (bottomBean.getType() == SweepBean.TYPE_EMPTY) {
                if (!bottomBean.isOpen) {
                    bottomBean.isOpen = true;
                    bottomBean.getItemView().setOpen(true);
                }
            } else if (bottomBean.getType() == SweepBean.TYPE_NUMBER) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.isOpen = true;
                    bottomBean.getItemView().setOpen(true);
                }
            }
        }
    }

    private void clickTop(int line, int row) {
        SweepBean bottomBean = null;
//        for (int i = line-1; i < line; i--) {
        try {
            bottomBean = sweepBeens[line][row];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bottomBean != null) {
            if (bottomBean.getType() == SweepBean.TYPE_EMPTY) {
                //打开这个方块
                clickLeft(line, row - 1);
                clickRight(line, row + 1);
                bottomBean.getItemView().setOpen(true);
                clickTop(line - 1, row);

                //算对角线
                checkDuijiaoxian(line - 1, row - 1);
                checkDuijiaoxian(line - 1, row + 1);
                checkDuijiaoxian(line + 1, row + 1);
                checkDuijiaoxian(line + 1, row - 1);
            } else if (bottomBean.getType() == SweepBean.TYPE_NUMBER) {
                //打开这个方块
                bottomBean.isOpen = true;
                bottomBean.getItemView().setOpen(true);
            }
        }

    }

    private void clickRight(int line, int row) {
        SweepBean bottomBean = null;
//        for (int i = 0; i < rows-row; i++) {
        try {
            bottomBean = sweepBeens[line][row];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bottomBean != null) {
            if (bottomBean.getType() == SweepBean.TYPE_EMPTY) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.isOpen = true;
                    clickTop(line - 1, row);
                    bottomBean.getItemView().setOpen(true);
                    clickBottom(line + 1, row);
//                    bottomBean.getTextView().setText("kai");
                    clickRight(line, row + 1);

                    //算对角线
                    checkDuijiaoxian(line - 1, row - 1);
                    checkDuijiaoxian(line - 1, row + 1);
                    checkDuijiaoxian(line + 1, row + 1);
                    checkDuijiaoxian(line + 1, row - 1);
                }
            } else if (bottomBean.getType() == SweepBean.TYPE_NUMBER) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.getItemView().setOpen(true);
                    bottomBean.isOpen = true;
//                    bottomBean.getTextView().setText("kai");

                }
            }
        }
//            else {
//                break;
//            }

//        }
    }

    private void clickLeft(int line, int row) {
        SweepBean bottomBean = null;
//        for (int i = row-1; i < row; i++) {
        try {
            bottomBean = sweepBeens[line][row];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bottomBean != null) {
            if (bottomBean.getType() == SweepBean.TYPE_EMPTY) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.isOpen = true;
                    bottomBean.getItemView().setOpen(true);
                    clickTop(line - 1, row);
//                    bottomBean.getTextView().setText("kai");
                    clickLeft(line, row - 1);
                    clickBottom(line + 1, row);

                    //算对角线
                    checkDuijiaoxian(line - 1, row - 1);
                    checkDuijiaoxian(line - 1, row + 1);
                    checkDuijiaoxian(line + 1, row + 1);
                    checkDuijiaoxian(line + 1, row - 1);
                }
            } else if (bottomBean.getType() == SweepBean.TYPE_NUMBER) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.getItemView().setOpen(true);
                    bottomBean.isOpen = true;
//                    bottomBean.getTextView().setText("kai");
                }
//                break;
            }
        }
//            else {
//                break;
//            }

//        }
    }


    private void clickBottom(int line, int row) {

        SweepBean bottomBean = null;
//        for (int i = 0; i < lines - i; i++) {
        try {
            bottomBean = sweepBeens[line][row];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bottomBean != null) {
            if (bottomBean.getType() == SweepBean.TYPE_EMPTY) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.getItemView().setOpen(true);
                    bottomBean.isOpen = true;
                    clickRight(line, row + 1);
                    clickBottom(line + 1, row);
                    clickLeft(line, row - 1);
//                    bottomBean.getTextView().setText("kai");

                    //算对角线
                    checkDuijiaoxian(line - 1, row - 1);
                    checkDuijiaoxian(line - 1, row + 1);
                    checkDuijiaoxian(line + 1, row + 1);
                    checkDuijiaoxian(line + 1, row - 1);
                }
            } else if (bottomBean.getType() == SweepBean.TYPE_NUMBER) {
                //打开这个方块
                if (!bottomBean.isOpen) {
                    bottomBean.isOpen = true;
                    bottomBean.getItemView().setOpen(true);
//                    bottomBean.getTextView().setText("kai");
                }
//                break;
            }
        }
//            else {
//                break;
//            }

//        }
    }
}
