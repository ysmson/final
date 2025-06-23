package tw.shawn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.AnswerDAO;
import tw.shawn.model.AttemptGroup;
import tw.shawn.model.AnswerGroupDTO;

import java.util.*;

/**
 * AnswerGroupedByAttemptAllController：
 * 查詢指定使用者的所有影片作答歷史（依影片與測驗次數群組）
 * GET /api/answerGroupAll?userId=1
 */
@RestController
@RequestMapping("/api")
public class AnswerGroupedByAttemptAllController {

    @Autowired
    private AnswerDAO answerDAO;

    @GetMapping("/answerGroupAll")
    public String getGroupedAnswersByUser(@RequestParam("userId") int userId) {
        List<AnswerGroupDTO> result = new ArrayList<>();

        try {
            Map<String, List<AttemptGroup>> rawData = answerDAO.getAnswersGroupedByUserAcrossVideos(userId);

            for (Map.Entry<String, List<AttemptGroup>> entry : rawData.entrySet()) {
                AnswerGroupDTO dto = new AnswerGroupDTO();
                dto.setVideoId(entry.getKey());
                dto.setVideoTitle(answerDAO.getVideoTitle(entry.getKey()));
                dto.setAttempts(entry.getValue());
                result.add(dto);
            }

            // ✅ 正確序列化 LocalDateTime
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return mapper.writeValueAsString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"伺服器錯誤：" + e.getMessage() + "\"}";
        }
    }
}
