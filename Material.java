public enum Material {
    BARRIER,
    EMPTY,
    SAND, 
    WATER;

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
