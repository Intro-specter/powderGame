package Simulation;

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
