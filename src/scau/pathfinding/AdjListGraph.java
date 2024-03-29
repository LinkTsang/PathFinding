package scau.pathfinding;

import java.util.Random;

/**
 * @author Link
 */
public class AdjListGraph implements Graph {

    private final Bag<DirectedEdge>[] adjacency; // 邻接的边
    private final int V;                         // 顶点数
    private int E;                               // 边数

    @SuppressWarnings("unchecked")
    public AdjListGraph(int n) {
        V = n;
        E = 0;
        adjacency = (Bag<DirectedEdge>[]) new Bag[n];
        for (int i = 0; i < n; ++i) {
            adjacency[i] = new Bag<>();
        }
    }

    /**
     * 生成顶点数为 n 的有向图
     *
     * @param n 顶点数
     * @return 顶点数为 n 的有向图
     */
    public static AdjListGraph Random(int n) {
        Random random = new Random();
        AdjListGraph g = new AdjListGraph(n);
        for (int i = 0; i < n; ++i) {
            int count = random.nextInt(n) + 1;
            for (int j = 0; j < count; ++j) {
                int to;
                do {
                    to = random.nextInt(n);
                } while (to == i);
                double weight = random.nextDouble() * 10;
                DirectedEdge edge = new DirectedEdge(i, to, weight);
                g.addEdge(edge);
            }
        }
        return g;
    }

    public static void main(String[] args) {
        int vertexCount = 10;
        AdjListGraph g = AdjListGraph.Random(vertexCount);
        System.out.println(g);
    }

    public static AdjListGraph fromAdjAdjMatrix(AdjMatrix g) {
        AdjListGraph adjListGraph = new AdjListGraph(g.V());
        for (int u = 0; u < g.V(); ++u) {
            for (int v = 0; v < g.V(); ++v) {
                if (g.getWeight(u, v) < Double.POSITIVE_INFINITY)
                    adjListGraph.addEdge(new DirectedEdge(u, v, g.getWeight(u, v)));
            }
        }
        return adjListGraph;
    }

    public static AdjListGraph Random(int v, int e) {
        Random random = new Random();
        AdjListGraph g = new AdjListGraph(v);
        while (g.E() < e) {
            int from;
            int to;
            do {
                from = random.nextInt(v);
                to = random.nextInt(v);
            } while (from == to);
            double weight = random.nextDouble() * 10;
            DirectedEdge edge = new DirectedEdge(from, to, weight);
            g.addEdge(edge);
        }
        return g;
    }

    /**
     * @return 顶点数
     */
    @Override
    public int V() {
        return V;
    }

    /**
     * @return 边数
     */
    @Override
    public int E() {
        return E;
    }

    /**
     * @param edge edge
     */
    public void addEdge(DirectedEdge edge) {
        adjacency[edge.from()].add(edge);
        E++;
    }

    /**
     * @param v vertex
     * @return adjacency
     */
    public Iterable<DirectedEdge> adjacency(int v) {
        return adjacency[v];
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < V; ++i) {
            for (DirectedEdge e : adjacency(i)) {
                buffer.append(e);
                buffer.append('\n');
            }
        }
        return buffer.toString();
    }
}
