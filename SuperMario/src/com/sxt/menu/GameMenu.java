package com.sxt.menu;
import com.sxt.util.GameSettings;
import com.sxt.util.Music;
import com.sxt.util.StaticValue;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
public class GameMenu {
    public enum Screen { MAIN, SETTINGS, TUTORIAL }
    private static final String[] MAIN_LABELS = {"START", "SETTINGS", "TUTORIALS"};
    private static final String[] TUTORIAL_LINES = {
            "CONTROLS",
            "  Left / Right  -  Move Mario",
            "  Up            -  Jump",
            "  Space         -  Fireball (big Mario)",
            "",
            "TIPS",
            "  Jump on enemies from above to defeat them",
            "  Hit ? blocks for coins and power-ups",
            "  Reach the flag at the end of level 3"
    };
    private Screen screen = Screen.MAIN;
    private int selectedIndex = 0;
    private int tutorialScroll = 0;
    private int marioAnimFrame = 0;
    private long lastAnimTime = 0;
    private final Rectangle[] mainButtonBounds = new Rectangle[3];
    private final Rectangle backButtonBounds = new Rectangle();
    private final Rectangle musicToggleBounds = new Rectangle();
    private Runnable onStartGame;
    public void setOnStartGame(Runnable onStartGame) {
        this.onStartGame = onStartGame;
    }
    public Screen getScreen() {
        return screen;
    }
    public void setScreen(Screen screen) {
        this.screen = screen;
        selectedIndex = 0;
        tutorialScroll = 0;
    }
    public void paint(Graphics g, int width, int height) {
        drawBackground(g, width, height);
        switch (screen) {
            case MAIN:
                drawMainMenu(g, width, height);
                break;
            case SETTINGS:
                drawSettings(g, width, height);
                break;
            case TUTORIAL:
                drawTutorial(g, width, height);
                break;
        }
    }
    private void drawBackground(Graphics g, int width, int height) {
        if (StaticValue.bg != null) {
            g.drawImage(StaticValue.bg, 0, 0, width, height, null);
        } else {
            g.setColor(new Color(92, 148, 252));
            g.fillRect(0, 0, width, height);
        }
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, width, height);
    }
    private void drawMainMenu(Graphics g, int width, int height) {
        drawTitle(g, width);
        drawDecorations(g, width, height);
        int btnW = 280;
        int btnH = 52;
        int startY = 260;
        int gap = 18;
        int centerX = width / 2;
        for (int i = 0; i < MAIN_LABELS.length; i++) {
            int x = centerX - btnW / 2;
            int y = startY + i * (btnH + gap);
            mainButtonBounds[i] = new Rectangle(x, y, btnW, btnH);
            boolean selected = i == selectedIndex;
            drawBrickButton(g, mainButtonBounds[i], MAIN_LABELS[i], selected);
        }
        drawMarioPreview(g, 80, height - 120);
        g.setColor(new Color(255, 255, 255, 200));
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("UP/DOWN + ENTER  or  CLICK", centerX - 95, height - 24);
    }
    private void drawTitle(Graphics g, int width) {
        String title = "JIXIAO";
        String subtitle = "ADVENTURE";
        g.setFont(new Font("Arial", Font.BOLD, 48));
        int titleW = g.getFontMetrics().stringWidth(title);
        drawOutlinedText(g, title, (width - titleW) / 2, 95, new Color(228, 56, 56), Color.WHITE, 3);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        int subW = g.getFontMetrics().stringWidth(subtitle);
        drawOutlinedText(g, subtitle, (width - subW) / 2, 130, new Color(255, 214, 0), new Color(80, 40, 0), 2);
    }
    private void drawDecorations(Graphics g, int width, int height) {
        if (StaticValue.tower != null) {
            g.drawImage(StaticValue.tower, width - 150, height - 200, 120, 120, null);
        }
        if (StaticValue.mg != null && !StaticValue.mg.isEmpty()) {
            g.drawImage(StaticValue.mg.get(0), 40, height - 90, 36, 36, null);
        }
        if (StaticValue.jinBi != null && StaticValue.jinBi.size() > 1) {
            g.drawImage(StaticValue.jinBi.get(1), width - 80, 160, 28, 28, null);
        }
        if (StaticValue.xh != null) {
            g.drawImage(StaticValue.xh, 120, 180, 32, 32, null);
        }
    }
    private void drawMarioPreview(Graphics g, int x, int y) {
        BufferedImage mario = StaticValue.stand_R;
        if (StaticValue.run_R != null && StaticValue.run_R.size() >= 2) {
            long now = System.currentTimeMillis();
            if (now - lastAnimTime > 200) {
                marioAnimFrame = (marioAnimFrame + 1) % 2;
                lastAnimTime = now;
            }
            mario = StaticValue.run_R.get(marioAnimFrame);
        }
        if (mario != null) {
            g.drawImage(mario, x, y, 48, 48, null);
        }
    }
    private void drawBrickButton(Graphics g, Rectangle bounds, String label, boolean selected) {
        BufferedImage brick = pickBrickTile();
        if (brick != null) {
            int tile = 32;
            for (int ty = bounds.y; ty < bounds.y + bounds.height; ty += tile) {
                for (int tx = bounds.x; tx < bounds.x + bounds.width; tx += tile) {
                    g.drawImage(brick, tx, ty, Math.min(tile, bounds.x + bounds.width - tx),
                            Math.min(tile, bounds.y + bounds.height - ty), null);
                }
            }
        } else {
            g.setColor(new Color(180, 90, 40));
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 8, 8);
        }
        if (selected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(255, 230, 80));
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4, 10, 10);
            g2.dispose();
        } else {
            g.setColor(new Color(60, 30, 10));
            g.drawRoundRect(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4, 10, 10);
        }
        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        int textX = bounds.x + (bounds.width - fm.stringWidth(label)) / 2;
        int textY = bounds.y + (bounds.height + fm.getAscent() - fm.getDescent()) / 2;
        drawOutlinedText(g, label, textX, textY, Color.WHITE, Color.BLACK, 2);
    }
    private BufferedImage pickBrickTile() {
        if (StaticValue.obstacle != null && StaticValue.obstacle.size() > 1) {
            return StaticValue.obstacle.get(1);
        }
        if (StaticValue.obstacle != null && !StaticValue.obstacle.isEmpty()) {
            return StaticValue.obstacle.get(0);
        }
        return null;
    }
    private void drawSettings(Graphics g, int width, int height) {
        drawPanel(g, width, height, "SETTINGS");
        int panelX = 140;
        int panelY = 200;
        int panelW = width - 280;
        int panelH = 200;
        g.setColor(new Color(255, 255, 255, 30));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 16, 16);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Background Music", panelX + 40, panelY + 55);
        String toggleLabel = GameSettings.musicEnabled ? "ON" : "OFF";
        musicToggleBounds.setBounds(panelX + panelW - 130, panelY + 30, 90, 40);
        drawBrickButton(g, musicToggleBounds, toggleLabel, selectedIndex == 0);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(new Color(220, 220, 220));
        g.drawString("Applies when you start a new game", panelX + 40, panelY + 100);
        drawBackButton(g, width, height);
    }
    private void drawTutorial(Graphics g, int width, int height) {
        drawPanel(g, width, height, "TUTORIALS");
        int panelX = 80;
        int panelY = 175;
        int panelW = width - 160;
        int panelH = 340;
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g.setColor(new Color(255, 214, 0));
        g.drawRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        int lineHeight = 22;
        int y = panelY + 28 - tutorialScroll * lineHeight;
        int visibleBottom = panelY + panelH - 10;
        for (String line : TUTORIAL_LINES) {
            if (y > panelY + 10 && y < visibleBottom) {
                if (line.startsWith("CONTROLS") || line.startsWith("TIPS")) {
                    g.setFont(new Font("Arial", Font.BOLD, 16));
                    g.setColor(new Color(255, 214, 0));
                    g.drawString(line, panelX + 24, y);
                    g.setFont(new Font("Monospaced", Font.PLAIN, 15));
                    g.setColor(Color.WHITE);
                } else {
                    g.drawString(line, panelX + 24, y);
                }
            }
            y += lineHeight;
        }
        int maxScroll = Math.max(0, TUTORIAL_LINES.length * lineHeight - panelH + 40);
        if (maxScroll > 0) {
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            g.setColor(new Color(200, 200, 200));
            g.drawString("UP/DOWN to scroll", panelX + panelW - 130, panelY + panelH - 8);
        }
        drawBackButton(g, width, height);
    }
    private void drawPanel(Graphics g, int width, int height, String title) {
        g.setFont(new Font("Arial", Font.BOLD, 36));
        int tw = g.getFontMetrics().stringWidth(title);
        drawOutlinedText(g, title, (width - tw) / 2, 155, new Color(255, 214, 0), Color.BLACK, 2);
    }
    private void drawBackButton(Graphics g, int width, int height) {
        backButtonBounds.setBounds(width / 2 - 70, height - 72, 140, 44);
        drawBrickButton(g, backButtonBounds, "BACK", selectedIndex == 1);
    }
    private void drawOutlinedText(Graphics g, String text, int x, int y, Color fill, Color outline, int thickness) {
        g.setColor(outline);
        for (int dx = -thickness; dx <= thickness; dx++) {
            for (int dy = -thickness; dy <= thickness; dy++) {
                if (dx != 0 || dy != 0) {
                    g.drawString(text, x + dx, y + dy);
                }
            }
        }
        g.setColor(fill);
        g.drawString(text, x, y);
    }
    public void keyPressed(KeyEvent e) {
        switch (screen) {
            case MAIN:
                handleMainKeys(e);
                break;
            case SETTINGS:
                handleSettingsKeys(e);
                break;
            case TUTORIAL:
                handleTutorialKeys(e);
                break;
        }
    }
    private void handleMainKeys(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                selectedIndex = (selectedIndex + MAIN_LABELS.length - 1) % MAIN_LABELS.length;
                break;
            case KeyEvent.VK_DOWN:
                selectedIndex = (selectedIndex + 1) % MAIN_LABELS.length;
                break;
            case KeyEvent.VK_ENTER:
                activateMainSelection();
                break;
            default:
                break;
        }
    }
    private void handleSettingsKeys(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                selectedIndex = 1 - selectedIndex;
                break;
            case KeyEvent.VK_ENTER:
                if (selectedIndex == 0) {
                    GameSettings.musicEnabled = !GameSettings.musicEnabled;
                    if (!GameSettings.musicEnabled) {
                        Music.stopMusic();
                    }
                } else {
                    screen = Screen.MAIN;
                    selectedIndex = 0;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                screen = Screen.MAIN;
                selectedIndex = 0;
                break;
            default:
                break;
        }
    }
    private void handleTutorialKeys(KeyEvent e) {
        int maxScroll = Math.max(0, TUTORIAL_LINES.length - 12);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                tutorialScroll = Math.max(0, tutorialScroll - 1);
                break;
            case KeyEvent.VK_DOWN:
                tutorialScroll = Math.min(maxScroll, tutorialScroll + 1);
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_ESCAPE:
                screen = Screen.MAIN;
                selectedIndex = 0;
                tutorialScroll = 0;
                break;
            default:
                break;
        }
    }
    public void mouseClicked(int x, int y) {
        switch (screen) {
            case MAIN:
                for (int i = 0; i < mainButtonBounds.length; i++) {
                    if (mainButtonBounds[i] != null && mainButtonBounds[i].contains(x, y)) {
                        selectedIndex = i;
                        activateMainSelection();
                        return;
                    }
                }
                break;
            case SETTINGS:
                if (musicToggleBounds.contains(x, y)) {
                    selectedIndex = 0;
                    GameSettings.musicEnabled = !GameSettings.musicEnabled;
                    if (!GameSettings.musicEnabled) {
                        Music.stopMusic();
                    }
                } else if (backButtonBounds.contains(x, y)) {
                    screen = Screen.MAIN;
                    selectedIndex = 0;
                }
                break;
            case TUTORIAL:
                if (backButtonBounds.contains(x, y)) {
                    screen = Screen.MAIN;
                    selectedIndex = 0;
                    tutorialScroll = 0;
                }
                break;
            default:
                break;
        }
    }
    private void activateMainSelection() {
        switch (selectedIndex) {
            case 0:
                if (onStartGame != null) {
                    onStartGame.run();
                }
                break;
            case 1:
                screen = Screen.SETTINGS;
                selectedIndex = 0;
                break;
            case 2:
                screen = Screen.TUTORIAL;
                selectedIndex = 0;
                tutorialScroll = 0;
                break;
            default:
                break;
        }
    }
}
