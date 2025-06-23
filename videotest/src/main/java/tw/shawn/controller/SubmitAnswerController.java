package tw.shawn.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.AnswerDAO;
import tw.shawn.model.Answer;

import java.io.BufferedReader;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SubmitAnswerController {

    @Autowired
    private AnswerDAO answerDAO;

    @PostMapping("/submitAnswer")
    public ResponseEntity<Map<String, Object>> submitAnswer(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        int insertCount = 0, failCount = 0, correctCount = 0;
        int gainedExp = 0;  // 🔥 累積獲得的經驗值

        try (BufferedReader reader = request.getReader()) {
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }

            System.out.println("[submitAnswer] 收到 JSON: " + jsonBuffer);

            JsonObject jsonObject = JsonParser.parseString(jsonBuffer.toString()).getAsJsonObject();

            int userId = jsonObject.get("userId").getAsInt();
            String videoId = jsonObject.get("videoId").getAsString();
            long attemptId = jsonObject.get("attemptId").getAsLong();
            String difficulty = jsonObject.has("difficulty")
                    ? jsonObject.get("difficulty").getAsString()
                    : "unknown";

            if (!jsonObject.has("answers") || !jsonObject.get("answers").isJsonArray()) {
                response.put("success", false);
                response.put("message", "❌ 缺少 answers 陣列");
                return ResponseEntity.badRequest().body(response);
            }

            JsonArray answersArray = jsonObject.getAsJsonArray("answers");

            for (int i = 0; i < answersArray.size(); i++) {
                JsonObject answerJson = answersArray.get(i).getAsJsonObject();

                if (!answerJson.has("quizId") || !answerJson.has("selectedIndex")) {
                    System.out.println("⚠️ 缺少 quizId 或 selectedIndex，跳過");
                    continue;
                }

                int quizId = answerJson.get("quizId").getAsInt();
                int selectedIndex = answerJson.get("selectedIndex").getAsInt();

                Answer fullAnswer = answerDAO.getAnswerDetails(quizId);
                if (fullAnswer == null) {
                    System.out.println("⚠️ 找不到題目 ID: " + quizId + "，跳過");
                    continue;
                }

                int correctIndex = answerDAO.getCorrectAnswerIndex(quizId, fullAnswer.getSource());
                boolean isCorrect = (selectedIndex == correctIndex);

                Answer answer = new Answer();
                answer.setUserId(userId);
                answer.setVideoId(videoId);
                answer.setAttemptId(attemptId);
                answer.setQuizId(quizId);
                answer.setSelectedOption(selectedIndex);
                answer.setCorrect(isCorrect);
                answer.setSource(fullAnswer.getSource());
                answer.setQuestion(fullAnswer.getQuestion());
                answer.setOption1(fullAnswer.getOption1());
                answer.setOption2(fullAnswer.getOption2());
                answer.setOption3(fullAnswer.getOption3());
                answer.setOption4(fullAnswer.getOption4());
                answer.setAnswer(fullAnswer.getAnswer());
                answer.setAnswerIndex(correctIndex);
                answer.setExplanation(fullAnswer.getExplanation());
                answer.setDifficulty(difficulty);

                try {
                    answerDAO.insertAnswer(answer);
                    insertCount++;

                    if (isCorrect) {
                        correctCount++;
                        gainedExp += 10; // ✅ 每答對一題加 10 EXP
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    failCount++;
                }
            }

            // ✨ 統計正確率
            int total = insertCount;
            double accuracy = total > 0 ? (correctCount * 100.0) / total : 0;
            String accuracyStr = String.format("%.1f", accuracy);

            // 嘗試抓來源
            String source = "unknown";
            if (answersArray.size() > 0 && answersArray.get(0).getAsJsonObject().has("quizId")) {
                int firstQuizId = answersArray.get(0).getAsJsonObject().get("quizId").getAsInt();
                Answer firstAnswer = answerDAO.getAnswerDetails(firstQuizId);
                if (firstAnswer != null && firstAnswer.getSource() != null) {
                    source = firstAnswer.getSource();
                }
            }

            // ✅ 更新 quiz_results 表
            try {
                answerDAO.insertQuizResult(userId, videoId, attemptId, total, correctCount, accuracyStr, source);
                System.out.println("✅ 成功寫入 quiz_results");
            } catch (Exception e) {
                System.out.println("⚠️ quiz_results 寫入失敗：" + e.getMessage());
            }

            // ✅ 加總經驗值（一次性更新）
            if (gainedExp > 0) {
                try {
                    answerDAO.addExp(userId, gainedExp);
                    System.out.println("✨ 獲得 EXP: " + gainedExp);
                } catch (Exception e) {
                    System.out.println("⚠️ 加 EXP 失敗：" + e.getMessage());
                }
            }

            // ✅ 回傳給前端的 JSON 統計
            response.put("success", true);
            response.put("inserted", insertCount);
            response.put("skipped", failCount);
            response.put("correctCount", correctCount);
            response.put("total", total);
            response.put("gainedExp", gainedExp); // ✅ 回傳這次加了多少經驗值
            response.put("accuracy", accuracyStr);
            response.put("message", "✅ 儲存 " + insertCount + " 筆答案，略過 " + failCount + " 筆");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "❌ 發生例外錯誤：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
