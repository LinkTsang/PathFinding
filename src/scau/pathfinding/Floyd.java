/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scau.pathfinding;

/**
 * @author 陈双意
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Floyd算法:用于多源最短路径的求解，算出来的是所有的节点到其余各节点之间的最短距离
 */
public class Floyd {

    private final int pathMatrix[][];       // pathMatrix[v][w] == -1 if no such path
    private final double distance[][];

    public Floyd(AdjMatrix g) {
        int n = g.V();
        pathMatrix = new int[n][n];
        distance = new double[n][n];

        int v, w, k;
        for (v = 0; v < n; ++v) {
            for (w = 0; w < n; ++w) {
                distance[v][w] = g.getWeight(v, w);
                if (distance[v][w] == Double.POSITIVE_INFINITY) {
                    pathMatrix[v][w] = -1;
                } else {
                    pathMatrix[v][w] = w;
                }
            }
        }
        for (k = 0; k < g.V(); ++k) {
            for (v = 0; v < g.V(); ++v) {
                for (w = 0; w < g.V(); ++w) {
                    if (distance[v][w] > distance[v][k] + distance[k][w]) {
                        distance[v][w] = distance[v][k] + distance[k][w];
                        pathMatrix[v][w] = pathMatrix[v][k];
                    }
                }
            }
        }
    }

    public static void Output() {
        AdjMatrix g = AdjMatrix.getTestAdjMatrix();
        Floyd y = new Floyd(g);

        System.out.println("Distance: ");
        System.out.printf("%-12c ", ' ');
        for (int j = 0; j < g.V(); j++) {
            System.out.printf("%-12d  ", j);
        }
        System.out.println();
        for (int i = 0; i < g.V(); i++) {
            System.out.printf("%-12d ", i);
            for (int j = 0; j < g.V(); j++) {
                System.out.printf("%-12f  ", y.distance[i][j]);
            }
            System.out.println();
        }

        System.out.println("Path Matrix: ");
        System.out.printf("%-12c ", ' ');
        for (int j = 0; j < g.V(); j++) {
            System.out.printf("%-12d  ", j);
        }
        System.out.println();
        for (int i = 0; i < g.V(); i++) {
            System.out.printf("%-12d ", i);
            for (int j = 0; j < g.V(); j++) {
                System.out.printf("%-12d  ", y.pathMatrix[i][j]);
            }
            System.out.println();
        }

        System.out.println("Path: ");
        for (int i = 0; i < g.V(); i++) {
            for (int j = 0; j < g.V(); j++) {
                System.out.printf("%d -> %d: ", i, j);
                if (!y.hasPath(i, j)) {
                    System.out.println("No path.");
                    continue;
                }
                int last = -1;
                for (DirectedEdge e : y.path(i, j)) {
                    System.out.printf("%d -> ", e.from());
                    last = e.to();
                }
                if (last != -1) {
                    System.out.printf("%d", last);
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Output();
    }

    public boolean hasPath(int from, int to) {
        return pathMatrix[from][to] != -1;
    }

    public Iterable<DirectedEdge> path(int from, int to) {
        if (!hasPath(from, to)) {
            return null;
        }
        List<DirectedEdge> path = new ArrayList<>();
        if (from == to) {
            path.add(new DirectedEdge(from, to, distance[from][to]));
            return path;
        }
        while (from != to) {
            path.add(new DirectedEdge(from, to, distance[from][to]));
            from = pathMatrix[from][to];
        }
        return path;
    }
}
