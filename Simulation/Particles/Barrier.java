package Simulation.Particles;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Barrier extends Particle {
    public Barrier(PowderGameBoard board, int index) {
        super(board, Material.BARRIER, index);
    }
}
