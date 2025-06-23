package tw.shawn.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tw.shawn.model.QuizResult;
import tw.shawn.model.QuizResultSummary;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class QuizResultDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 儲存一次測驗結果（含 difficulty）
     */
    public void insertQuizResult(int userId, String videoId, int correctCount, int totalCount, String source, long attemptId, String difficulty) {
        String sql = """
            INSERT INTO quiz_results 
            (user_id, video_id, correct_answers, total_questions, submitted_at, source, attempt_id, difficulty) 
            VALUES (?, ?, ?, ?, NOW(), ?, ?, ?)
        """;
        jdbcTemplate.update(sql, userId, videoId, correctCount, totalCount, source, attemptId, difficulty);
    }

    /**
     * 查詢最新一次測驗結果（單一影片）
     */
    public QuizResult getLatestQuizResult(int userId, String videoId) {
        String sql = "SELECT * FROM quiz_results WHERE user_id = ? AND video_id = ? ORDER BY submitted_at DESC LIMIT 1";
        List<QuizResult> list = jdbcTemplate.query(sql, new Object[]{userId, videoId}, quizResultMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查詢某使用者所有測驗紀錄
     */
    public List<QuizResult> getAllResultsByUser(int userId) {
        String sql = "SELECT * FROM quiz_results WHERE user_id = ? ORDER BY submitted_at DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, quizResultMapper);
    }

    public List<QuizResult> getResultsByUser(int userId) {
        return getAllResultsByUser(userId);
    }

    /**
     * 依影片統計測驗總數與答對數（影片標題＋來源）
     */
    public List<QuizResultSummary> getQuizSummaryByUser(int userId) {
        String sql = """
            SELECT 
                q.video_id,
                v.title AS video_title,
                CASE 
                  WHEN COUNT(DISTINCT q.source) = 1 THEN MAX(q.source)
                  ELSE '混合'
                END AS source,
                COUNT(*) AS total_quiz_count,
                SUM(q.total_questions) AS total,
                SUM(q.correct_answers) AS correct
            FROM quiz_results q
            JOIN video v ON q.video_id = v.video_id
            WHERE q.user_id = ?
            GROUP BY q.video_id, v.title
        """;

        return jdbcTemplate.query(sql, new Object[]{userId}, quizResultSummaryMapper);
    }

    // ----------- Mapper 區 -----------

    private final RowMapper<QuizResult> quizResultMapper = (rs, rowNum) -> {
        QuizResult result = new QuizResult();
        result.setUserId(rs.getInt("user_id"));
        result.setVideoId(rs.getString("video_id"));
        result.setCorrectAnswers(rs.getInt("correct_answers"));
        result.setTotalQuestions(rs.getInt("total_questions"));
        result.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
        result.setSource(rs.getString("source"));
        result.setAttemptId(rs.getLong("attempt_id"));
        
        return result;
    };

    private final RowMapper<QuizResultSummary> quizResultSummaryMapper = (rs, rowNum) -> {
        QuizResultSummary summary = new QuizResultSummary();
        summary.setVideoId(rs.getString("video_id"));
        summary.setVideoTitle(rs.getString("video_title"));
        summary.setSource(rs.getString("source"));
        summary.setTotalQuizCount(rs.getInt("total_quiz_count"));
        summary.setTotal(rs.getInt("total"));
        summary.setCorrect(rs.getInt("correct"));
        return summary;
    };

    /**
     * 統計某次測驗的總題數（answer 表）
     */
    public int sumTotalQuestions(int userId, String videoId, String source) {
        String sql = """
            SELECT COUNT(*) FROM answer 
            WHERE user_id = ? AND video_id = ? AND source = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, videoId, source);
    }

    /**
     * 統計某次測驗的答對題數（answer 表）
     */
    public int sumCorrectAnswers(int userId, String videoId, String source) {
        String sql = """
            SELECT COUNT(*) FROM answer 
            WHERE user_id = ? AND video_id = ? AND source = ? AND is_correct = true
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, videoId, source);
    }
}
