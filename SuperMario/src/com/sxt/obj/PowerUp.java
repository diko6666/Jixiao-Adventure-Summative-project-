package com.sxt.obj;
import com.sxt.util.BackGround;
import com.sxt.util.GameSettings;
import com.sxt.util.StaticValue;
import java.awt.image.BufferedImage;
public class PowerUp implements Runnable {
    private int x;
    private int y;
    private int type;
    private BufferedImage show;
    private BackGround bg;
    private Thread thread = new Thread(this);
    private int image_type = 0;
    public PowerUp() {
    }
    public PowerUp(int x, int y, int type, BackGround bg) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.bg = bg;
        if (type == StaticValue.CHEST_POWER_UP_TYPE) {
            show = StaticValue.chest;
        } else {
            show = StaticValue.mogu.get(0);
        }
        // Power-ups and coins get their own little loop so they can keep animating.
        thread.start();
    }
    public void eat() {
        // Once Mario grabs it, the item just disappears from the stage.
        this.bg.getPowerUpList().remove(this);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getType() { return type; }
    public BufferedImage getShow() { return show; }
    @Override
    public void run() {
        while (true) {
            try {
                if (GameSettings.paused) {
                    Thread.sleep(50);
                    continue;
                }
                // Mushroom, flower, and coin all have their own tiny animation.
                if (type == 0) {
                    image_type = image_type == 1 ? 0 : 1;
                    show = StaticValue.mg.get(image_type);
                }
                if (type == 1) {
                    show = StaticValue.xh;
                }
                if (type == 2) {
                    image_type = image_type == 1 ? 0 : 1;
                    show = StaticValue.jinBi.get(image_type);
                }
                if (type == StaticValue.CHEST_POWER_UP_TYPE) {
                    show = StaticValue.chest;
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
