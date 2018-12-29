package model;

public class Coordinate {

    private int coordinateX;
    private int coordinateY;

    public Coordinate(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public Coordinate(Coordinate coordinate) {
        if (coordinate != null) {
            coordinateX = coordinate.getCoordinateX();
            coordinateY = coordinate.getCoordinateY();
        }
        else {
            coordinateX = -1;
            coordinateY = -1;
        }
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public void set(Coordinate coordinate) {
        coordinateX = coordinate.getCoordinateX();
        coordinateY = coordinate.getCoordinateY();
    }

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(this.getCoordinateX() + coordinate.getCoordinateX(),
                this.getCoordinateY() + coordinate.getCoordinateY());
    }

    public Coordinate add(int coordinateX, int coordinateY) {
        return new Coordinate(this.getCoordinateX() + coordinateX, this.getCoordinateY() + coordinateY);
    }

    boolean atLeftOf(Coordinate coordinate) {
        return coordinateX < coordinate.getCoordinateX();
    }

    boolean atRightOf(Coordinate coordinate) {
        return coordinateX > coordinate.getCoordinateX();
    }

    boolean atUpOf(Coordinate coordinate) {
        return coordinateY < coordinate.getCoordinateY();
    }

    boolean atDownOf(Coordinate coordinate) {
        return coordinateY > coordinate.getCoordinateY();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Coordinate coordinate = (Coordinate) obj;
        return coordinateX == coordinate.getCoordinateX() &&
                coordinateY == coordinate.getCoordinateY();
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 17;
        result = prime * result + coordinateX;
        result = prime * result + coordinateY;
        return result;
    }

    @Override
    public String toString() {
        return "x:" + coordinateX + ",y:" + coordinateY;
    }
}

