package scau.pathfinding.gridmap;

import java.util.Arrays;

/**
 * @author Link
 */
public class AStarSearch extends GridPathSearch {
    private Distance distance = Distance.Manhattan;

    public AStarSearch(GridMap map) {
        int rowCount = map.getRowCount();    // 地图行数
        int colCount = map.getColCount();    // 地图列数
        Vertex source = map.getSource();     // 源
        Vertex target = map.getTarget();     // 目标

        // 格子地图
        cells = new Cell[rowCount][colCount];
        Cell[][] original = map.getCells();
        for (int i = 0; i < map.getCells().length; ++i) {
            cells[i] = Arrays.copyOf(original[i], original[i].length);
        }

        // vertexTo[r0][c0]: 到顶点 (r0, c0) 的 顶点 vertex(r1, c1)
        vertexTo = new Vertex[rowCount][colCount];

        // distTo[r][c]: 从源 s 到 (r, c) 的实际距离
        double[][] distTo = new double[rowCount][colCount];
        for (int i = 0; i < rowCount; ++i)
            for (int j = 0; j < colCount; ++j)
                distTo[i][j] = Double.POSITIVE_INFINITY;
        distTo[source.row()][source.col()] = 0;

        // 待访问的顶点
        PriorityQueue<Double> pq = new PriorityQueue<>(rowCount * colCount);
        // 同 Dijkstra 初始化索引优先队列，
        // 不过索引编码为 rowCount * row + col，
        // 索引对应的权值为 f(v) = g() + h()，
        // 由于一开始 g(s) = 0，故 f(s) = g(s) + h(s) = h(s)。
        pq.insert(source.row() * colCount + source.col(), heuristicDistance(source, target));
        boolean done = false;
        while (!pq.isEmpty() && !done) {
            ++stepCount;

            // 选取具有 f(v) 最小的顶点 v
            int v = pq.delMin();
            int row = v / rowCount, col = v % rowCount;
            for (Vertex w : map.getNeighbors(row, col)) {
                int wRow = w.row();
                int wCol = w.col();

                double weight = cells[wRow][wCol].getWeight();
                double distToW = distTo[row][col] + weight;

                if (distTo[wRow][wCol] > distToW) {
                    // 松弛操作
                    distTo[wRow][wCol] = distToW;
                    cells[wRow][wCol].setDirection(w.getDirection());

                    int wIndex = wRow * colCount + wCol;
                    // 计算 f(v) = g(v) + h(v)
                    double fScore = distTo[wRow][wCol] + heuristicDistance(w, target);
                    if (pq.contains(wIndex)) {
                        pq.changeKey(wIndex, fScore);
                    } else {
                        pq.insert(wIndex, fScore);
                    }
                    vertexTo[wRow][wCol] = new Vertex(row, col);
                }

                if (wRow == target.row() && col == target.getCol()) {
                    // 找到目标，停止搜素
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
