package Simulation.Particles;

import java.awt.Color;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Fire extends Particle {
    private static Random rng = new Random();
    private static final Color STD_FIRE_COLOR = new Color(255, 150, 100);
    private static final Color FIRST_ALT_FIRE_COLOR = new Color(255, 225, 200);
    private static final Color SECOND_ALT_FIRE_COLOR = new Color(255, 255, 225);
    private static final int EVAPORATE_WATER_CHANCE = 10;
    private static final int MELT_ICE_CHANCE = 10;
    private static final int SPUTTER_CHANCE = 20;

    public Fire(PowderGameBoard board, int index) {
        super(board, Material.FIRE, index);
        this.occlusionValue = 0;
        this.randomizeColor();
    }

    public void randomizeColor() {
        this.color = (rng.nextInt(3) == 0) ? STD_FIRE_COLOR : (rng.nextInt(2) == 0) ? FIRST_ALT_FIRE_COLOR : SECOND_ALT_FIRE_COLOR;
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            default:
                return false;
        }
    }

    public boolean neighbourInteraction() {
        Direction[] neighbours = {Direction.U, Direction.L, Direction.R, Direction.D};

        for (Direction dir : neighbours) {
            Particle particle = this.board.getCell(this.board.applyDirToIndex(this.index, dir));

            if (particle.equals(Material.WATER)) {
                if (rng.nextInt(EVAPORATE_WATER_CHANCE) == 0) {
                    this.board.setCell(new Cloud(this.board, particle.getIndex())); 
                } else if (rng.nextInt(SPUTTER_CHANCE) == 0) {
                    this.board.setCell(new Empty(this.board, this.index));
                }
            } else if (particle.equals(Material.ICE)) {
                if (rng.nextInt(MELT_ICE_CHANCE) == 0) {
                    this.board.setCell(new Water(this.board, particle.getIndex()));
                }else if (rng.nextInt(SPUTTER_CHANCE) == 0) {
                    this.board.setCell(new Empty(this.board, this.index));
                }
            } else if (particle.equals(Material.SEED)) {
                Seed seed = (Seed)(particle);
                seed.catchFire();
            } else if (particle.equals(Material.WOOD)) {
                Wood seed = (Wood)(particle);
                seed.catchFire();
            }
        }

        return false;
    }

    public void update() {
        this.flipActive();

        if (rng.nextInt(SPUTTER_CHANCE) == 0) {
            board.setCell(new Empty(board, this.index));
            return;
        }

        this.randomizeColor();

        if (this.neighbourInteraction()) {return;}

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
    }
}
