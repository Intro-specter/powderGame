package Simulation.Particles;

import java.awt.Color;
import java.util.Random;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Wood extends Particle {
    private static Random rng = new Random();
    private static final Color FIRST_STD_WOOD_COLOR = new Color(150, 75, 0);
    private static final Color SECOND_STD_WOOD_COLOR = new Color(100, 50, 0);
    private static final int BRANCH_POINT_CHANCE = 25;
    private static final int WATER_SWAP_CHANCE = 10;
    private static final int GROW_CHANCE = 10;
    private static final int GROW_SEED_CHANCE = 50;
    private static final int GENERATE_FIRE_CHANCE = 10;
    private static final int CATCH_FIRE_CHANCE = 10;
    private static final int BURN_AWAY_CHANCE = 25;
    private static final int SPREAD_FIRE_CHANCE = 4;
    private boolean altColor;
    private boolean burning = false;
    private boolean branchPoint;

    public Wood(PowderGameBoard board, int index) {
        super(board, Material.WOOD, index);
        this.occlusionValue = 7;
        this.altColor = rng.nextBoolean();
        this.branchPoint = rng.nextInt(BRANCH_POINT_CHANCE) == 0;
        this.color = (this.altColor) ? SECOND_STD_WOOD_COLOR : FIRST_STD_WOOD_COLOR;
    }

    public boolean rollGrowChance() {
        return rng.nextInt(GROW_CHANCE) < 9;
    }

    public void grow(Direction dir) {
        if (this.branchPoint) {
            Direction[] upDirs = { Direction.U, Direction.UL, Direction.UR };
            for (Direction newDir : upDirs) {
                if (this.board.getNearbyParticle(this.index, newDir).equals(Material.SEED) && this.rollGrowChance()) {
                    Seed seed = (Seed)this.board.getNearbyParticle(this.index, newDir);
                    seed.grow();
                } else if (this.board.getNearbyParticle(this.index, newDir).equals(Material.WOOD) && this.rollGrowChance()) {
                    Wood wood = (Wood)this.board.getNearbyParticle(this.index, newDir);
                    wood.grow(newDir);
                }
            }
        } else {
            if (this.board.getNearbyParticle(this.index, dir).equals(Material.SEED) && this.rollGrowChance()) {
                Seed seed = (Seed)this.board.getNearbyParticle(this.index, dir);
                seed.grow();
            } else if (this.board.getNearbyParticle(this.index, dir).equals(Material.WOOD) && this.rollGrowChance()) {
                Wood wood = (Wood)this.board.getNearbyParticle(this.index, dir);
                wood.grow(dir);
            }
        }

        if (this.branchPoint) {
            Direction[] upDirs = { Direction.U, Direction.UL, Direction.UR };
            for (Direction newDir : upDirs) {
                if ((this.board.getNearbyParticle(this.index, newDir).equals(Material.EMPTY) || this.board.getNearbyParticle(this.index, newDir).equals(Material.WATER) || this.board.getNearbyParticle(this.index, newDir).equals(Material.CLOUD)) && this.rollGrowChance()) {
                    this.board.setCell((rng.nextInt(GROW_SEED_CHANCE) == 0) ? new Seed(this.board, this.board.applyDirToIndex(this.index, newDir)) : new Wood(this.board, this.board.applyDirToIndex(this.index, newDir)));
                    return;
                }
            }
        } else {
            if ((this.board.getNearbyParticle(this.index, dir).equals(Material.EMPTY) || this.board.getNearbyParticle(this.index, dir).equals(Material.WATER)) && this.rollGrowChance()) {
                this.board.setCell((rng.nextInt(GROW_SEED_CHANCE) == 0) ? new Seed(this.board, this.board.applyDirToIndex(this.index, dir)) : new Wood(this.board, this.board.applyDirToIndex(this.index, dir)));
                return;
            }
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

            if (particle.equals(Material.SEED)) {
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

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, (this.altColor) ? SECOND_STD_WOOD_COLOR : FIRST_STD_WOOD_COLOR, Color.BLACK);
    }

    public boolean isSupported() {
        return (this.board.getCell(this.board.applyDirToIndex(this.index, Direction.DL)).getMaterial().isIn(Material.STRUCTURAL_MATERIALS)) || (this.board.getCell(this.board.applyDirToIndex(this.index, Direction.DR)).getMaterial().isIn(Material.STRUCTURAL_MATERIALS));
    }

    public void update() {
        this.flipActive();

        if (this.burning && this.burn()) {
            return;
        }

        Particle downParticle = this.board.getCell(this.board.applyDirToIndex(this.index, Direction.D));

        if (!this.isSupported() && this.canSwap(downParticle.getMaterial())) {
            this.board.swapCells(this.index, this.board.applyDirToIndex(this.index, Direction.D));
        }
    }
}
