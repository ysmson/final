package tw.shawn.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tw.shawn.model.Video;

import java.sql.ResultSet;
import java.util.List;

/**
 * VideoDAO：處理影片 video 表相關資料存取邏輯
 */
@Repository
public class VideoDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VideoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * ✅ 影片資料表對應的 RowMapper（資料庫欄位對應到 Video 物件屬性）
     * 用於 JdbcTemplate 查詢結果轉換為 Video 物件
     */
    private static final RowMapper<Video> videoRowMapper = (rs, rowNum) -> {
        Video v = new Video();
        v.setId(rs.getString("id"));                     // 主鍵 ID
        v.setVideoId(rs.getString("video_id"));          // YouTube 影片 ID
        v.setTitle(rs.getString("title"));               // 標題
        v.setDescription(rs.getString("description"));   // 描述
        v.setThumbnailUrl(rs.getString("thumbnail_url")); // 縮圖網址
        v.setPublishedAt(rs.getString("published_at"));   // 發佈日期
        return v;
    };

    /**
     * ✅ 新增單一影片記錄至 video 表
     *
     * @param v Video 物件，包含影片基本資料
     */
    public void insertVideo(Video v) {
        String sql = "INSERT INTO video (video_id, title, description, thumbnail_url, published_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, v.getVideoId(), v.getTitle(), v.getDescription(), v.getThumbnailUrl(), v.getPublishedAt());
    }

    /**
     * ✅ 批次新增影片資料（逐筆插入）
     *
     * @param videos List<Video> 多筆影片資料
     * 注意：本方法會逐筆呼叫 insertVideo，非使用 batch insert
     */
    public void insertVideoList(List<Video> videos) {
        for (Video v : videos) {
            insertVideo(v);
        }
    }

    /**
     * ✅ 根據主鍵 id 查詢單一影片記錄
     *
     * @param id video 表中的主鍵欄位（非 YouTube videoId）
     * @return 對應的 Video 物件，若找不到則回傳 null
     */
    public Video getVideoById(String id) {
        String sql = "SELECT * FROM video WHERE id = ?";
        return jdbcTemplate.query(sql, videoRowMapper, id).stream().findFirst().orElse(null);
    }

    /**
     * ✅ 查詢所有影片，並依照標題長度與字母排序
     *
     * @return List<Video> 全部影片清單
     * 排序依據：標題長度（越短越前）→ 再按字母排序
     */
    public List<Video> getAllVideos() {
        String sql = "SELECT * FROM video ORDER BY LENGTH(title), title";
        return jdbcTemplate.query(sql, videoRowMapper);
    }

    /**
     * ✅ 查詢影片的 YouTube 原始連結網址（若欄位有存）
     *
     * @param id 主鍵 ID
     * @return 對應的 youtube_url 字串，若無則為 null
     */
    public String getYoutubeUrl(String id) {
        String sql = "SELECT youtube_url FROM video WHERE id = ?";
        return jdbcTemplate.query(sql, (rs) -> rs.next() ? rs.getString("youtube_url") : null, id);
    }

    /**
     * ✅ 查詢最新加入的影片（依照 ID 倒序排列）
     *
     * @return 最新一筆 Video 物件，若無則為 null
     */
    public Video getLatestVideo() {
        String sql = "SELECT * FROM video ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.query(sql, videoRowMapper).stream().findFirst().orElse(null);
    }

    /**
     * ✅ 依照指定欄位排序查詢所有影片
     *
     * @param sortBy 可傳入的排序參數有：title / published / videoId
     * @return 排序後的 List<Video>
     * 若 sortBy 不合法，預設以 title 排序
     */
    public List<Video> getAllVideosSorted(String sortBy) {
        // 判斷排序條件，避免 SQL Injection（用 switch 控制）
        String orderClause = switch (sortBy) {
            case "title" -> "ORDER BY title";
            case "published" -> "ORDER BY published_at DESC";
            case "videoId" -> "ORDER BY video_id";
            default -> "ORDER BY title";
        };
        String sql = "SELECT * FROM video " + orderClause;
        return jdbcTemplate.query(sql, videoRowMapper);
    }
}
