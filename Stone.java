import java.util.Random;

public class Stone extends Particle {
    private static Random rng = new Random();
    private static final int WATER_SWAP_CHANCE = 2;
    private static final int SAND_SWAP_CHANCE = 1000;

    public Stone(int index) {
        super(Material.STONE, index);
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

    public void update(PowderGameBoard board) {
        this.flipActive();

        Particle downParticle = board.getNearbyParticle(this.index, Direction.D);
        if (this.canSwap(downParticle.getMaterial())) {
            board.swapCells(this.index, downParticle.getIndex());
            return;
        }
    }
}
