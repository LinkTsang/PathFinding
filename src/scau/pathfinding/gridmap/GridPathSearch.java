package scau.pathfinding.gridmap;

import java.util.Iterator;

/**
 * @author Link
 */
public class GridPathSearch {
    protected Cell[][] cells;
    protected Vertex[][] vertexTo;
    protected int stepCount = 0;

    public Vertex[][] getVertexTo() {
        return vertexTo;
    }

    public Vertex vertexTo(int row, int col) {
        return vertexTo[row][col];
    }

    /**
     * @return Snapshot of directions and vertexTo
     */
    public Iterator<AStarSearch.Snap> iterator() {
        return new Iterator<AStarSearch.Snap>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public AStarSearch.Snap next() {
                return null;
            }
        };
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getStepCount() {
        return stepCount;
    }

    public static class Snap {
        Direction[][] directions;
        Vertex[][] vertexTo;

        public Snap(Direction[][] directions, Vertex[][] pathTo) {
            this.directions = directions;
            this.vertexTo = pathTo;
        }
    }
}
