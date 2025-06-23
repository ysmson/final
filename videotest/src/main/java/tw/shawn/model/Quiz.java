package tw.shawn.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int id;

    @Column(name = "video_id")
    private String videoId;

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Column(name = "correct_index")
    private int correctIndex;

    // ✅ 新增正確答案選項文字與 ABC 標示
    @Transient
    private String correctAnswer; // 如：「甲是對的」
    
    @Transient
    private String correctOption; // 如：「A」

    private String explanation;
    private String source;

    public Quiz() {}

    // --- Getter / Setter ---

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

    // ✅ 支援 setOptions 方法
    public void setOptions(List<String> options) {
        if (options != null && options.size() >= 4) {
            this.option1 = options.get(0);
            this.option2 = options.get(1);
            this.option3 = options.get(2);
            this.option4 = options.get(3);
        }
    }

    // ✅ 取得所有選項作為 List
    public List<String> getOptions() {
        return List.of(option1, option2, option3, option4);
    }
    @Column(name = "difficulty")
    private String difficulty;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
