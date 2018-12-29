package model;

import io.RecordWriter;
import javafx.scene.image.Image;
import model.cell.Cell;
import model.cell.CellPool;

public class Property {

    private String name;
    private Image image;
    private Cell cell;

    private static RecordWriter writer = new RecordWriter();

    public Property(String name, Image image) {
        this.name = name;
        this.image = image;
        this.cell = null;
    }

    public Property(String name, Cell cell) {
        this.name = name;
        this.cell = cell;
        this.image = null;
    }

    public String getName() {
        return name;
    }

    Image getImage() {
        return image;
    }

    public Cell getCell() {
        return cell;
    }

    void setCell(Coordinate coordinate) throws Exception {
        Cell oldCell = cell;
        try {
            cell = CellPool.getInstance().borrowObject(coordinate, 16);
            writer.writeProperty(this);
            if (oldCell != null)
                CellPool.getInstance().returnObject(oldCell.getCoordinate(), oldCell);
        } catch (Exception e) {
            cell = oldCell;
            throw e;
        }
    }

    void removeCell() {
        if (cell != null) {
            try {
                CellPool.getInstance().returnObject(cell.getCoordinate(), cell);
                cell = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
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
