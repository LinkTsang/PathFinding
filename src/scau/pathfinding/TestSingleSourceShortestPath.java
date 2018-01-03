package scau.pathfinding;

/**
 * @author Link
 * BFS, Dijkstra, BellmanFord, SPFA 运行时间测试
 */
public class TestSingleSourceShortestPath {
    public static void main(String[] args) {
        AdjListGraph[] graphs = {
                AdjListGraph.Random(10),
                AdjListGraph.Random(100),
                AdjListGraph.Random(1000),
                AdjListGraph.Random(2000)
        };
        int count = 10;

        for (AdjListGraph g : graphs) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testBFS(g);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / count);
        }
        System.out.println();

        for (AdjListGraph g : graphs) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testDijkstra(g);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / count);
        }
        System.out.println();

        for (AdjListGraph g : graphs) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testSPFA(g);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / count);
        }
        System.out.println();

        for (AdjListGraph g : graphs) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testBellmanFord(g);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / count);
        }
        System.out.println();
    }

    public static long testBFS(AdjListGraph g) {
        return testBFS(g, false, -1);
    }

    public static long testBFS(AdjListGraph g, boolean showPath, int dest) {
        long startTime = System.nanoTime();
        SingleSourceShortestPath sp = new BFS(g, 0);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.printf("%s (V: %d, E: %d) cost %f ms\n",
                sp.getClass().getSimpleName(),
                g.V(), g.E(),
                duration / 1000000.0);
        if (showPath) {
            sp.showPathTo(dest);
        }
        return duration;
    }

    public static long testDijkstra(AdjListGraph g) {
        return testDijkstra(g, false, -1);
    }

    public static long testDijkstra(AdjListGraph g, boolean showPath, int dest) {
        long startTime = System.nanoTime();
        SingleSourceShortestPath sp = new Dijkstra(g, 0);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.printf("%s (V: %d, E: %d) cost %f ms\n",
                sp.getClass().getSimpleName(),
                g.V(), g.E(),
                duration / 1000000.0);
        if (showPath) {
            sp.showPathTo(dest);
        }
        return duration;
    }

    public static long testSPFA(AdjListGraph g) {
        return testSPFA(g, false, -1);
    }

    public static long testSPFA(AdjListGraph g, boolean showPath, int dest) {
        long startTime = System.nanoTime();
        SingleSourceShortestPath sp = new SPFA(g, 0);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.printf("%s (V: %d, E: %d) cost %f ms\n",
                sp.getClass().getSimpleName(),
                g.V(), g.E(),
                duration / 1000000.0);
        if (showPath) {
            sp.showPathTo(dest);
        }
        return duration;
    }

    public static long testBellmanFord(AdjListGraph g) {
        return testBellmanFord(g, false, -1);
    }

    public static long testBellmanFord(AdjListGraph g, boolean showPath, int dest) {
        long startTime = System.nanoTime();
        SingleSourceShortestPath sp = new BellmanFord(g, 0);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.printf("%s (V: %d, E: %d) cost %f ms\n",
                sp.getClass().getSimpleName(),
                g.V(), g.E(),
                duration / 1000000.0);
        if (showPath) {
            sp.showPathTo(dest);
        }
        return duration;
    }
}
