package org.apache.activemq.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.management.j2ee.statistics.Statistic;
import javax.management.j2ee.statistics.Stats;

public class StatsImpl extends StatisticImpl implements Stats, Resettable {
    private Set<StatisticImpl> set;

    public StatsImpl() {
        this(new CopyOnWriteArraySet());
    }

    public StatsImpl(Set<StatisticImpl> set) {
        super("stats", "many", "Used only as container, not Statistic");
        this.set = set;
    }

    public void reset() {
        for (Statistic stat : getStatistics()) {
            if (stat instanceof Resettable) {
                ((Resettable) stat).reset();
            }
        }
    }

    public Statistic getStatistic(String name) {
        for (StatisticImpl stat : this.set) {
            if (stat.getName() != null && stat.getName().equals(name)) {
                return stat;
            }
        }
        return null;
    }

    public String[] getStatisticNames() {
        List<String> names = new ArrayList();
        for (StatisticImpl stat : this.set) {
            names.add(stat.getName());
        }
        String[] answer = new String[names.size()];
        names.toArray(answer);
        return answer;
    }

    public Statistic[] getStatistics() {
        Statistic[] answer = new Statistic[this.set.size()];
        this.set.toArray(answer);
        return answer;
    }

    protected void addStatistic(String name, StatisticImpl statistic) {
        this.set.add(statistic);
    }
}
