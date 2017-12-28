package scau.pathfinding;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Link
 */
public class BellmanFord implements ShortPath {
    private double distanceTo[];
    private DirectedEdge edgeTo[];
    private boolean hasNegativeCycle = false;

    public BellmanFord(AdjListGraph g, int s) {
        distanceTo = new double[g.V()];
        edgeTo = new DirectedEdge[g.V()];

        for (int v = 0; v < g.V(); ++v) {
            distanceTo[v] = Double.POSITIVE_INFINITY;
        }
        distanceTo[s] = 0;

        for (int pass = 0; pass < g.V(); ++pass) {
            for (int v = 0; v < g.V(); ++v) {
                for (DirectedEdge e : g.adjacency(v)) {
                    relax(e);
                }
            }
        }
        hasNegativeCycle = hasNegativeCycle(g);
    }

    public static void main(String[] args) {
        test0();
        test1();
    }

    public static void showPath(AdjListGraph g, int src, int dest) {
        BellmanFord bf = new BellmanFord(g, src);
        if (!bf.hasNegativeCycle()) {
            Iterable<DirectedEdge> paths = bf.pathTo(dest);
            if (paths != null) {
                for (DirectedEdge e : paths) {
                    System.out.print(e);
                    System.out.print(' ');
                }
            } else {
                System.out.println("No such path.");
            }
        } else {
            System.out.println("Found negative cycle.");
        }
    }

    public static void test0() {
        AdjListGraph g = new AdjListGraph(4);
        g.addEdge(new DirectedEdge(0, 2, 4.2));
        showPath(g, 0, 1);
    }

    public static void test1() {
        int vertexCount = 10;
        AdjListGraph g = AdjListGraph.Random(vertexCount);
        System.out.println(g);
        showPath(g, 0, 1);
    }

    private boolean hasNegativeCycle(AdjListGraph g) {
        for (int v = 0; v < g.V(); ++v) {
            for (DirectedEdge e : g.adjacency(v)) {
                if (distanceTo[e.to()] > distanceTo[v] + e.weight()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        double vw = distanceTo[v] + e.weight();
        if (distanceTo[w] > vw) {
            distanceTo[w] = vw;
            edgeTo[w] = e;
        }
    }

    @Override
    public double distanceTo(int v) {
        return distanceTo[v];
    }

    @Override
    public boolean hasPathTo(int v) {
        return distanceTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * @param v destination
     * @return either paths or null if no such path
     */
    @Override
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Deque<DirectedEdge> path = new ArrayDeque<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }
}
