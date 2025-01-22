package Simulation;

import java.util.ArrayList;
import java.util.Collections;

import GUI.MouseHandler;
import Simulation.Particles.Barrier;
import Simulation.Particles.Empty;
import Simulation.Particles.Particle;

public class PowderGameBoard {
    public final int MAX_OCCLUSION = 10;
    private ArrayList<Particle> board;
    private int width;
    private int height;
    private int placingRadius = 1;

    public PowderGameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new ArrayList<Particle>();
        for (int i = 0; i < width * height; i++) {
            this.board.add(new Empty(this, i));
        }
        this.createBarrier();
    }

    public PowderGameBoard() {
        this(10, 10);
    }

    public int getPlacingRadius() {
        return this.placingRadius;
    }

    public void setPlacingRadius(int radius) {
        this.placingRadius = radius;
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

    public void wipe() {
        this.board.replaceAll(
                element -> (element.equals(Material.BARRIER)) ? element : new Empty(this, element.getIndex())); // clear
                                                                                                                // non-BARRIERs
                                                                                                                // from
                                                                                                                // board
    }

    public void createBarrier() {
        this.board.replaceAll(
                element -> (element.equals(Material.BARRIER)) ? new Empty(this, element.getIndex()) : element); // clear
                                                                                                                // BARRIERs
                                                                                                                // from
                                                                                                                // board
        for (int i = 0; i < this.width; i++) { // Floor and ceiling
            this.setCell(new Barrier(this, i));
            this.setCell(new Barrier(this, (this.height - 1) * this.width + i));
        }
        for (int j = 1; j < this.height - 1; j++) { // Sides
            this.setCell(new Barrier(this, this.width * j));
            this.setCell(new Barrier(this, this.width * j + this.width - 1));
        }
    }

    public void changeBoardWidth(int newWidth) { // to be executed BEFORE setting this.width
        // We insert or remove an element at the end of each row
        int difference = newWidth - this.width;
        if (difference > 0) { // if widening the board
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < difference; j++) {
                    this.board.add(i * this.width + this.width + i * difference + j,
                            new Empty(this, i * this.width + this.width + i * difference + j));
                }
            }
        } else if (difference < 0) { // if shrinking the board
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j > difference; j--) {
                    this.board.remove(i * this.width + this.width + i * difference + difference);
                }
            }
        }
        this.assignAllIndices();
    }

    public void changeBoardHeight(int newHeight) { // to be executed BEFORE setting this.height
        // We insert or remove elements at the end of the board
        int difference = (newHeight - this.height) * this.width;
        if (difference > 0) { // if lengthening the board
            for (int i = 0; i < difference; i++) {
                this.board.add(new Empty(this, this.board.size() + 1));
            }
        } else if (difference < 0) { // if shortening the board
            for (int i = 0; i > difference; i--) {
                this.board.remove(this.board.size() - 1);
            }
        }
        this.assignAllIndices();
    }

    public int[] indexToVec(int index) {
        int[] vec = new int[2];
        vec[0] = index % this.width; // x
        vec[1] = index / this.width; // y
        return vec;
    }

    public int vecToIndex(int x, int y) {
        return x + this.width * y;
    }

    public int dirToIndex(Direction dir) {
        switch (dir) {
            case UL:
                return -this.width - 1;
            case U:
                return -this.width;
            case UR:
                return -this.width + 1;
            case L:
                return -1;
            case R:
                return 1;
            case DL:
                return this.width - 1;
            case D:
                return this.width;
            case DR:
                return this.width + 1;
            default:
                return 0;
        }
    }

    public int applyDirToIndex(int index, Direction dir) {
        return index + dirToIndex(dir);
    }

    public void assignAllIndices() {
        for (int i = 0; i < this.board.size(); i++) {
            this.getCell(i).setIndex(i);
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

    public void attemptPlace(int index, MouseHandler mouseHandler) {
        int[] vec = indexToVec(index);
        int xEnd = (vec[0] < this.width - this.placingRadius) ? vec[0] + this.placingRadius + 1 : this.width;
        int yEnd = (vec[1] < this.height - this.placingRadius) ? vec[1] + this.placingRadius + 1 : this.height;
        for (int i = (vec[0] > this.placingRadius) ? vec[0] - this.placingRadius : 0; i < xEnd; i++) {
            for (int j = (vec[1] > this.placingRadius) ? vec[1] - this.placingRadius : 0; j < yEnd; j++) {
                try {
                    if ((this.getCell(this.vecToIndex(i, j)).equals(Material.EMPTY)
                            || mouseHandler.getChosenMaterial().equals(Material.EMPTY))
                            && !this.getCell(this.vecToIndex(i, j)).equals(Material.BARRIER)) {
                        this.setCell(mouseHandler.getChosenMaterial().toParticle(this, this.vecToIndex(i, j)));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void update() {
        @SuppressWarnings("unchecked") // <-- sign of very good programming 👍👍👍
        ArrayList<Particle> copyBoard = (ArrayList<Particle>) this.board.clone();
        Collections.shuffle(copyBoard); // update the array in a random order
        for (Particle particle : copyBoard) {
            if (particle.isActive()) {
                particle.update();
            }
        }
    }

    public void readyBoard() {
        for (Particle particle : this.board) {
            if (!particle.isActive()) {
                particle.flipActive();
            }
        }
    }

    public void downDepthFilter() {
        for (int i = 0; i < this.width; i++) {
            int cumulative_occlusion = 0;
            for (int j = 0; j < this.height; j++) {
                Particle toCheck = this.getCell(this.vecToIndex(i, j));
                cumulative_occlusion += toCheck.getOcclusionValue();
                if (toCheck.getMaterial().isIn(Material.DOWN_DEPTH_RECOLORABLE)) {
                    toCheck.applyOcclusion(cumulative_occlusion);
                }

            }
        }
    }

    public void stepSim() {
        this.readyBoard();
        this.update();
    }

    public void stepFancy() {
        this.stepSim();
        this.downDepthFilter();
    }
    // downDepthFilter has crazy overhead and doesn't really need calling every tick
    // to look nice,
    // so we can separate it to be called every few frames.

    @Override
    public String toString() {
        String outString = "";
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                outString += this.board.get(vecToIndex(x, y)) + " ";
            }
            outString += "\n";
        }
        return outString;
    }
}
