package Simulation.Particles;

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Ice extends Particle {
    private static Random rng = new Random();
    private static final Color STD_ICE_COLOR = new Color(225, 225, 255);
    private static final int RECOLOR_CHANCE = 25;
    private static final int FREEZE_WATER_CHANCE = 100;
    private static final int MELT_TO_WATER_CHANCE = 100;

    public Ice(PowderGameBoard board, int index) {
        super(board, Material.ICE, index);
        this.color = STD_ICE_COLOR;
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

    public boolean neighbourInteraction() {
        Direction[] neighbours = {Direction.U, Direction.L, Direction.R, Direction.D};
        for (Direction dir : neighbours) {    
            Particle particle = this.board.getCell(this.board.applyDirToIndex(this.index, dir));

            if (particle.getMaterial() == Material.WATER && rng.nextInt(FREEZE_WATER_CHANCE) == 0) {
                this.board.setCell(new Ice(this.board, particle.getIndex()));
            }
        }

        return false;
    }

    public void update() {
        this.flipActive();

        if (rng.nextInt(RECOLOR_CHANCE) == 0) {
            this.color = Particle.shiftColorTowardsTarget(STD_ICE_COLOR, Color.BLUE, board.getHeight(), this.getDepthInDirection(Direction.U));
        }

        if (neighbourInteraction()) {
            return;
        } else if (rng.nextInt(MELT_TO_WATER_CHANCE) == 0) {
            this.board.setCell(new Water(this.board, this.index));
            return;
        }

        Particle downParticle = this.board.getNearbyParticle(this.index, Direction.D);
        if (this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, downParticle.getIndex());
            return;
        }
    }
}
