package tw.shawn.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AttemptGroup：表示一次測驗紀錄（同一組 attemptId 對應的多題作答）
 */
public class AttemptGroup {

    private long attemptId; // 測驗批次編號
    private LocalDateTime submittedAt; // 提交時間
    private String source; // 題目來源：GPT 或 local
    private int total;     // 題目總數
    private int correct;   // 答對題數
    private double accuracy; // 正確率

    private List<AnswerRecord> questions; // 本次所有題目與作答詳解

    // --- Getter / Setter ---

    public long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(long attemptId) {
        this.attemptId = attemptId;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public List<AnswerRecord> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AnswerRecord> questions) {
        this.questions = questions;
    }
}
