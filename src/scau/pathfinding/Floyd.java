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
 * Floyd算法:用于多源最短路径的求解，算出来的是所有的节点到其余各节点之间的最短距离
 */
public class Floyd {

    private final double pathMatrix[][];
    private final double distance[][];

    public Floyd(AdjMatrix g) {
        int n = g.V();
        pathMatrix = new double[n][n];
        distance = new double[n][n];
        int v, w, k;
        for (v = 0; v < n; ++v) {
            for (w = 0; w < n; ++w) {
                distance[v][w] = g.getWeight(v, w);
                pathMatrix[v][w] = w;
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
        for (int i = 0; i < g.V(); i++) {
            for (int j = 0; j < g.V(); j++) {
                System.out.printf("%12f  ", y.distance[i][j]);
            }
            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        Output();
    }
}
