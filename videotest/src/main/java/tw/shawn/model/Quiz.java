package tw.shawn.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * ✅ Quiz：選擇題的資料模型，對應資料表 quiz
 *
 * 代表單一題目（與某部影片 videoId 關聯）
 * 包含題目、4 個選項、正確答案索引、解析、來源等資訊
 */
@Entity
@Table(name = "quiz")
public class Quiz {

    // ✅ 主鍵欄位，自動編號（對應 quiz_id）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int id;

    // ✅ 關聯的影片 ID（FK 對應 video 表的 video_id）
    @Column(name = "video_id")
    private String videoId;

    // ✅ 題目內容
    private String question;

    // ✅ 四個選項（必備）
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    // ✅ 正確答案的索引值（0~3）
    @Column(name = "correct_index")
    private int correctIndex;

    // ✅ 非資料庫欄位，用來傳送正確答案文字（如：「甲是對的」）
    @Transient
    private String correctAnswer;

    // ✅ 非資料庫欄位，用來傳送正確選項代號（如：「A」）
    @Transient
    private String correctOption;

    // ✅ 該題的解析說明（例如為何選這一題）
    private String explanation;

    // ✅ 題目來源（local / GPT）
    private String source;

    // ✅ 題目難易度（easy / medium / hard）
    @Column(name = "difficulty")
    private String difficulty;

    // ✅ 無參數建構子（JPA 必備）
    public Quiz() {}

    // --- 以下為標準 Getter / Setter 區域 ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // ✅ 提供以 List 傳入選項的 setter（通常用於自動產題時）
    public void setOptions(List<String> options) {
        if (options != null && options.size() >= 4) {
            this.option1 = options.get(0);
            this.option2 = options.get(1);
            this.option3 = options.get(2);
            this.option4 = options.get(3);
        }
    }

    // ✅ 將四個選項包成 List 回傳（用於迴圈顯示等用途）
    public List<String> getOptions() {
        return List.of(option1, option2, option3, option4);
    }
}
