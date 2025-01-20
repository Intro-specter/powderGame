package Simulation.Particles;

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Ice extends Particle {
    private static Random rng = new Random();
    private static final Color STD_ICE_COLOR = new Color(225, 225, 255);
    private static final int FREEZE_WATER_CHANCE = 50;
    private static final int MELT_TO_WATER_CHANCE = 40;

    public Ice(PowderGameBoard board, int index) {
        super(board, Material.ICE, index);
        this.color = STD_ICE_COLOR;
        this.occlusionValue = 5;
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

    public boolean isEncased() {
        return this.board.getCell(this.board.applyDirToIndex(this.index, Direction.U)).equals(this) 
            && this.board.getCell(this.board.applyDirToIndex(this.index, Direction.L)).equals(this) 
            && this.board.getCell(this.board.applyDirToIndex(this.index, Direction.R)).equals(this) 
            && this.board.getCell(this.board.applyDirToIndex(this.index, Direction.D)).equals(this);
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

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, STD_ICE_COLOR, Color.BLUE);
    }

    public void update() {
        this.flipActive();

        if (neighbourInteraction()) {
            return;
        } else if (rng.nextInt(MELT_TO_WATER_CHANCE) == 0 && !this.isEncased()) {
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
