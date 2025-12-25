package Simulation.Particles;

/*
 * ISSUE: Cells don't get updated at the same time, 
 * so for example one can place new cells in front of another before it checks, 
 * making it non-deterministic
 * 
 * Proposed Fix: Make a static var that controls all cells, so each tick they all either check or update
 */

import java.awt.Color;
import java.util.ArrayList;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class GolCell extends Particle {
    private static final Color STD_GOL_COLOR = Color.WHITE;
    private boolean doomed;
    private int[] toPopulate = new int[0];
    private boolean hibernating = true;

    public GolCell(PowderGameBoard board, int index) {
        super(board, Material.GOL_CELL, index);
        this.color = STD_GOL_COLOR;
        this.occlusionValue = 0;
    }

    public boolean isHibernating() {
        return this.hibernating;
    }

    public static int countGoLCellNeighboursAtIndex(PowderGameBoard board, int index) {
        int count = 0;
        Direction[] neighbours = { 
            Direction.UR, Direction.U, Direction.UL, 
            Direction.R,               Direction.L, 
            Direction.DR, Direction.D, Direction.DL 
        };
        
        for (Direction dir : neighbours) {
            Particle particle = board.getCell(board.applyDirToIndex(index, dir));
            if (particle.equals(Material.GOL_CELL)) {
                GolCell cell = (GolCell)particle;
                if (cell.isHibernating()) {
                    count++;
                }
            }
        }

        return count;
    }

    public void update() {
        this.flipActive();

        if (this.hibernating) {
            this.hibernating = false;
            return;
        }

        for (int ind : this.toPopulate) {
            if (this.board.getCell(ind).equals(Material.EMPTY)) {
                this.board.setCell(new GolCell(this.board, ind));
            }
        }

        if (this.doomed) {
            this.board.setCell(new Empty(this.board, this.index));
            return;
        }

        int neighbourCount = countGoLCellNeighboursAtIndex(this.board, this.index);
        if (neighbourCount < 2 || neighbourCount > 3) {
            this.doomed = true;
        }

        Direction[] neighbours = { 
            Direction.UR, Direction.U, Direction.UL, 
            Direction.R,               Direction.L, 
            Direction.DR, Direction.D, Direction.DL 
        };
        ArrayList<Particle> validNeighbours = new ArrayList<Particle>();
        for (Direction dir  : neighbours) {
            Particle particle = this.board.getCell(this.board.applyDirToIndex(this.index, dir));
            if (particle.equals(Material.EMPTY) && countGoLCellNeighboursAtIndex(this.board, particle.getIndex()) == 3) {
                validNeighbours.add(particle);
            }
        }
        this.toPopulate = new int[validNeighbours.size()];
        if (this.toPopulate.length > 0) {
            int toPopulateIndex = 0;
            for (Particle par : validNeighbours) {
                this.toPopulate[toPopulateIndex] = par.getIndex();
                toPopulateIndex++;
            }
        }
    }
}
