package Controller;

import Model.*;
import java.util.*;

public class DashboardController {
    private DataCenter db;

    public DashboardController(DataCenter db) {
        this.db = db;
    }

    //คำนวณและดึงข้อมูลมาแสดง
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("election_status", db.election.status.name());

        if (db.election.status == ElectionStatusModel.OPEN) {
            //คำนวณข้อมูลตอนเปิดโหวต
            dashboardData.put("total_received_ballots", db.ballots.size());
        } 
        else if (db.election.status == ElectionStatusModel.CLOSED) {
            //คำนวณข้อมูลตอนตรวจสอบ
            calculateScores(); 
            dashboardData.put("pending_patterns", getPendingGroupsSummary());
            dashboardData.put("temporary_scores", getCandidatesScoreSummary());
        } 
        else if (db.election.status == ElectionStatusModel.SUMMARIZED) {
            //คำนวณข้อมูลตอนสรุปผล
            calculateScores(); 
            dashboardData.put("final_scores", getCandidatesScoreSummary());
            dashboardData.put("total_approved_ballots", countBallotsByStatus(BallotStatusModel.APPROVED));
            dashboardData.put("total_rejected_ballots", countBallotsByStatus(BallotStatusModel.REJECTED));
        }

        return dashboardData;
    }

    //คำนวณคะแนน
    private void calculateScores() {
        for (CandidateModel c : db.candidates.values()) {
            c.score = 0;
        }
        for (BallotModel b : db.ballots) {
            if (b.getStatus() == BallotStatusModel.APPROVED) {
                for (int i = 0; i < 3; i++) {
                    String candidateId = b.ranking.get(i);
                    CandidateModel c = db.candidates.get(candidateId);
                    if (c != null) {
                        c.score += db.election.rankingPoints.get(i); 
                    }
                }
            }
        }
    }

    //สรุปบัตรที่ยัง pending
    private Map<String, Integer> getPendingGroupsSummary() {
        Map<String, Integer> summary = new HashMap<>();
        for (Map.Entry<String, List<BallotModel>> entry : db.patternGroups.entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getValue().get(0).getStatus() == BallotStatusModel.PENDING) {
                summary.put(entry.getKey(), entry.getValue().size());
            }
        }
        return summary;
    }

    //เรียงลำดับคะแนน
    private List<String> getCandidatesScoreSummary() {
        List<String> result = new ArrayList<>();
        db.candidates.values().stream()
            .sorted((c1, c2) -> Integer.compare(c2.score, c1.score)) 
            .forEach(c -> result.add(c.name + " (" + c.id + "): " + c.score + " points"));
        return result;
    }

    //นับใบตาม status
    private long countBallotsByStatus(BallotStatusModel targetStatus) {
        return db.ballots.stream().filter(b -> b.getStatus() == targetStatus).count();
    }
}