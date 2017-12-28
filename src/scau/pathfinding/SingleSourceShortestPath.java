package scau.pathfinding;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Link
 */
public abstract class SingleSourceShortestPath {
    protected double distanceTo[];
    protected DirectedEdge edgeTo[];

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
}
