package scau.pathfinding.gridmap;

/**
 * @author Link
 */
public class Cell {
    private Type type = Type.EMPTY;
    private Direction direction = Direction.None;
    private double weight = 1;

    public boolean isPassable() {
        return type != Type.BLOCK;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public enum Type {
        EMPTY,
        BLOCK,
        SOURCE,
        TARGET
    }
}
