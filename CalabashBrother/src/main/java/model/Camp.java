package model;

import java.util.Collections;
import java.util.List;

class CampProperty<T extends Creature> {

    private T leader;
    private List<T> soldier;
    private NonCalabashBrother supporter;
    private Formation formation;

    CampProperty(T leader, List<T> soldier, NonCalabashBrother supporter, Formation formation) {
        this.leader = leader;
        this.soldier = soldier;
        this.supporter = supporter;
        this.formation = formation;
    }

    T getLeader() {
        return leader;
    }

    void setLeader(T leader) {
        this.leader = leader;
    }

    List<T> getSoldier() {
        return soldier;
    }

    void setSoldier(List<T> soldier) {
        this.soldier = soldier;
    }

    NonCalabashBrother getSupporter() {
        return supporter;
    }

    void setSupporter(NonCalabashBrother supporter) {
        this.supporter = supporter;
    }

    Formation getFormation() {
        return formation;
    }

    void setFormation(Formation formation) {
        this.formation = formation;
    }
}

abstract class Camp<T extends Creature> {

    CampProperty<T> property;

    Camp(T leader, List<T> soldier, NonCalabashBrother supporter, Formation formation) {
        this.property = new CampProperty<>(leader, soldier, supporter, formation);
    }

    T getLeader() {
        return property.getLeader();
    }

    void setLeader(T leader) {
        property.setLeader(leader);
    }

    List<T> getSoldier() {
        return property.getSoldier();
    }

    void setSoldier(List<T> soldier) {
        property.setSoldier(soldier);
    }

    NonCalabashBrother getSupporter() {
        return property.getSupporter();
    }

    void setSupporter(NonCalabashBrother supporter) {
        property.setSupporter(supporter);
    }

    Formation getFormation() {
        return property.getFormation();
    }

    void setFormation(Formation formation) {
        property.setFormation(formation);
    }
}

class CalabashCamp extends Camp<CalabashBrother> {

    CalabashCamp(CalabashBrother leader, List<CalabashBrother> soldier, NonCalabashBrother supporter, Formation formation) {
        super(leader, soldier, supporter, formation);
    }

    void shuffle() {
        Collections.shuffle(property.getSoldier());
    }

    void numberOffByRank() {
        System.out.println(property.getLeader().getRank().toString());
        for (CalabashBrother brother : property.getSoldier()) {
            System.out.println(brother.getRank().toString());
        }
    }

    void numberOffByColor() {
        System.out.println(property.getLeader().getColor().toString());
        for (CalabashBrother brother : property.getSoldier()) {
            System.out.println(brother.getColor().toString());
        }
    }

    void sortByRank() {
        for (int i = 0; i < property.getSoldier().size() - 1; ++i)
            for (int j = 0; j < property.getSoldier().size() - 1 - i; ++j)
                if (property.getSoldier().get(j).getRank().ordinal() > property.getSoldier().get(j + 1).getRank().ordinal())
                    swapCalabash(j, j + 1);
    }

    void sortByColor() {
        for (int i = 1; i < property.getSoldier().size(); ++i) {
            int low = 0, high = i - 1, mid;
            while (low <= high) {
                mid = (low + high) / 2;
                if (property.getSoldier().get(mid).getColor().ordinal() >= property.getSoldier().get(i).getColor().ordinal())
                    high = mid - 1;
                else
                    low = mid + 1;
            }
            for (int j = i; j > low; --j)
                swapCalabash(j - 1, j);
        }
    }

    private void swapCalabash(int index1, int index2) {
        CalabashBrother tmp = property.getSoldier().get(index1);
        property.getSoldier().set(index1, property.getSoldier().get(index2));
        property.getSoldier().set(index2, tmp);
        System.out.println(property.getSoldier().get(index2).getRank().toString() + ": " + index1 + "->" + index2);
        System.out.println(property.getSoldier().get(index1).getRank().toString() + ": " + index2 + "->" + index1);
    }
}

class NonCalabashCamp extends Camp<Creature> {
    NonCalabashCamp(Creature leader, List<Creature> soldier, NonCalabashBrother supporter, Formation formation) {
        super(leader, soldier, supporter, formation);
    }
}
