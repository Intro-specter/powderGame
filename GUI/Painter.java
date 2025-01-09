package GUI;
import javax.swing.JPanel;

import Simulation.PowderGameBoard;

import java.awt.*;

public class Painter extends JPanel {
    private PowderGameBoard parentBoard;
    private int scale;
    private boolean paused;
    private int milliseconds_per_frame;

    public Painter(PowderGameBoard inBoard, int scale, boolean paused, int milliseconds_per_frame) {
        this.parentBoard = inBoard;
        this.scale = scale;
        this.paused = paused;
        this.milliseconds_per_frame = milliseconds_per_frame;
    }

    public int getMSPerFrame() {
        return this.milliseconds_per_frame;
    }

    public void setMSPerFrame(int mspf) {
        this.milliseconds_per_frame = mspf;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void flipPaused() {
        this.paused = !this.paused;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < this.parentBoard.getHeight(); i++) {
            for (int j = 0; j < this.parentBoard.getWidth(); j++) {
                g2d.setColor(this.parentBoard.getCell(this.parentBoard.posToIndex(j, i)).getMaterial().toColor());
                g2d.fillRect(j*this.scale, i*this.scale, this.scale, this.scale);
            }
        }
    }
}
