package GUI;

import javax.swing.*;
import java.awt.event.*;

public class WheelListener extends JPanel implements MouseWheelListener {
    private int moveDir = 0; // -1 = UP, 0 = NONE, 1 = DOWN

    public int getMoveDir() {
        return this.moveDir;
    }

    public void setMoveDir(int newVal) {
        this.moveDir = newVal;
    }

    public void resetMoveDir() {
        this.moveDir = 0;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) { // Mouse Wheel UP
            this.moveDir = -1;
        } else if (notches > 0) {
            this.moveDir = 1;
        } else {
            this.moveDir = 0;
        }
    }
}