package controller;

import io.RecordWriter;
import javafx.application.Platform;
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

import java.util.*;

public class Controller implements Observer {

    public KeyEventHandler keyEventHandler;
    public MouseEventHandler mouseEventHandler;
    private Model model;
    private View view;
    private List<Thread> creatureThreads = new ArrayList<>(Model.getCreatures().size());
    private boolean isSet = false;
    private Random random = new Random();

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        model.addObserver(this);
        keyEventHandler = new KeyEventHandler();
        mouseEventHandler = new MouseEventHandler();
        for(Creature creature : Model.getCreatures()) {
            creatureThreads.add(new Thread(creature));
        }
        for (Thread thread : creatureThreads)
            thread.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(null);
                    alert.setHeaderText(null);
                    if (Model.isReplay)
                        alert.setContentText("回放结束");
                    else
                        alert.setContentText("葫芦娃胜利!");
                    alert.show();
                    view.ballView.setVisible(false);
                    view.vBox.setVisible(true);
                }
        );
        model.clear();
        Model.isReplay = false;
        isSet = false;
    }

    public class KeyEventHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            if (Model.isReplay)
                return;
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
                } catch (Exception e) {
                }
            }

        }
    }

    public class MouseEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (Model.isReplay)
                return;
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
                        RecordWriter writer = new RecordWriter();
                        writer.clear();
                        if (!isSet) {
                            model.clear();
                            model.setNonCalabashCamp(Formation.WING);
                            isSet = true;
                        }
                        model.addCamp();
                        view.vBox.setVisible(false);
                        view.ballView.setX(Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX() * View.scaleX - 10);
                        view.ballView.setY(Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY() * View.scaleY - 10);
                        view.ballView.setVisible(true);
                        break;
                    case "精彩回放":
                        model.clear();
                        Model.isReplay = true;
                        view.vBox.setVisible(false);
                        break;
                }
            } else if (event.getSource() instanceof Canvas) {
                synchronized (Model.getCalabashLeader()) {
                    int sourceX = (int)(event.getX() / View.scaleX);
                    int sourceY = (int)(event.getY() / View.scaleY);
                    for (Creature creature : Model.getCreatures()) {
                        if (creature instanceof CalabashBrother && !creature.equals(Model.getCalabashLeader())
                                && creature.getCell() != null) {
                            int destX = creature.getCell().getCoordinate().getCoordinateX();
                            int destY = creature.getCell().getCoordinate().getCoordinateY();
                            if (Math.abs(sourceX - destX) <= 2 && Math.abs(sourceY - destY) <= 2) {
                                if (random.nextInt(20) != 10) {
                                    Model.getCalabashLeader().notify();
                                    model.setCalabashLeader((CalabashBrother) creature);
                                }
                                else {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle(null);
                                    alert.setHeaderText(null);
                                    alert.setContentText("传球失败，老爷爷直接死掉！");
                                    alert.show();
                                    view.ballView.setVisible(false);
                                    view.vBox.setVisible(true);
                                    model.clear();
                                    for (Thread thread : creatureThreads)
                                        thread.interrupt();
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

