package com.sxt.menu;
import com.sxt.util.StaticValue;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
public class GameOverScreen {
    public enum Type { GAME_OVER, VICTORY }
    private final Rectangle retryBounds = new Rectangle();
    private final Rectangle menuBounds = new Rectangle();
    private int selectedIndex = 0;
    private Runnable onRetry;
    private Runnable onMenu;
    public void setOnRetry(Runnable onRetry) {
        this.onRetry = onRetry;
    }
    public void setOnMenu(Runnable onMenu) {
        this.onMenu = onMenu;
    }
    public void resetSelection() {
        selectedIndex = 0;
    }
    public void paint(Graphics g, int width, int height, Type type, int score) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        int panelW = 480;
        int panelH = type == Type.GAME_OVER ? 340 : 320;
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;
        g.setColor(new Color(30, 20, 80, 230));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 24, 24);
        Graphics2D border = (Graphics2D) g.create();
        border.setColor(new Color(255, 214, 0));
        border.setStroke(new BasicStroke(4));
        border.drawRoundRect(panelX + 2, panelY + 2, panelW - 4, panelH - 4, 24, 24);
        border.dispose();
        if (type == Type.GAME_OVER) {
            drawMarioSprite(g, panelX + 36, panelY + 52, StaticValue.stand_L, 56);
            g.setFont(new Font("Arial", Font.BOLD, 52));
            drawOutlinedText(g, "GAME OVER", panelX + 120, panelY + 95, new Color(228, 56, 56), Color.WHITE, 3);
        } else {
            drawMarioSprite(g, panelX + 36, panelY + 52, StaticValue.stand_R, 56);
            if (StaticValue.tower != null) {
                g.drawImage(StaticValue.tower, panelX + panelW - 100, panelY + 30, 70, 70, null);
            }
            g.setFont(new Font("Arial", Font.BOLD, 40));
            drawOutlinedText(g, "YOU WIN!", panelX + 120, panelY + 90, new Color(255, 214, 0), new Color(80, 40, 0), 3);
        }
        g.setFont(new Font("Arial", Font.BOLD, 22));
        String scoreText = "SCORE: " + score;
        int scoreW = g.getFontMetrics().stringWidth(scoreText);
        drawOutlinedText(g, scoreText, panelX + (panelW - scoreW) / 2, panelY + 155, Color.WHITE, Color.BLACK, 2);
        if (type == Type.GAME_OVER) {
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.setColor(new Color(220, 220, 255));
            g.drawString("Don't give up — try again!", panelX + 130, panelY + 185);
        }
        int btnW = 180;
        int btnH = 48;
        int btnY = panelY + panelH - 95;
        retryBounds.setBounds(panelX + 40, btnY, btnW, btnH);
        menuBounds.setBounds(panelX + panelW - btnW - 40, btnY, btnW, btnH);
        drawBrickButton(g, retryBounds, "RETRY", selectedIndex == 0);
        drawBrickButton(g, menuBounds, "MENU", selectedIndex == 1);
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("LEFT/RIGHT + ENTER  or  CLICK", panelX + 130, panelY + panelH - 18);
    }
    private void drawMarioSprite(Graphics g, int x, int y, BufferedImage img, int size) {
        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
        }
    }
    private void drawBrickButton(Graphics g, Rectangle bounds, String label, boolean selected) {
        BufferedImage brick = pickBrickTile();
        if (brick != null) {
            int tile = 32;
            for (int ty = bounds.y; ty < bounds.y + bounds.height; ty += tile) {
                for (int tx = bounds.x; tx < bounds.x + bounds.width; tx += tile) {
                    g.drawImage(brick, tx, ty,
                            Math.min(tile, bounds.x + bounds.width - tx),
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
        }
        g.setFont(new Font("Arial", Font.BOLD, 20));
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                selectedIndex = 0;
                break;
            case KeyEvent.VK_RIGHT:
                selectedIndex = 1;
                break;
            case KeyEvent.VK_ENTER:
                activateSelection();
                break;
            default:
                break;
        }
    }
    public void mouseClicked(int x, int y) {
        if (retryBounds.contains(x, y)) {
            selectedIndex = 0;
            activateSelection();
        } else if (menuBounds.contains(x, y)) {
            selectedIndex = 1;
            activateSelection();
        }
    }
    private void activateSelection() {
        if (selectedIndex == 0) {
            if (onRetry != null) {
                onRetry.run();
            }
        } else if (onMenu != null) {
            onMenu.run();
        }
    }
}
