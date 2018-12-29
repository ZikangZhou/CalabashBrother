package model.cell;

import algorithm.ShortestPath;
import model.Coordinate;
import model.Creature;
import model.Model;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

import java.util.*;


public class CellPool extends GenericKeyedObjectPool<Coordinate, Cell> {

    private static Random random = new Random();

    private CellPool() {

        super(new CellFactory(), new CellPoolConfig());
        setBlockWhenExhausted(false);

    }

    public static CellPool getInstance() {
        return CellPoolHolder.cellPool;
    }

    public static List<Coordinate> getPath(Coordinate coordinate1, Coordinate coordinate2) {
        Map<Coordinate, Boolean> isEmpty = new HashMap<>();
        ShortestPath sp = new ShortestPath();
        ShortestPath.Graph<Coordinate> graph = sp.new Graph<>();

        for (Creature creature : Model.getCreatures()) {
            if (creature.getCell() != null && !creature.getCell().getCoordinate().equals(coordinate2))
                isEmpty.put(creature.getCell().getCoordinate(), false);
        }

        for (int i = 0; i < Model.row; ++i) {
            for (int j = 0; j < Model.col; ++j) {
                if (!isEmpty.keySet().contains(Model.coordinates[i][j])) {
                    isEmpty.put(Model.coordinates[i][j], true);
                }
            }
        }

        int flag = random.nextInt(2);
        if (flag == 0 && coordinate2.getCoordinateX() + 1 < Model.row &&
                isEmpty.get(Model.coordinates[coordinate2.getCoordinateX() + 1][coordinate2.getCoordinateY()])) {
            isEmpty.remove(coordinate2);
            isEmpty.put(coordinate2, false);
        }

        for (int i = 0; i < Model.row; ++i) {
            for (int j = 0; j < Model.col; ++j) {
                List<ShortestPath.Edge<Coordinate>> edges = new ArrayList<>();
                if (i - 1 >= 0 && isEmpty.get(Model.coordinates[i - 1][j])) {
                    edges.add(sp.new Edge<>(Model.coordinates[i - 1][j], 1.0));
                }
                if (i + 1 < Model.row && isEmpty.get(Model.coordinates[i + 1][j])) {
                    edges.add(sp.new Edge<>(Model.coordinates[i + 1][j], 1.0));
                }
                if (j - 1 >= 0 && isEmpty.get(Model.coordinates[i][j - 1])) {
                    edges.add(sp.new Edge<>(Model.coordinates[i][j - 1], 1.0));
                }
                if (j + 1 < Model.col && isEmpty.get(Model.coordinates[i][j + 1])) {
                    edges.add(sp.new Edge<>(Model.coordinates[i][j + 1], 1.0));
                }
                graph.add(Model.coordinates[i][j], edges);
            }
        }
        if (coordinate2.getCoordinateX() + 1 < Model.row &&
                isEmpty.get(Model.coordinates[coordinate2.getCoordinateX() + 1][coordinate2.getCoordinateY()])) {
            //int flag = random.nextInt(2);
            if (flag == 0)
                return graph.getShortestPath(coordinate1,
                        Model.coordinates[coordinate2.getCoordinateX() + 1][coordinate2.getCoordinateY()]);
            else
                return graph.getShortestPath(coordinate1, coordinate2);
        } else {
            return graph.getShortestPath(coordinate1, coordinate2);
        }
    }

    private static class CellPoolHolder {
        private static CellPool cellPool = new CellPool();
    }
}