package scau.pathfinding.gridmap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Link
 * Breadth First Search
 */
public class BFS extends GridPathSearch {
    public BFS(GridMap map) {
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

        boolean visited[][] = new boolean[rowCount][colCount];
        visited[source.getRow()][source.getCol()] = true;
        Queue<Vertex> queue = new LinkedList<>();
        queue.offer(source);
        cells[source.getRow()][source.getCol()].setDirection(Direction.None);

        boolean done = false;
        while (!queue.isEmpty()) {
            ++stepCount;

            Vertex current = queue.poll();
            for (Vertex neighbor : map.getNeighbors(current)) {
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
}
