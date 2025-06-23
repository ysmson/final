package tw.shawn.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ✅ 增加使用者經驗值
     *
     * @param userId 使用者 ID
     * @param exp    要增加的經驗值
     */
    public void addExp(int userId, int exp) {
        String sql = "UPDATE users SET exp = exp + ? WHERE id = ?";
        jdbcTemplate.update(sql, exp, userId);
    }

    /**
     * ✅ 查詢使用者目前經驗值
     *
     * @param userId 使用者 ID
     * @return 經驗值，若查不到則預設為 0
     */
    public int getExp(int userId) {
        String sql = "SELECT exp FROM users WHERE id = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return result != null ? result : 0;
    }

    /**
     * ✅ 檢查使用者是否存在
     *
     * @param userId 使用者 ID
     * @return true 表示存在，false 表示不存在
     */
    public boolean userExists(int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    /**
     * ✅ 插入一位新使用者（預設經驗值為 0）
     *
     * @param userId   使用者 ID
     * @param username 使用者帳號
     * @param password 使用者密碼
     */
    public void insertUser(int userId, String username, String password) {
        String sql = "INSERT INTO users (id, username, password, exp) VALUES (?, ?, ?, 0)";
        jdbcTemplate.update(sql, userId, username, password);
    }
}
