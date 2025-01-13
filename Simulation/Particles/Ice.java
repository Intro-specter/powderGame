package Simulation.Particles;

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Ice extends Particle {
    private static Random rng = new Random();
    private static final int FREEZE_WATER_CHANCE = 100;
    private static final int MELT_TO_WATER_CHANCE = 100;

    public Ice(int index) {
        super(Material.ICE, index);
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.CLOUD:
                return true;
            default:
                return false;
        }
    }

    public boolean neighbourInteraction(PowderGameBoard board) {
        Direction[] neighbours = {Direction.U, Direction.L, Direction.R, Direction.D};
        for (Direction dir : neighbours) {    
            Particle particle = board.getCell(board.applyDirToIndex(this.index, dir));

            if (particle.getMaterial() == Material.WATER && rng.nextInt(FREEZE_WATER_CHANCE) == 0) {
                board.setCell(new Ice(particle.getIndex()));
            }
        }

        return false;
    }

    public void update(PowderGameBoard board) {
        this.flipActive();

        if (neighbourInteraction(board)) {
            return;
        } else if (rng.nextInt(MELT_TO_WATER_CHANCE) == 0) {
            board.setCell(new Water(this.index));
            return;
        }

        Particle downParticle = board.getNearbyParticle(this.index, Direction.D);
        if (this.canSwap(downParticle.getMaterial())) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }
    }
}
