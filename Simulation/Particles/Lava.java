package Simulation.Particles;

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Lava extends Particle {
    private static Random rng = new Random();
    private static final Color STD_LAVA_COLOR = new Color(255, 125, 75);
    private static final Color ALT_LAVA_COLOR = new Color(255, 150, 100);
    private static final int STONE_SWAP_CHANCE = 10;
    private static final int FREEZE_TO_STONE_CHANCE = 5;

    public Lava(PowderGameBoard board, int index) {
        super(board, Material.LAVA, index);
        this.color = STD_LAVA_COLOR;
        this.occlusionValue = 7;
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

    public boolean neighbourInteraction() {
        Direction[] neighbours = { Direction.U, Direction.L, Direction.R, Direction.D };
        for (Direction dir : neighbours) {
            Particle particle = this.board.getCell(this.board.applyDirToIndex(this.index, dir));

            if (particle.getMaterial() == Material.WATER) {
                this.board.setCell(new Cloud(this.board, particle.getIndex()));
                if (rng.nextInt(FREEZE_TO_STONE_CHANCE) == 0) {
                    this.board.setCell(new Stone(this.board, this.index));
                    return true;
                }
            } else if (particle.getMaterial() == Material.ICE) {
                this.board.setCell(new Water(this.board, particle.getIndex()));
                return true;
            } else if (particle.getMaterial() == Material.SAND) {
                this.board.setCell(new Lava(this.board, particle.getIndex()));
                return true;
            }
        }

        return false;
    }

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, this.color = (rng.nextInt(2) == 0) ? STD_LAVA_COLOR : ALT_LAVA_COLOR,
                Color.WHITE);
    }

    public void update() {
        this.flipActive();

        if (neighbourInteraction()) {
            return;
        }

        Particle downParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.D));
        if (this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle leftParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.L));
        Particle rightParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.R));

        if (this.canSwap(leftParticle.getMaterial())) {
            if (this.canSwap(rightParticle.getMaterial()) && rng.nextInt(2) == 0) {
                this.board.swapCells(this.index, rightParticle.getIndex());
                return;
            }
            this.board.swapCells(this.index, leftParticle.getIndex());
            return;
        } else if (this.canSwap(rightParticle.getMaterial())) {
            this.board.swapCells(this.index, rightParticle.getIndex());
            return;
        }
    }
}
