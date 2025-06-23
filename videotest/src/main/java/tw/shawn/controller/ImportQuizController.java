package tw.shawn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.QuizDAO;
import tw.shawn.model.Quiz;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImportQuizController {

    @Autowired
    private QuizDAO quizDAO;

    @PostMapping("/importQuiz")
    public ResponseEntity<?> importQuiz(@RequestParam String videoId,
                                        @RequestBody List<Quiz> quizList,
                                        @RequestParam(required = false, defaultValue = "local") String source) {
        if (videoId == null || videoId.isEmpty()) {
            return ResponseEntity.badRequest().body("缺少 videoId");
        }

        if (quizList == null || quizList.isEmpty()) {
            return ResponseEntity.badRequest().body("題目清單為空");
        }

        try {
            // 呼叫三參數版本，source 預設是 local
            int insertedCount = quizDAO.insertQuizListAvoidDuplicate(videoId, quizList, source);
            return ResponseEntity.ok("成功新增 " + insertedCount + " 筆題目");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("匯入題庫時發生錯誤");
        }
    }
}
