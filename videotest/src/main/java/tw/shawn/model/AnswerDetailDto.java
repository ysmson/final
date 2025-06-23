package tw.shawn.model;

/**
 * AnswerDetailDto：用於回傳使用者作答詳解資訊（文字化的選項與答案）
 */
public class AnswerDetailDto {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correct;        // 正確答案文字
    private String selected;       // 使用者選擇的文字
    private Integer correctIndex;  // 正確答案索引
    private Integer selectedIndex; // 使用者選擇索引
    private String source;         // 題目來源（GPT / local）
    private String explanation;    // 題目解析

    // --- Getter / Setter ---
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

    public String getCorrect() {
        return correct;
    }
    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getSelected() {
        return selected;
    }
    public void setSelected(String selected) {
        this.selected = selected;
    }

    public Integer getCorrectIndex() {
        return correctIndex;
    }
    public void setCorrectIndex(Integer correctIndex) {
        this.correctIndex = correctIndex;
    }

    public Integer getSelectedIndex() {
        return selectedIndex;
    }
    public void setSelectedIndex(Integer selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getExplanation() {
        return explanation;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
