package tw.shawn.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.QuizResultDAO;
import tw.shawn.model.QuizResultSummary;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizSummaryController {

    @Autowired
    private QuizResultDAO quizResultDAO;

    /**
     * 查詢指定使用者對各影片的整體測驗統計資料
     * @param userId 使用者 ID
     * @return JSON 陣列，每筆包含影片標題、videoId、來源、總題數、答對數等
     */
    @GetMapping("/quizSummary")
    public String getQuizSummary(@RequestParam int userId) {
        try {
            List<QuizResultSummary> list = quizResultDAO.getQuizSummaryByUser(userId);

            JsonArray arr = new JsonArray();
            for (QuizResultSummary q : list) {
                JsonObject obj = new JsonObject();
                obj.addProperty("title", q.getVideoTitle());
                obj.addProperty("videoId", q.getVideoId());
                obj.addProperty("source", q.getSource());
                obj.addProperty("totalQuizCount", q.getTotalQuizCount());
                obj.addProperty("totalQuestions", q.getTotal());
                obj.addProperty("correctAnswers", q.getCorrect());
                arr.add(obj);
            }

            return arr.toString();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "統計資料查詢失敗：" + e.getMessage());
            return error.toString();
        }
    }
}
