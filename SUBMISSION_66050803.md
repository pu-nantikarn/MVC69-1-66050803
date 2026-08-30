# SUBMISSION - Exit Exam MVC 1/2569 (อาทิตย์เช้า)

## 1. วิธีเปิดโปรแกรม
- ภาษา/เฟรมเวิร์ก: java  
- Entry point / คำสั่งเปิดโปรแกรม: เปิดไฟล์ main.java แล้วกด run
- หมายเหตุที่จำเป็น (ถ้ามี): ในการใส่ไอดี login และโหวตผู้สมัครให้ใช้ตัวอักษรพิมพ์ใหญ่ เช่น C01 O01 และถ้าหากต้องการกลับไปที่หน้าก่อนหน้าให้กดปิดหน้าต่างนั้นที่ต้องการจะออก

## 2. ตารางเชื่อมโยง Requirements

| Requirement | Model / Domain | Controller / Action | View / Screen |
|---|---|---|---|
| R1 | BallotModel,BallotStatusModel, CandidateModel, DataCenter, ElectionModel, ElectionStatusModel, OfficerModel, VoterModel | DashboardController, OfficerController, VotingController | DashoboardView, MainMenuView, OffiecerView, VotingView |
| R2 |CandidateModel, DataCenter, VoterModel, ElectionModel, BallotModel | VotingController | VotingView |
| R3 | ElectionModel, BallotModel, DataCenter, OfficerModel, ElectionStatusModel | OfficerController | OfficerView |
| R4 | BallotModel, ElectionModel, CandidateModel, DataCenter | OfficerController, DashboardController | OfficerView, DashboardView |
| R5 | ElectionModel, BallotModel, CandidateModel, DAtaCenter | DashboardController, VotingController, OfficerController | VotingView, DsashboardView |

## 3. ผลการทดสอบ

| กรณี | ผ่าน/ไม่ผ่าน | หมายเหตุ (เฉพาะที่จำเป็น) |
|---|---|---|
| T1 |ผ่าน |-|
| T2 |ผ่าน |-|
| T3 |ผ่าน |-|
| T4 |ผ่าน |-|
| T5 |ผ่าน |-|
| T6 |ผ่าน |-|

## 4. ความแตกต่างระหว่างแบบที่ออกกับโปรแกรมจริง (ถ้ามี)
ระบุไม่เกิน 3 ข้อ
1. บางชื่อตัวแปรแปรหรือชื่อฟังก์ชันในไดอะแกรมอาจไม่ตรงกับในโค้ดแต่จะการทำงานจะเหมือนกัน
2. 
3. 

## 5. บันทึกการใช้ Generative AI
หากไม่ได้ใช้ ให้ระบุ **ไม่ได้ใช้ Generative AI**

| เวลาโดยประมาณ | เครื่องมือ | ใช้เพื่ออะไร | นำคำแนะนำไปใช้อย่างไร |
|---|---|---|---|

| | Gemini | ขยายความและสรุป requirement เพิ่มเติม | ใช้สร้าง flow และทำความเข้าใจ requirement ให้มากขึ้น |

| | Gemini |หาวิธีให้แสดง pop-up ตอน detect ข้อผิดพลาดได้|ใช้โครงโค้ดที่ได้มาปรับให้เงื่อนไขที่ตั้งขึ้น pop-up เป็น panel|

| | Gemini | หาวิธีจัดกลุ่ม list ที่มี pattern เดียวกัน | ใช้เป็นโครงร่างโปรแกรมในการหา pattern เดียวกัน |

| | Gemini | ช่วยสอนวิธีการสร้าง Jframe | ใช้ในการสร้าง GUI ใน Java |

| | Gemini |หาวิธีสร้างโครงร่างโปรแกรมในการรับข้อมูลและบันทึกจากที่ป้อน| ใช้ให้มีการบันทึกข้อมูลลตอนกด submit ข้อมูลไป|


