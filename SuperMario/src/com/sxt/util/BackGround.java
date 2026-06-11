package com.sxt.util;
import com.sxt.obj.PowerUp;
import com.sxt.obj.Enemy;
import com.sxt.obj.Fireball;
import com.sxt.obj.Obstacle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
public class BackGround {
    private BufferedImage bgImage = null;
    private int sort;
    private boolean flag;
    private List<Obstacle> obstacleList = new ArrayList<>();
    private List<Enemy> enemyList = new ArrayList<>();
    private List<PowerUp> powerUpList = new ArrayList<>();
    private List<Fireball> fireballList = new ArrayList<>();
    private BufferedImage gan = null;
    private BufferedImage tower = null;
    private boolean isReach = false;
    private boolean isBase = false;
    public BackGround() {
    }
    public BackGround(int sort, boolean flag) {
        this.sort = sort;
        this.flag = flag;
        if (flag) {
            bgImage = StaticValue.bg2;
        } else {
            bgImage = StaticValue.bg;
        }
        if (sort == 1) {
            for (int i = 0; i < 27; i++) {
                obstacleList.add(new Obstacle(i * 30, 420, 1, this));
            }
            for (int j = 0; j <= 120; j += 30) {
                for (int i = 0; i < 27; i++) {
                    obstacleList.add(new Obstacle(i * 30, 570 - j, 2, this));
                }
            }
            for (int i = 120; i <= 150; i += 30) {
                obstacleList.add(new Obstacle(i, 300, 7, this));
            }
            obstacleList.add(new Obstacle(180, 300, 9, this));
            obstacleList.add(new Obstacle(210, 300, 7, this));
            for (int i = 300; i <= 570; i += 30) {
                if (i == 360 || i == 390 || i == 480 || i == 510 || i == 540) {
                    obstacleList.add(new Obstacle(i, 300, 7, this));
                } else {
                    obstacleList.add(new Obstacle(i, 300, 0, this));
                }
            }
            for (int i = 420; i <= 450; i += 30) {
                obstacleList.add(new Obstacle(i, 240, 7, this));
            }
            for (int i = 360; i <= 600; i += 25) {
                if (i == 360) {
                    obstacleList.add(new Obstacle(620, i, 3, this));
                    obstacleList.add(new Obstacle(645, i, 4, this));
                } else {
                    obstacleList.add(new Obstacle(620, i, 5, this));
                    obstacleList.add(new Obstacle(645, i, 6, this));
                }
            }
            enemyList.add(new Enemy(670, 385, true, 1, this));
            enemyList.add(new Enemy(635, 420, true, 2, 328, 428, this));
            enemyList.add(new Enemy(580, 385, true, 3, this));
        }
        if (sort == 2) {
            for (int i = 0; i < 27; i++) {
                obstacleList.add(new Obstacle(i * 30, 420, 1, this));
            }
            for (int j = 0; j <= 120; j += 30) {
                for (int i = 0; i < 27; i++) {
                    obstacleList.add(new Obstacle(i * 30, 570 - j, 2, this));
                }
            }
            obstacleList.add(new Obstacle(210, 300, 9, this));
            for (int i = 360; i <= 600; i += 25) {
                if (i == 360) {
                    obstacleList.add(new Obstacle(60, i, 3, this));
                    obstacleList.add(new Obstacle(85, i, 4, this));
                } else {
                    obstacleList.add(new Obstacle(60, i, 5, this));
                    obstacleList.add(new Obstacle(85, i, 6, this));
                }
            }
            for (int i = 330; i <= 600; i += 25) {
                if (i == 330) {
                    obstacleList.add(new Obstacle(620, i, 3, this));
                    obstacleList.add(new Obstacle(645, i, 4, this));
                } else {
                    obstacleList.add(new Obstacle(620, i, 5, this));
                    obstacleList.add(new Obstacle(645, i, 6, this));
                }
            }
            obstacleList.add(new Obstacle(300, 330, 0, this));
            for (int i = 270; i <= 330; i += 30) {
                if (i == 270 || i == 330) {
                    obstacleList.add(new Obstacle(i, 360, 0, this));
                } else {
                    obstacleList.add(new Obstacle(i, 360, 7, this));
                }
            }
            for (int i = 240; i <= 360; i += 30) {
                if (i == 240 || i == 360) {
                    obstacleList.add(new Obstacle(i, 390, 0, this));
                } else {
                    obstacleList.add(new Obstacle(i, 390, 7, this));
                }
            }
            obstacleList.add(new Obstacle(240, 300, 0, this));
            for (int i = 360; i <= 540; i += 60) {
                obstacleList.add(new Obstacle(i, 270, 7, this));
            }
            enemyList.add(new Enemy(75, 420, true, 2, 328, 418, this));
            enemyList.add(new Enemy(635, 420, true, 2, 298, 388, this));
            enemyList.add(new Enemy(200, 385, true, 1, this));
            enemyList.add(new Enemy(500, 385, true, 1, this));
            enemyList.add(new Enemy(220, 385, true, 3, this));
        }
        if (sort == 3) {
            for (int i = 0; i < 27; i++) {
                obstacleList.add(new Obstacle(i * 30, 420, 1, this));
            }
            for (int j = 0; j <= 120; j += 30) {
                for (int i = 0; i < 27; i++) {
                    obstacleList.add(new Obstacle(i * 30, 570 - j, 2, this));
                }
            }
            int temp = 290;
            for (int i = 390; i >= 270; i -= 30) {
                for (int j = temp; j <= 410; j += 30) {
                    obstacleList.add(new Obstacle(j, i, 7, this));
                }
                temp += 30;
            }
            temp = 60;
            for (int i = 390; i >= 360; i -= 30) {
                for (int j = temp; j <= 90; j += 30) {
                    obstacleList.add(new Obstacle(j, i, 7, this));
                }
                temp += 30;
            }
            // Bonus cloud for stage 3. Jump up here and grab the chest for extra score.
            for (int i = 150; i <= 270; i += 30) {
                obstacleList.add(new Obstacle(i, 300, StaticValue.CLOUD_OBSTACLE_TYPE, this));
            }
            powerUpList.add(new PowerUp(210, 270, StaticValue.CHEST_POWER_UP_TYPE, this));
            gan = StaticValue.gan;
            tower = StaticValue.tower;
            obstacleList.add(new Obstacle(515, 220, 8, this));
            enemyList.add(new Enemy(150, 385, true, 1, this));
        }
    }
    public BufferedImage getBgImage() {
        return bgImage;
    }
    public int getSort() {
        return sort;
    }
    public boolean isFlag() {
        return flag;
    }
    public List<Obstacle> getObstacleList() {
        return obstacleList;
    }
    public BufferedImage getGan() {
        return gan;
    }
    public BufferedImage getTower() {
        return tower;
    }
    public boolean isReach() {
        return isReach;
    }
    public void setReach(boolean reach) {
        isReach = reach;
    }
    public boolean isBase() {
        return isBase;
    }
    public void setBase(boolean base) {
        isBase = base;
    }
    public List<Enemy> getEnemyList() {
        return enemyList;
    }
    public List<PowerUp> getPowerUpList() {
        return powerUpList;
    }
    public List<Fireball> getFireballList() {
        return fireballList;
    }
}
