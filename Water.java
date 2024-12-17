import java.util.Random;

public class Water extends Particle {
    private static Random rng = new Random();

    public Water(int index) {
        super(Material.WATER, index);
    }

    public void update(PowderGameBoard board) {
        this.flipActive();

        boolean dopen = false;
        boolean lopen = false;
        boolean ropen = false;

        switch (board.getCell(board.applyDirToIndex(this.index, Direction.D)).getMaterial()) {
            case Material.EMPTY:
                dopen = true;
                break;
            default:
                break;
        }

        if (dopen) {
            board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.D));
            return;
        } 

        switch (board.getCell(board.applyDirToIndex(this.index, Direction.L)).getMaterial()) {
            case Material.EMPTY:
                lopen = true;
                break;
            default:
                break;
        }
        switch (board.getCell(board.applyDirToIndex(this.index, Direction.R)).getMaterial()) {
            case Material.EMPTY:
                ropen = true;
                break;
            default:
                break;
        }
        if (lopen) {
            if (ropen) {
                if (rng.nextInt(2)==1) {
                    board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.L));
                } else {
                    board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.R));
                }
            } else {
                board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.L));
            }
        } else if (ropen) {
            board.swapCells(this.index, board.applyDirToIndex(this.index, Direction.R));
        }
    }
}
