package Simulation.Particles;

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Lava extends Particle {
    private static Random rng = new Random();
    private static final int STONE_SWAP_CHANCE = 10;
    private static final int FREEZE_TO_STONE_CHANCE = 5;

    public Lava(int index) {
        super(Material.LAVA, index);
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.CLOUD:
                return true;
            case Material.WATER:
                return true;
            case Material.STONE:
                return rng.nextInt(STONE_SWAP_CHANCE) == 0;
            default:
                return false;
        }
    }

    public boolean neighbourInteraction(PowderGameBoard board) {
        Direction[] neighbours = {Direction.U, Direction.L, Direction.R, Direction.D};
        for (Direction dir : neighbours) {    
            Particle particle = board.getCell(board.applyDirToIndex(this.index, dir));

            if (particle.getMaterial() == Material.WATER) {
                board.setCell(new Cloud(particle.getIndex()));
                if (rng.nextInt(FREEZE_TO_STONE_CHANCE) == 0) {
                    board.setCell(new Stone(this.index));
                    return true;
                }
            } else if (particle.getMaterial() == Material.ICE) {
                board.setCell(new Water(particle.getIndex()));
                return true;
            } else if (particle.getMaterial() == Material.SAND) {
                board.setCell(new Lava(particle.getIndex()));
                return true;
            }
        }

        return false;
    }

    public void update(PowderGameBoard board) {
        this.flipActive();

        if (neighbourInteraction(board)) {
            return;
        }

        Particle downParticle = board.getCell(board.applyDirToIndex(this.index, Direction.D)); 
        if (this.canSwap(downParticle.getMaterial())) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle leftParticle = board.getCell(board.applyDirToIndex(this.index, Direction.L));
        Particle rightParticle = board.getCell(board.applyDirToIndex(this.index, Direction.R));

        if (this.canSwap(leftParticle.getMaterial())) {
            if (this.canSwap(rightParticle.getMaterial()) && rng.nextInt(2)==0) {
                board.swapCells(this.index, rightParticle.getIndex());
                return;
            }
            board.swapCells(this.index, leftParticle.getIndex());
            return;
        } else if (this.canSwap(rightParticle.getMaterial())) {
            board.swapCells(this.index, rightParticle.getIndex());
            return;
        }
    }
}
