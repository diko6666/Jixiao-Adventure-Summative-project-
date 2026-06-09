package com.sxt.obj;
import com.sxt.util.BackGround;
import com.sxt.util.GameSettings;
import com.sxt.util.StaticValue;
import java.awt.image.BufferedImage;
public class Mario implements Runnable {
    private int x;
    private int y;
    private int width = 25;
    private int height = 25;
    private String status = null;
    private boolean big = false;
    private boolean eatHua = false;
    private BufferedImage show = null;
    private BackGround backGround = new BackGround();
    private Thread thread = null;
    private int xSpeed;
    private int ySpeed;
    private int index;
    private int upTime = 0;
    private boolean isOK;
    private boolean isDeath = false;
    private int score = 0;
    private boolean face_to = true;
    private boolean isDying = false;
    public Mario() {
    }
    public Mario(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        show = StaticValue.stand_R;
        this.status = "stand--right";
        // Mario keeps his own little loop running, so he can move and fall even when the key is not being pressed.
        thread = new Thread(this);
        thread.start();
    }
    public void death() {
        // Once Mario starts dying, the game just lets the animation play out.
        if (!isDying) {
            isDying = true;
            xSpeed = 0;
            ySpeed = -15;
        }
    }
    public void leftMove() {
        xSpeed = -5;
        if (backGround.isReach()) {
            xSpeed = 0;
        }
        if (status.indexOf("jump") != -1) {
            status = "jump--left";
        } else {
            status = "move--left";
        }
        this.face_to = true;
    }
    public void rightMove() {
        xSpeed = 5;
        if (backGround.isReach()) {
            xSpeed = 0;
        }
        if (status.indexOf("jump") != -1) {
            status = "jump--right";
        } else {
            status = "move--right";
        }
        this.face_to = false;
    }
    public void leftStop() {
        xSpeed = 0;
        if (status.indexOf("jump") != -1) {
            status = "jump--left";
        } else {
            status = "stop--left";
        }
        this.face_to = true;
    }
    public void rightStop() {
        xSpeed = 0;
        if (status.indexOf("jump") != -1) {
            status = "jump--right";
        } else {
            status = "stop--right";
        }
        this.face_to = false;
    }
    public void fire() {
        // Fireballs only come out when Mario has the flower power.
        if (this.eatHua && this.big) {
            backGround.getFireballList().add(new Fireball(this.x, this.y, this.face_to, backGround));
        }
    }
    public void jump() {
        // Jump height changes a bit when Mario is big, so the bigger form feels stronger.
        if (status.indexOf("jump") == -1) {
            if (status.indexOf("left") != -1) {
                status = "jump--left";
                this.face_to = true;
            } else {
                status = "jump--right";
                this.face_to = false;
            }
            if (this.isEatHua() == false) {
                ySpeed = -10;
                upTime = 7;
            } else {
                ySpeed = -20;
                upTime = 5;
            }
        }
        if (backGround.isReach()) {
            ySpeed = 0;
        }
    }
    public void fall() {
        if (status.indexOf("left") != -1) {
            status = "jump--left";
            this.face_to = true;
        } else {
            status = "jump--right";
            this.face_to = false;
        }
        ySpeed = 10;
    }
    @Override
    public void run() {
        while (true) {
            try {
                if (GameSettings.paused) {
                    Thread.sleep(50);
                    continue;
                }
                // If Mario is in the dying state, he just keeps falling until the run is over.
                if (isDying) {
                    ySpeed += 1;
                    y += ySpeed;
                    if (y > 650) {
                        isDeath = true;
                    }
                    Thread.sleep(50);
                    continue;
                }
                boolean onObstacle = false;
                boolean canRight = true;
                boolean canLeft = true;
                // Hitting the flag changes the end of the stage into the little victory walk.
                if (backGround.isFlag() && this.x >= 500) {
                    this.backGround.setReach(true);
                    if (this.backGround.isBase()) {
                        status = "move--right";
                        if (x < 690) {
                            x += 5;
                        } else {
                            isOK = true;
                        }
                    } else {
                        if (y < 395) {
                            xSpeed = 0;
                            this.y += 5;
                            status = "jump--right";
                        }
                        if (y > 395) {
                            this.y = 395;
                            status = "stop--right";
                        }
                    }
                } else {
                    // Check the world around Mario: blocks, power-ups, and enemies all get handled here.
                    for (int i = 0; i < backGround.getObstacleList().size(); i++) {
                        Obstacle ob = backGround.getObstacleList().get(i);
                        if (ob.getY() == this.y + 25 && (ob.getX() > this.x - 30 && ob.getX() < this.x + 25)) {
                            onObstacle = true;
                        }
                        if ((ob.getY() >= this.y - 30 && ob.getY() <= this.y - 20) && (ob.getX() > this.x - 30 && ob.getX() < this.x + 25)) {
                            if (ob.getType() == 0) {
                                backGround.getObstacleList().remove(ob);
                                score += 1;
                            }
                            if (ob.getType() == 9) {
                                if (this.big) {
                                    backGround.getPowerUpList().add(new PowerUp(ob.getX(), this.y - 55, 1, backGround));
                                } else {
                                    backGround.getPowerUpList().add(new PowerUp(ob.getX(), this.y - 55, 0, backGround));
                                }
                                ob.setType(11);
                            }
                            if (ob.getType() == 11) {
                                score += 1;
                                backGround.getPowerUpList().add(new PowerUp(ob.getX() + 5, this.y - 53, 2, backGround));
                                ob.setType(12);
                            }
                            if (ob.getType() == 12) {
                                score += 5;
                                ob.setType(7);
                            }
                            upTime = 0;
                        }
                        if (ob.getX() == this.x + 25 && (ob.getY() > this.y - 30 && ob.getY() < this.y + 25)) {
                            canRight = false;
                        }
                        if (ob.getX() == this.x - 30 && (ob.getY() > this.y - 30 && ob.getY() < this.y + 25)) {
                            canLeft = false;
                        }
                    }
                    // Pick up mushrooms, flowers, and coins when Mario touches them.
                    for (int i = 0; i < backGround.getPowerUpList().size(); i++) {
                        PowerUp d = backGround.getPowerUpList().get(i);
                        if ((d.getX() + 35 > this.x && d.getX() - 25 < this.x) && (d.getY() + 35 > this.y && d.getY() - 20 < this.y)) {
                            if (d.getType() == 0) {
                                d.eat();
                                this.setBig(true);
                            }
                            if (d.getType() == 1) {
                                d.eat();
                                this.setEatHua(true);
                            }
                        }
                        if ((d.getY() + 35 > this.y && d.getY() - 25 < this.y) && (d.getX() + 35 > this.x && d.getX() - 20 < this.y)) {
                            if (d.getType() == 2) {
                                this.score += 5;
                                d.eat();
                            }
                        }
                    }
                    // Enemy hits are handled here. Jump on top, touch from the side, or just gg.
                    for (int i = 0; i < backGround.getEnemyList().size(); i++) {
                        Enemy e = backGround.getEnemyList().get(i);
                        if (e.getY() == this.y + 20 && (e.getX() - 25 <= this.x && e.getX() + 35 >= this.x)) {
                            if (e.getType() == 1) {
                                e.death();
                                score += 2;
                                upTime = 3;
                                ySpeed = -10;
                            } else if (e.getType() == 2) {
                                death();
                            } else if (e.getType() == 3) {
                                score += 2;
                                upTime = 3;
                                ySpeed = -10;
                                e.death();
                                e.setY(e.getY() + 10);
                            } else if (e.getType() == 4) {
                                score += 2;
                                upTime = 3;
                                ySpeed = -10;
                                e.death();
                            }
                        }
                        if ((e.getX() + 35 > this.x && e.getX() - 25 < this.x) && (e.getY() + 35 > this.y && e.getY() - 20 < this.y)) {
                            death();
                        }
                    }
                    // If Mario is standing on a block, keep him there. If not, let gravity pull him down.
                    if (onObstacle && upTime == 0) {
                        if (status.indexOf("left") != -1) {
                            if (xSpeed != 0) {
                                status = "move--left";
                            } else {
                                status = "stop--left";
                            }
                        } else {
                            if (xSpeed != 0) {
                                status = "move--right";
                            } else {
                                status = "stop--right";
                            }
                        }
                    } else {
                        if (upTime != 0) {
                            upTime--;
                        } else {
                            fall();
                        }
                        y += ySpeed;
                    }
                }
                // Stop Mario from walking through walls.
                if ((canLeft && xSpeed < 0) || (canRight && xSpeed > 0)) {
                    x += xSpeed;
                    if (x < 0) {
                        x = 0;
                    }
                }
                // Swap between the sprite frames so the run animation does not look frozen.
                if (status.contains("move")) {
                    index = index == 0 ? 1 : 0;
                }
                if ("move--left".equals(status)) {
                    show = StaticValue.run_L.get(index);
                }
                if ("move--right".equals(status)) {
                    show = StaticValue.run_R.get(index);
                }
                if ("stop--left".equals(status)) {
                    show = StaticValue.stand_L;
                }
                if ("stop--right".equals(status)) {
                    show = StaticValue.stand_R;
                }
                if ("jump--left".equals(status)) {
                    show = StaticValue.jump_L;
                }
                if ("jump--right".equals(status)) {
                    show = StaticValue.jump_R;
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
    public BufferedImage getShow() { return show; }
    public void setBackGround(BackGround backGround) { this.backGround = backGround; }
    public boolean isOK() { return isOK; }
    public boolean isDeath() { return isDeath; }
    public int getScore() { return score; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public boolean isBig() { return big; }
    public void setBig(boolean big) { this.big = big; }
    public boolean isEatHua() { return eatHua; }
    public void setEatHua(boolean eatHua) { this.eatHua = eatHua; }
}
