package scau.pathfinding.visual;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author Link
 */
public class GridMap {

    private final static Image TARGET_IMAGE
            = new Image(GridMap.class.getClassLoader().getResource("scau/pathfinding/resources/target.png").toExternalForm());
    private final static Image SOURCE_IMAGE
            = new Image(GridMap.class.getClassLoader().getResource("scau/pathfinding/resources/source.png").toExternalForm());
    private final static Image BLOCK_IMAGE
            = new Image(GridMap.class.getClassLoader().getResource("scau/pathfinding/resources/block.png").toExternalForm());
    private Font cellTextFont = new Font("chiller", 30);
    private int colCount;
    private int rowCount;
    private double cellHeight;
    private double cellWidth;
    private DoubleProperty height;
    private DoubleProperty width;
    private Cell cells[][];
    private Vertex sourceVertex;
    private Vertex targetVertex;
    private Vertex vertexTo[][];
    private Action action = Action.DISABLE;
    private boolean isMouseInMap;

    private double mousePositionX;
    private double mousePositionY;
    private boolean dirtyPath = false;
    private FindingMethod findingMethod = FindingMethod.BFS;

    public GridMap(double width, double height, int col, int row) {
        widthProperty().set(width);
        heightProperty().set(height);
        setUpCells(row, col);
    }

    private Iterable<Vertex> getNeighbors(Vertex position) {
        List<Vertex> neighbors = new ArrayList<>();
        if (position.getRow() - 1 >= 0 && cells[position.getRow() - 1][position.getCol()].isPassable()) {
            neighbors.add(new Vertex(position.getRow() - 1, position.getCol(), Direction.Up));
        }
        if (position.getCol() - 1 >= 0 && cells[position.getRow()][position.getCol() - 1].isPassable()) {
            neighbors.add(new Vertex(position.getRow(), position.getCol() - 1, Direction.Left));
        }
        if (position.getRow() + 1 < rowCount && cells[position.getRow() + 1][position.getCol()].isPassable()) {
            neighbors.add(new Vertex(position.getRow() + 1, position.getCol(), Direction.Down));
        }
        if (position.getCol() + 1 < colCount && cells[position.getRow()][position.getCol() + 1].isPassable()) {
            neighbors.add(new Vertex(position.getRow(), position.getCol() + 1, Direction.Right));
        }
        return neighbors;
    }

