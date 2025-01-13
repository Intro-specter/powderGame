package GUI;

/*
 * TODO: Comment things fr
 *
 * The main function here implements all the graphics and game logic at once, 
 * which is tough to debug.
 * TODO: Compartmentalize main fn
 * TODO: Separate Graphics, Input, and Game Logic into Threads
 * 
 * Known issues at runtime:
 * - Index out of range exception when PowderGameBoard.attemptPlace() would 
 *   attempt to place outside bounds (top or bottom of screen)
 *     - Additional side effect that causes wrap-around 
 *       on the sides for placing (because the game is stored in a 1D-array)
 * - Flickering edges when paused, presumably because PowderGameBoard.createBarrier() 
 *   is constantly getting called for some reason.
 */

import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Simulation.Material;
import Simulation.PowderGameBoard;

import java.awt.Dimension;
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
        final int INIT_MILLISECONDS_PER_FRAME = 8;
        final boolean START_PAUSED = false;
        final int MAX_SCALE = 20;
        final int MIN_SCALE = 1;

        PowderGameBoard board = new PowderGameBoard(INIT_HEIGHT, INIT_HEIGHT);
        Painter pixelPainter = new Painter(board, INIT_SCALE, START_PAUSED, INIT_MILLISECONDS_PER_FRAME);
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
                if (e.getKeyCode() == KeyEvent.VK_O && pixelPainter.getMSPerFrame() > 1){
                    pixelPainter.setMSPerFrame(pixelPainter.getMSPerFrame()/2);
                } else if (e.getKeyCode() == KeyEvent.VK_P && pixelPainter.getMSPerFrame() == 0){
                    pixelPainter.setMSPerFrame(1);
                } else if (e.getKeyCode() == KeyEvent.VK_P && pixelPainter.getMSPerFrame() < 257){
                    pixelPainter.setMSPerFrame(pixelPainter.getMSPerFrame()*2);
                } else if (e.getKeyCode() == KeyEvent.VK_O && pixelPainter.getMSPerFrame() == 1){
                    pixelPainter.setMSPerFrame(0);
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
                } else if(e.getKeyCode() == KeyEvent.VK_6){ 
                    mouseHandler.setChosenMaterial(Material.ICE);
                } else if(e.getKeyCode() == KeyEvent.VK_7){ 
                    mouseHandler.setChosenMaterial(Material.LAVA);
                }
            }
        });

        frame.setSize(INIT_WIDTH*INIT_SCALE, INIT_HEIGHT*INIT_SCALE);
        frame.setMinimumSize(new Dimension(400, 100));

        frame.add(pixelPainter);
        frame.addMouseListener(mouseHandler);
        frame.addMouseMotionListener(mouseHandler.getMotionListener());
        frame.addMouseWheelListener(wheelListener);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JLabel dataLabel = new JLabel();
        dataLabel.setText("Hello, World");
        pixelPainter.add(dataLabel);

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
                    pixelPainter.setScale((pixelPainter.getScale() < MAX_SCALE) ? pixelPainter.getScale() + 1 : pixelPainter.getScale());
                    wheelListener.setMoveDir(0);;
                    break;
                case 1: //DOWN
                    pixelPainter.setScale((pixelPainter.getScale() > MIN_SCALE) ? pixelPainter.getScale() - 1 : pixelPainter.getScale());
                    wheelListener.setMoveDir(0);;
                    break;   
            }

            if (mouseHandler.isActive()) {
                board.attemptPlace(board.vecToIndex((mouseHandler.getX() + X_OFFSET)/pixelPainter.getScale(), (mouseHandler.getY() + Y_OFFSET)/pixelPainter.getScale()), mouseHandler);
            }

            if (!pixelPainter.isPaused()) {
                board.executeTick();
                try {
                    TimeUnit.MILLISECONDS.sleep(pixelPainter.getMSPerFrame());
                } catch(Exception e) {
                    System.out.println(e);
                }
            }

            dataLabel.setText("Selected Material: " + mouseHandler.getChosenMaterial().getName() + ", Placing Radius: " + board.getPlacingRadius() + ", Paused: " + pixelPainter.isPaused() + ", Sim Speed: " + pixelPainter.getMSPerFrame() + ", Board Dimensions: (" + board.getWidth() + "," + board.getHeight() + "), Cell Scale: " + pixelPainter.getScale());

            frame.repaint();
        }
    }
}
