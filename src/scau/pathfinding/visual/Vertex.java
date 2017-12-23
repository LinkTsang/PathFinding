package scau.pathfinding.visual;

/**
 * @author Link
 */
public class Vertex implements Comparable<scau.pathfinding.visual.Vertex> {
    private Direction direction;
    private int col;
    private int row;
    private double value;

    public Vertex(int row, int col, double value) {
        this.col = col;
        this.row = row;
        this.value = value;
    }

    public Vertex(int row, int col) {
        this.col = col;
        this.row = row;
        this.direction = Direction.None;
        this.value = 1;
    }

    public Vertex(int row, int col, Direction direction) {
        this.col = col;
        this.row = row;
        this.direction = direction;
    }

    @Override
    public int compareTo(scau.pathfinding.visual.Vertex v) {
        return Double.compare(this.value, v.value);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof scau.pathfinding.visual.Vertex && compareTo((scau.pathfinding.visual.Vertex) object) == 0;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
