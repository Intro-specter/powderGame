package Simulation.Particles;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Empty extends Particle {
    private static final Color STD_EMPTY_COLOR = new Color(75, 75, 75);
    public Empty(PowderGameBoard board, int index) {
        super(board, Material.EMPTY, index);
        this.color = STD_EMPTY_COLOR;
    }
}
