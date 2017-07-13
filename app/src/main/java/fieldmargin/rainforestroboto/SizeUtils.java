package fieldmargin.rainforestroboto;


public class SizeUtils {
    public static int mMaxRows;
    public static int mMaxColumns;

    public static void calculateDimensionOfScene(int cellSize, int sceneWidth, int sceneHeight) {
        mMaxRows = calculateMaxRows(cellSize, sceneHeight);
        mMaxColumns = calculateMaxCols(cellSize, sceneWidth);
    }

    private static int calculateMaxRows(int cellSize, int sceneHeight) {
        return sceneHeight / cellSize;
    }

    private static int calculateMaxCols(int cellSize, int sceneWidth) {
        return sceneWidth / cellSize;
    }


}
