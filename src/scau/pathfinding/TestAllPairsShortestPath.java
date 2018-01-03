package scau.pathfinding;

/**
 * @author Link
 * Floyd, Johnson 运行时间测试
 */
public class TestAllPairsShortestPath {
    public static void main(String[] args) {
        testFloydAndJohnson(AdjMatrix.Random(10));
        testSparseGraph();
        testDenseGraph();
    }

    private static void runTest(int count, AdjMatrix[] adjMatrices, AdjListGraph[] adjListGraphs) {
        for (AdjMatrix g : adjMatrices) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testFloyd(g);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / count);
        }
        System.out.println();
        for (AdjListGraph g : adjListGraphs) {
            double sum = 0;
            for (int i = 0; i < count; ++i) {
                long duration = testJohnson(g);
                sum += duration / 1000000.0;
            }
            System.out.printf("average: %f ms\n", sum / count);
        }
        System.out.println();
    }

    private static void testSparseGraph() {
        int count = 10;
        int vSizes[] = {
                10, 100, 1000, 2000
        };
        AdjMatrix[] adjMatrices = new AdjMatrix[vSizes.length];
        for (int i = 0; i < adjMatrices.length; ++i) {
            adjMatrices[i] = AdjMatrix.Random(vSizes[i], (int) (vSizes[i] * Math.log(vSizes[i])));
        }
        AdjListGraph[] adjListGraphs = new AdjListGraph[adjMatrices.length];
        for (int i = 0; i < adjListGraphs.length; ++i) {
            adjListGraphs[i] = AdjListGraph.fromAdjAdjMatrix(adjMatrices[i]);
        }
        runTest(count, adjMatrices, adjListGraphs);
    }

    private static void testDenseGraph() {
        int count = 10;
        int vSizes[] = {
                10, 100, 1000, 2000
        };
        AdjMatrix[] adjMatrices = new AdjMatrix[vSizes.length];
        for (int i = 0; i < adjMatrices.length; ++i) {
            adjMatrices[i] = AdjMatrix.Random(vSizes[i], vSizes[i] * vSizes[i]);
        }
        AdjListGraph[] adjListGraphs = new AdjListGraph[adjMatrices.length];
        for (int i = 0; i < adjListGraphs.length; ++i) {
            adjListGraphs[i] = AdjListGraph.fromAdjAdjMatrix(adjMatrices[i]);
        }
        runTest(count, adjMatrices, adjListGraphs);
    }


    private static long testJohnson(AdjListGraph g) {
        long startTime = System.nanoTime();
        Johnson j = new Johnson(g);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.printf("%s (V: %d, E: %d) cost %f ms\n",
                j.getClass().getSimpleName(),
                g.V(), g.E(),
                duration / 1000000.0);
        return duration;
    }

    private static long testFloyd(AdjMatrix g) {
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

    private static void testFloydAndJohnson(AdjMatrix g) {
        Floyd y = new Floyd(g);

        AdjListGraph adjListGraph = AdjListGraph.fromAdjAdjMatrix(g);
        Johnson j = new Johnson(adjListGraph);
        for (int u = 0; u < adjListGraph.V(); u++) {
            System.out.printf("%-12d ", u);
            for (int v = 0; v < adjListGraph.V(); v++) {
                System.out.printf("%-12f  ", j.distance(u, v));
            }
            System.out.println();
            System.out.printf("%-12d ", u);
            for (int v = 0; v < adjListGraph.V(); v++) {
                System.out.printf("%-12f  ", y.distance(u, v));
            }
            System.out.println();
            System.out.println();
        }
    }
}
