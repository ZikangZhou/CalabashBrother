package model;

import javafx.scene.image.Image;
import model.cell.Cell;
import model.cell.CellPool;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

enum NonCalabashBrother implements Creature {

    GRANDPA("老爷爷", new Image("GRANDPA.png")) {
        @Override
        public void run() {
            while (true) {
                boolean flag = random.nextBoolean();
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                    if (flag)
                        moveUp();
                    else
                        moveDown();
                    notify();
                    Thread.yield();
                } catch (Exception e) {
                }
            }
        }
    },

    SNAKE("蛇精", new Image("SNAKE.png")) {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                    moveTowards(GRANDPA);
                    GRANDPA.wait();
                } catch (Exception e) {
                }
            }
        }
    },

    SCORPION("蝎子精", new Image("SCORPION.png")) {
        @Override
        public void run() {
            while (true) {
                synchronized (Model.getCalabashLeader()) {
                    try {
                        moveTowards(Model.getCalabashLeader());
                        Model.getCalabashLeader().wait();
                    } catch (InterruptedException e) {
                        System.out.println("interrupt when try to wait on calabashLeader.");
                    }
                }
            }
        }
    };

    Random random = new Random();

    Property property;

    NonCalabashBrother(String name, Image image) {
        property = new Property(name, image);
    }

    public String getName() {
        return property.getName();
    }

    public Image getImage() {
        return property.getImage();
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
        }
    }

    public void removeCell() {
        property.removeCell();
    }

    public void moveDown() {
        try {
            property.moveDown();
        } catch (Exception e) {
        }
    }

    public void moveLeft() {
        try {
            property.moveLeft();
        } catch (Exception e) {
        }
    }

    public void moveRight() {
        try {
            property.moveRight();
        } catch (Exception e) {
        }
    }

    public void moveUp() {
        try {
            property.moveUp();
        } catch (Exception e) {
        }
    }
}

enum Color {

    RED("红色"), ORANGE("橙色"), YELLOW("黄色"), GREEN("绿色"), CYAN("青色"), BLUE("蓝色"), PURPLE("紫色");

    private String color;

    Color(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}

enum Rank {

    FIRST("老大"), SECOND("老二"), THIRD("老三"), FOURTH("老四"), FIFTH("老五"), SIXTH("老六"), SEVENTH("老七");

    private String rank;

    Rank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return rank;
    }
}

public interface Creature extends Runnable {

    String getName();

    Cell getCell();

    void setCell(Coordinate coordinate);

    Image getImage();

    void removeCell();

    void moveUp() throws Exception;

    void moveDown() throws Exception;

    void moveLeft() throws Exception;

    void moveRight() throws Exception;

    default void moveTowards(Creature creature) {
        if (creature.getCell() != null && getCell() != null) {
            List<Coordinate> path = CellPool.getPath(getCell().getCoordinate(),
                    creature.getCell().getCoordinate());
            if (path.size() != 0) {
                Coordinate destination = path.get(path.size() - 1);
                if (destination.atUpOf(getCell().getCoordinate())) {
                    try {
                        moveUp();
                    } catch (Exception e) {
                    }
                } else if (destination.atDownOf(getCell().getCoordinate())) {
                    try {
                        moveDown();
                    } catch (Exception e) {
                    }
                } else if (destination.atLeftOf(getCell().getCoordinate())) {
                    try {
                        moveLeft();
                    } catch (Exception e) {
                    }
                } else if (destination.atRightOf(getCell().getCoordinate())) {
                    try {
                        moveRight();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}

interface Calabash extends Creature {

    Rank getRank();

    Color getColor();
}

class Property {

    private String name;
    private Image image;
    private Cell cell;

    Property(String name, Image image) {
        this.name = name;
        this.image = image;
        this.cell = null;
    }

    String getName() {
        return name;
    }

    Image getImage() {
        return image;
    }

    Cell getCell() {
        return cell;
    }

    void setCell(Coordinate coordinate) throws Exception {
        Cell oldCell = cell;
        try {
            cell = CellPool.getInstance().borrowObject(coordinate, 16);
            if (oldCell != null)
                CellPool.getInstance().returnObject(oldCell.getCoordinate(), oldCell);
        } catch (Exception e) {
            cell = oldCell;
            throw e;
        }
    }

    void removeCell() {
        if (cell != null) {
            CellPool.getInstance().returnObject(cell.getCoordinate(), cell);
            cell = null;
        }
    }

    void moveUp() throws Exception {
        Coordinate target = cell.getCoordinate().add(0, -1);
        setCell(Model.coordinates[target.getCoordinateX()][target.getCoordinateY()]);
    }

    void moveDown() throws Exception {
        Coordinate target = cell.getCoordinate().add(0, 1);
        setCell(Model.coordinates[target.getCoordinateX()][target.getCoordinateY()]);
    }

    void moveLeft() throws Exception {
        Coordinate target = cell.getCoordinate().add(-1, 0);
        setCell(Model.coordinates[target.getCoordinateX()][target.getCoordinateY()]);
    }

    void moveRight() throws Exception {
        Coordinate target = cell.getCoordinate().add(1, 0);
        setCell(Model.coordinates[target.getCoordinateX()][target.getCoordinateY()]);
    }
}

class CalabashProperty extends Property {

    private Color color;
    private Rank rank;

    CalabashProperty(Image image, Color color, Rank rank) {
        super(color.toString().charAt(0) + "娃", image);
        this.color = color;
        this.rank = rank;
    }

    Rank getRank() {
        return rank;
    }

    Color getColor() {
        return color;
    }
}

class Underlying implements Creature {

    private Property property;

    Underlying() {
        property = new Property("小喽啰", new Image("UNDERLYING.png"));
    }

    public String getName() {
        return property.getName();
    }

    public Image getImage() {
        return property.getImage();
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
        }
    }

    public void removeCell() {
        property.removeCell();
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                int index = Model.getNonCalabashSoldier().indexOf(this);
                moveTowards(Model.getCalabashSoldier().get(index));
                Model.getCalabashSoldier().get(index).wait();
            } catch (Exception e) {
            }
        }
    }

    public void moveDown() {
        try {
            property.moveDown();
        } catch (Exception e) {
        }
    }

    public void moveLeft() {
        try {
            property.moveLeft();
        } catch (Exception e) {
        }
    }

    public void moveRight() {
        try {
            property.moveRight();
        } catch (Exception e) {
        }
    }

    public void moveUp() {
        try {
            property.moveUp();
        } catch (Exception e) {
        }
    }
}

