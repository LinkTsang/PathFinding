package scau.pathfinding;

import java.util.Stack;

/**
 * 检测图是否存在环
 * @author Link
 */
public class DirectedCycleChecker {
    private boolean[] marked;
    private DirectedEdge[] edgeTo;
    private boolean[] onStack;
    private Stack<DirectedEdge> cycle;
    
    public DirectedCycleChecker(AdjListGraph g) {
        marked  = new boolean[g.V()];
        onStack = new boolean[g.V()];
        edgeTo  = new DirectedEdge[g.V()];
        for (int v = 0; v < g.V(); ++v)
            if (!marked[v]) dfs(g, v);
    }

    private void dfs(AdjListGraph g, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (DirectedEdge e : g.adjacency(v)) {
            int w = e.to();
            if (cycle != null) {
                return;
            } else if (!marked[w]) {
                edgeTo[w] = e;
                dfs(g, w);
            } else if (onStack[w]) {
                // 找到环，回溯
                cycle = new Stack<DirectedEdge>();
                DirectedEdge f = e;
                while (f.from() != w) {
                    cycle.push(f);
                    f = edgeTo[f.from()];
                }
                cycle.push(f);
                return;
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }

    public static void main(String[] args) {
        int vertexCount = 10;
        AdjListGraph g = AdjListGraph.Random(vertexCount);
        DirectedCycleChecker checker = new DirectedCycleChecker(g);
        if(checker.hasCycle()) {
            System.out.println("Found cycle: ");
            for(DirectedEdge e : checker.cycle) {
                System.out.println(e);
            }
        }
    }
}
