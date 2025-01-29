package Simulation.Particles;

import java.awt.Color;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Seed extends Particle {
    private static Random rng = new Random();
    private static final Color STD_SEED_COLOR = new Color(50, 150, 50);
    private static final int WATER_SWAP_CHANCE = 15;
    private static final int GROW_CHANCE = 2;
    private static final int GENERATE_FIRE_CHANCE = 5;
    private static final int CATCH_FIRE_CHANCE = 5;
    private static final int BURN_AWAY_CHANCE = 5;
    private static final int SPREAD_FIRE_CHANCE = 2;
    private boolean burning = false;

    public Seed(PowderGameBoard board, int index) {
        super(board, Material.SEED, index);
        this.occlusionValue = 3;
        this.color = STD_SEED_COLOR;
    }

    public boolean canSwap(Material otherMaterial) {
        if (otherMaterial.isIn(Material.GASES)) {return true;}

        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.WATER:
                return rng.nextInt(WATER_SWAP_CHANCE) == 0;
            default:
                return false;
        }
    }

    public void catchFire() {
        this.burning = (this.burning) ? true : (rng.nextInt(CATCH_FIRE_CHANCE) == 0);
    }

    public boolean burn() {
        if (rng.nextInt(GENERATE_FIRE_CHANCE) == 0 && this.board.getNearbyParticle(this.index, Direction.U).equals(Material.EMPTY)) {
            this.board.setCell(new Fire(this.board, this.board.applyDirToIndex(this.index, Direction.U)));
            return true;
        } else if (rng.nextInt(BURN_AWAY_CHANCE) == 0) {
            this.board.setCell(new Fire(this.board, this.index));
            return true;
        } else if (rng.nextInt(SPREAD_FIRE_CHANCE) == 0) {
            return this.neighbourInteraction();
        }
        return false;
    }

    public boolean neighbourInteraction() {
        Direction[] neighbours = {Direction.U, Direction.UR, Direction.UL, Direction.L, Direction.R, Direction.D, Direction.DR, Direction.DL};

        for (Direction dir : neighbours) {
            Particle particle = this.board.getCell(this.board.applyDirToIndex(this.index, dir));

            if (particle.equals(Material.WATER) || particle.equals(Material.ICE)) {
                this.burning = false;
            } else if (particle.equals(Material.SEED)) {
                Seed seed = (Seed)(particle);
                seed.catchFire();
                return true;
            } else if (particle.equals(Material.WOOD)) {
                Wood seed = (Wood)(particle);
                seed.catchFire();
                return true;
            }
        }

        return false;
    }

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, STD_SEED_COLOR, Color.BLACK);
    }

    public boolean canGrow() {
        Direction[] neighbours = { Direction.L, Direction.R, Direction.D };
        for (Direction dir : neighbours) {
            if (this.board.getCell(this.board.applyDirToIndex(this.index, dir)).getMaterial().isIn(Material.SOIL_MATERIALS)) {
                return true;
            }
        }
        return false;
    }

    public void grow() {
        Direction dir = Direction.U;
        if (this.board.getNearbyParticle(this.index, dir).equals(Material.SEED) && rng.nextInt(GROW_CHANCE) == 0) {
            Seed seed = (Seed)this.board.getNearbyParticle(this.index, dir);
            seed.grow();
        } else if (this.board.getNearbyParticle(this.index, dir).equals(Material.WOOD) && rng.nextInt(GROW_CHANCE) == 0) {
            Wood wood = (Wood)this.board.getNearbyParticle(this.index, dir);
            wood.grow(dir);
        } else if ((this.board.getNearbyParticle(this.index, dir).equals(Material.EMPTY) || this.board.getNearbyParticle(this.index, dir).equals(Material.WATER) || this.board.getNearbyParticle(this.index, dir).equals(Material.CLOUD)) && rng.nextInt(GROW_CHANCE) == 0) {
            this.board.setCell(new Wood(this.board, this.board.applyDirToIndex(this.index, dir)));
            return;
        }
    }

    public void update() {
        this.flipActive(); 

        if (this.burning && this.burn()) {
            return;
        } else if (this.canGrow()) {
            this.grow();
        }    

        Particle downParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.D));

        if (this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, downParticle.getIndex());
        }

        Particle downLeftParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.DL));
        Particle downRightParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.DR));
        if (this.canSwap(downLeftParticle.getMaterial())) {
            if (this.canSwap(downRightParticle.getMaterial()) && rng.nextInt(2) == 0) {
                this.board.swapCells(this.index, downRightParticle.getIndex());
                return;
            }
            this.board.swapCells(this.index, downLeftParticle.getIndex());
            return;
        } else if (this.canSwap(downRightParticle.getMaterial())) {
            this.board.swapCells(this.index, downRightParticle.getIndex());
            return;
        }
    }
}
