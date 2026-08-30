package Model;

import java.util.*;

public class DataCenter {
    public ElectionModel election;
    public Map<String, CandidateModel> candidates = new LinkedHashMap<>();
    public Map<String, VoterModel> voters = new LinkedHashMap<>();
    public Map<String, OfficerModel> officers = new LinkedHashMap<>(); 
    public List<BallotModel> ballots = new ArrayList<>();

    public Map<String, List<BallotModel>> patternGroups = new HashMap<>(); //เก็บ Ballot ที่รูปแบบเหมือนกัน

    public void initSeedData() {
        //ข้อมูลการเลือกตั้ง
        election = new ElectionModel("E01", "การเลือกตั้งประธานชมรมโปร่งใสจริง ๆ นะ", "OPEN", Arrays.asList(3, 2, 1), 3);

        //ข้อมูลเจ้าหน้าที่
        officers.put("O01", new OfficerModel("O01", "กรรมการผู้ไม่เปิดโพย"));

        //ข้อมูลผู้สมัคร
        candidates.put("C01", new CandidateModel("C01", "Null Pointer"));
        candidates.put("C02", new CandidateModel("C02", "Merge Conflict"));
        candidates.put("C03", new CandidateModel("C03", "Works on My Machine"));
        candidates.put("C04", new CandidateModel("C04", "404 Policy Not Found"));
        candidates.put("C05", new CandidateModel("C05", "Ctrl+Z Nation"));

        //ข้อมูลผู้มีสิทธิ์เลือกตั้ง
        voters.put("V01", new VoterModel("V01", "โพยอยู่ไหน", true));
        voters.put("V02", new VoterModel("V02", "บังเอิญเหมือนกัน", true));
        voters.put("V03", new VoterModel("V03", "เลือกเองจริง ๆ", true));
        voters.put("V04", new VoterModel("V04", "ใจตรงกันเฉย ๆ", true));
        voters.put("V05", new VoterModel("V05", "ขอดูอีกที", true));
        voters.put("V06", new VoterModel("V06", "บัตรสุดท้าย", true));
        voters.put("V07", new VoterModel("V07", "ไม่ได้อยู่กลุ่มไลน์", true));

        //ข้อมูลบัตรลงคะแนนตั้งต้น
        ballots.add(new BallotModel("B01", "V01", Arrays.asList("C01", "C02", "C03")));
        ballots.add(new BallotModel("B02", "V02", Arrays.asList("C01", "C02", "C03")));
        ballots.add(new BallotModel("B03", "V03", Arrays.asList("C02", "C03", "C04")));
        
        //อัปเดตสถานะการโหวตของผู้เลือก
        voters.get("V01").hasVoted = true;
        voters.get("V02").hasVoted = true;
        voters.get("V03").hasVoted = true;
    }
}
