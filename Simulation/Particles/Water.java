package Simulation.Particles;
/*
 * TODO: Try a temperature variable to influence condensation and freezing
 */

import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Water extends Particle {
    private static Random rng = new Random();
    private static final int CLOUD_SWAP_CHANCE = 10;
    private static final int SAND_SWAP_CHANCE = 500;
    private static final int ICE_SWAP_CHANCE = 5;
    private static final int EVAPORATE_CHANCE = 2000;
    private static final int FREEZE_TO_ICE_CHANCE = 5000;
    private static final int WEATHER_STONE_CHANCE = 1000;

    public Water(int index) {
        super(Material.WATER, index);
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

    public void update(PowderGameBoard board) {
        this.flipActive();

        Particle downParticle = board.getCell(board.applyDirToIndex(this.index, Direction.D)); 
        if (this.canSwap(downParticle.getMaterial())) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle leftParticle = board.getCell(board.applyDirToIndex(this.index, Direction.L));
        Particle rightParticle = board.getCell(board.applyDirToIndex(this.index, Direction.R));

        if (leftParticle.getMaterial() == Material.STONE && rng.nextInt(WEATHER_STONE_CHANCE) == 0) {
            board.setCell(new Sand(leftParticle.getIndex()));
            return;
        }

        if (rightParticle.getMaterial() == Material.STONE && rng.nextInt(WEATHER_STONE_CHANCE) == 0) {
            board.setCell(new Sand(rightParticle.getIndex()));
            return;
        }

        if (board.getCell(board.applyDirToIndex(this.index, Direction.U)).getMaterial() == Material.EMPTY) {
            if (rng.nextInt(EVAPORATE_CHANCE) == 0) {
                board.setCell(new Cloud(this.index));
                return;
            } else if (rng.nextInt(FREEZE_TO_ICE_CHANCE) == 0) {
                board.setCell(new Ice(this.index));
                return;
            }
        }

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
