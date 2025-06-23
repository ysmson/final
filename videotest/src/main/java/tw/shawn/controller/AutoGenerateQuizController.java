package tw.shawn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.QuizDAO;
import tw.shawn.model.Quiz;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class AutoGenerateQuizController {

	@Autowired
	private QuizDAO quizDAO;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${openai.api.key}")
	private String openaiApiKey;

	@Value("${openai.api.url}")
	private String openaiApiUrl;

	@Value("${openai.model.chat:gpt-3.5-turbo}")
	private String openaiModel;

	/**
     * 根據影片 ID 與難度，自動產生 GPT 題目，寫入資料庫並回傳
     * GET /api/autoGenerateQuiz?videoId=xxx&difficulty=easy
     */
    @GetMapping("/autoGenerateQuiz")
    public ResponseEntity<?> autoGenerateQuiz(@RequestParam("videoId") String videoId,
                                              @RequestParam(value = "difficulty", defaultValue = "medium") String difficulty,
    	                                      @RequestParam(value = "quizNum", defaultValue = "5") int quizNum) {

        if (videoId == null || videoId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(JSONObject.wrap("❗ 缺少 videoId"));
        }

        // 讀取 transcript 檔案
        String transcript;
        try {
            Resource resource = resourceLoader.getResource("classpath:transcripts/" + videoId + ".txt");
            transcript = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(JSONObject.wrap("❗ 找不到 transcript 檔案"));
        }

        // 組 Prompt 給 GPT
        String prompt = "請僅回傳 JSON 陣列，不要加上任何註解或文字。"
                + "請產生 " + quizNum + " 題繁體中文選擇題，難度為「" + difficulty + "」，不要簡體中文，語意要清楚、適合 Java 初學者的測驗題目。"
                + "每題包含 question、options（陣列）、answer（正確答案文字）與 explanation（詳細說明為什麼這是正解）。\n\n"
                + transcript;

        // 呼叫 GPT
        String responseText;
        try {
            responseText = callOpenAI(prompt);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(JSONObject.wrap("❌ OpenAI API 請求失敗"));
        }

        if (responseText == null) {
            return ResponseEntity.status(500).body(JSONObject.wrap("❌ GPT 回傳為 null"));
        }

        // 擷取 JSON 陣列
        String quizJsonText = extractJsonArray(responseText);
        JSONArray quizArr;
        try {
            quizArr = new JSONArray(quizJsonText);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(JSONObject.wrap("❌ GPT 回傳格式錯誤，無法解析為 JSON 陣列"));
        }

        // 寫入資料庫
        List<Quiz> insertedList = new ArrayList<>();
        try {
        	for (int i = 0; i < Math.min(quizArr.length(), quizNum); i++) {
                JSONObject q = quizArr.getJSONObject(i);
                if (!q.has("question") || !q.has("options") || !q.has("answer")) continue;

                JSONArray opts = q.getJSONArray("options");
                if (opts.length() < 2) continue;

                String correctAnswer = q.getString("answer").trim();
                int correctIndex = -1;
                for (int j = 0; j < opts.length(); j++) {
                    if (opts.getString(j).trim().equals(correctAnswer)) {
                        correctIndex = j;
                        break;
                    }
                }
                if (correctIndex == -1) continue;

                Quiz quiz = new Quiz();
                quiz.setVideoId(videoId);
                quiz.setQuestion(q.getString("question"));
                quiz.setOption1(opts.optString(0, ""));
                quiz.setOption2(opts.optString(1, ""));
                quiz.setOption3(opts.optString(2, ""));
                quiz.setOption4(opts.optString(3, ""));
                quiz.setCorrectIndex(correctIndex);
                quiz.setCorrectOption(correctAnswer);
                quiz.setExplanation(q.optString("explanation", "無提供"));
                quiz.setSource("gpt");
                quiz.setDifficulty(difficulty); // ✅ 套用前端傳來的難度參數

                int generatedId = quizDAO.insertQuiz(quiz);
                quiz.setId(generatedId);
                insertedList.add(quiz);
            }

            // 回傳前端
            JSONArray resultArray = new JSONArray();
            for (Quiz quiz : insertedList) {
                JSONObject obj = new JSONObject();
                obj.put("id", quiz.getId());
                obj.put("question", quiz.getQuestion());

                JSONArray options = new JSONArray();
                options.put(quiz.getOption1());
                options.put(quiz.getOption2());
                options.put(quiz.getOption3());
                options.put(quiz.getOption4());
                obj.put("options", options);

                resultArray.put(obj);
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(resultArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(JSONObject.wrap("❌ 資料庫寫入錯誤"));
        }
    }

	// 呼叫 OpenAI API
	private String callOpenAI(String prompt) throws IOException {
		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

		JSONObject requestJson = new JSONObject();
		requestJson.put("model", openaiModel);

		JSONArray messages = new JSONArray();
		messages.put(new JSONObject().put("role", "user").put("content", prompt));
		requestJson.put("messages", messages);

		Request request = new Request.Builder().url(openaiApiUrl).addHeader("Authorization", "Bearer " + openaiApiKey)
				.post(okhttp3.RequestBody.create(requestJson.toString(), MediaType.get("application/json"))).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				return null;
			String bodyStr = response.body().string();
			JSONObject respJson = new JSONObject(bodyStr);
			return respJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
		}
	}

	// 擷取 GPT 回傳中的 JSON 陣列
	private static String extractJsonArray(String text) {
		int start = text.indexOf("[");
		int end = text.lastIndexOf("]");
		if (start != -1 && end != -1 && end > start) {
			return text.substring(start, end + 1);
		}
		return "[]";
	}
}
