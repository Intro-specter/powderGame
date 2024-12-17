import java.util.ArrayList;

/*
 * TODO: Add an update method to update every cell according to Particle and checkedList
 */

public class PowderGameBoard {
    private ArrayList<Particle> board;
    private int width;
    private int height;

    public PowderGameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new ArrayList<Particle>();
        for (int i = 0; i < width*height; i++) {
            this.board.add(new Empty(i));
        }
        this.createBarrier();
    }

    public PowderGameBoard() {
        this(10, 10);
    }

    public ArrayList<Particle> getboard() {
        return board;
    }

    public int getSize() {
        return this.width * this.height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.changeBoardWidth(width);
        this.width = width;
        this.createBarrier();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.changeBoardHeight(height);
        this.height = height;
        this.createBarrier();
    }

    public void createBarrier() {
        this.board.replaceAll(element -> (element.equals(Material.BARRIER)) ? new Empty(element.getIndex()) : element); // clear BARRIERs from board
        System.out.println(this.board.size());
        for (int i = 0; i < this.width; i++) { // Floor and ceiling
            this.setCell(new Barrier(i));
            this.setCell(new Barrier((this.height - 1) * this.width + i));
        }
        for (int j = 1; j < this.height - 1; j++) { // Sides
            this.setCell(new Barrier(this.width * j));
            this.setCell(new Barrier(this.width * j + this.width - 1));
        }
    }

    public void changeBoardWidth(int newWidth) { // to be executed BEFORE setting this.width
        // We insert or remove an element at the end of each row
        int difference = newWidth - this.width;
        if (difference > 0) { // if widening the board
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < difference; j++) {
                    this.board.add(i * this.width + this.width + i * difference + j, new Empty(i * this.width + this.width + i * difference + j));
                }
            } 
        } else if (difference < 0) { // if shrinking the board
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j > difference; j--) {
                    this.board.remove(i * this.width + this.width + i * difference + difference);
                }
            } 
        }
    }

    public void changeBoardHeight(int newHeight) { // to be executed BEFORE setting this.height
        // We insert or remove elements at the end of the board
        int difference = (newHeight - this.height) * this.width;
        if (difference > 0) { // if lengthening the board
            for (int i = 0; i < difference; i++) {
                this.board.add(new Empty(this.board.size() + 1));
            }
        } else if (difference < 0) { // if shortening the board
            for (int i = 0; i > difference; i--) {
                this.board.remove(this.board.size() - 1);
            }
        }
    }

    public int[] indexToPos(int index) {
        int[] pos = new int[2];
        pos[0] = index % this.width; // x
        pos[1] = index / this.width; // y
        return pos;
    }

    public int posToIndex(int x, int y) {
        return x + this.width * y;
    }

    public int cellIndexToCheckedIndex(int index) {
        int[] pos = indexToPos(index);
        return posToIndex(pos[0]-1, pos[1]-1);
    }

    public int checkedIndexToCellIndex(int index) {
        int[] pos = new int[2];
        pos[0] = index % (this.width - 2); // x
        pos[1] = index / (this.width - 2); // y
        return posToIndex(pos[0]+1, pos[1]+1);
    }

    public int applyDirToIndex(int index, Direction dir) {
        switch (dir) {
            case UL:
                return index - this.width - 1;
            case U:
                return index - this.width;
            case UR:
                return index - this.width + 1; 
            case L:
                return index - 1;
            case R:
                return index + 1;
            case DL:
                return index + this.width - 1;
            case D:
                return index + this.width;
            case DR: 
                return index + this.width + 1;
            default: // case Particle.C
                return index;
        }
    }

    public Particle getNearbyParticle(int index, Direction dir) {
        return this.board.get(this.applyDirToIndex(index, dir));
    }

    public Particle getCell(int index) {
        return this.board.get(index);
    }

    public void setCell(Particle particle) {
        this.board.set(particle.getIndex(), particle);
    }

    private void moveCell(int index, Particle particle) {
        particle.setIndex(index);
        this.setCell(particle);
    }

    public void swapCells(int firstIndex, int secondIndex) {
        Particle temp = this.getCell(secondIndex);
        this.moveCell(secondIndex, this.getCell(firstIndex));
        this.moveCell(firstIndex, temp);
    }

    public void update() {
        for (Particle particle : this.board) {
            if (particle.getActive()) {
                particle.update(this);
            }
        }
    }

    public void readyBoard() {
        for (Particle particle : this.board) {
            if (!particle.getActive()) {
                particle.flipActive();
            }
        }
    }

    public void executeTick() {
        this.readyBoard();
        this.update();
    }

    @Override
    public String toString() {
        String outString = "";
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                outString += this.board.get(posToIndex(x, y)) + " ";
            }
            outString += "\n";
        }
        return outString;
    }
}
