package Simulation.Particles;

import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Barrier extends Particle {
    private static final Color STD_BARRIER_COLOR = new Color(0, 0, 100);

    public Barrier(PowderGameBoard board, int index) {
        super(board, Material.BARRIER, index);
        this.color = STD_BARRIER_COLOR;
    }
}
