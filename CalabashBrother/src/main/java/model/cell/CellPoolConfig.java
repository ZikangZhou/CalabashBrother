package model.cell;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

class CellPoolConfig extends GenericKeyedObjectPoolConfig<Cell> {

    CellPoolConfig() {
        setMaxIdlePerKey(1);
        setMaxTotalPerKey(1);
    }
}
