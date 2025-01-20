package Simulation.Particles;
/*
 * Currently, for things like the canSwap() fn of subclasses, I use switch cases for unique behaviour
 * respective to each other material, which creates exponential complexity. I might try putting some intermediate
 * classes or attributes in to differentiate powders, liquids and gases and then define behaviour according
 * to traits like density, velocity, temperature, etc. though this requires a complete overhaul of the
 * current particle simulation.
 */

import java.awt.Color;
import java.lang.reflect.Field;

import Simulation.Direction;
import Simulation.Material;
import Simulation.PowderGameBoard;

public class Particle {
    protected Material material;
    protected PowderGameBoard board;
    protected int index;
    protected boolean active = false;
    protected Color color = Color.BLACK;
    protected int occlusionValue = 0;

    public Particle(PowderGameBoard board, Material material, int index) {
        this.board = board;
        this.material = material;
        this.index = index;
    }

    public Material getMaterial() {
        return this.material;
    }

    // no setMaterial since it's not supposed to change

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOcclusionValue() {
        return this.occlusionValue;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void flipActive() {
        this.setActive(!this.active);
    }

    protected void stdOcclusion(int totalOcclusionValue, Color start, Color end) {
        this.color = Particle.shiftColorTowardsTarget(start, end, this.board.getHeight()*this.board.MAX_OCCLUSION, totalOcclusionValue);
    }

    public void applyOcclusion(int totalOcclusionValue) {
        this.stdOcclusion(totalOcclusionValue, Color.WHITE, Color.BLACK);
    }

    public int getDepthInDirection(Direction dir) {
        int count = 0;
        int searchIndex = this.index;
        do {
            count++;
            searchIndex += board.dirToIndex(dir);
        } while (board.getCell(searchIndex).equals(this) && !board.getCell(searchIndex).equals(Material.BARRIER));
        return count;
    }

    public static Color addToColor(Color color, int add) {
        return new Color((color.getRed() > Math.abs(add)) ? (color.getRed() + add) % 256 : color.getRed(), 
            (color.getGreen() > Math.abs(add)) ? (color.getGreen() + add) % 256 : color.getGreen(), 
            (color.getBlue() > Math.abs(add)) ? (color.getBlue() + add) % 256 : color.getBlue());
    }

    public static Color shiftColorTowardsTarget(Color color, Color targetColor, int max, int progress) {
        return new Color(color.getRed()+(targetColor.getRed()-color.getRed())*progress/max,
        color.getGreen()+(targetColor.getGreen()-color.getGreen())*progress/max,
        color.getBlue()+(targetColor.getBlue()-color.getBlue())*progress/max);
    } 

    public void update() { // we overload this to implement the per tick updates
        return;
    }

    public boolean hasAttribute(String attributeName) { // use this to check for possible generic actions in update
        Class<?> type = this.getClass();
        for (Field field : type.getDeclaredFields()) {
            if (field.getName().equals(attributeName)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Particle other) {
        return this.material == other.getMaterial();
    }

    public boolean equals(Material other) {
        return this.material == other;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return this.material.getName();
    }

    @Override
    public String toString() {
        return this.material.toString();
    }
}
