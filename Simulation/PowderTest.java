package Simulation;
import java.util.concurrent.TimeUnit;

import Simulation.Particles.Sand;
import Simulation.Particles.Water;
public class PowderTest {
    public static void main(String[] args) {
        PowderGameBoard testBoard = new PowderGameBoard();

        // Resizing
        System.out.println(testBoard);
        testBoard.setWidth(5);
        System.out.println(testBoard);
        testBoard.setWidth(50);
        System.out.println(testBoard);
        testBoard.setHeight(5);
        System.out.println(testBoard);
        testBoard.setHeight(10);

        // Simulation
        for (int i = 0; i < 120; i++) {
            testBoard.setCell(new Water(testBoard, (int)(testBoard.getWidth()*1.5)));
            testBoard.executeTick();
            System.out.println(testBoard);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        for (int i = 0; i < 30; i++) {
            testBoard.executeTick();
            System.out.println(testBoard);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        for (int i = 0; i < 45; i++) {
            testBoard.setCell(new Sand(testBoard, (int)(testBoard.getWidth()*1.5)));
            testBoard.executeTick();
            System.out.println(testBoard);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        for (int i = 0; i < 105; i++) {
            testBoard.executeTick();
            System.out.println(testBoard);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println(testBoard);
        testBoard.setWidth(25);
        for (int i = 0; i < 105; i++) {
            testBoard.executeTick();
            System.out.println(testBoard);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
