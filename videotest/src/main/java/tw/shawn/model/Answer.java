package tw.shawn.model;

import java.util.Date;

/**
 * Answer：使用者作答紀錄的資料模型（非 JPA）
 */
public class Answer {
    private Integer id;
    private Integer userId;
    private Integer quizId;
    private Integer selectedOption;
    private Boolean isCorrect;
    private String source;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String videoId;
    private String answerText;       // 真實欄位名稱
    private Integer answerIndex;
    private String explanation;
    private Long attemptId;
    private Date createdAt;
    private Date answeredAt;
    private Date submittedAt;

    public Answer() {
        // 無參數建構子
    }

    // Getter / Setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public Integer getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Integer selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    // 新增 getAnswer()、setAnswer() 方便兼容
    public String getAnswer() {
        return answerText;
    }

    public void setAnswer(String answer) {
        this.answerText = answer;
    }

    public Integer getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(Integer answerIndex) {
        this.answerIndex = answerIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Date answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    /**
     * 根據選項索引取得對應的選項文字
     * @param index 0-based (0~3)
     * @return 選項文字或 null
     */
    public String getOptionTextByIndex(Integer index) {
        if (index == null) return null;
        switch (index) {
            case 0: return option1;
            case 1: return option2;
            case 2: return option3;
            case 3: return option4;
            default: return null;
        }
    }
    private String difficulty;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getDifficultyLabel() {
        if (difficulty == null) return "未分類";
        switch (difficulty.toLowerCase()) {
            case "easy": return "簡單";
            case "medium": return "中等";
            case "hard": return "困難";
            default: return difficulty;
        }
    }
    public String getDifficultyColor() {
        if (difficulty == null) return "#999";
        switch (difficulty.toLowerCase()) {
            case "easy": return "#4caf50";   // 綠
            case "medium": return "#ff9800"; // 橙
            case "hard": return "#f44336";   // 紅
            default: return "#999";          // 灰
        }
    }

}
