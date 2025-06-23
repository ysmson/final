package tw.shawn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.AnswerDAO;
import tw.shawn.model.AnswerDetailDto;
import tw.shawn.model.Answer;

import java.util.ArrayList;
import java.util.List;

/**
 * AnswerDetailController：查詢單次作答詳解（正確/錯誤、選項詳解）
 * 路徑：GET /api/answerDetail?userId=123&videoId=abc123&attemptId=1750xxxxxx
 */
@RestController
@RequestMapping("/api")
public class AnswerDetailController {

    @Autowired
    private AnswerDAO answerDAO;

    @GetMapping("/answerDetail")
    public List<AnswerDetailDto> getAnswerDetail(@RequestParam("userId") int userId,
                                                 @RequestParam("videoId") String videoId,
                                                 @RequestParam("attemptId") long attemptId) {
        List<AnswerDetailDto> result = new ArrayList<>();

        // ✅ 改為只查詢這次 attemptId 的紀錄
        List<Answer> records = answerDAO.getAnswersByUserAndAttemptId(userId, videoId, attemptId);

        for (Answer record : records) {
            AnswerDetailDto dto = new AnswerDetailDto();

            dto.setQuestion(record.getQuestion());
            dto.setOption1(record.getOption1());
            dto.setOption2(record.getOption2());
            dto.setOption3(record.getOption3());
            dto.setOption4(record.getOption4());
            dto.setCorrectIndex(record.getAnswerIndex());
            dto.setSelectedIndex(record.getSelectedOption());
            dto.setSource(record.getSource());
            dto.setExplanation(record.getExplanation());

            // 安全處理選項轉換為文字
            List<String> options = List.of(
                    record.getOption1() != null ? record.getOption1() : "",
                    record.getOption2() != null ? record.getOption2() : "",
                    record.getOption3() != null ? record.getOption3() : "",
                    record.getOption4() != null ? record.getOption4() : ""
            );

            Integer answerIndex = record.getAnswerIndex();
            Integer selectedIndex = record.getSelectedOption();

            String correctText = (answerIndex != null && answerIndex >= 0 && answerIndex < options.size())
                    ? options.get(answerIndex) : "";

            String selectedText = (selectedIndex != null && selectedIndex >= 0 && selectedIndex < options.size())
                    ? options.get(selectedIndex) : "";

            dto.setCorrect(correctText);
            dto.setSelected(selectedText);

            result.add(dto);
        }

        return result;
    }
}
