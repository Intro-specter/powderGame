import java.util.Random;

public class Sand extends Particle {
    private static Random rng = new Random();

    public Sand(int index) {
        super(Material.SAND, index);
    }

    public void update(PowderGameBoard board) {
        this.flipActive();

        boolean dopen = false;
        boolean dlopen = false;
        boolean dropen = false;

        switch (board.getCell(board.applyDirToIndex(this.index, Direction.D)).getMaterial()) {
            case Material.EMPTY:
                dopen = true;
                break;
            case Material.WATER:
                dopen = true;
                break;
            default:
                break;
        }

        if (dopen) {
            board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.D));
            return;
        } 

        switch (board.getCell(board.applyDirToIndex(this.index, Direction.DL)).getMaterial()) {
            case Material.EMPTY:
                dlopen = true;
                break;
            case Material.WATER:
                dlopen = true;
                break;
            default:
                break;
        }
        switch (board.getCell(board.applyDirToIndex(this.index, Direction.DR)).getMaterial()) {
            case Material.EMPTY:
                dropen = true;
                break;
            case Material.WATER:
                dropen = true;
                break;
            default:
                break;
        }
        if (dlopen) {
            if (dropen) {
                if (rng.nextInt(2)==1) {
                    board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.DL));
                } else {
                    board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.DR));
                }
            } else {
                board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.DL));
            }
        } else if (dropen) {
            board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.DR));
        }
    }
}
