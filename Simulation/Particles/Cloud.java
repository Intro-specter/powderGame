package Simulation.Particles;

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Cloud extends Particle {
    private static Random rng = new Random();
    private static final Color STD_CLOUD_COLOR = new Color(200, 200, 250);
    private static final Color ALT_CLOUD_COLOR = new Color(230, 230, 230);
    private static final int CONDENSE_CHANCE = 10000;

    public Cloud(PowderGameBoard board, int index) {
        super(board, Material.CLOUD, index);
        this.color = STD_CLOUD_COLOR;
        this.occlusionValue = 2;
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            default:
                return false;
        }
    }

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, (rng.nextInt(4) == 0) ? STD_CLOUD_COLOR : ALT_CLOUD_COLOR, Color.BLACK);
    }

    public void update() {
        this.flipActive();

        Particle upParticle = this.board.getNearbyParticle(this.index, Direction.U);
        if (this.canSwap(upParticle.getMaterial()) && rng.nextInt(4) == 0) {
            this.board.swapCells(this.index, upParticle.getIndex());
            return;
        }

        Particle leftParticle = this.board.getNearbyParticle(this.index, Direction.L);
        if (this.canSwap(leftParticle.getMaterial()) && rng.nextInt(4) == 0) {
            this.board.swapCells(this.index, leftParticle.getIndex());
            return;
        }

        Particle rightParticle = this.board.getNearbyParticle(this.index, Direction.R);
        if (this.canSwap(rightParticle.getMaterial()) && rng.nextInt(4) == 0) {
            this.board.swapCells(this.index, rightParticle.getIndex());
            return;
        }

        Particle downParticle = this.board.getNearbyParticle(this.index, Direction.D);
        if (this.canSwap(downParticle.getMaterial()) && rng.nextInt(8) == 0) {
            this.board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        if (upParticle.getMaterial() == Material.CLOUD && leftParticle.getMaterial() == Material.CLOUD
                && rightParticle.getMaterial() == Material.CLOUD && downParticle.getMaterial() == Material.CLOUD
                && rng.nextInt(CONDENSE_CHANCE) == 0) {
            this.board.setCell(new Water(this.board, this.index));
            return;
        }
    }
}
