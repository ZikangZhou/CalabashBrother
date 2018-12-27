package controller;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.CalabashBrother;
import model.Creature;
import model.Formation;
import model.Model;
import view.View;

import java.util.Random;

public class Controller {

    public KeyEventHandler keyEventHandler;
    public MouseEventHandler mouseEventHandler;
    private Model model;
    private View view;
    private boolean isSet = false;
    private Random random = new Random();

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        keyEventHandler = new KeyEventHandler();
        mouseEventHandler = new MouseEventHandler();
    }

    public class KeyEventHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            synchronized (Model.getCalabashLeader()) {
                try {
                    switch (event.getCode()) {
                        case W:
                            Model.getCalabashLeader().moveUp();
                            Model.getCalabashLeader().notify();
                            break;
                        case S:
                            Model.getCalabashLeader().moveDown();
                            Model.getCalabashLeader().notify();
                            break;
                        case A:
                            Model.getCalabashLeader().moveLeft();
                            Model.getCalabashLeader().notify();
                            break;
                        case D:
                            Model.getCalabashLeader().moveRight();
                            Model.getCalabashLeader().notify();
                            break;
                    }
                    if (Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() >= 20) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(null);
                        alert.setHeaderText(null);
                        alert.setContentText("葫芦娃胜利!");
                        alert.showAndWait();
                        view.ballView.setVisible(false);
                        view.vBox.setVisible(true);
                        model.clear();
                        isSet = false;
                    }
                } catch (Exception e) {
                }
            }

        }
    }

    public class MouseEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() instanceof Button) {
                String text = ((Button) event.getSource()).getText();
                System.out.println(text);
                switch (text) {
                    case "鹤翼阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.WING);
                        isSet = true;
                        break;
                    case "雁行阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.FLIGHT);
                        isSet = true;
                        break;
                    case "衡轭阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.YOKE);
                        isSet = true;
                        break;
                    case "长蛇阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.LINE);
                        isSet = true;
                        break;
                    case "鱼鳞阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.SCALE);
                        isSet = true;
                        break;
                    case "方円阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.SQUARE);
                        isSet = true;
                        break;
                    case "偃月阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.CRESCENT);
                        isSet = true;
                        break;
                    case "锋矢阵":
                        model.clear();
                        model.setNonCalabashCamp(Formation.ARROW);
                        isSet = true;
                        break;
                    case "开始游戏":
                        if (!isSet) {
                            model.clear();
                            model.setNonCalabashCamp(Formation.WING);
                            isSet = true;
                        }
                        view.vBox.setVisible(false);
                        model.addCamp();
                        view.ballView.setX(Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() * View.scaleX - 10);
                        view.ballView.setY(Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY() * View.scaleY - 10);
                        view.ballView.setVisible(true);
                        break;
                }
            } else if (event.getSource() instanceof Canvas) {
                synchronized (Model.getCalabashLeader()) {
                    int sourceX = (int)(event.getX() / View.scaleX);
                    int sourceY = (int)(event.getY() / View.scaleY);
                    for (Creature creature : Model.getCreatures()) {
                        if (creature instanceof CalabashBrother && !creature.equals(Model.getCalabashLeader())) {
                            int destX = creature.getCell().getCoordinate().getCoordinateX();
                            int destY = creature.getCell().getCoordinate().getCoordinateY();
                            if (sourceX == destX && sourceY == destY) {
                                if (random.nextInt(8) != 5) {
                                    Model.getCalabashLeader().notify();
                                    model.setCalabashLeader((CalabashBrother) creature);
                                }
                                else {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle(null);
                                    alert.setHeaderText(null);
                                    alert.setContentText("传球失败，老爷爷直接死掉！");
                                    alert.showAndWait();
                                    view.ballView.setVisible(false);
                                    view.vBox.setVisible(true);
                                    model.clear();
                                    isSet = false;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}

