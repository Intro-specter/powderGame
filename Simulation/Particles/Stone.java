package Simulation.Particles;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;
import java.awt.Color;

public class Stone extends Particle {
    private static Random rng = new Random();
    private static final Color STD_STONE_COLOR = new Color(150, 125, 125);
    private static final int WATER_SWAP_CHANCE = 2;
    private static final int SAND_SWAP_CHANCE = 1000;

    public Stone(PowderGameBoard board, int index) {
        super(board, Material.STONE, index);
        this.color = STD_STONE_COLOR;
        this.occlusionValue = 10;
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.CLOUD:
                return true;
            case Material.WATER:
                return rng.nextInt(WATER_SWAP_CHANCE) == 0;
            case Material.SAND:
                return rng.nextInt(SAND_SWAP_CHANCE) == 0;
            default:
                return false;
        }
    }

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, STD_STONE_COLOR, Color.BLACK);
    }

    public void update() {
        this.flipActive();

        if (this.board.getNearbyParticle(this.index, Direction.DR).equals(Material.STONE) || this.board.getNearbyParticle(this.index, Direction.L).equals(Material.STONE)) {
            return;
        }

        Particle downParticle = this.board.getNearbyParticle(this.index, Direction.D);
        if (this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, downParticle.getIndex());
            return;
        }
    }
}
