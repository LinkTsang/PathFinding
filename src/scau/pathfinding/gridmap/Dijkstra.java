package scau.pathfinding.gridmap;

import java.util.Arrays;

/**
 * @author Link
 */
public class Dijkstra extends GridPathSearch {
    public Dijkstra(GridMap map, boolean weighted) {
        int rowCount = map.getRowCount();
        int colCount = map.getColCount();
        Vertex source = map.getSource();
        Vertex target = map.getTarget();

        this.cells = new Cell[rowCount][colCount];
        Cell[][] original = map.getCells();
        for (int i = 0; i < map.getCells().length; ++i) {
            cells[i] = Arrays.copyOf(original[i], original[i].length);
        }

        vertexTo = new Vertex[rowCount][colCount];
        double distTo[][] = new double[rowCount][colCount];
        PriorityQueue<Double> pq = new PriorityQueue<>(rowCount * colCount);
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                distTo[i][j] = Integer.MAX_VALUE;
            }
        }

        distTo[source.getRow()][source.getCol()] = 0;
        pq.insert(source.getRow() * colCount + source.getCol(), 0.0);
        boolean done = false;
        while (!pq.isEmpty() && !done) {
            ++stepCount;

            int v = pq.delMin();
            int row = v / rowCount, col = v % rowCount;
            for (Vertex w : map.getNeighbors(new Vertex(row, col))) {
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
}
