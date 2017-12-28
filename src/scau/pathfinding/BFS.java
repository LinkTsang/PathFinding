package scau.pathfinding;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Link
 * Breadth First Search
 */
public class BFS extends SingleSourceShortestPath {

    public BFS(AdjListGraph g, int s) {
        super(g);
        for (int v = 0; v < g.V(); v++) {
            distanceTo[v] = Double.POSITIVE_INFINITY;
        }
        distanceTo[s] = 0.0;

        boolean visited[] = new boolean[g.V()];
        visited[s] = true;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (DirectedEdge e : g.adjacency(v)) {
                int neighbor = e.to();
                if (visited[neighbor])
                    continue;
                queue.offer(neighbor);
                visited[neighbor] = true;
                distanceTo[neighbor] = distanceTo[v] + 1;
                edgeTo[neighbor] = e;
            }
        }
    }

    public static void main(String[] args) {
        test0();
    }

    public static void test0() {
        int vertexCount = 10;
        AdjListGraph g = AdjListGraph.Random(vertexCount);
        System.out.println(g);
        SingleSourceShortestPath sp = new BFS(g, 0);
        sp.showPathTo(1);
    }
}
