package model;

import javafx.scene.image.Image;
import model.cell.Cell;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public enum CalabashBrother implements Calabash {

    RED(new Image("RED.png"), Color.RED, Rank.FIRST),
    ORANGE(new Image("ORANGE.png"), Color.ORANGE, Rank.SECOND),
    YELLOW(new Image("YELLOW.png"), Color.YELLOW, Rank.THIRD),
    GREEN(new Image("GREEN.png"), Color.GREEN, Rank.FOURTH),
    CYAN(new Image("CYAN.png"), Color.CYAN, Rank.FIFTH),
    BLUE(new Image("BLUE.png"), Color.BLUE, Rank.SIXTH),
    PURPLE(new Image("PURPLE.png"), Color.PURPLE, Rank.SEVENTH);

    private Random random = new Random();

    CalabashProperty property;

    CalabashBrother(Image image, Color color, Rank rank) {
        property = new CalabashProperty(image, color, rank);
    }

    public String getName() {
        return property.getName();
    }

    public Image getImage() {
        return property.getImage();
    }

    public Rank getRank() {
        return property.getRank();
    }

    public Color getColor() {
        return property.getColor();
    }

    public Cell getCell() {
        return property.getCell();
    }

    public void setCell(Coordinate coordinate) {
        try {
            property.setCell(coordinate);
            if (property.getCell() != null)
                property.getCell().setCreature(this);
        } catch (Exception e) {
            System.out.println("cell for creature at x: " + coordinate.getCoordinateX() + ", y: "
                    + coordinate.getCoordinateY() + " is not available now");
        }
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted() || Model.isReplay || Model.end())
                continue;
            if (Model.getCalabashSoldier().indexOf(this) != -1) {
                int flag = random.nextInt(4);
                if (getCell() != null) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                        if (flag == 0 && getCell().getCoordinate().getCoordinateX() + 1 >= 0 &&
                                getCell().getCoordinate().getCoordinateX() + 1 < Model.row &&
                                (!getCell().getCoordinate().atRightOf(Model.getCalabashLeader().getCell().getCoordinate()) ||
                                        Math.abs(getCell().getCoordinate().getCoordinateX() + 1 - Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX()) <= 3)) {
                            try {
                                moveRight();
                            } catch (Exception e) {
                            }
                        } else if (flag == 1 && getCell().getCoordinate().getCoordinateX() - 1 >= 0 &&
                                getCell().getCoordinate().getCoordinateX() - 1 < Model.row &&
                                (!getCell().getCoordinate().atLeftOf(Model.getCalabashLeader().getCell().getCoordinate()) ||
                                        Math.abs(getCell().getCoordinate().getCoordinateX() - 1 - Model.getCalabashLeader().getCell().getCoordinate().getCoordinateX()) <= 3)) {
                            try {
                                moveLeft();
                            } catch (Exception e) {
                            }
                        } else if (flag == 2 && getCell().getCoordinate().getCoordinateY() + 1 >= 0 &&
                                getCell().getCoordinate().getCoordinateY() + 1 < Model.col &&
                                (!getCell().getCoordinate().atDownOf(Model.getCalabashLeader().getCell().getCoordinate()) ||
                                        Math.abs(getCell().getCoordinate().getCoordinateY() + 1 - Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY()) <= 3)) {
                            try {
                                moveDown();
                            } catch (Exception e) {
                            }
                        } else if (flag == 3 && getCell().getCoordinate().getCoordinateY() - 1 >= 0 &&
                                getCell().getCoordinate().getCoordinateY() - 1 < Model.col &&
                                (!getCell().getCoordinate().atUpOf(Model.getCalabashLeader().getCell().getCoordinate()) ||
                                        Math.abs(getCell().getCoordinate().getCoordinateY() - 1 - Model.getCalabashLeader().getCell().getCoordinate().getCoordinateY()) <= 3)) {
                            try {
                                moveUp();
                            } catch (Exception e) {
                            }
                        }
                        try {
                            notify();
                        } catch (Exception e) {
                        }
                        Thread.yield();
                    } catch (Exception e) {
                    }

                }
            }
        }
    }

    public void removeCell() {
        property.removeCell();
    }

    public void moveDown() throws Exception {
        property.moveDown();
    }

    public void moveLeft() throws Exception {
        property.moveLeft();
    }

    public void moveRight() throws Exception {
        property.moveRight();
    }

    public void moveUp() throws Exception {
        property.moveUp();
    }
}
