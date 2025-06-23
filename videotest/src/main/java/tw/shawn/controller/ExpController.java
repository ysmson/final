package tw.shawn.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.UserDAO;

/**
 * ExpController：接收使用者經驗值更新請求（來源可能是觀看影片或答題）
 * API 路徑：POST /api/exp
 * 輸入 JSON 範例：
 * {
 *     "userId": 123,
 *     "source": "watch",
 *     "exp": 10
 * }
 */
@RestController
@RequestMapping("/api")
public class ExpController {

    @Autowired
    private UserDAO userDAO;

    @PostMapping("/exp")
    public String updateExp(@RequestBody String body) {
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            if (!json.has("userId") || !json.has("source") || !json.has("exp")) {
                return "❌ 錯誤：請求資料不完整或不是有效的 JSON 格式";
            }

            int userId = json.get("userId").getAsInt();
            String source = json.get("source").getAsString();
            int exp = json.get("exp").getAsInt();

            userDAO.addExp(userId, exp);

            return "✅ 成功累加經驗值：" + exp + " 點（來源：" + source + "）";

        } catch (Exception e) {
            e.printStackTrace();
            return "🚫 發生錯誤：" + e.getMessage();
        }
    }
}
