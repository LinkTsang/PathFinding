package scau.pathfinding.visual;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import scau.pathfinding.gridmap.GridMap;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Link
 */
public class MainViewController implements Initializable {
    private final static double SECOND_PER_FRAME = 1.0 / 60;  // 60 PFS
    public ComboBox<GridMap.FindingMethod> pathFindingMethod;
    public TextField mapRowTextField;
    public TextField mapColTextField;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Canvas canvas;

    private GridMap gridMap;
    private GridMapView gridMapView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pathFindingMethod.getItems().setAll(GridMap.FindingMethod.values());
        pathFindingMethod.getSelectionModel().selectFirst();
        pathFindingMethod.valueProperty().addListener((observable, oldValue, newValue) -> {
            gridMap.setFindingMethod(newValue);
            gridMap.updatePath();
        });

        mapRowTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateMapSizeTextField(mapRowTextField, newValue);
        });
        mapColTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateMapSizeTextField(mapColTextField, newValue);
        });

        canvas.setOnMouseExited(e -> {
            gridMapView.setMouseInMap(false);
        });

        canvas.setOnMouseEntered(e -> {
            gridMapView.setMouseInMap(true);
        });

        canvas.setOnMouseMoved(e -> {
            double mousePositionX = e.getX();
            double mousePositionY = e.getY();
            gridMapView.onMouseMoved(mousePositionX, mousePositionY);
        });

        canvas.setOnMouseClicked(e -> {
            double mousePositionX = e.getX();
            double mousePositionY = e.getY();
            if (e.getButton() == MouseButton.PRIMARY) {
                gridMapView.onMouseAction(mousePositionX, mousePositionY);
            } else if (e.getButton() == MouseButton.SECONDARY) {
                gridMapView.setAction(GridMapView.Action.DISABLE);
            }
        });


        int row = 16, col = 16;
        //this.gridMap = new GridMapView(canvas.getWidth(), canvas.getHeight(), row, col);
        this.gridMap = new GridMap(row, col);
        this.gridMapView = new GridMapView(canvas.getWidth(), canvas.getHeight(), gridMap);
        gridMap.setSource(row / 2, col / 3);
        gridMap.setTarget(row / 2, col * 2 / 3);
        gridMap.updatePath(GridMap.FindingMethod.BFS);

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.seconds(SECOND_PER_FRAME),
                (ActionEvent ae) -> {
                    update();
                    render(canvas.getGraphicsContext2D());
                });
        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gridMapView.render(gc);
    }

    public void update() {
        gridMapView.update();
    }

    public void triggerBlockButton_OnAction(ActionEvent ae) {
        gridMapView.setAction(GridMapView.Action.PUT_OR_REMOVE_BLOCK);
    }

    public void setTargetButton_OnAction(ActionEvent ae) {
        gridMapView.setAction(GridMapView.Action.PUT_TARGET);
    }

    public void setSourceButton_OnAction(ActionEvent ae) {
        gridMapView.setAction(GridMapView.Action.PUT_SOURCE);
    }

    public void pathFindingMethod_OnAction(ActionEvent ae) {
        gridMap.setFindingMethod(pathFindingMethod.getSelectionModel().getSelectedItem());
    }

    private void updateMapSizeTextField(TextField textField, String newValue) {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
        if (Integer.valueOf(newValue) > 99) textField.setText("99");
        if (Integer.valueOf(newValue) < 5) textField.setText("5");

        int row = Integer.valueOf(mapRowTextField.getText());
        int col = Integer.valueOf(mapColTextField.getText());
//        gridMap.setMapSize(row, col);
    }
}
