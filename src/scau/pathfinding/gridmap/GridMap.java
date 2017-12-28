package scau.pathfinding.gridmap;

import java.util.ArrayList;
import java.util.List;

public class GridMap {
    private int colCount;
    private int rowCount;
    private Cell[][] cells;
    private Vertex sourceVertex;
    private Vertex targetVertex;
    private Vertex[][] vertexTo;
    private boolean dirtyPath = false;
    private FindingMethod findingMethod;
    private ResizeListener resizedListener = ResizeListener.EMPTY;

    public GridMap(int rowCount, int colCount) {
        this.findingMethod = FindingMethod.BFS;
        setUpCells(rowCount, colCount);
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    public Vertex[][] getVertexTo() {
        return vertexTo;
    }

    public boolean isDirtyPath() {
        return dirtyPath;
    }

    public Vertex getSource() {
        return sourceVertex;
    }

    public Vertex getTarget() {
        return targetVertex;
    }

    public Iterable<Vertex> getNeighbors(Vertex position) {
        return getNeighbors(position.getRow(), position.getCol());
    }

    public Iterable<Vertex> getNeighbors(int row, int col) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        if (row - 1 >= 0 && cells[row - 1][col].isPassable()) {
            neighbors.add(new Vertex(row - 1, col, Direction.Up));
        }
        if (col - 1 >= 0 && cells[row][col - 1].isPassable()) {
            neighbors.add(new Vertex(row, col - 1, Direction.Left));
        }
        if (row + 1 < rowCount && cells[row + 1][col].isPassable()) {
            neighbors.add(new Vertex(row + 1, col, Direction.Down));
        }
        if (col + 1 < colCount && cells[row][col + 1].isPassable()) {
            neighbors.add(new Vertex(row, col + 1, Direction.Right));
        }
        return neighbors;
    }

    public ResizeListener getResizedListener() {
        return resizedListener;
    }

    public void setResizedListener(ResizeListener resizedListener) {
        this.resizedListener = resizedListener;
    }

    public void setUpCells(int row, int col) {
        this.rowCount = row;
        this.colCount = col;
        cells = new Cell[col][];
        for (int i = 0; i < col; ++i) {
            cells[i] = new Cell[row];
            for (int j = 0; j < row; ++j) {
                cells[i][j] = new Cell();
            }
        }

        vertexTo = new Vertex[col][];
        for (int i = 0; i < col; ++i) {
            vertexTo[i] = new Vertex[row];
        }

        setSource(0, 0);
        setTarget(rowCount - 1, colCount - 1);

        resizedListener.handle(row, col);
    }

    public void setCellType(int row, int col, Cell.Type type) {
        dirtyPath = true;
        cells[row][col].setType(type);
    }

    public void putBlock(int row, int col) {
        setCellType(row, col, Cell.Type.BLOCK);
    }

    public void removeBlock(int row, int col) {
        setCellType(row, col, Cell.Type.EMPTY);
    }

    public void setSource(int row, int col) {
        if (sourceVertex != null) {
            cells[sourceVertex.getRow()][sourceVertex.getCol()].setType(Cell.Type.EMPTY);
            setCellType(row, col, Cell.Type.SOURCE);
        }
        sourceVertex = new Vertex(row, col, cells[row][col].getWeight());
    }

    public void setTarget(int row, int col) {
        if (targetVertex != null) {
            cells[targetVertex.getRow()][targetVertex.getCol()].setType(Cell.Type.EMPTY);
            setCellType(row, col, Cell.Type.TARGET);
        }
        targetVertex = new Vertex(row, col, cells[row][col].getWeight());
    }

    public void updatePath(FindingMethod method) {
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                cells[i][j].setDirection(Direction.None);
                vertexTo[i][j] = null;
            }
        }
        GridPathSearch gps = null;
        switch (method) {
            case BFS:
                gps = new BFS(this);
                break;
            case DIJKSTRA:
                gps = new Dijkstra(this, true);
                break;
            case DIJKSTRA_WITHOUT_WEIGHT:
                gps = new Dijkstra(this, false);
                break;
            case A_STAR:
                gps = new AStarSearch(this);
                break;
            default:
                return;
        }
        cells = gps.getCells();
        vertexTo = gps.getVertexTo();
        System.out.println("Finished in " + gps.getStepCount() + " steps.");
        dirtyPath = false;
    }

    public FindingMethod getFindingMethod() {
        return findingMethod;
    }

    public void setFindingMethod(FindingMethod findingMethod) {
        this.findingMethod = findingMethod;
    }

    public void update() {
        if (isDirtyPath()) {
            updatePath();
        }
    }

    public void updatePath() {
        updatePath(findingMethod);
    }

    public void setMapSize(int row, int col) {
        setUpCells(row, col);
    }

    public Cell cell(int row, int col) {
        return cells[row][col];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public static enum FindingMethod {
        BFS,
        DIJKSTRA,
        DIJKSTRA_WITHOUT_WEIGHT,
        A_STAR
    }

    public interface ResizeListener {
        public static ResizeListener EMPTY = new ResizeListener() {
            @Override
            public void handle(int row, int col) {
                // do nothing
            }
        };

        void handle(int row, int col);
    }
}