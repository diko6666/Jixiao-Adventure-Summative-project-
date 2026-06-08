package com.sxt.util;
import com.sxt.obj.Enemy;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class StaticValue {
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
    private static final String IMAGE_DIR = "/images/";
    private static BufferedImage readImage(String fileName) throws IOException {
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
        try {
            huoQiu_R.add(readImage("huoqiu1.png"));
            huoQiu_L.add(readImage("huoqiu2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
