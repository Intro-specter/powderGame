package GUI;

import javax.swing.JPanel;

import Simulation.PowderGameBoard;

import java.awt.*;

public class Painter extends JPanel {
    private PowderGameBoard parentBoard;
    private int scale;
    private boolean paused;
    private boolean showingText = false;
    private boolean showingTextBackground = true;
    private int millisecondsPerFrame;
    private boolean fancyGraphics = true;

    public Painter(PowderGameBoard inBoard, int scale, boolean paused, int millisecondsPerFrame) {
        this.parentBoard = inBoard;
        this.scale = scale;
        this.paused = paused;
        this.millisecondsPerFrame = millisecondsPerFrame;
    }

    public boolean isFancy() {
        return this.fancyGraphics;
    }

    public void setFancy(boolean fancy) {
        this.fancyGraphics = fancy;
    }

    public void flipFancy() {
        this.fancyGraphics = !this.fancyGraphics;
    }

    public boolean isShowingText() {
        return this.showingText;
    }

    public void setShowingText(boolean showingText) {
        this.showingText = showingText;
    }

    public void flipShowingText() {
        this.showingText = !this.showingText;
    }

    public boolean isShowingTextBackground() {
        return this.showingTextBackground;
    }

    public void setShowingTextBackground(boolean showingTextBackground) {
        this.showingTextBackground = showingTextBackground;
    }

    public void flipShowingTextBackground() {
        this.showingTextBackground = !this.showingTextBackground;
    }

    public int getMSPerFrame() {
        return this.millisecondsPerFrame;
    }

    public void setMSPerFrame(int mspf) {
        this.millisecondsPerFrame = mspf;
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
                g2d.setColor(this.parentBoard.getCell(this.parentBoard.vecToIndex(j, i)).getColor());
                g2d.fillRect(j * this.scale, i * this.scale, this.scale, this.scale);
            }
        }
    }
}
