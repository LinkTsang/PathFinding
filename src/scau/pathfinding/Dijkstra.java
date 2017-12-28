/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scau.pathfinding;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author 陈双意
 */

/**
 * Dijkstra(迪杰斯特拉)算法用于计算一个节点到其他所有节点的最短路径。
 */
public class Dijkstra {

    private final DirectedEdge[] edgeTo;
    private final double[] distanceTo;
    private final IndexMinPQ<Double> pq;

    public Dijkstra(AdjListGraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distanceTo = new double[G.V()];
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
        Dijkstra sp = new Dijkstra(G, 0);
        System.out.println(G.toString());
        for (int v = 0; v < G.V(); v++) {
            System.out.println("0 hasPathTo " + v + " : " + sp.hasPathTo(v));
            showPathTo(sp, v);
        }
    }

    public static void test1() {
        AdjListGraph G = AdjListGraph.Random(8);
        Dijkstra sp = new Dijkstra(G, 2);
        System.out.println(G.toString());
        for (int v = 0; v < G.V(); v++) {
            System.out.println("2 hasPathTo " + v + ": " + sp.hasPathTo(v));
            showPathTo(sp, v);
        }
    }

    public static void main(String[] args) {
        test0();
        test1();
    }

    private static void showPathTo(Dijkstra sp, int v) {
        if (sp.hasPathTo(v)) {
            for (DirectedEdge e : sp.pathTo(v)) {
                System.out.print(e);
                System.out.print(' ');
            }
            System.out.println();
        }
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

    public double distanceTo(int v) {

        return distanceTo[v];
    }

    public boolean hasPathTo(int v) {
        return distanceTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Deque<DirectedEdge> path = new ArrayDeque<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }
}
