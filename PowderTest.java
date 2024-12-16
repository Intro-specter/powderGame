public class PowderTest {
    public static void main(String[] args) {
        PowderGameBoard testBoard = new PowderGameBoard();

        // Resizing
        System.out.println(testBoard);
        testBoard.setWidth(5);
        System.out.println(testBoard);
        testBoard.setWidth(10);
        System.out.println(testBoard);
        testBoard.setHeight(5);
        System.out.println(testBoard);
        testBoard.setHeight(10);
        
        testBoard.setCell(new Sand(26));

        System.out.println(testBoard);
        testBoard.executeTick();

        System.out.println(testBoard);
    }
}
