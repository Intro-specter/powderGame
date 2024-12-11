/*
 * TODO: Define Material properties (where to check and in what order)
 * Maybe a method that takes an instance of PowderGameBoard to check cells of?
 * then do so in an order to define Behaviour
 */

public enum Material {
    BARRIER,
    EMPTY,
    SAND, 
    WATER;

    public Action getInteraction(Material other) {
        switch (this) {
            case SAND:
                switch (other) {
                    case EMPTY:
                        return Action.SWAP;
                    case SAND:
                        return Action.STOP;
                    case WATER:
                        return Action.SWAP;
                }
            case WATER:
                switch (other) {
                    case EMPTY:
                        return Action.SWAP;
                    case SAND:
                        return Action.STOP;
                    case WATER:
                        return Action.STOP;
                }
        }
        return Action.STOP;
    }

    @Override
    public String toString() {
        switch (this) {
            case BARRIER:
                return "■";
            case SAND:
                return "▲";
            case WATER:
                return "~";
            default:
                return ".";
        }
    }
}
