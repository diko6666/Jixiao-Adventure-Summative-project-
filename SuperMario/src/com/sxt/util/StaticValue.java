package com.sxt.util;
import com.sxt.obj.Enemy;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class StaticValue {
    public static final int CLOUD_OBSTACLE_TYPE = 13;
    public static final int CHEST_POWER_UP_TYPE = 3;
    public static BufferedImage bg = null;
    public static BufferedImage bg2 = null;
    public static BufferedImage jump_L = null;
    public static BufferedImage jump_R = null;
    public static BufferedImage stand_L = null;
    public static BufferedImage stand_R = null;
    public static BufferedImage tower = null;
    public static List<BufferedImage> mg = new ArrayList<>();
    public static BufferedImage xh = null;
    public static BufferedImage gan = null;
    public static List<BufferedImage> huoQiu_L = new ArrayList<>();
    public static List<BufferedImage> huoQiu_R = new ArrayList<>();
    public static List<BufferedImage> obstacle = new ArrayList<>();
    public static List<BufferedImage> run_L = new ArrayList<>();
    public static List<BufferedImage> run_R = new ArrayList<>();
    public static List<BufferedImage> mogu = new ArrayList<>();
    public static List<BufferedImage> flower = new ArrayList<>();
    public static List<BufferedImage> toise_L = new ArrayList<>();
    public static List<BufferedImage> toise_R = new ArrayList<>();
    public static List<BufferedImage> shell = new ArrayList<>();
    public static List<BufferedImage> jinBi = new ArrayList<>();
    public static BufferedImage chest = null;
    private static final String IMAGE_DIR = "/images/";
    private static BufferedImage readImage(String fileName) throws IOException {
        // Try the packed game files first, then fall back to the loose project files.
        // So it still works in the app and inside the editor.
        URL resource = StaticValue.class.getResource(IMAGE_DIR + fileName);
        if (resource != null) {
            return ImageIO.read(resource);
        }
        File imageFile = new File(System.getProperty("user.dir"), "src/images/" + fileName);
        if (imageFile.isFile()) {
            return ImageIO.read(imageFile);
        }
        imageFile = new File(System.getProperty("user.dir"), "SuperMario/src/images/" + fileName);
        if (imageFile.isFile()) {
            return ImageIO.read(imageFile);
        }
        imageFile = new File(System.getProperty("user.dir"), "assets/images/" + fileName);
        if (imageFile.isFile()) {
            return ImageIO.read(imageFile);
        }
        throw new IOException("Cannot find image: " + fileName);
    }
    public static void init() {
        // Load the shared pictures once at startup.
        // After that, the rest of the game just grabs them when needed.
        try {
            bg = readImage("bg.png");
            bg2 = readImage("bg2.png");
            stand_L = readImage("s_mario_stand_L.png");
            stand_R = readImage("s_mario_stand_R.png");
            tower = readImage("tower.png");
            gan = readImage("gan.png");
            jump_L = readImage("s_mario_jump1_L.png");
            jump_R = readImage("s_mario_jump1_R.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= 3; i++) {
            try {
                mg.add(readImage("mushroom" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            xh = readImage("xiaohua.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            huoQiu_L.add(readImage("huoqiu2.png"));
            huoQiu_R.add(readImage("huoqiu1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1;i <= 4;i++) {
            try {
                jinBi.add(readImage("jinbi" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 2; i++) {
            try {
                run_L.add(readImage("s_mario_run" + i + "_L.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 2; i++) {
            try {
                run_R.add(readImage("s_mario_run" + i + "_R.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            obstacle.add(readImage("brick.png"));
            obstacle.add(readImage("soil_up.png"));
            obstacle.add(readImage("soil_base.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= 4; i++) {
            try {
                obstacle.add(readImage("pipe" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            obstacle.add(readImage("brick2.png"));
            obstacle.add(readImage("flag.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= 2; i++) {
            try {
                toise_L.add(readImage("Ltortoise" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 2; i++) {
            try {
                toise_R.add(readImage("Rtortoise" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 4; i++) {
            try {
                shell.add(readImage("shell" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 3; i++) {
            try {
                mogu.add(readImage("fungus" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 2; i++) {
            try {
                flower.add(readImage("flower1." + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 4; i++) {
            try {
                obstacle.add(readImage("box1." + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // These two are made in code because the project does not have cloud or chest pictures yet.
        obstacle.add(createCloudBlock());
        chest = createChestImage();
        try {
            huoQiu_R.add(readImage("huoqiu1.png"));
            huoQiu_L.add(readImage("huoqiu2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static BufferedImage createCloudBlock() {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(255, 255, 255, 235));
        g.fillOval(0, 11, 16, 13);
        g.fillOval(7, 5, 17, 18);
        g.fillOval(16, 10, 14, 14);
        g.fillRoundRect(4, 16, 22, 9, 8, 8);
        g.setColor(new Color(160, 215, 245, 180));
        g.drawArc(3, 13, 12, 9, 180, 180);
        g.drawArc(13, 13, 12, 9, 180, 180);
        g.dispose();
        return image;
    }
    private static BufferedImage createChestImage() {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(105, 55, 20));
        g.fillRoundRect(4, 10, 22, 16, 4, 4);
        g.setColor(new Color(205, 115, 35));
        g.fillRoundRect(5, 6, 20, 12, 6, 6);
        g.setColor(new Color(255, 210, 70));
        g.fillRect(6, 15, 18, 3);
        g.fillRect(13, 7, 4, 19);
        g.setColor(new Color(45, 25, 10));
        g.drawRoundRect(4, 6, 22, 20, 4, 4);
        g.setColor(new Color(250, 230, 120));
        g.fillRoundRect(12, 15, 6, 7, 2, 2);
        g.setColor(Color.BLACK);
        g.drawLine(15, 18, 15, 21);
        g.dispose();
        return image;
    }
}
