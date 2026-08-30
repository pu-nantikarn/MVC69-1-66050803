package Model;
import java.util.*;

public class BallotModel {
    public String id;
    public String voterId;
    public List<String> ranking;
    public BallotStatusModel status;

    public BallotModel(String id, String voterId, List<String> ranking) {
        this.id = id;
        this.voterId = voterId;
        this.ranking = ranking;
        this.status = BallotStatusModel.PENDING;
    }
    
    public BallotStatusModel getStatus() {
        return this.status;
    }

    public String getPatternKey() {
        return String.join("-", ranking);
    }

    public void changeStatus(BallotStatusModel newStatus, OfficerModel officer) throws IllegalStateException, IllegalArgumentException {
        if (officer == null) {
            throw new IllegalArgumentException("Error: Officer identity is required.");
        }

        if (this.status == BallotStatusModel.PENDING && (newStatus == BallotStatusModel.APPROVED || newStatus == BallotStatusModel.REJECTED)) {
            this.status = newStatus;
        } 
        else {
            throw new IllegalStateException(
                String.format("Status transition from %s to %s is not allowed.", 
                this.status, newStatus)
            );
        }
    }
}
