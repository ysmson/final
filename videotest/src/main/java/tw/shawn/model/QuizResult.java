package tw.shawn.model;

import java.time.LocalDateTime;

/**
 * ✅ QuizResult：代表一次測驗結果的簡化資料模型
 * 不對應資料表，主要用於記錄提交後的測驗結果資訊（答對幾題、來源、難度、時間等）
 */
public class QuizResult {

    private int userId;               // ✅ 使用者 ID
    private String videoId;           // ✅ 對應的影片 ID
    private int correctAnswers;       // ✅ 此次測驗答對題數
    private int totalQuestions;       // ✅ 此次測驗的總題數
    private LocalDateTime submittedAt; // ✅ 測驗提交時間（使用 Java 8 時間格式）
    private String source;            // ✅ 題目來源（如 GPT、自動產生、本地題庫等）
    private long attemptId;           // ✅ 測驗批次編號（用來分組這一份作答紀錄）
    private String difficulty;        // ✅ 題目的難易度（如 easy、medium、hard）

    // --- 以下為標準 Getter / Setter 方法 ---

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
