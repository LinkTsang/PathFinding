package scau.pathfinding;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Link
 */
public abstract class SingleSourceShortestPath {
    protected double distanceTo[];          // 从起点到某个顶点的路径长度
    protected DirectedEdge edgeTo[];        // 从起点到某个顶点的最后一条边

    protected SingleSourceShortestPath(AdjListGraph G) {
        edgeTo = new DirectedEdge[G.V()];
        distanceTo = new double[G.V()];
    }

    public double distanceTo(int v) {
        return distanceTo[v];
    }

    public boolean hasPathTo(int v) {
        return distanceTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * @param v destination
     * @return either paths or null if no such path
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Deque<DirectedEdge> path = new ArrayDeque<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    public void showPathTo(int v) {
        if (hasPathTo(v)) {
            for (DirectedEdge e : pathTo(v)) {
                System.out.print(e);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public double getPathLength(int v) {
        if (hasPathTo(v)) {
            double length = 0;
            for (DirectedEdge e : pathTo(v)) {
                length += e.weight();
            }
            return length;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
}
