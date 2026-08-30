package Controller;

import Model.*;
import java.util.*;

public class VotingController {
    private DataCenter db;
    private int nextBallotId = 4; 

    public VotingController(DataCenter db) {
        this.db = db;
    }

    public List<CandidateModel> getCandidates() {
        return new ArrayList<>(db.candidates.values());
    }

    //รับข้อมูลที่โหวตและบันทึก
    public void submitVote(String voterId, List<String> rankedCandidateIds) throws IllegalArgumentException, IllegalStateException {
        if (db.election.status != ElectionStatusModel.OPEN) {
            throw new IllegalStateException("Cannot vote: Election is not in OPEN status");
        }

        VoterModel voter = db.voters.get(voterId);
        if (voter == null) {
            throw new IllegalArgumentException("Voter data not found");
        }
        if (!voter.active) {
            throw new IllegalArgumentException("Voter is suspended (Inactive)");
        }
        if (voter.hasVoted) {
            throw new IllegalArgumentException("This voter has already voted");
        }

        if (rankedCandidateIds == null || rankedCandidateIds.size() != 3) {
            throw new IllegalArgumentException("Vote rejected: You must rank exactly 3 candidates");
        }
        for (String cId : rankedCandidateIds) {
            if (cId == null || cId.trim().isEmpty()) {
                throw new IllegalArgumentException("Vote rejected: You must fill in all 3 candidate slots");
            }
            if (!db.candidates.containsKey(cId)) {
                throw new IllegalArgumentException("Vote rejected: Candidate ID '" + cId + "' does not exist");
            }
        }

        Set<String> uniqueCheck = new HashSet<>(rankedCandidateIds);
        if (uniqueCheck.size() != 3) {
            throw new IllegalArgumentException("Vote rejected: Duplicate candidates in a single ballot are not allowed");
        }

        String newBallotId = "B0" + (nextBallotId++);
        BallotModel newBallot = new BallotModel(newBallotId, voterId, rankedCandidateIds);
        db.ballots.add(newBallot);
        
        voter.hasVoted = true;
    }
}