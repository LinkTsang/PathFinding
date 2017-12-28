package scau.pathfinding.visual;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import scau.pathfinding.gridmap.Cell;
import scau.pathfinding.gridmap.GridMap;
import scau.pathfinding.gridmap.Vertex;


/**
 * @author Link
 */
public class GridMapView {

    private final static Image TARGET_IMAGE
            = new Image(GridMapView.class.getClassLoader().getResource("scau/pathfinding/resources/target.png").toExternalForm());
    private final static Image SOURCE_IMAGE
            = new Image(GridMapView.class.getClassLoader().getResource("scau/pathfinding/resources/source.png").toExternalForm());
    private final static Image BLOCK_IMAGE
            = new Image(GridMapView.class.getClassLoader().getResource("scau/pathfinding/resources/block.png").toExternalForm());
    private final GridMap gridMap;
    private Font cellTextFont = new Font("chiller", 30);
    private double cellHeight;
    private double cellWidth;
    private DoubleProperty height;
    private DoubleProperty width;
    private Action action = Action.DISABLE;
    private boolean isMouseInMap;
    private double mousePositionX;
    private double mousePositionY;

    public GridMapView(double width, double height, GridMap gridMap) {
        widthProperty().set(width);
        heightProperty().set(height);
        this.gridMap = gridMap;
        gridMap.setResizedListener(this::setCellSize);
        setCellSize(gridMap.getRowCount(), gridMap.getColCount());
    }

    public final DoubleProperty widthProperty() {
        if (width == null) {
            width = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    // Do Something
                }

                @Override
                public Object getBean() {
                    return GridMapView.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return width;
    }

    public final DoubleProperty heightProperty() {
        if (height == null) {
            height = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    // Do something
                }

                @Override
                public Object getBean() {
                    return GridMapView.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return height;
    }

    /**
     * col => column, from left to right
     * row => row, form top to bottom
     *
     * @param gc GraphicsContext
     */
    public void render(GraphicsContext gc) {
        gc.save();
        gc.setStroke(Color.GRAY.brighter());
        for (int i = 0; i < gridMap.getRowCount(); ++i) {
            for (int j = 0; j < gridMap.getColCount(); ++j) {
                double y = i * cellHeight;
                double x = j * cellWidth;
                gc.strokeRect(x, y, cellWidth, cellHeight);
                Cell cell = gridMap.getCells()[i][j];

                switch (cell.getDirection()) {
                    case Left:
                        gc.fillText("←", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    case Right:
                        gc.fillText("→", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    case Up:
                        gc.fillText("↑", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    case Down:
                        gc.fillText("↓", x + cellWidth / 2, y + cellHeight / 2);
                        break;
                    default:
                        break;
                }

                renderCell(gc, i, j, cell);
            }
        }
        gc.restore();

        renderFloating(gc);
    }

    private void renderPath(GraphicsContext gc) {
        Vertex v = gridMap.getTargetVertex();
        if (v == null) return;
        Vertex next = gridMap.getVertexTo()[v.getRow()][v.getCol()];
        gc.setStroke(Color.GREEN);
        while (next != null) {
            double x0 = v.getCol() * cellWidth;
            double y0 = v.getRow() * cellHeight;
            int i = next.getRow();
            int j = next.getCol();
            double x1 = j * cellHeight;
            double y1 = i * cellWidth;
            gc.strokeLine(x0 + cellWidth / 2, y0 + cellHeight / 2, x1 + cellWidth / 2, y1 + cellHeight / 2);
            v = next;
            next = gridMap.getVertexTo()[i][j];
        }
    }

    private void renderFloating(GraphicsContext gc) {
        if (action != Action.DISABLE) {
            int row = (int) (mousePositionY / cellHeight);
            int col = (int) (mousePositionX / cellWidth);
            double x = col * cellHeight;
            double y = row * cellWidth;
            switch (action) {
                case PUT_OR_REMOVE_BLOCK:
                    gc.drawImage(BLOCK_IMAGE, x, y, cellWidth, cellHeight);
                    break;
                case PUT_SOURCE:
                    gc.drawImage(SOURCE_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                    break;
                case PUT_TARGET:
                    gc.drawImage(TARGET_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                    break;
            }
        }

        renderPath(gc);
    }

    private void renderCell(GraphicsContext gc, int row, int col, Cell cell) {
        double y = row * cellHeight;
        double x = col * cellWidth;
        switch (cell.getType()) {
            case BLOCK:
                gc.drawImage(BLOCK_IMAGE, x, y, cellWidth, cellHeight);
                break;
            case SOURCE:
                gc.drawImage(SOURCE_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                break;
            case TARGET:
                gc.drawImage(TARGET_IMAGE, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                break;
            default:
                break;
        }
    }

    public void update() {
        gridMap.update();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isMouseInMap() {
        return isMouseInMap;
    }

    public void setMouseInMap(boolean mouseInMap) {
        isMouseInMap = mouseInMap;
    }

    public void onMouseMoved(double mousePositionX, double mousePositionY) {
        if (action == Action.DISABLE) return;
        this.mousePositionX = mousePositionX;
        this.mousePositionY = mousePositionY;
    }

    public void onMouseAction(double mousePositionX, double mousePositionY) {
        if (action != Action.DISABLE) {
            int row = (int) (mousePositionY / cellHeight);
            int col = (int) (mousePositionX / cellWidth);
            switch (action) {
                case PUT_OR_REMOVE_BLOCK:
                    if (gridMap.getCells()[row][col].getType() == Cell.Type.EMPTY) {
                        gridMap.putBlock(row, col);
                    } else {
                        gridMap.removeBlock(row, col);
                    }
                    break;
                case PUT_SOURCE:
                    if (gridMap.getCells()[row][col].getType() == Cell.Type.EMPTY) {
                        gridMap.setSource(row, col);
                    }
                    break;
                case PUT_TARGET:
                    if (gridMap.getCells()[row][col].getType() == Cell.Type.EMPTY) {
                        gridMap.setTarget(row, col);
                    }
                    break;
            }
        }
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(double cellHeight) {
        this.cellHeight = cellHeight;
    }

    public Font getCellTextFont() {
        return cellTextFont;
    }

    public void setCellTextFont(Font cellTextFont) {
        this.cellTextFont = cellTextFont;
    }

    public DoubleProperty getWidth() {
        return width;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }

    public DoubleProperty getHeight() {
        return height;
    }

    private void setCellSize(int row, int col) {
        setCellHeight(getHeight().get() / row);
        setCellWidth(getWidth().get() / col);
        setCellTextFont(new Font("chiller", Math.min(getCellWidth() * 2, getCellHeight() * 2)));
    }

    public enum Action {
        DISABLE,
        PUT_OR_REMOVE_BLOCK,
        PUT_SOURCE,
        PUT_TARGET
    }
}
