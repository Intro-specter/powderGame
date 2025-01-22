package Simulation.Particles;
/*
 * TODO: Try a temperature variable to influence condensation and freezing
 */

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Water extends Particle {
    private static Random rng = new Random();
    private static final Color STD_WATER_COLOR = new Color(100, 100, 255);
    private static final int CLOUD_SWAP_CHANCE = 10;
    private static final int SAND_SWAP_CHANCE = 500;
    private static final int ICE_SWAP_CHANCE = 5;
    private static final int EVAPORATE_CHANCE = 2000;
    private static final int WEATHER_STONE_CHANCE = 1000;

    public Water(PowderGameBoard board, int index) {
        super(board, Material.WATER, index);
        this.color = STD_WATER_COLOR;
        this.occlusionValue = 3;
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.SAND:
                return rng.nextInt(SAND_SWAP_CHANCE) == 0;
            case Material.CLOUD:
                return rng.nextInt(CLOUD_SWAP_CHANCE) == 0;
            case Material.ICE:
                return rng.nextInt(ICE_SWAP_CHANCE) == 0;
            default:
                return false;
        }
    }

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, STD_WATER_COLOR, Color.BLACK);
    }

    public void update() {
        this.flipActive();

        Particle downParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.D));
        if (this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle leftParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.L));
        Particle rightParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.R));

        if (leftParticle.getMaterial() == Material.STONE && rng.nextInt(WEATHER_STONE_CHANCE) == 0) {
            this.board.setCell(new Sand(this.board, leftParticle.getIndex()));
            return;
        }

        if (rightParticle.getMaterial() == Material.STONE && rng.nextInt(WEATHER_STONE_CHANCE) == 0) {
            this.board.setCell(new Sand(this.board, rightParticle.getIndex()));
            return;
        }

        if (this.board.getCell(this.board.applyDirToIndex(this.index, Direction.U)).getMaterial() == Material.EMPTY) {
            if (rng.nextInt(EVAPORATE_CHANCE) == 0) {
                this.board.setCell(new Cloud(this.board, this.index));
                return;
            }
        }

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
