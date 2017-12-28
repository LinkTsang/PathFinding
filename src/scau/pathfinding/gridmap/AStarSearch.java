package scau.pathfinding.gridmap;

import java.util.Arrays;

/**
 * @author Link
 */
public class AStarSearch extends GridPathSearch {
    private Distance distance = Distance.Manhattan;

    public AStarSearch(GridMap map) {
        int rowCount = map.getRowCount();
        int colCount = map.getColCount();
        Vertex source = map.getSource();
        Vertex target = map.getTarget();

        vertexTo = new Vertex[rowCount][colCount];
        cells = new Cell[rowCount][colCount];
        Cell[][] original = map.getCells();
        for (int i = 0; i < map.getCells().length; ++i) {
            cells[i] = Arrays.copyOf(original[i], original[i].length);
        }

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
            for (Vertex w : map.getNeighbors(row, col)) {
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
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
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

    public static enum Distance {
        Manhattan,
        Chebyshev,
        Euclidean
    }

}
