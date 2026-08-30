package Model;

import java.util.List;

public class ElectionModel {
    public String id;
    public String title;
    public ElectionStatusModel status;
    public List<Integer> rankingPoints;
    public int PatternThreshold;

    public ElectionModel(String id, String title, String status, List<Integer> points, int threshold) {
        this.id = id;
        this.title = title;
        this.status = ElectionStatusModel.valueOf(status);
        this.rankingPoints = points;
        this.PatternThreshold = threshold;
    }
}
