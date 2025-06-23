package tw.shawn.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.QuizResultDAO;
import tw.shawn.model.QuizResultSummary;

import java.util.List;

/**
 * QuizHistoryController：查詢使用者歷史測驗紀錄摘要清單
 * 路徑：GET /api/quizHistory?userId=xxx
 * 回傳內容：影片、來源、總題數、答對數等彙總資訊
 */
@RestController
@RequestMapping("/api")
public class QuizHistoryController {

    @Autowired
    private QuizResultDAO quizResultDAO;

    @GetMapping("/quizHistory")
    public String getQuizHistory(@RequestParam("userId") int userId) {
        JsonArray jsonArr = new JsonArray();

        try {
            List<QuizResultSummary> resultList = quizResultDAO.getQuizSummaryByUser(userId);

            for (QuizResultSummary result : resultList) {
                JsonObject obj = new JsonObject();
                obj.addProperty("videoId", result.getVideoId());
                obj.addProperty("videoTitle", result.getVideoTitle()); // 🔍建議補上影片標題
                obj.addProperty("source", result.getSource());
                obj.addProperty("totalQuizCount", result.getTotalQuizCount());
                obj.addProperty("total", result.getTotal());
                obj.addProperty("correct", result.getCorrect());
                jsonArr.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "伺服器錯誤：" + e.getMessage());
            return error.toString();
        }

        return jsonArr.toString();
    }
}
