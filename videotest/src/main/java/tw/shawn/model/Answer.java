package tw.shawn.model;

import java.util.Date;

/**
 * Answer：使用者作答紀錄的資料模型（非 JPA）
 * 對應資料表欄位，包括 quizId、使用者選擇的選項、是否答對、來源、解析等。
 */
public class Answer {

    // ========== 基本欄位 ==========
    private Integer id;                 // 作答記錄主鍵 ID
    private Integer userId;            // 使用者 ID（外鍵）
    private Integer quizId;            // 對應的 quiz 題目 ID（外鍵）
    private Integer selectedOption;    // 使用者選擇的選項索引（0~3）
    private Boolean isCorrect;         // 是否答對
    private String source;             // 題目來源（如 gpt/local）
    private String question;           // 題目文字
    private String option1;            // 選項 A
    private String option2;            // 選項 B
    private String option3;            // 選項 C
    private String option4;            // 選項 D
    private String videoId;            // 所屬影片 ID
    private String answerText;         // 正確答案的文字
    private Integer answerIndex;       // 正確答案的索引（0~3）
    private String explanation;        // 該題解析說明
    private Long attemptId;            // 作答批次 ID（一次測驗共用）
    private Date createdAt;            // 資料建立時間
    private Date answeredAt;           // 使用者作答時間
    private Date submittedAt;          // 提交整份測驗的時間

    // 難度（如 easy / medium / hard）
    private String difficulty;

    public Answer() {
        // 無參數建構子（必要）
    }

    // ========== Getter / Setter 區 ==========

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

    /**
     * 🔁 兼容方法：getAnswer() 實際等同於 getAnswerText()
     * 方便其他程式碼中用 answer 來取代 answerText
     */
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

    // ========== 額外輔助方法區 ==========

    /**
     * 根據 index 取得對應選項文字（0=A、1=B、2=C、3=D）
     *
     * @param index 選項索引值
     * @return 該選項的文字，若無對應則回傳 null
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

    // ========== 難度相關 ==========
    
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * 將內部難度英文代碼轉換為中文標籤
     *
     * @return 中文難度名稱（簡單 / 中等 / 困難）
     */
    public String getDifficultyLabel() {
        if (difficulty == null) return "未分類";
        switch (difficulty.toLowerCase()) {
            case "easy": return "簡單";
            case "medium": return "中等";
            case "hard": return "困難";
            default: return difficulty;
        }
    }

    /**
     * 根據難度回傳對應顏色（用於前端顯示）
     *
     * @return 難度對應的 HEX 顏色碼
     */
    public String getDifficultyColor() {
        if (difficulty == null) return "#999";
        switch (difficulty.toLowerCase()) {
            case "easy": return "#4caf50";   // 綠色
            case "medium": return "#ff9800"; // 橙色
            case "hard": return "#f44336";   // 紅色
            default: return "#999";          // 灰色（未知）
        }
    }

}
