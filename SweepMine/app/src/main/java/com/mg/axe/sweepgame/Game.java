package com.mg.axe.sweepgame;

/**
 * Created by Axe on 2017/8/6.
 */

public interface Game {

    /**
     * 开始游戏
     */
    public void startGame();

    /**
     * 暂停游戏
     */
    public void pauseGame();

    /**
     * 停止游戏
     */
    public void stopGame();

    /**
     * 游戏胜利
     */
    public void gameWin();

    /**
     * 游戏失败
     */
    public void gameOver();
}
