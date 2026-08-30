package Controller;

import Model.*;
import java.util.*;

public class OfficerController {
    private DataCenter db;

    public OfficerController(DataCenter db) {
        this.db = db;
    }

    //ปิดโหวต
    public void closeElection(String officerId) throws IllegalStateException, IllegalArgumentException {
        OfficerModel officer = validateOfficer(officerId);

        if (db.election.status != ElectionStatusModel.OPEN) {
            throw new IllegalStateException("Error: Election is not in OPEN status[cite: 33]");
        }

        db.election.status = ElectionStatusModel.CLOSED;

        Map<String, List<BallotModel>> tempGroups = new HashMap<>();
        for (BallotModel b : db.ballots) {
            tempGroups.computeIfAbsent(b.getPatternKey(), k -> new ArrayList<>()).add(b);
        }

        //หา pattern ที่ซ้ำ
        for (Map.Entry<String, List<BallotModel>> entry : tempGroups.entrySet()) {
            List<BallotModel> group = entry.getValue();
            
            if (group.size() >= db.election.PatternThreshold) {
                db.patternGroups.put(entry.getKey(), group);
            } else {
                group.forEach(b -> b.changeStatus(BallotStatusModel.APPROVED, officer));
            }
        }
        
        autoSummarizeIfNeeded();
    }

    // save บัตรโหวตที่ pattern เหมือนกัน
    public void reviewPatternGroup(String officerId, String patternKey, boolean isApproved) {
        OfficerModel officer = validateOfficer(officerId);

        if (db.election.status != ElectionStatusModel.CLOSED) {
            throw new IllegalStateException("Error: System is not in CLOSED status[cite: 33]");
        }

        List<BallotModel> group = db.patternGroups.get(patternKey);
        if (group == null || group.isEmpty()) {
            throw new IllegalArgumentException("Ballot pattern group not found in the system");
        }

        if (group.get(0).getStatus() != BallotStatusModel.PENDING) {
            throw new IllegalStateException("Error: This pattern group has already been reviewed[cite: 33]");
        }

        BallotStatusModel finalStatus = isApproved ? BallotStatusModel.APPROVED : BallotStatusModel.REJECTED;
        
        for (BallotModel b : group) {
            b.changeStatus(finalStatus, officer);
        }

        autoSummarizeIfNeeded();
    }

    //ตรวจสอบบัตรโหวตหมดทุกอันให้เปลี่ยนสถานะเป็นสรุปผลทันที
    private void autoSummarizeIfNeeded() {
        boolean hasPending = db.ballots.stream().anyMatch(b -> b.getStatus() == BallotStatusModel.PENDING);
        if (!hasPending && db.election.status == ElectionStatusModel.CLOSED) {
            db.election.status = ElectionStatusModel.SUMMARIZED;
        }
    }

    //ตรวจสอบว่าคนเปลี่ยนสถานะใช่พนักงานมั้ย
    private OfficerModel validateOfficer(String officerId) {
        OfficerModel officer = db.officers.get(officerId);
        if (officer == null) {
            throw new IllegalArgumentException("Officer data not found in the system[cite: 33]");
        }
        return officer;
    }

    public List<BallotModel> getAllBallots() {
        return db.ballots;
    }

    public List<String> getPendingPatterns() {
        List<String> pendingList = new ArrayList<>();
        for (Map.Entry<String, List<BallotModel>> entry : db.patternGroups.entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getValue().get(0).getStatus() == BallotStatusModel.PENDING) {
                pendingList.add(entry.getKey());
            }
        }
        return pendingList;
    }

    public String getElectionStatus() {
        return db.election.status.name();
    }
}