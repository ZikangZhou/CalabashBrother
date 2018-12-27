package view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Creature;
import model.Model;
import model.cell.Cell;

import java.util.ArrayList;
import java.util.List;

public class View extends Stage {

    public static int scaleX = 59;
    public static int scaleY = 39;
    public static int creatureWidth = 28;
    public static int creatureHeight = 28;
    public ImageView ballView = new ImageView(new Image("select.png"));
    public Scene scene;
    public Canvas canvas = new Canvas();
    public VBox vBox = new VBox(20);
    public List<Button> buttons = new ArrayList<>();
    private GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

    public View() {

        Group root = new Group();
        root.getChildren().add(canvas);
        root.getChildren().add(vBox);
        root.getChildren().add(ballView);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        buttons.add(new Button("鹤翼阵"));
        buttons.add(new Button("雁行阵"));
        buttons.add(new Button("衡轭阵"));
        buttons.add(new Button("长蛇阵"));
        buttons.add(new Button("鱼鳞阵"));
        buttons.add(new Button("方円阵"));
        buttons.add(new Button("偃月阵"));
        buttons.add(new Button("锋矢阵"));
        buttons.add(new Button("开始游戏"));
        for (Button button : buttons) {
            button.setPrefWidth(110);
            button.setPrefHeight(69);
        }
        vBox.getChildren().addAll(buttons);
        ballView.setFitWidth(50);
        ballView.setFitHeight(50);
        ballView.setVisible(false);
        scene = new Scene(root);
        setScene(scene);
        setTitle("葫芦兄弟大闹超级碗");
        setWidth(1280);
        setHeight(800);
        show();
    }

    public void update() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.drawImage(new Image("background.png"), 0, 0, canvas.getWidth(), canvas.getHeight());
        if (Model.getCalabashLeader().getCell() != null && ((int)ballView.getX() !=
                Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() * scaleX - 10 ||
                (int)ballView.getY() != Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY() * scaleY - 10)) {
            Timeline timeline = new Timeline();
            if (Math.abs(ballView.getX() - (Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() * scaleX - 10)) <= scaleX &&
                    Math.abs(ballView.getY() - (Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY() * scaleY - 10)) <= scaleY) {
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), new KeyValue(ballView.xProperty(),
                        Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() * scaleX - 10),
                        new KeyValue(ballView.yProperty(),
                                Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY() * scaleY - 10)));
            } else {
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), new KeyValue(ballView.xProperty(),
                        Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() * scaleX - 10),
                        new KeyValue(ballView.yProperty(),
                                Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY() * scaleY - 10)));
            }
            timeline.play();
        }
        for (Creature creature : Model.getCreatures()) {
            Cell cell = creature.getCell();
            if (cell != null) {
                graphicsContext.drawImage(creature.getImage(), creature.getCell().getCoordinate().getCoordinateX() * scaleX,
                        creature.getCell().getCoordinate().getCoordinateY() * scaleY, creatureWidth, creatureHeight);
            }
        }
    }
}