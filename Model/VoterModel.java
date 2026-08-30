package Model;

public class VoterModel {
    public String id;
    public String name;
    public boolean active;
    public boolean hasVoted = false;

    public VoterModel(String id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
