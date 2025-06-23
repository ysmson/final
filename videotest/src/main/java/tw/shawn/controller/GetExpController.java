package tw.shawn.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.UserDAO;

/**
 * GetExpController：回傳指定使用者的經驗值
 * 請求：GET /api/getExp?userId=123
 * 回應：{ "exp": 25 }
 */
@RestController
@RequestMapping("/api")
public class GetExpController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/getExp")
    public String getExp(@RequestParam("userId") int userId) {
        try {
            int exp = userDAO.getExp(userId);

            JsonObject json = new JsonObject();
            json.addProperty("exp", exp);
            return json.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
