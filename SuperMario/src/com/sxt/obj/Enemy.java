package com.sxt.obj;
import com.sxt.util.BackGround;
import com.sxt.util.GameSettings;
import com.sxt.util.StaticValue;
import java.awt.*;
import java.awt.image.BufferedImage;
public class Enemy implements Runnable {
    private int x;
    private int y;
    private int type;
    public boolean face_to = true;
    private BufferedImage show;
    private BackGround bg;
    private int max_up = 0;
    private int max_down = 0;
    private Thread thread = new Thread(this);
    private int image_type = 0;
    public Enemy() {
    }
    public Enemy(int x, int y, boolean face_to, int type, BackGround bg) {
        this.x = x;
        this.y = y;
        this.face_to = face_to;
        this.type = type;
        this.bg = bg;
        show = StaticValue.mogu.get(0);
        thread.start();
    }
    public Enemy(int x, int y, boolean face_to, int type, int max_up, int max_down, BackGround bg) {
        this.x = x;
        this.y = y;
        this.face_to = face_to;
        this.type = type;
        this.max_up = max_up;
        this.max_down = max_down;
        this.bg = bg;
        show = StaticValue.flower.get(0);
        thread.start();
    }
    public void death() {
        if (this.type == 1) {
            show = StaticValue.mogu.get(1);
            this.bg.getEnemyList().remove(this);
        } else if (this.type == 3) {
            type = 4;
        } else if (this.type == 4) {
            show = StaticValue.shell.get(0);
            this.bg.getEnemyList().remove(this);
        } else if (this.type == 2) {
            this.bg.getEnemyList().remove(this);
        }
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public BufferedImage getShow() { return show; }
    public int getType() { return type; }
    public Rectangle getRec() { return new Rectangle(this.x, this.y, 35, 35); }
    @Override
    public void run() {
        while (true) {
            try {
                if (GameSettings.paused) {
                    Thread.sleep(50);
                    continue;
                }
                if (type == 1) {
                    if (face_to) {
                        this.x -= 2;
                    } else {
                        this.x += 2;
                    }
                    image_type = image_type == 1 ? 0 : 1;
                    show = StaticValue.mogu.get(image_type);
                } else if (type == 3) {
                    if (face_to) {
                        this.x -= 2;
                        show = StaticValue.toise_L.get(image_type);
                    } else {
                        this.x += 2;
                        show = StaticValue.toise_R.get(image_type);
                    }
                    image_type = image_type == 1 ? 0 : 1;
                } else if (type == 4) {
                    if (face_to) {
                        this.x -= 5;
                    } else {
                        this.x += 5;
                    }
                    image_type = image_type == 1 ? 0 : 1;
                    show = StaticValue.shell.get(image_type);
                }
                boolean canLeft = true;
                boolean canRight = true;
                for (int i = 0; i < bg.getObstacleList().size(); i++) {
                    Obstacle ob1 = bg.getObstacleList().get(i);
                    if (ob1.getX() == this.x + 36 && (ob1.getY() + 50 > this.y && ob1.getY() - 26 < this.y)) {
                        canRight = false;
                    }
                    if (ob1.getX() == this.x - 36 && (ob1.getY() + 50 > this.y && ob1.getY() - 26 < this.y)) {
                        canLeft = false;
                    }
                }
                if ((face_to && !canLeft) || this.x == 20) {
                    face_to = false;
                } else if ((!face_to && !canRight) || this.x == 754) {
                    face_to = true;
                }
                if (type == 2) {
                    if (face_to) {
                        this.y -= 2;
                    } else {
                        this.y += 2;
                    }
                    image_type = image_type == 1 ? 0 : 1;
                    if (face_to && (this.y == max_up)) {
                        face_to = false;
                    }
                    if (!face_to && (this.y == max_down)) {
                        face_to = true;
                    }
                    show = StaticValue.flower.get(image_type);
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
