package tw.shawn.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.QuizDAO;
import tw.shawn.model.Quiz;

import java.util.List;

/**
 * LoadQuizController：根據 videoId 與題目來源載入題庫（最多 5 題）
 * 路徑：GET /api/loadQuiz?videoId=xxx&source=gpt
 * 回傳：JSON 陣列，每題含 quizId、question、options[]、answer、source
 */
@RestController
@RequestMapping("/api")
public class LoadQuizController {

    @Autowired
    private QuizDAO quizDAO;

    @GetMapping("/loadQuiz")
    public String loadQuiz(@RequestParam("videoId") String videoId,
                           @RequestParam("source") String source,
                           @RequestParam("difficulty") String difficulty) {
        JsonArray quizArray = new JsonArray();

        // ❗️需確認 quizDAO 有此方法
        List<Quiz> quizList = quizDAO.getQuizzesByVideoSourceDifficulty(videoId, source, difficulty, 5);

        for (Quiz quiz : quizList) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", quiz.getId());
            obj.addProperty("videoId", videoId);
            obj.addProperty("question", quiz.getQuestion());
            JsonArray options = new JsonArray();
            options.add(quiz.getOption1());
            options.add(quiz.getOption2());
            options.add(quiz.getOption3());
            options.add(quiz.getOption4());
            obj.add("options", options);
            obj.addProperty("answer", quiz.getCorrectOption());
            obj.addProperty("source", quiz.getSource());
            quizArray.add(obj);
        }
        return quizArray.toString();
    }
}
