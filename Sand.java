import java.util.Random;

public class Sand extends Particle {
    private static Random rng = new Random();
    private static final int WATER_SWAP_CHANCE = 20;
    private static final int COMPRESS_TO_STONE_CHANCE = 10000;

    public Sand(int index) {
        super(Material.SAND, index);
    }

    public boolean canSwap(Material otherMaterial) {
        switch (otherMaterial) {
            case Material.EMPTY:
                return true;
            case Material.CLOUD:
                return true;
            case Material.WATER:
                return rng.nextInt(WATER_SWAP_CHANCE) == 0; // (100/bound)% chance
            default:
                return false;
        }
    }

    public boolean canCompress(PowderGameBoard board) {
        Particle downParticle = board.getNearbyParticle(this.index, Direction.D);
        Particle upParticle = board.getNearbyParticle(this.index, Direction.U);
        if ((downParticle.getMaterial() == Material.BARRIER || downParticle.getMaterial() == Material.SAND) && (upParticle.getMaterial() == Material.BARRIER || upParticle.getMaterial() == Material.SAND)) {
            return rng.nextInt(COMPRESS_TO_STONE_CHANCE) == 0;
        }
        return false;
    }

    public void update(PowderGameBoard board) { // TODO: Add ability to compress into Stone
        this.flipActive();

        if (this.canCompress(board)) {
            board.setCell(new Stone(this.index));
        }

        Particle downParticle = board.getCell(board.applyDirToIndex(this.index, Direction.D)); 
        if (this.canSwap(downParticle.getMaterial())) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }

        Particle downLeftParticle = board.getCell(board.applyDirToIndex(this.index, Direction.DL));
        Particle downRightParticle = board.getCell(board.applyDirToIndex(this.index, Direction.DR));
        if (this.canSwap(downLeftParticle.getMaterial())) {
            if (this.canSwap(downRightParticle.getMaterial()) && rng.nextInt(2)==0) {
                board.swapCells(this.index, downRightParticle.getIndex());
                return;
            }
            board.swapCells(this.index, downLeftParticle.getIndex());
            return;
        } else if (this.canSwap(downRightParticle.getMaterial())) {
            board.swapCells(this.index, downRightParticle.getIndex());
            return;
        }
    }
}
