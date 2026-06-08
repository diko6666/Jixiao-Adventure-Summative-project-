package com.sxt;
import com.sxt.menu.GameMenu;
import com.sxt.menu.GameOverScreen;
import com.sxt.obj.*;
import com.sxt.util.BackGround;
import com.sxt.util.GameSettings;
import com.sxt.util.Music;
import com.sxt.util.StaticValue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
public class MyFrame extends JFrame implements KeyListener, Runnable {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private enum GameState { MENU, PLAYING, PAUSED, GAME_OVER, VICTORY }
    private GameState state = GameState.MENU;
    private final GameMenu menu = new GameMenu();
    private final GameOverScreen gameOverScreen = new GameOverScreen();
    private GameOverScreen.Type endScreenType = GameOverScreen.Type.GAME_OVER;
    private int finalScore = 0;
    private boolean musicStarted = false;
    private List<BackGround> allBg = new ArrayList<>();
    private BackGround nowBg = new BackGround();
    private Image offScreenImage = null;
    private Mario mario = new Mario();
    private Thread thread = new Thread(this);
    private int pauseSelection = 0;
    private final Rectangle resumeButton = new Rectangle();
    private final Rectangle quitButton = new Rectangle();
    private boolean leftHeld = false;
    private boolean rightHeld = false;
    public MyFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.addKeyListener(this);
        this.setTitle("Jixiao Adventure");
        this.setFocusable(true);
        this.requestFocusInWindow();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
        StaticValue.init();
        menu.setOnStartGame(this::startGame);
        gameOverScreen.setOnRetry(this::retryGame);
        gameOverScreen.setOnMenu(this::returnToMenu);
        thread.start();
        repaint();
    }
    private void handleMouseClick(int x, int y) {
        if (state == GameState.MENU) {
            menu.mouseClicked(x, y);
            repaint();
        } else if (state == GameState.PAUSED) {
            if (resumeButton.contains(x, y)) {
                resume();
            } else if (quitButton.contains(x, y)) {
                returnToMenu();
            }
        } else if (state == GameState.GAME_OVER || state == GameState.VICTORY) {
            gameOverScreen.mouseClicked(x, y);
            repaint();
        }
    }
    private void pause() {
        state = GameState.PAUSED;
        GameSettings.paused = true;
        pauseSelection = 0;
        repaint();
    }
    private void resume() {
        state = GameState.PLAYING;
        GameSettings.paused = false;
        repaint();
    }
    private void startGame() {
        if (!musicStarted && GameSettings.musicEnabled) {
            new Music();
            musicStarted = true;
        }
        resetLevelState();
        state = GameState.PLAYING;
        repaint();
    }
    private void retryGame() {
        resetLevelState();
        state = GameState.PLAYING;
        gameOverScreen.resetSelection();
        repaint();
    }
    private void returnToMenu() {
        GameSettings.paused = false;
        leftHeld = false;
        rightHeld = false;
        state = GameState.MENU;
        menu.setScreen(GameMenu.Screen.MAIN);
        gameOverScreen.resetSelection();
        repaint();
    }
    private void resetLevelState() {
        leftHeld = false;
        rightHeld = false;
        mario = new Mario(10, 355, WIDTH, HEIGHT);
        allBg.clear();
        for (int i = 1; i <= 3; i++) {
            allBg.add(new BackGround(i, i == 3));
        }
        nowBg = allBg.get(0);
        mario.setBackGround(nowBg);
    }
    private void showGameOver() {
        finalScore = mario.getScore();
        endScreenType = GameOverScreen.Type.GAME_OVER;
        state = GameState.GAME_OVER;
        gameOverScreen.resetSelection();
    }
    private void showVictory() {
        finalScore = mario.getScore();
        endScreenType = GameOverScreen.Type.VICTORY;
        state = GameState.VICTORY;
        gameOverScreen.resetSelection();
    }
    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(WIDTH, HEIGHT);
        }
        Graphics graphics = offScreenImage.getGraphics();
        if (state == GameState.MENU) {
            menu.paint(graphics, WIDTH, HEIGHT);
            g.drawImage(offScreenImage, 0, 0, this);
            graphics.dispose();
            return;
        }
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.drawImage(nowBg.getBgImage(), 0, 0, this);
        for (Enemy e : nowBg.getEnemyList()) {
            graphics.drawImage(e.getShow(), e.getX(), e.getY(), this);
        }
        for (PowerUp d : nowBg.getPowerUpList()) {
            graphics.drawImage(d.getShow(), d.getX(), d.getY(), this);
        }
        for (Obstacle ob : nowBg.getObstacleList()) {
            graphics.drawImage(ob.getShow(), ob.getX(), ob.getY(), this);
        }
        graphics.drawImage(nowBg.getTower(), 620, 270, this);
        graphics.drawImage(nowBg.getGan(), 500, 220, this);
        for (Fireball hq : nowBg.getFireballList()) {
            graphics.drawImage(hq.getShow(), hq.getX(), hq.getY(), this);
        }
        if (!mario.isBig()) {
            graphics.drawImage(mario.getShow(), mario.getX(), mario.getY(), 25, 25, this);
        } else {
            graphics.drawImage(mario.getShow(), mario.getX(), mario.getY() - 15, 28, 40, this);
        }
        Color c = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 25));
        graphics.drawString("Score: " + mario.getScore(), 300, 100);
        graphics.setColor(c);
        if (state == GameState.PAUSED) {
            Graphics2D g2d = (Graphics2D) graphics.create();
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            g2d.dispose();
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.BOLD, 48));
            String pauseText = "PAUSED";
            int tw = graphics.getFontMetrics().stringWidth(pauseText);
            graphics.drawString(pauseText, (WIDTH - tw) / 2, 200);
            int btnW = 200;
            int btnH = 50;
            int centerX = WIDTH / 2;
            resumeButton.setBounds(centerX - btnW / 2, 280, btnW, btnH);
            quitButton.setBounds(centerX - btnW / 2, 350, btnW, btnH);
            graphics.setFont(new Font("Arial", Font.BOLD, 24));
            if (pauseSelection == 0) {
                graphics.setColor(new Color(255, 215, 0));
                graphics.fillRoundRect(resumeButton.x, resumeButton.y, resumeButton.width, resumeButton.height, 10, 10);
                graphics.setColor(Color.BLACK);
            } else {
                graphics.setColor(new Color(100, 100, 100));
                graphics.fillRoundRect(resumeButton.x, resumeButton.y, resumeButton.width, resumeButton.height, 10, 10);
                graphics.setColor(Color.WHITE);
            }
            String resumeText = "RESUME";
            int rtw = graphics.getFontMetrics().stringWidth(resumeText);
            graphics.drawString(resumeText, centerX - rtw / 2, resumeButton.y + 33);
            if (pauseSelection == 1) {
                graphics.setColor(new Color(255, 215, 0));
                graphics.fillRoundRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height, 10, 10);
                graphics.setColor(Color.BLACK);
            } else {
                graphics.setColor(new Color(100, 100, 100));
                graphics.fillRoundRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height, 10, 10);
                graphics.setColor(Color.WHITE);
            }
            String quitText = "QUIT TO MENU";
            int qtw = graphics.getFontMetrics().stringWidth(quitText);
            graphics.drawString(quitText, centerX - qtw / 2, quitButton.y + 33);
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.PLAIN, 14));
            String hint = "ESC to resume  |  UP/DOWN + ENTER to select";
            int htw = graphics.getFontMetrics().stringWidth(hint);
            graphics.drawString(hint, (WIDTH - htw) / 2, 450);
        }
        if (state == GameState.GAME_OVER || state == GameState.VICTORY) {
            gameOverScreen.paint(graphics, WIDTH, HEIGHT, endScreenType, finalScore);
        }
        g.drawImage(offScreenImage, 0, 0, this);
        graphics.dispose();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new);
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (state == GameState.MENU) {
            menu.keyPressed(e);
            repaint();
            return;
        }
        if (state == GameState.PAUSED) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE || pauseSelection == 0) {
                    resume();
                } else {
                    returnToMenu();
                }
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                pauseSelection = 1 - pauseSelection;
                repaint();
            }
            return;
        }
        if (state == GameState.GAME_OVER || state == GameState.VICTORY) {
            gameOverScreen.keyPressed(e);
            repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            pause();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightHeld = true;
            mario.rightMove();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftHeld = true;
            mario.leftMove();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            mario.jump();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            mario.fire();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (state != GameState.PLAYING) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftHeld = false;
            if (rightHeld) {
                mario.rightMove();
            } else {
                mario.leftStop();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightHeld = false;
            if (leftHeld) {
                mario.leftMove();
            } else {
                mario.rightStop();
            }
        }
    }
    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(state == GameState.PLAYING ? 50 : 100);
                if (state != GameState.PLAYING) {
                    continue;
                }
                if (mario.getX() >= 775) {
                    nowBg = allBg.get(nowBg.getSort());
                    mario.setBackGround(nowBg);
                    mario.setX(10);
                    mario.setY(355);
                }
                if (mario.isDeath()) {
                    showGameOver();
                }
                if (mario.isOK()) {
                    showVictory();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
