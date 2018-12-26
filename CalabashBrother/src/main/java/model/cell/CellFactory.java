package model.cell;

import model.Coordinate;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class CellFactory extends BaseKeyedPooledObjectFactory<Coordinate, Cell> {

    @Override
    public Cell create(Coordinate coordinate) {
        return new Cell(coordinate);
    }

    @Override
    public PooledObject<Cell> wrap(Cell cell) {
        return new DefaultPooledObject<>(cell);
    }

    @Override
    public void passivateObject(Coordinate coordinate, PooledObject<Cell> pooledObject) {
        pooledObject.getObject().remove();
    }
}
