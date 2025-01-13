package Simulation.Particles;
/*
 * Currently, for things like the canSwap() fn of subclasses, I use switch cases for unique behaviour
 * respective to each other material, which creates exponential complexity. I might try putting some intermediate
 * classes or attributes in to differentiate powders, liquids and gases and then define behaviour according
 * to traits like density, velocity, temperature, etc. though this requires a complete overhaul of the
 * current particle simulation.
 * 
 * TODO: make board a static attribute instead of a parameter
 */

import java.awt.Color;
import java.lang.reflect.Field;

import Simulation.Material;
import Simulation.PowderGameBoard;

public class Particle {
    protected Material material;
    protected PowderGameBoard board;
    protected int index;
    protected boolean active = false;

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

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void flipActive() {
        this.setActive(!this.active);
    }

    public void update() { // we overload this to implement the per tick updates
        return;
    }

    public boolean hasAttribute(String attributeName) { // use this to check for possible generic actions in update
        Class<?> type = this.getClass();
        for (Field field : type.getFields()) {
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
        return this.material.toColor();
    }

    public String getName() {
        return this.material.getName();
    }

    @Override
    public String toString() {
        return this.material.toString();
    }
}
