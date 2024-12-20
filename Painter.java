import javax.swing.JPanel;
import java.awt.*;

public class Painter extends JPanel {
    private PowderGameBoard parentBoard;
    private int scale;
    private boolean paused;

    public Painter(PowderGameBoard inBoard, int scale, boolean paused) {
        this.parentBoard = inBoard;
        this.scale = scale;
        this.paused = paused;
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
