package Simulation;
import java.awt.Color;
import java.util.Random;

import Simulation.Particles.*;

public enum Material {
    BARRIER,
    EMPTY,
    SAND, 
    WATER,
    CLOUD,
    STONE,
    ICE,
    LAVA;

    private static Random rng = new Random();

    public static final Color EMPTY_COLOR = new Color(75, 75, 75);
    public static final Color BARRIER_COLOR = new Color(0, 0, 100);
    public static final Color SAND_COLOR = new Color(255, 255, 100);
    public static final Color WATER_COLOR = new Color(100, 100, 255);
    public static final Color WATER_COLOR_ALT = new Color(125, 125, 255);
    public static final Color CLOUD_COLOR = new Color(225, 225, 255);
    public static final Color CLOUD_COLOR_ALT = new Color(205, 205, 255);
    public static final Color STONE_COLOR = new Color(150, 125, 125);
    public static final Color ICE_COLOR = new Color(225, 225, 255);
    public static final Color LAVA_COLOR = new Color(255, 150, 100);
    public static final Color LAVA_COLOR_ALT = new Color(255, 200, 150);

    public Color toColor() {
        switch (this) {
            case EMPTY:
                return EMPTY_COLOR;
            case BARRIER:
                return BARRIER_COLOR;
            case SAND:
                return SAND_COLOR;
            case WATER:
                return (rng.nextInt(2)==0) ? WATER_COLOR : WATER_COLOR_ALT;
            case CLOUD:
                return (rng.nextInt(2)==0) ? CLOUD_COLOR : CLOUD_COLOR_ALT;
            case STONE:
                return STONE_COLOR;
            case ICE:
                return ICE_COLOR;
            case LAVA:
                return (rng.nextInt(2)==0) ? LAVA_COLOR : LAVA_COLOR_ALT;
            default:
                return Color.BLACK;
        }
    }

    public Particle toParticle(PowderGameBoard board, int index) throws Exception {
        switch (this) {
            case EMPTY:
                return new Empty(board, index);
            case BARRIER:
                return new Barrier(board, index);
            case SAND:
                return new Sand(board, index);
            case WATER:
                return new Water(board, index);
            case CLOUD:
                return new Cloud(board, index);
            case STONE:
                return new Stone(board, index);
            case ICE:
                return new Ice(board, index);
            case LAVA:
                return new Lava(board, index);
            default:
                throw new Exception("Unrecognized Particle for Material: " + this.getName());
        }
    }

    public String getName() {
        switch (this) {
            case EMPTY:
                return "Empty";
            case BARRIER:
                return "Barrier";
            case SAND:
                return "Sand";
            case WATER:
                return "Water";
            case CLOUD:
                return "Cloud";
            case STONE:
                return "Stone";
            case ICE:
                return "Ice";
            case LAVA:
                return "Lava";
            default:
                return this.toString();
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        switch (this) {
            case EMPTY:
                return ".";
            case BARRIER:
                return "■";
            case SAND:
                return ANSI_YELLOW + "■" + ANSI_RESET;
            case WATER:
                return ANSI_BLUE + "■" + ANSI_RESET;
            default:
                return "?";
        }
    }
}
