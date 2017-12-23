package scau.pathfinding;

/**
 * @author Link
 */
public interface ShortPath {
    double distanceTo(int v);

    boolean hasPathTo(int v);

    Iterable<DirectedEdge> pathTo(int v);
}
