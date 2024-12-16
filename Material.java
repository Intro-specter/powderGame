public enum Material {
    BARRIER,
    EMPTY,
    SAND, 
    WATER;

    @Override
    public String toString() {
        switch (this) {
            case EMPTY:
                return ".";
            case BARRIER:
                return "■";
            case SAND:
                return "*";
            case WATER:
                return "~";
            default:
                return "?";
        }
    }
}
