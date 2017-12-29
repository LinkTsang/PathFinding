package scau.pathfinding;

/**
 * @author Link
 */
public class Johnson {
    private boolean hasNegativeCycle;
    private double distance[][];

    public Johnson(AdjListGraph g) {
        AdjListGraph g0 = new AdjListGraph(g.V() + 1);
        int s = g.V();
        for (int i = 0; i < g.V(); ++i) {
            for (DirectedEdge e : g.adjacency(i)) {
                g0.addEdge(e);
            }
            g0.addEdge(new DirectedEdge(s, i, 0));
        }

        BellmanFord bf = new BellmanFord(g0, s);
        if (bf.hasNegativeCycle()) {
            hasNegativeCycle = true;
            return;
        }

        AdjListGraph g1 = new AdjListGraph(g0.V());
        for (int i = 0; i < g0.V(); ++i) {
            for (DirectedEdge e : g0.adjacency(i)) {
                double weight = e.weight() + bf.distanceTo(e.from()) - bf.distanceTo(e.to());
                g1.addEdge(new DirectedEdge(e.from(), e.to(), weight));
            }
            g1.addEdge(new DirectedEdge(s, i, 0));
        }

        distance = new double[g.V()][g.V()];
        for (int u = 0; u < g.V(); ++u) {
            Dijkstra dijkstra = new Dijkstra(g1, u);
            for (int v = 0; v < g.V(); ++v) {
                distance[u][v] = dijkstra.distanceTo(v) + bf.distanceTo(v) - bf.distanceTo(u);
            }
        }
    }

    public boolean isHasNegativeCycle() {
        return hasNegativeCycle;
    }

    public double distance(int u, int v) {
        return distance[u][v];
    }
}
