import java.util.Timer;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import java.awt.event.*;

public class PowderGameWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Powder Game");
        final int INIT_WIDTH = 200;
        final int INIT_HEIGHT = 100;
        final int INIT_SCALE = 4;
        final int FRAME_WIDTH_OFFSET = 0;
        final int FRAME_HEIGHT_OFFSET = 0;
        final int X_OFFSET = -8;
        final int Y_OFFSET = -30;
        final int MILLISECONDS_PER_FRAME = 10;
        final boolean START_PAUSED = false;

        int placingRadius = 3;

        PowderGameBoard board = new PowderGameBoard(INIT_HEIGHT, INIT_HEIGHT);
        Painter pixelPainter = new Painter(board, INIT_SCALE, START_PAUSED);
        MouseHandler mouseHandler = new MouseHandler();
        mouseHandler.setMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseHandler.setX(e.getX());
                mouseHandler.setY(e.getY());
            }
        });
        WheelListener wheelListener = new WheelListener();

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_R){ // wipe board with R
                    board.wipe();
                }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){ // close the program with ESC
                    frame.setVisible(false);
                    frame.dispose();
                    System.exit(0);
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE){ // pause updates with SPACE
                    pixelPainter.flipPaused();
                }

                if(e.getKeyCode() == KeyEvent.VK_1){ 
                    mouseHandler.setChosenMaterial(Material.EMPTY);
                } else if(e.getKeyCode() == KeyEvent.VK_2){ 
                    mouseHandler.setChosenMaterial(Material.SAND);
                } else if(e.getKeyCode() == KeyEvent.VK_3){ 
                    mouseHandler.setChosenMaterial(Material.WATER);
                }
            }
        });

        frame.add(pixelPainter);
        frame.addMouseListener(mouseHandler);
        frame.addMouseMotionListener(mouseHandler.getMotionListener());
        frame.addMouseWheelListener(wheelListener);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(INIT_WIDTH*INIT_SCALE, INIT_HEIGHT*INIT_SCALE);

        int topInset = frame.getInsets().top;
        int bottomInset = frame.getInsets().bottom;
        int leftInset = frame.getInsets().left;
        int rightInset = frame.getInsets().right;

        for (int i = (int)(board.getWidth()*1.5)-15; i < (int)(board.getWidth()*1.5)+15; i++) {
            for (int j = 1; j < 11; j++) {
                board.setCell(new Sand(board.posToIndex(i, j)));
            }
        }

        while (true) {
            if (Math.abs((frame.getWidth() + FRAME_WIDTH_OFFSET - leftInset - rightInset) - (board.getWidth() * pixelPainter.getScale())) > 2) {
                board.setWidth((frame.getWidth() + FRAME_WIDTH_OFFSET - leftInset - rightInset) / pixelPainter.getScale());
                continue;
            }
            if (Math.abs((frame.getHeight() + FRAME_HEIGHT_OFFSET - topInset - bottomInset) - (board.getHeight() * pixelPainter.getScale())) > 2) {
                board.setHeight((frame.getHeight() + FRAME_HEIGHT_OFFSET - topInset - bottomInset) / pixelPainter.getScale());
                continue;
            }

            // change scale with scrolling
            switch (wheelListener.getMoveDir()) {
                case -1: //UP
                    pixelPainter.setScale(pixelPainter.getScale() + 1);
                    wheelListener.setMoveDir(0);;
                    break;
                case 1: //DOWN
                    pixelPainter.setScale((pixelPainter.getScale() > 2) ? pixelPainter.getScale() - 1 : pixelPainter.getScale());
                    wheelListener.setMoveDir(0);;
                    break;   
            }

            if (mouseHandler.isActive()) {
                board.attemptPlace(board.posToIndex((mouseHandler.getX() + X_OFFSET)/pixelPainter.getScale(), (mouseHandler.getY() + Y_OFFSET)/pixelPainter.getScale()), mouseHandler, placingRadius);
            }

            if (!pixelPainter.isPaused()) {
                board.executeTick();
                try {
                    TimeUnit.MILLISECONDS.sleep(MILLISECONDS_PER_FRAME);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }

            frame.repaint();
        }
    }
}
