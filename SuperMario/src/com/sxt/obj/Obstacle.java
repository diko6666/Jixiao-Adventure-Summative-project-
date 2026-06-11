package com.sxt.obj;
import com.sxt.util.BackGround;
import com.sxt.util.GameSettings;
import com.sxt.util.StaticValue;
import java.awt.*;
import java.awt.image.BufferedImage;
public class Obstacle implements Runnable {
    private int x;
    private int y;
    private int type;
    private BufferedImage show = null;
    private BackGround bg = null;
    private Thread thread = new Thread(this);
    public Obstacle(int x, int y, int type, BackGround bg) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.bg = bg;
        show = StaticValue.obstacle.get(type);
        // Only the moving pipe needs its own loop.
        if (type == 8) {
            thread.start();
        }
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    public BufferedImage getShow() { return show; }
    @Override
    public void run() {
        while (true) {
            try {
                if (GameSettings.paused) {
                    Thread.sleep(50);
                    continue;
                }
                // When the level is cleared, the pipe sinks down to match the ending scene.
                if (this.bg.isReach()) {
                    if (this.y < 374) {
                        this.y += 5;
                    } else {
                        this.bg.setBase(true);
                    }
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public Rectangle getRec() {
        return new Rectangle(this.x, this.y, 30, 30);
    }
}
