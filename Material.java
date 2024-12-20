import java.awt.Color;

public enum Material {
    BARRIER,
    EMPTY,
    SAND, 
    WATER;

    public static final Color EMPTY_COLOR = new Color(50, 50, 50);
    public static final Color BARRIER_COLOR = new Color(0, 0, 100);
    public static final Color SAND_COLOR = new Color(255, 255, 100);
    public static final Color WATER_COLOR = new Color(100, 100, 255);

    public Color toColor() {
        switch (this) {
            case EMPTY:
                return EMPTY_COLOR;
            case BARRIER:
                return BARRIER_COLOR;
            case SAND:
                return SAND_COLOR;
            case WATER:
                return WATER_COLOR;
            default:
                return Color.BLACK;
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        switch (this) {
            case EMPTY:
                return ".";
            case BARRIER:
                return "■";
            case SAND:
                return ANSI_YELLOW + "■" + ANSI_RESET;
            case WATER:
                return ANSI_BLUE + "■" + ANSI_RESET;
            default:
                return "?";
        }
    }
}
