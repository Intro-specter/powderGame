package Simulation.Particles;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Sand extends Particle {
    private static Random rng = new Random();
    private static final int WATER_SWAP_CHANCE = 20;

    public Sand(int index) {
        super(Material.SAND, index);
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.CLOUD:
                return true;
            case Material.WATER:
                return rng.nextInt(WATER_SWAP_CHANCE) == 0; // (100/bound)% chance
            default:
                return false;
        }
    }

    public void update(PowderGameBoard board) {
        this.flipActive();

        Particle downParticle = board.getCell(board.applyDirToIndex(this.index, Direction.D)); 
        if (this.canSwap(downParticle.getMaterial())) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle downLeftParticle = board.getCell(board.applyDirToIndex(this.index, Direction.DL));
        Particle downRightParticle = board.getCell(board.applyDirToIndex(this.index, Direction.DR));
        if (this.canSwap(downLeftParticle.getMaterial())) {
            if (this.canSwap(downRightParticle.getMaterial()) && rng.nextInt(2)==0) {
                board.swapCells(this.index, downRightParticle.getIndex());
                return;
            }
            board.swapCells(this.index, downLeftParticle.getIndex());
            return;
        } else if (this.canSwap(downRightParticle.getMaterial())) {
            board.swapCells(this.index, downRightParticle.getIndex());
            return;
        }
    }
}
