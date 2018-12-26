package model;

public enum Formation {

    WING {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-1, 1)),
                            new Coordinate(leader.add(-2, -2)),
                            new Coordinate(leader.add(-2, 2)),
                            new Coordinate(leader.add(-3, -3)),
                            new Coordinate(leader.add(-3, 3))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(1, 1)),
                            new Coordinate(leader.add(2, -2)),
                            new Coordinate(leader.add(2, 2)),
                            new Coordinate(leader.add(3, -3)),
                            new Coordinate(leader.add(3, 3))};
                    break;
            }
        }
    },

    FLIGHT {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-2, -2)),
                            new Coordinate(leader.add(-3, -3)),
                            new Coordinate(leader.add(-4, -4)),
                            new Coordinate(leader.add(-5, -5)),
                            new Coordinate(leader.add(-6, -6))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(2, -2)),
                            new Coordinate(leader.add(3, -3)),
                            new Coordinate(leader.add(4, -4)),
                            new Coordinate(leader.add(5, -5)),
                            new Coordinate(leader.add(6, -6))};
                    break;
            }
        }
    },

    YOKE {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, 0)),
                            new Coordinate(leader.add(-1, 2)),
                            new Coordinate(leader.add(-1, -2)),
                            new Coordinate(leader.add(-2, 1)),
                            new Coordinate(leader.add(-2, 3)),
                            new Coordinate(leader.add(-2, -1))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, 0)),
                            new Coordinate(leader.add(1, 2)),
                            new Coordinate(leader.add(1, -2)),
                            new Coordinate(leader.add(2, 1)),
                            new Coordinate(leader.add(2, 3)),
                            new Coordinate(leader.add(2, -1))};
                    break;
            }
        }
    },

    LINE {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-1, -2)),
                            new Coordinate(leader.add(-1, -3)),
                            new Coordinate(leader.add(-1, 1)),
                            new Coordinate(leader.add(-1, 2)),
                            new Coordinate(leader.add(-1, 3))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(1, -2)),
                            new Coordinate(leader.add(1, -3)),
                            new Coordinate(leader.add(1, 1)),
                            new Coordinate(leader.add(1, 2)),
                            new Coordinate(leader.add(1, 3))};
                    break;
            }
        }
    },

    SCALE {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-1, 1)),
                            new Coordinate(leader.add(-2, 0)),
                            new Coordinate(leader.add(-2, -2)),
                            new Coordinate(leader.add(-2, 2)),
                            new Coordinate(leader.add(-3, 0))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(1, 1)),
                            new Coordinate(leader.add(2, 0)),
                            new Coordinate(leader.add(2, -2)),
                            new Coordinate(leader.add(2, 2)),
                            new Coordinate(leader.add(3, 0))};
                    break;
            }
        }
    },

    SQUARE {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-1, 1)),
                            new Coordinate(leader.add(-2, -2)),
                            new Coordinate(leader.add(-2, 2)),
                            new Coordinate(leader.add(-3, -1)),
                            new Coordinate(leader.add(-3, 1))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(1, 1)),
                            new Coordinate(leader.add(2, -2)),
                            new Coordinate(leader.add(2, 2)),
                            new Coordinate(leader.add(3, -1)),
                            new Coordinate(leader.add(3, 1))};
                    break;
            }
        }
    },

    CRESCENT {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(1, 1)),
                            new Coordinate(leader.add(2, -2)),
                            new Coordinate(leader.add(2, 2)),
                            new Coordinate(leader.add(3, -3)),
                            new Coordinate(leader.add(3, 3))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-1, 1)),
                            new Coordinate(leader.add(-2, -2)),
                            new Coordinate(leader.add(-2, 2)),
                            new Coordinate(leader.add(-3, -3)),
                            new Coordinate(leader.add(-3, 3))};
                    break;
            }
        }
    },

    ARROW {
        void setSoldier() {
            switch (direction) {
                case LEFT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(-1, -1)),
                            new Coordinate(leader.add(-1, 0)),
                            new Coordinate(leader.add(-1, 1)),
                            new Coordinate(leader.add(-2, 0)),
                            new Coordinate(leader.add(-3, 0)),
                            new Coordinate(leader.add(-4, 0))};
                    break;

                case RIGHT:
                    this.soldier = new Coordinate[]{
                            new Coordinate(leader.add(1, -1)),
                            new Coordinate(leader.add(1, 0)),
                            new Coordinate(leader.add(1, 1)),
                            new Coordinate(leader.add(2, 0)),
                            new Coordinate(leader.add(3, 0)),
                            new Coordinate(leader.add(4, 0))};
                    break;
            }
        }
    };

    Direction direction;
    Coordinate leader;
    Coordinate[] soldier;
    Coordinate supporter;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (this.leader != null)
            setSoldier();
    }

    public Coordinate getLeader() {
        return leader;
    }

    void setLeader(Coordinate leader) {
        this.leader = leader;
        if (this.direction != null)
            setSoldier();
    }

    public Coordinate[] getSoldier() {
        return soldier;
    }

    public Coordinate getSupporter() {
        return supporter;
    }

    public void setSupporter(Coordinate supporter) {
        this.supporter = supporter;
    }

    abstract void setSoldier();

    void instantiate(Direction direction, Coordinate leader, Coordinate supporter) {
        setDirection(direction);
        setLeader(leader);
        setSupporter(supporter);
        setSoldier();
    }

    public enum Direction {LEFT, RIGHT}
}

