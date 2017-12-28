package scau.pathfinding;

/**
 * @author Link
 */
public class BellmanFord extends SingleSourceShortestPath {
    private boolean hasNegativeCycle = false;

    public BellmanFord(AdjListGraph g, int s) {
        super(g);

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

    public static void test0() {
        AdjListGraph g = new AdjListGraph(4);
        g.addEdge(new DirectedEdge(0, 2, 4.2));
        System.out.println(g);
        SingleSourceShortestPath sp = new BellmanFord(g, 0);
        sp.showPathTo(1);
    }

    public static void test1() {
        int vertexCount = 10;
        AdjListGraph g = AdjListGraph.Random(vertexCount);
        System.out.println(g);
        SingleSourceShortestPath sp = new BellmanFord(g, 0);
        sp.showPathTo(1);
    }

    public void showPathTo(int dest) {
        if (!hasNegativeCycle()) {
            Iterable<DirectedEdge> paths = pathTo(dest);
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
}
