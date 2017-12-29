/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scau.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 陈双意
 */
public class AdjMatrix {

    private final List<Integer> VList;
    private final double[][] edges;
    private int E;

    public AdjMatrix(int n) {
        edges = new double[n][n];
        VList = new ArrayList<>(n);
        E = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    /**
     * 生成顶点数为 n 的有向图
     *
     * @param n 顶点数
     * @return 顶点数为 n 的有向图
     */
    @SuppressWarnings("empty-statement")
    public static AdjMatrix Random(int n) {
        Random random = new Random();
        AdjMatrix g = new AdjMatrix(n);
        for (int i = 0; i < n; ++i) {
            g.insertVertex(i);
        }
        int count = random.nextInt(n * n) + 1;
        for (int i = 0; i < count; i++) {
            g.insertEdge(random.nextInt(n), random.nextInt(n), random.nextDouble() * 10);
        }
        return g;
    }

    public static AdjMatrix Random(int v, int e) {
        Random random = new Random();
        AdjMatrix g = new AdjMatrix(v);
        for (int i = 0; i < v; ++i) {
            g.insertVertex(i);
        }
        while (g.E() < e) {
            int from;
            int to;
            do {
                from = random.nextInt(v);
                to = random.nextInt(v);
            } while (from == to);
            double weight = random.nextDouble() * 10;
            g.insertEdge(from, to, weight);
        }
        return g;
    }

    public static AdjMatrix getTestAdjMatrix() {
        int vexCount = 8;
        return AdjMatrix.Random(vexCount);
    }

    public static void test() {
        getTestAdjMatrix().Output();
    }

    public static void main(String[] args) {
        test();
    }

    public int V() {
        return VList.size();
    }

    public int E() {
        return E;
    }

    public Object getIndexValue(int i) {
        return VList.get(i);
    }

    public double getWeight(int v1, int v2) {
        return edges[v1][v2];
    }

    public void insertVertex(int vertex) {
        VList.add(vertex);
    }

    public void insertEdge(int v1, int v2, double weight) {
        edges[v1][v2] = weight;
        E++;
    }

    public void Output() {
        for (int i = 0; i < VList.size(); ++i) {
            for (int j = 0; j < VList.size(); ++j) {
                System.out.println(edges[i][j]);
            }
        }
    }

}
