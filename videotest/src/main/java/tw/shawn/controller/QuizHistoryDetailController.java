package tw.shawn.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.AnswerDAO;
import tw.shawn.dao.QuizResultDAO;
import tw.shawn.model.Answer;
import tw.shawn.model.QuizResult;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


/**
 * QuizHistoryDetailController：
 * 回傳某位使用者的所有測驗紀錄與答題詳解
 * 路徑：GET /api/quizHistoryDetail?userId=123
 */
@RestController
@RequestMapping("/api")
public class QuizHistoryDetailController {

    @Autowired
    private QuizResultDAO quizResultDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @GetMapping("/quizHistoryDetail")
    public String getQuizHistoryGrouped(@RequestParam("userId") int userId) {
        JsonArray videoArray = new JsonArray();

        try {
            List<QuizResult> results = quizResultDAO.getResultsByUser(userId);
            Map<String, JsonObject> videoMap = new LinkedHashMap<>();

            for (QuizResult result : results) {
                String videoId = result.getVideoId();
                String videoTitle = answerDAO.getVideoTitle(videoId);

                // 若尚未加入該影片，先建立外層物件
                if (!videoMap.containsKey(videoId)) {
                    JsonObject videoObj = new JsonObject();
                    videoObj.addProperty("videoId", videoId);
                    videoObj.addProperty("videoTitle", videoTitle);
                    videoObj.add("attempts", new JsonArray());
                    videoMap.put(videoId, videoObj);
                }

                // 查詢該次 attempt 的所有題目詳解
                List<Answer> answers = answerDAO.getAnswersByUserAndAttemptId(
                        userId, videoId, result.getAttemptId());

                JsonArray answerArr = new JsonArray();
                for (int i = 0; i < answers.size(); i++) {
                    Answer a = answers.get(i);
                    JsonObject ans = new JsonObject();
                    ans.addProperty("question", a.getQuestion());
                    ans.addProperty("selectedAnswer", a.getOptionTextByIndex(a.getSelectedOption()));
                    ans.addProperty("correctAnswer", a.getAnswer());
                    ans.addProperty("isCorrect", a.isCorrect());
                    ans.addProperty("explanation", a.getExplanation());
                    ans.addProperty("option1", a.getOption1());
                    ans.addProperty("option2", a.getOption2());
                    ans.addProperty("option3", a.getOption3());
                    ans.addProperty("option4", a.getOption4());
                    ans.addProperty("correctIndex", a.getAnswerIndex());
                    ans.addProperty("selectedIndex", a.getSelectedOption());
                    ans.addProperty("difficulty", a.getDifficulty());
                    ans.addProperty("originalIndex", i); // ✅ 題目原始順序
                    answerArr.add(ans);
                }

                JsonObject attemptObj = new JsonObject();
                attemptObj.addProperty("attemptId", result.getAttemptId());
                attemptObj.addProperty("submittedAt", result.getSubmittedAt().toString());
                attemptObj.addProperty("total", result.getTotalQuestions());
                attemptObj.addProperty("correct", result.getCorrectAnswers());
                attemptObj.add("answers", answerArr);

                // 放入影片群組中
                JsonObject videoObj = videoMap.get(videoId);
                videoObj.getAsJsonArray("attempts").add(attemptObj);
            }

            // 收集所有影片資料
            videoMap.values().forEach(videoArray::add);

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "處理錯誤：" + e.getMessage());
            return error.toString();
        }

        return videoArray.toString();
    }
}