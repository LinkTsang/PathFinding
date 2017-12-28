package scau.pathfinding.visual;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Link
 */
public class AStarSearch {
    private Cell[][] cells;
    private Vertex[][] vertexTo;
    private int stepCount = 0;
    private Distance distance = Distance.Manhattan;

    public AStarSearch(GridMap gridMap) {
        int rowCount = gridMap.getRowCount();
        int colCount = gridMap.getColCount();
        Vertex source = gridMap.getSource();
        Vertex target = gridMap.getTarget();

        this.cells = new Cell[rowCount][colCount];
        Cell[][] original = gridMap.getCells();
        for (int i = 0; i < gridMap.getCells().length; ++i) {
            cells[i] = Arrays.copyOf(original[i], original[i].length);
        }

        Vertex vertexTo[][] = new Vertex[rowCount][colCount];

        double[][] distTo = new double[rowCount][colCount];      // actual distance
        for (int i = 0; i < rowCount; ++i)
            for (int j = 0; j < colCount; ++j)
                distTo[i][j] = Double.POSITIVE_INFINITY;
        distTo[source.row()][source.col()] = 0;

        PriorityQueue<Double> pq = new PriorityQueue<>(rowCount * colCount);
        pq.insert(source.row() * colCount + source.col(), heuristicDistance(source, target));
        boolean done = false;
        while (!pq.isEmpty() && !done) {
            ++stepCount;

            int v = pq.delMin();
            int row = v / rowCount, col = v % rowCount;
            for (Vertex w : gridMap.getNeighbors(row, col)) {
                int wRow = w.row();
                int wCol = w.col();

                double weight = cells[wRow][wCol].getWeight();
                double distToW = distTo[row][col] + weight;

                if (distTo[wRow][wCol] > distToW) {
                    distTo[wRow][wCol] = distToW;
                    cells[wRow][wCol].setDirection(w.getDirection());

                    int wIndex = wRow * colCount + wCol;
                    double fScore = distTo[wRow][wCol] + heuristicDistance(w, target);
                    if (pq.contains(wIndex)) {
                        pq.changeKey(wIndex, fScore);
                    } else {
                        pq.insert(wIndex, fScore);
                    }
                    vertexTo[wRow][wCol] = new Vertex(row, col);
                }

                if (wRow == target.row() && col == target.getCol()) {
                    done = true;
                    break;
                }
            }
        }
        this.vertexTo = vertexTo;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Vertex[][] getVertexTo() {
        return vertexTo;
    }

    public Vertex vertexTo(int row, int col) {
        return vertexTo[row][col];
    }

    /**
     * @return Snapshot of directions and vertexTo
     */
    public Iterator<Snap> iterator() {
        return new Iterator<Snap>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Snap next() {
                return null;
            }
        };
    }

    private double heuristicDistance(Vertex start, Vertex goal) {
        switch (distance) {
            case Manhattan:
                return Math.abs(start.col() - goal.col()) + Math.abs(start.row() - goal.row());
            case Chebyshev:
                return Math.max(Math.abs(start.col() - goal.col()), Math.abs(start.row() - goal.row()));
            case Euclidean: {
                double a = start.col() - goal.col();
                double b = start.row() - goal.row();
                return Math.sqrt(a * a + b * b);
            }
            default:
                return Math.abs(start.col() - goal.col()) + Math.abs(start.row() - goal.row());
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getStepCount() {
        return stepCount;
    }

    public static enum Distance {
        Manhattan,
        Chebyshev,
        Euclidean
    }

    public class Snap {
        Direction[][] directions;
        Vertex[][] vertexTo;

        public Snap(Direction[][] directions, Vertex[][] pathTo) {
            this.directions = directions;
            this.vertexTo = pathTo;
        }
    }
}
