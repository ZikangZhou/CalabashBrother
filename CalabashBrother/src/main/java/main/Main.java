package main;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Model;
import view.View;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        ExecutorService exec = Executors.newCachedThreadPool();
        final Model model = new Model();
        final View view = new View();
        final Controller controller = new Controller(model, view);

        exec.execute(model);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                view.update();
            }
        }.start();

        view.scene.setOnKeyReleased(controller.keyEventHandler);
        view.canvas.setOnMouseClicked(controller.mouseEventHandler);
        for (Button button : view.buttons)
            button.setOnMouseClicked(controller.mouseEventHandler);
    }
}
