package scau.pathfinding;

/**
 * @author Link
 * Floyd 运行时间测试
 */
public class TestAllPairsShortestPath {
    public static void main(String[] args) {
        testFloyd(10);
        int list[] = {10, 100, 1000, 2000};
        for (int x : list) {
            double sum = 0;
            for (int i = 0; i < 5; ++i) {
                long duration = testFloyd(x);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / 5);
        }
        System.out.println();
    }

    private static long testFloyd(int v) {
        AdjMatrix g = AdjMatrix.Random(v);
        long startTime = System.nanoTime();
        Floyd y = new Floyd(g);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.printf("%s (V: %d, E: %d) cost %f ms\n",
                y.getClass().getSimpleName(),
                g.V(), g.E(),
                duration / 1000000.0);
        return duration;
    }
}
