package Simulation.Particles;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Cloud extends Particle {
    private static Random rng = new Random();
    private static final Color STD_CLOUD_COLOR = new Color(225, 225, 255);
    private static final int RECOLOR_CHANCE = 25;
    private static final int CONDENSE_CHANCE = 10000;

    public Cloud(PowderGameBoard board, int index) {
        super(board, Material.CLOUD, index);
        this.color = STD_CLOUD_COLOR;
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            default:
                return false;
        }
    }

    public void update() {
        this.flipActive();

        if (rng.nextInt(RECOLOR_CHANCE) == 0) {
            this.color = Particle.shiftColorTowardsTarget(STD_CLOUD_COLOR, Color.BLACK, board.getHeight(), this.getDepthInDirection(Direction.D));
        }

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

        if (upParticle.getMaterial() == Material.CLOUD && leftParticle.getMaterial() == Material.CLOUD && rightParticle.getMaterial() == Material.CLOUD && downParticle.getMaterial() == Material.CLOUD && rng.nextInt(CONDENSE_CHANCE) == 0) {
            this.board.setCell(new Water(this.board, this.index));
            return;
        }
    }
}
