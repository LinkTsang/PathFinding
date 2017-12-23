package scau.pathfinding;

/**
 * @author Link
 */
public class DirectedEdge {
    private final int v;
    private final int w;
    private final double weight;

    public DirectedEdge(int from, int to, double weight)
    {
        v = from;
        w = to;
        this.weight = weight;
    }
    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%d->%d %.2f", v, w, weight);
    }
}
