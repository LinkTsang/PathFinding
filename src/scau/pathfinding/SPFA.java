package scau.pathfinding;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author Link
 */
public class SPFA implements ShortPath {
    private double[] distanceTo;
    private DirectedEdge[] edgeTo;
    private boolean[] onQueue;
    private Queue<Integer> queue;
    private int cost;
    private Iterable<DirectedEdge> cycle;

    public SPFA(AdjListGraph g, int s) {
        distanceTo  = new double[g.V()];
        edgeTo  = new DirectedEdge[g.V()];
        onQueue = new boolean[g.V()];

        for (int v = 0; v < g.V(); v++)
            distanceTo[v] = Double.POSITIVE_INFINITY;
        distanceTo[s] = 0.0;

        queue = new LinkedList<>();
        queue.offer(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.poll();
            onQueue[v] = false;
            relax(g, v);
        }
    }

    private void relax(AdjListGraph G, int v)
    {
        for (DirectedEdge e : G.adjacency(v))
        {
            int w = e.to();
            if (distanceTo[w] > distanceTo[v] + e.weight())
            {
                distanceTo[w] = distanceTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w])
                {
                    queue.offer(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0)
                findNegativeCycle();
        }
    }


    private boolean hasNegativeCycle() {
        return cycle != null;
    }

    private Iterable<DirectedEdge> nagativeCycle() {
        return cycle;
    }

    private void findNegativeCycle() {
        int V = edgeTo.length;
        AdjListGraph graph = new AdjListGraph(V);
        for (DirectedEdge e : edgeTo) {
            if (e != null) {
                graph.addEdge(e);
            }
        }
        DirectedCycleChecker checker = new DirectedCycleChecker(graph);
        cycle = checker.cycle();
    }

    public static void main(String[] args) {
        AdjListGraph g0 = test0();
        AdjListGraph g1 = test1();

        showPath(g0, 0, 1);
        showPath(g1, 0, 1);

        BellmanFord.showPath(g0, 0, 1);
        BellmanFord.showPath(g1, 0, 1);
    }

    public static void showPath(AdjListGraph g, int src, int dest) {
        SPFA spfa = new SPFA(g, src);
        if (!spfa.hasNegativeCycle()) {
            Iterable<DirectedEdge> paths = spfa.pathTo(dest);
            if (paths != null) {
                for (DirectedEdge e : paths) {
                    System.out.println(e);
                }
            } else {
                System.out.println("No such path.");
            }
        } else {
            System.out.println("Found negative cycle.");
        }
    }

    public static AdjListGraph test0() {
        AdjListGraph g = new AdjListGraph(4);
        g.addEdge(new DirectedEdge(0, 2, 4.2));
        return g;
    }

    public static AdjListGraph test1() {
        int vertexCount = 10;
        AdjListGraph g = AdjListGraph.Random(vertexCount);
        //System.out.println(g);
        return g;
    }

    @Override
    public double distanceTo(int v) {
        return distanceTo[v];
    }

    @Override
    public boolean hasPathTo(int v) {
        return distanceTo[v] < Double.POSITIVE_INFINITY;
    }

    @Override
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }
}
