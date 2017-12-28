/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scau.pathfinding;

/**
 * @author 陈双意
 */

/**
 * Dijkstra(迪杰斯特拉)算法用于计算一个节点到其他所有节点的最短路径。
 */
public class Dijkstra extends SingleSourceShortestPath {
    private final IndexMinPQ<Double> pq;

    public Dijkstra(AdjListGraph G, int s) {
        super(G);
        pq = new IndexMinPQ<>(G.V());
        for (int v = 0; v < G.V(); v++) {
            distanceTo[v] = Double.POSITIVE_INFINITY;
        }
        distanceTo[s] = 0.0;
        pq.insert(s, 0.0);
        while (!pq.isEmpty()) {
            relax(G, pq.delMin());
        }
    }

    public static void test0() {
        AdjListGraph G = AdjListGraph.Random(8);
        long startTime = System.nanoTime();
        Dijkstra sp = new Dijkstra(G, 0);
        long endTime = System.nanoTime();
        System.out.printf("Dijkstra test0 cost %f ms\n", (endTime - startTime) / 1000000.0);
        System.out.println(G.toString());
        for (int v = 0; v < G.V(); v++) {
            System.out.println("0 hasPathTo " + v + " : " + sp.hasPathTo(v));
            sp.showPathTo(v);
        }
    }

    public static void test1() {
        AdjListGraph G = AdjListGraph.Random(8);
        long startTime = System.nanoTime();
        Dijkstra sp = new Dijkstra(G, 2);
        long endTime = System.nanoTime();
        System.out.printf("Dijkstra test1 cost %f ms\n", (endTime - startTime) / 1000000.0);
        System.out.println(G.toString());
        for (int v = 0; v < G.V(); v++) {
            System.out.println("2 hasPathTo " + v + ": " + sp.hasPathTo(v));
            sp.showPathTo(v);
        }
    }

    public static void main(String[] args) {
        test0();
        test1();
    }

    private void relax(AdjListGraph G, int v) {
        for (DirectedEdge e : G.adjacency(v)) {
            int w = e.to();
            if (distanceTo[w] > distanceTo[v] + e.weight()) {
                distanceTo[w] = distanceTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) {
                    pq.changeKey(w, distanceTo[w]);
                } else {
                    pq.insert(w, distanceTo[w]);
                }
            }
        }

    }
}
