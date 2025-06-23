package tw.shawn.model;

import java.util.List;

/**
 * AnswerGroupDTO：封裝某部影片的所有測驗紀錄資訊
 */
public class AnswerGroupDTO {
    private String videoId;                  // 影片 ID
    private String videoTitle;               // 影片標題
    private List<AttemptGroup> attempts;     // 該影片的所有作答群組（每次測驗）

    // --- Getter / Setter ---
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public List<AttemptGroup> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<AttemptGroup> attempts) {
        this.attempts = attempts;
    }
}
