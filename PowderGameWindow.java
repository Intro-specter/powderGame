/*
 * TODO: See if checking for things like valid coords in the runtime will make it faster than try catching
 */

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
        final int MILLISECONDS_PER_FRAME = 5;
        final boolean START_PAUSED = false;

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
                if(e.getKeyCode() == KeyEvent.VK_Q){ // decrease placing radius with Q
                    board.setPlacingRadius((board.getPlacingRadius() > 0) ? board.getPlacingRadius() - 1 : board.getPlacingRadius());
                } else if(e.getKeyCode() == KeyEvent.VK_E){ // increase placing radius with E
                    board.setPlacingRadius((board.getPlacingRadius() < 10) ? board.getPlacingRadius() + 1 : board.getPlacingRadius());
                }

                if(e.getKeyCode() == KeyEvent.VK_1){ 
                    mouseHandler.setChosenMaterial(Material.EMPTY);
                } else if(e.getKeyCode() == KeyEvent.VK_2){ 
                    mouseHandler.setChosenMaterial(Material.SAND);
                } else if(e.getKeyCode() == KeyEvent.VK_3){ 
                    mouseHandler.setChosenMaterial(Material.WATER);
                } else if(e.getKeyCode() == KeyEvent.VK_4){ 
                    mouseHandler.setChosenMaterial(Material.CLOUD);
                } else if(e.getKeyCode() == KeyEvent.VK_5){ 
                    mouseHandler.setChosenMaterial(Material.STONE);
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

        while (true) {
            if (Math.abs((frame.getWidth() + FRAME_WIDTH_OFFSET - leftInset - rightInset) - (board.getWidth() * pixelPainter.getScale())) > 0) {
                board.setWidth((frame.getWidth() + FRAME_WIDTH_OFFSET - leftInset - rightInset) / pixelPainter.getScale());
            }
            if (Math.abs((frame.getHeight() + FRAME_HEIGHT_OFFSET - topInset - bottomInset) - (board.getHeight() * pixelPainter.getScale())) > 0) {
                board.setHeight((frame.getHeight() + FRAME_HEIGHT_OFFSET - topInset - bottomInset) / pixelPainter.getScale());
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
                board.attemptPlace(board.posToIndex((mouseHandler.getX() + X_OFFSET)/pixelPainter.getScale(), (mouseHandler.getY() + Y_OFFSET)/pixelPainter.getScale()), mouseHandler);
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
