import java.util.Random;

public class Water extends Particle {
    private static Random rng = new Random();
    private static final int CLOUD_SWAP_CHANCE = 10;
    private static final int SAND_SWAP_CHANCE = 500;
    private static final int EVAPORATE_CHANCE = 200;
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
            default:
                return false;
        }
    }

    public void update(PowderGameBoard board) { // TODO: Add ability to turn Stone to Sand
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

        if (downParticle.getMaterial() == Material.WATER && leftParticle.getMaterial() == Material.WATER && rightParticle.getMaterial() == Material.WATER && board.getCell(board.applyDirToIndex(this.index, Direction.U)).getMaterial() == Material.EMPTY && rng.nextInt(EVAPORATE_CHANCE) == 0) {
            board.setCell(new Cloud(this.index));
            return;
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
