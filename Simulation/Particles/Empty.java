package Simulation.Particles;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Empty extends Particle {
    public Empty(PowderGameBoard board, int index) {
        super(board, Material.EMPTY, index);
    }
}
