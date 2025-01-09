package Simulation.Particles;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Cloud extends Particle {
    private static Random rng = new Random();
    private static final int CONDENSE_CHANCE = 10000;

    public Cloud(int index) {
        super(Material.CLOUD, index);
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            default:
                return false;
        }
    }

    public void update(PowderGameBoard board) {
        this.flipActive();

        Particle upParticle = board.getNearbyParticle(this.index, Direction.U);
        if (this.canSwap(upParticle.getMaterial()) && rng.nextInt(4) == 0) {
            board.swapCells(this.index, upParticle.getIndex());
            return;
        }

        Particle leftParticle = board.getNearbyParticle(this.index, Direction.L);
        if (this.canSwap(leftParticle.getMaterial()) && rng.nextInt(4) == 0) {
            board.swapCells(this.index, leftParticle.getIndex());
            return;
        }

        Particle rightParticle = board.getNearbyParticle(this.index, Direction.R);
        if (this.canSwap(rightParticle.getMaterial()) && rng.nextInt(4) == 0) {
            board.swapCells(this.index, rightParticle.getIndex());
            return;
        }

        Particle downParticle = board.getNearbyParticle(this.index, Direction.D);
        if (this.canSwap(downParticle.getMaterial()) && rng.nextInt(8) == 0) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        if (upParticle.getMaterial() == Material.CLOUD && leftParticle.getMaterial() == Material.CLOUD && rightParticle.getMaterial() == Material.CLOUD && downParticle.getMaterial() == Material.CLOUD && rng.nextInt(CONDENSE_CHANCE) == 0) {
            board.setCell(new Water(this.index));
            return;
        }
    }
}
