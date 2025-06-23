package tw.shawn.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tw.shawn.model.Quiz;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class QuizDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ✅ 寫入單筆題目，並回傳資料庫自動產生的 quiz_id
     */
    public int insertQuiz(Quiz quiz) {
        String sql = "INSERT INTO quiz (video_id, question, option1, option2, option3, option4, correct_index, explanation, source, difficulty) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, quiz.getVideoId());
            ps.setString(2, quiz.getQuestion());
            ps.setString(3, quiz.getOption1());
            ps.setString(4, quiz.getOption2());
            ps.setString(5, quiz.getOption3());
            ps.setString(6, quiz.getOption4());
            ps.setInt(7, quiz.getCorrectIndex());
            ps.setString(8, quiz.getExplanation());
            ps.setString(9, quiz.getSource());
            ps.setString(10, quiz.getDifficulty());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /**
     * ✅ 根據影片 ID 與題目來源載入題庫（限制筆數）
     */
    public List<Quiz> getQuizzesByVideoIdAndSource(String videoId, String source, int limit) {
        String sql = "SELECT * FROM quiz WHERE video_id = ? AND source = ? ORDER BY quiz_id LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{videoId, source, limit}, this::mapRowToQuiz);
    }

    /**
     * ✅ 額外補充：查詢最新的 GPT 題目（如果未來想用）
     */
    public List<Quiz> getLatestQuizzesByVideoAndSource(String videoId, String source, int limit) {
        String sql = "SELECT * FROM quiz WHERE video_id = ? AND source = ? ORDER BY quiz_id DESC LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{videoId, source, limit}, this::mapRowToQuiz);
    }

    /**
     * ✅ 將 ResultSet 映射為 Quiz 物件
     */
    private Quiz mapRowToQuiz(ResultSet rs, int rowNum) {
        try {
            Quiz quiz = new Quiz();
            quiz.setId(rs.getInt("quiz_id"));
            quiz.setVideoId(rs.getString("video_id"));
            quiz.setQuestion(rs.getString("question"));
            quiz.setOption1(rs.getString("option1"));
            quiz.setOption2(rs.getString("option2"));
            quiz.setOption3(rs.getString("option3"));
            quiz.setOption4(rs.getString("option4"));
            quiz.setCorrectIndex(rs.getInt("correct_index"));
            quiz.setExplanation(rs.getString("explanation"));
            quiz.setSource(rs.getString("source"));
            quiz.setDifficulty(rs.getString("difficulty"));
            return quiz;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int insertQuizListAvoidDuplicate(String videoId, List<Quiz> quizList, String source) {
        int insertCount = 0;
        String checkSql = "SELECT COUNT(*) FROM quiz WHERE video_id = ? AND question = ?";
        String insertSql = """
            INSERT INTO quiz (video_id, question, option1, option2, option3, option4,
                              correct_index, explanation, source, difficulty)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        for (Quiz quiz : quizList) {
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, videoId, quiz.getQuestion());
            if (count != null && count == 0) {
                jdbcTemplate.update(insertSql,
                    quiz.getVideoId(), quiz.getQuestion(),
                    quiz.getOption1(), quiz.getOption2(), quiz.getOption3(), quiz.getOption4(),
                    quiz.getCorrectIndex(), quiz.getExplanation(), source, quiz.getDifficulty()
                );
                insertCount++;
            }
        }
        return insertCount;
    }
    public List<Quiz> getQuizzesByVideoSourceDifficulty(String videoId, String source, String difficulty, int limit) {
        String sql = """
            SELECT * FROM quiz
            WHERE video_id = ? AND source = ? AND difficulty = ?
            ORDER BY quiz_id
            LIMIT ?
        """;

        return jdbcTemplate.query(sql, new Object[]{videoId, source, difficulty, limit}, this::mapRowToQuiz);
    }

}
