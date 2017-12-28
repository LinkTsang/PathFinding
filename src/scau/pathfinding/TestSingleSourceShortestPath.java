package scau.pathfinding;

/**
 * @author Link
 * BFS, Dijkstra, BellmanFord, SPFA 运行时间测试
 */
public class TestSingleSourceShortestPath {
    public static void main(String[] args) {
        int list[] = {10, 100, 1000, 2000};
        int count = 10;

        for (int x : list) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testBFS(x);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / 5);
        }
        System.out.println();

        for (int x : list) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testDijkstra(x);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / 5);
        }
        System.out.println();

        for (int x : list) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testBellmanFord(x);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / 5);
        }
        System.out.println();

        for (int x : list) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testSPFA(x);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / 5);
        }
        System.out.println();
    }

    public static long testBFS(int v) {
        return testBFS(v, false, -1);
    }

    public static long testBFS(int v, boolean showPath, int dest) {
        AdjListGraph g = AdjListGraph.Random(v);
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

    public static long testDijkstra(int v) {
        return testDijkstra(v, false, -1);
    }

    public static long testDijkstra(int v, boolean showPath, int dest) {
        AdjListGraph g = AdjListGraph.Random(v);
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

    public static long testSPFA(int v) {
        return testSPFA(v, false, -1);
    }

    public static long testSPFA(int v, boolean showPath, int dest) {
        AdjListGraph g = AdjListGraph.Random(v);
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

    public static long testBellmanFord(int v) {
        return testSPFA(v, false, -1);
    }

    public static long testBellmanFord(int v, boolean showPath, int dest) {
        AdjListGraph g = AdjListGraph.Random(v);
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
