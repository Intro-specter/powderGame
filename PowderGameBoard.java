import java.util.ArrayList;

public class PowderGameBoard {
    private ArrayList<Material> materialBoard;
    private boolean[] checkedList;
    private int width;
    private int height;

    public PowderGameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.checkedList = new boolean[(width-1)*(height-1)];
        this.materialBoard = new ArrayList<Material>();
        for (int i = 0; i < width*height; i++) {
            this.materialBoard.add(Material.EMPTY);
        }
        this.createBarrier();
    }

    public PowderGameBoard() {
        this(10, 10);
    }

    public ArrayList<Material> getMaterialBoard() {
        return materialBoard;
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
        this.checkedList = new boolean[(this.width-1)*(this.height-1)];
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.changeBoardHeight(height);
        this.height = height;
        this.createBarrier();
        this.checkedList = new boolean[(this.width-1)*(this.height-1)];
    }

    public void createBarrier() {
        this.materialBoard.replaceAll(element -> (element == Material.BARRIER) ? Material.EMPTY : element); // clear BARRIERs from board
        for (int i = 0; i < this.width; i++) { // Floor and ceiling
            this.materialBoard.set(i, Material.BARRIER);
            this.materialBoard.set((this.height - 1) * this.width + i, Material.BARRIER);
        }
        for (int j = 1; j < this.height - 1; j++) { // Sides
            this.materialBoard.set(this.width * j, Material.BARRIER);
            this.materialBoard.set(this.width * j + this.width - 1, Material.BARRIER);
        }
    }

    public void changeBoardWidth(int newWidth) { // to be executed BEFORE setting this.width
        // We insert or remove an element at the end of each row
        int difference = newWidth - this.width;
        if (difference > 0) { // if widening the board
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < difference; j++) {
                    this.materialBoard.add(i * this.width + this.width + i * difference + j, Material.EMPTY);
                }
            } 
        } else if (difference < 0) { // if shrinking the board
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j > difference; j--) {
                    this.materialBoard.remove(i * this.width + this.width + i * difference + difference);
                }
            } 
        }
    }

    public void changeBoardHeight(int newHeight) { // to be executed BEFORE setting this.height
        // We insert or remove elements at the end of the board
        int difference = newHeight - this.height;
        if (difference > 0) { // if lengthening the board
            for (int i = 0; i < difference; i++) {
                this.materialBoard.add(Material.EMPTY);
            }
        } else if (difference < 0) { // if shortening the board
            for (int i = 0; i > difference; i--) {
                this.materialBoard.remove(this.materialBoard.size() - 1);
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
            default:
                return index;
        }
    }

    @Override
    public String toString() {
        String outString = "";
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                outString += this.materialBoard.get(posToIndex(x, y)) + " ";
            }
            outString += "\n";
        }
        return outString;
    }
}
