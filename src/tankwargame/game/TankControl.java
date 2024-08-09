package tankwargame.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TankControl extends KeyAdapter {
    private Tank tank;
    private final int forwardKey;
    private final int backwardKey;
    private final int leftKey;
    private final int rightKey;
    private final int fireKey;

    private boolean forwardPressed = false;
    private boolean backwardPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public TankControl(Tank tank, int forwardKey, int backwardKey, int leftKey, int rightKey, int fireKey) {
        this.tank = tank;
        this.forwardKey = forwardKey;
        this.backwardKey = backwardKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.fireKey = fireKey;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == forwardKey) {
            forwardPressed = true;
        }
        if (key == backwardKey) {
            backwardPressed = true;
        }
        if (key == leftKey) {
            leftPressed = true;
        }
        if (key == rightKey) {
            rightPressed = true;
        }
        if (key == fireKey) {
            System.out.println("Fire key pressed");
            tank.fire();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == forwardKey) {
            forwardPressed = false;
        }
        if (key == backwardKey) {
            backwardPressed = false;
        }
        if (key == leftKey) {
            leftPressed = false;
        }
        if (key == rightKey) {
            rightPressed = false;
        }
         if (key == fireKey) {
            System.out.println("Fire key pressed");
            tank.fire();
        }
    }

    public boolean isForwardPressed() {
        return forwardPressed;
    }

    public boolean isBackwardPressed() {
        return backwardPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}
