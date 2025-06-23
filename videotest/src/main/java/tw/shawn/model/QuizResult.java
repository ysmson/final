package tw.shawn.model;

import java.time.LocalDateTime;

public class QuizResult {

    private int userId;
    private String videoId;
    private int correctAnswers;
    private int totalQuestions;
    private LocalDateTime submittedAt;
    private String source;
    private long attemptId;
    private String difficulty; // ⬅️ 新增欄位

    // Getter / Setter
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
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

    public long getAttemptId() {
        return attemptId;
    }
    public void setAttemptId(long attemptId) {
        this.attemptId = attemptId;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
