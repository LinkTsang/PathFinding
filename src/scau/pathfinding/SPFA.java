package scau.pathfinding;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Link
 */
public class SPFA extends SingleSourceShortestPath {
    private boolean[] onQueue;
    private Queue<Integer> queue;
    private int cost;
    private Iterable<DirectedEdge> cycle;

    public SPFA(AdjListGraph g, int s) {
        super(g);

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

    public static void test0() {
        AdjListGraph g = new AdjListGraph(4);
        g.addEdge(new DirectedEdge(0, 2, 4.2));
        SPFA spfa = new SPFA(g, 0);
        spfa.showPathTo(1);
    }

    public static void test1() {
        AdjListGraph g = AdjListGraph.Random(10);
        SPFA spfa = new SPFA(g, 0);
        spfa.showPathTo(1);
    }

    public static void main(String[] args) {
        test0();
        test1();
    }

    public void showPathTo(int dest) {
        if (!hasNegativeCycle()) {
            Iterable<DirectedEdge> paths = pathTo(dest);
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

    private void relax(AdjListGraph G, int v) {
        for (DirectedEdge e : G.adjacency(v)) {
            int w = e.to();
            if (distanceTo[w] > distanceTo[v] + e.weight()) {
                distanceTo[w] = distanceTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
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
}