    public final DoubleProperty widthProperty() {
        if (width == null) {
            width = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    // Do Something
                }

                @Override
                public Object getBean() {
                    return GridMap.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return width;
    }

    public final DoubleProperty heightProperty() {
        if (height == null) {
            height = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    // Do something
                }

                @Override
                public Object getBean() {
                    return GridMap.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return height;
    }

    private void setUpCells(int row, int col) {
        this.rowCount = row;
        this.colCount = col;
        this.cellHeight = height.get() / row;
        this.cellWidth = width.get() / col;
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

        cellTextFont = new Font("chiller", Math.min(cellWidth * 2, cellHeight * 2));
    }

    /**
     * x => column, from left to right
     * y => row, form top to bottom
     *
     * @param gc GraphicsContext
     */
    public void render(GraphicsContext gc) {
        gc.save();
        gc.setStroke(Color.GRAY.brighter());
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                double y = i * cellHeight;
                double x = j * cellWidth;
                gc.strokeRect(x, y, cellWidth, cellHeight);
                Cell cell = cells[i][j];

                switch (cell.getDirection()) {
                    case Left:
                        gc.fillText("←", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    case Right:
                        gc.fillText("→", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    case Up:
                        gc.fillText("↑", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    case Down:
                        gc.fillText("↓", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    default:
                        break;
                }

                renderCell(gc, i, j, cell);
            }
        }
        gc.restore();

        renderFloating(gc);
    }

    private void renderPath(GraphicsContext gc) {
        Vertex last = null;
        Vertex v = targetVertex;
        if (v == null) return;
        Vertex next = vertexTo[v.getRow()][v.getCol()];
        gc.setStroke(Color.GREEN);
        while (next != null) {
            double x0 = v.getCol() * cellWidth;
            double y0 = v.getRow() * cellHeight;
            int i = next.getRow();
            int j = next.getCol();
            double x1 = j * cellHeight;
            double y1 = i * cellWidth;
            gc.strokeLine(x0 + cellWidth / 2, y0 + cellHeight / 2, x1 + cellWidth / 2, y1 + cellHeight / 2);
            v = next;
            next = vertexTo[i][j];
        }
    }

    private void renderFloating(GraphicsContext gc) {
        if (action != Action.DISABLE) {
            int row = (int) (mousePositionY / cellHeight);
            int col = (int) (mousePositionX / cellWidth);
            double x = col * cellHeight;
            double y = row * cellWidth;
            switch (action) {
                case PUT_OR_REMOVE_BLOCK:
                    gc.drawImage(BLOCK_IMAGE, x, y, cellWidth, cellHeight);
                    break;
                case PUT_SOURCE:
                    gc.drawImage(SOURCE_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                    break;
                case PUT_TARGET:
                    gc.drawImage(TARGET_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                    break;
            }
        }

        renderPath(gc);
    }

    private void renderCell(GraphicsContext gc, int row, int col, Cell cell) {
        double y = row * cellHeight;
        double x = col * cellWidth;
        switch (cell.getType()) {
            case BLOCK:
                gc.drawImage(BLOCK_IMAGE, x, y, cellWidth, cellHeight);
                break;
            case SOURCE:
                gc.drawImage(SOURCE_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                break;
            case TARGET:
                gc.drawImage(TARGET_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                break;
            default:
                break;
        }
    }

    private void setCellType(int row, int col, Cell.Type type) {
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
        switch (method) {
            case BFS:
                bfs();
                break;
            case DIJKSTRA:
                dijkstra(true);
                break;
            case DIJKSTRA_WITHOUT_WEIGHT:
                dijkstra(false);
                break;
            default:
                return;
        }
        dirtyPath = false;
    }

    public void update() {
        if (dirtyPath) {
            updatePath(findingMethod);
        }
    }

    public void bfs() {
        Vertex source = sourceVertex;
        Vertex target = targetVertex;

        Queue<Vertex> queue = new LinkedList<>();
        boolean visited[][] = new boolean[rowCount][colCount];
        queue.offer(source);

        visited[source.getRow()][source.getCol()] = true;
        cells[source.getRow()][source.getCol()].setDirection(Direction.None);

        boolean done = false;
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            for (Vertex neighbor : getNeighbors(current)) {
                if (visited[neighbor.getRow()][neighbor.getCol()])
                    continue;
                queue.offer(neighbor);
                int row = neighbor.getRow();
                int col = neighbor.getCol();
                visited[row][col] = true;
                cells[row][col].setDirection(neighbor.getDirection());

                vertexTo[row][col] = new Vertex(current.getRow(), current.getCol());

                if (row == target.getRow() && col == target.getCol()) {
                    done = true;
                    break;
                }
            }
            if (done) break;
        }
    }

    private void dijkstra(boolean weighted) {
        double distTo[][] = new double[rowCount][colCount];
        PriorityQueue<Double> pq = new PriorityQueue<>(rowCount * colCount);
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                distTo[i][j] = Integer.MAX_VALUE;
            }
        }

        Vertex source = sourceVertex;
        Vertex target = targetVertex;

        distTo[source.getRow()][source.getCol()] = 0;
        pq.insert(source.getRow() * colCount + source.getCol(), 0.0);
        boolean done = false;
        while (!pq.isEmpty() && !done) {
            int v = pq.delMin();
            int row = v / rowCount, col = v % rowCount;
            for (Vertex w : getNeighbors(new Vertex(row, col))) {
                int wRow = w.getRow();
                int wCol = w.getCol();
                double weight = weighted ? cells[wRow][wCol].getWeight() : 1;
                if (distTo[wRow][wCol] > distTo[row][col] + weight) {
                    distTo[wRow][wCol] = distTo[row][col] + weight;
                    cells[wRow][wCol].setDirection(w.getDirection());
                    int wIndex = wRow * colCount + wCol;
                    if (pq.contains(wIndex)) {
                        pq.changeKey(wIndex, distTo[wRow][wCol]);
                    } else {
                        pq.insert(wIndex, distTo[wRow][wCol]);
                    }

                    vertexTo[wRow][wCol] = new Vertex(row, col);
                }

                if (wRow == target.getRow() && col == target.getCol()) {
                    done = true;
                    break;
                }
            }
        }
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isMouseInMap() {
        return isMouseInMap;
    }

    public void setMouseInMap(boolean mouseInMap) {
        isMouseInMap = mouseInMap;
    }

    public void onMouseMoved(double mousePositionX, double mousePositionY) {
        if (action == Action.DISABLE) return;
        this.mousePositionX = mousePositionX;
        this.mousePositionY = mousePositionY;
    }

    public void onMouseAction(double mousePositionX, double mousePositionY) {
        if (action != Action.DISABLE) {
            int row = (int) (mousePositionY / cellHeight);
            int col = (int) (mousePositionX / cellWidth);
            switch (action) {
                case PUT_OR_REMOVE_BLOCK:
                    if (cells[row][col].getType() == Cell.Type.EMPTY) {
                        putBlock(row, col);
                    } else {
                        removeBlock(row, col);
                    }
                    break;
                case PUT_SOURCE:
                    if (cells[row][col].getType() == Cell.Type.EMPTY) {
                        setSource(row, col);
                    }
                    break;
                case PUT_TARGET:
                    if (cells[row][col].getType() == Cell.Type.EMPTY) {
                        setTarget(row, col);
                    }
                    break;
            }
        }
    }

    public FindingMethod getFindingMethod() {
        return findingMethod;
    }

    public void setFindingMethod(FindingMethod findingMethod) {
        this.findingMethod = findingMethod;
    }

    public void updatePath() {
        updatePath(findingMethod);
    }

    public void setMapSize(int row, int col) {
        setUpCells(row, col);
    }

    public static enum Action {
        DISABLE,
        PUT_OR_REMOVE_BLOCK,
        PUT_SOURCE,
        PUT_TARGET
    }

    public static enum FindingMethod {
        BFS,
        DIJKSTRA,
        DIJKSTRA_WITHOUT_WEIGHT,
    }
}
