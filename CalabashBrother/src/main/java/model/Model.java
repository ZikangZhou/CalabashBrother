package model;

import io.RecordReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class Model extends Observable implements Runnable {

    public static Coordinate[][] coordinates;
    public static int row = 22;
    public static int col = 20;
    public static boolean isReplay = false;
    private static CalabashBrother calabashLeader = CalabashBrother.RED;
    private static List<CalabashBrother> calabashSoldier = new ArrayList<>();
    private static NonCalabashBrother calabashSupporter = NonCalabashBrother.GRANDPA;
    private static Formation calabashFormation = Formation.LINE;
    private static CalabashCamp calabashCamp;
    private static Creature nonCalabashLeader = NonCalabashBrother.SCORPION;
    private static List<Creature> nonCalabashSoldier = new ArrayList<>();
    private static NonCalabashBrother nonCalabashSupporter = NonCalabashBrother.SNAKE;
    private static Formation nonCalabashFormation;
    private static NonCalabashCamp nonCalabashCamp;
    private static List<Creature> creatures = new ArrayList<>();

    private BufferedReader reader;

    public Model() {

        coordinates = new Coordinate[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                coordinates[i][j] = new Coordinate(i, j);
            }
        }

        try {
            File file = new File("record.json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reader = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        calabashSoldier.add(CalabashBrother.ORANGE);
        calabashSoldier.add(CalabashBrother.YELLOW);
        calabashSoldier.add(CalabashBrother.GREEN);
        calabashSoldier.add(CalabashBrother.CYAN);
        calabashSoldier.add(CalabashBrother.BLUE);
        calabashSoldier.add(CalabashBrother.PURPLE);


        for (int i = 0; i < 6; ++i)
            nonCalabashSoldier.add(new Underlying("小喽啰" + i));

        creatures.add(calabashLeader);
        creatures.addAll(calabashSoldier);
        creatures.add(calabashSupporter);
        creatures.add(nonCalabashLeader);
        creatures.addAll(nonCalabashSoldier);
        creatures.add(nonCalabashSupporter);

    }

    public static Creature getCalabashLeader() {
        return calabashLeader;
    }

    public static List<CalabashBrother> getCalabashSoldier() {
        return calabashSoldier;
    }

    public static List<Creature> getNonCalabashSoldier() {
        return nonCalabashSoldier;
    }

    public void setCalabashLeader(CalabashBrother calabashLeader) {
        int pos = calabashSoldier.indexOf(calabashLeader);
        calabashSoldier.remove(pos);
        calabashSoldier.add(pos, Model.calabashLeader);
        Model.calabashLeader = calabashLeader;
        calabashCamp = new CalabashCamp(calabashLeader, calabashSoldier, calabashSupporter, calabashFormation);
    }

    static Creature getNonCalabashLeader() {
        return nonCalabashLeader;
    }

    public static List<Creature> getCreatures() {
        return creatures;
    }

    public void clear() {
        for (Creature creature : creatures) {
            creature.removeCell();
        }
    }

    public void addCamp() {

        calabashFormation.instantiate(Formation.Direction.LEFT, new Coordinate(10, 10),
                new Coordinate(20, 10));
        calabashCamp = new CalabashCamp(calabashLeader, calabashSoldier, calabashSupporter, calabashFormation);

        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if (coordinates[i][j].equals(calabashCamp.getFormation().getLeader())) {
                    calabashCamp.getLeader().setCell(coordinates[i][j]);
                    break;
                }
                for (int k = 0; k < calabashSoldier.size(); ++k) {
                    if (coordinates[i][j].equals(calabashCamp.getFormation().getSoldier()[k])) {
                        calabashCamp.getSoldier().get(k).setCell(coordinates[i][j]);
                        break;
                    }
                }
                if (coordinates[i][j].equals(calabashCamp.getFormation().getSupporter())) {
                    calabashCamp.getSupporter().setCell(coordinates[i][j]);
                    break;
                }

                if (coordinates[i][j].equals(nonCalabashCamp.getFormation().getLeader())) {
                    nonCalabashCamp.getLeader().setCell(coordinates[i][j]);
                    break;
                }
                for (int k = 0; k < nonCalabashSoldier.size(); ++k) {
                    if (coordinates[i][j].equals(nonCalabashCamp.getFormation().getSoldier()[k])) {
                        nonCalabashCamp.getSoldier().get(k).setCell(coordinates[i][j]);
                        break;
                    }
                }
                if (coordinates[i][j].equals(nonCalabashCamp.getFormation().getSupporter())) {
                    nonCalabashCamp.getSupporter().setCell(coordinates[i][j]);
                    break;
                }

            }
        }

    }

    public void setNonCalabashCamp(Formation formation) {
        nonCalabashFormation = formation;
        nonCalabashFormation.instantiate(Formation.Direction.RIGHT, new Coordinate(11, 10),
                new Coordinate(21, 10));
        nonCalabashCamp = new NonCalabashCamp(nonCalabashLeader, nonCalabashSoldier, nonCalabashSupporter,
                nonCalabashFormation);
    }

    public static boolean end() {
        if (calabashLeader.getCell() != null)
            return calabashLeader.getCell().getCoordinate().getCoordinateX() >= 20;
        else
            return false;
    }

    private void replay() {
        RecordReader recordReader = new RecordReader();
        try {
            String line = reader.readLine();
            Property property = recordReader.readProperty(line);
            if (property != null) {
                property.getCell().getCreature().setCell(property.getCell().getCoordinate());
            } else {
                setChanged();
                notifyObservers();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        nonCalabashLeader.moveTowards(calabashLeader);
    }

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                if (isReplay) {
                    replay();
                }else {
                    update();
                }
                if (end()) {
                    try {
                        reader.close();
                        reader = new BufferedReader(new FileReader(new File("record.json")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setChanged();
                    notifyObservers();
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted");
        }
    }
}
