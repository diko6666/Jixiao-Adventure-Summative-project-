package com.sxt.obj;
import com.sxt.util.BackGround;
import com.sxt.util.GameSettings;
import com.sxt.util.StaticValue;
import java.awt.*;
import java.awt.image.BufferedImage;
public class Fireball implements Runnable {
    private int x;
    private int y;
    private boolean face_to = true;
    private BufferedImage show;
    private BackGround bg;
    private int image_type = 0;
    private Thread thread = new Thread(this);
    public Fireball(int x, int y, boolean face_to, BackGround bg) {
        this.x = x;
        this.y = y;
        this.face_to = face_to;
        this.bg = bg;
        show = face_to ? StaticValue.huoQiu_L.get(0) : StaticValue.huoQiu_R.get(0);
        thread.start();
    }
    @Override
    public void run() {
        while (true) {
            try {
                if (GameSettings.paused) {
                    Thread.sleep(50);
                    continue;
                }
                if (face_to) {
                    this.x -= 10;
                    show = StaticValue.huoQiu_L.get(image_type);
                } else {
                    this.x += 10;
                    show = StaticValue.huoQiu_R.get(image_type);
                }
                image_type = image_type == 1 ? 0 : 1;
                for (int i = 0; i < bg.getObstacleList().size(); i++) {
                    Obstacle ob1 = bg.getObstacleList().get(i);
                    if (this.getRec().intersects(ob1.getRec())) {
                        this.y = 900;
                        this.bg.getFireballList().remove(this);
                    }
                }
                for (int i = 0; i < bg.getEnemyList().size(); i++) {
                    Enemy enemy = bg.getEnemyList().get(i);
                    if (this.getRec().intersects(enemy.getRec())) {
                        this.y = 900;
                        this.bg.getFireballList().remove(this);
                        enemy.death();
                    }
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public boolean isFace_to() { return face_to; }
    public void setFace_to(boolean face_to) { this.face_to = face_to; }
    public BufferedImage getShow() { return show; }
    public void setShow(BufferedImage show) { this.show = show; }
    public BackGround getBg() { return bg; }
    public void setBg(BackGround bg) { this.bg = bg; }
    public Rectangle getRec() { return new Rectangle(this.x, this.y, 15, 15); }
}
