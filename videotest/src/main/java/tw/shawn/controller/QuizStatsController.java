package tw.shawn.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.QuizResultDAO;

/**
 * QuizStatsController：
 * 回傳指定使用者在指定影片與來源的答題總數與正確數
 * GET /api/quizStats?videoId=xxx&userId=1&source=gpt
 */
@RestController
@RequestMapping("/api")
public class QuizStatsController {

    @Autowired
    private QuizResultDAO quizResultDAO;

    @GetMapping("/quizStats")
    public String getQuizStats(
            @RequestParam String videoId,
            @RequestParam int userId,
            @RequestParam(required = false) String source) {

        try {
            int total = quizResultDAO.sumTotalQuestions(userId, videoId, source);
            int correct = quizResultDAO.sumCorrectAnswers(userId, videoId, source);

            JsonObject json = new JsonObject();
            json.addProperty("total", total);
            json.addProperty("correct", correct);
            json.addProperty("accuracy", total > 0
                    ? String.format("%.2f", (double) correct / total * 100)
                    : "N/A");

            return json.toString();

        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "統計錯誤：" + e.getMessage());
            return error.toString();
        }
    }
}
