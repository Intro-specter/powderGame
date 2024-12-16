import java.lang.reflect.Field;

public class Particle {
    protected Material material;
    protected int index;
    protected boolean active = false;

    public Particle(Material material, int index) {
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

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void flipActive() {
        this.setActive(!this.active);
    }

    public void update(PowderGameBoard board) { // we overload this to implement the per tick updates
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

    @Override
    public String toString() {
        return this.material.toString();
    }
}
