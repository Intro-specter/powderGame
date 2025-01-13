package Simulation.Particles;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Sand extends Particle {
    private static Random rng = new Random();
    private static final int WATER_SWAP_CHANCE = 20;

    public Sand(PowderGameBoard board, int index) {
        super(board, Material.SAND, index);
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

    public void update() {
        this.flipActive();

        Particle downParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.D)); 
        if (this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle downLeftParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.DL));
        Particle downRightParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.DR));
        if (this.canSwap(downLeftParticle.getMaterial())) {
            if (this.canSwap(downRightParticle.getMaterial()) && rng.nextInt(2)==0) {
                this.board.swapCells(this.index, downRightParticle.getIndex());
                return;
            }
            this.board.swapCells(this.index, downLeftParticle.getIndex());
            return;
        } else if (this.canSwap(downRightParticle.getMaterial())) {
            this.board.swapCells(this.index, downRightParticle.getIndex());
            return;
        }
    }
}
