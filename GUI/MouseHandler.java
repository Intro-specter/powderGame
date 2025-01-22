package GUI;

import java.awt.event.*;
import java.awt.event.MouseAdapter;

import Simulation.Material;

public class MouseHandler extends MouseAdapter {
    private final int X_OFFSET = 0;
    private final int Y_OFFSET = 0;
    private int x;
    private int y;
    private boolean active = false;
    private MouseMotionAdapter motionListener;
    private Material chosenMaterial = Material.EMPTY;

    public Material getChosenMaterial() {
        return this.chosenMaterial;
    }

    public void setChosenMaterial(Material material) {
        this.chosenMaterial = material;
    }

    public MouseMotionAdapter getMotionListener() {
        return this.motionListener;
    }

    public void setMotionListener(MouseMotionAdapter listener) {
        this.motionListener = listener;
    }

    public int getX() {
        return x + X_OFFSET;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y + Y_OFFSET;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void mousePressed(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        this.active = true;
    }

    public void mouseReleased(MouseEvent e) {
        this.active = false;
    }

    public void mouseExited(MouseEvent e) {
        this.active = false;
    }
}