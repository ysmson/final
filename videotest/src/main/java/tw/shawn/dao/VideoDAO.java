package tw.shawn.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tw.shawn.model.Video;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VideoDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VideoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ 影片映射器
    private static final RowMapper<Video> videoRowMapper = (rs, rowNum) -> {
        Video v = new Video();
        v.setId(rs.getString("id"));
        v.setVideoId(rs.getString("video_id"));
        v.setTitle(rs.getString("title"));
        v.setDescription(rs.getString("description"));
        v.setThumbnailUrl(rs.getString("thumbnail_url"));
        v.setPublishedAt(rs.getString("published_at"));
        return v;
    };

    // ✅ 新增影片
    public void insertVideo(Video v) {
        String sql = "INSERT INTO video (video_id, title, description, thumbnail_url, published_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, v.getVideoId(), v.getTitle(), v.getDescription(), v.getThumbnailUrl(), v.getPublishedAt());
    }

    // ✅ 批次新增
    public void insertVideoList(List<Video> videos) {
        for (Video v : videos) {
            insertVideo(v);
        }
    }

    // ✅ 查單筆 by 主鍵
    public Video getVideoById(String id) {
        String sql = "SELECT * FROM video WHERE id = ?";
        return jdbcTemplate.query(sql, videoRowMapper, id).stream().findFirst().orElse(null);
    }

    // ✅ 查全部（排序）
    public List<Video> getAllVideos() {
        String sql = "SELECT * FROM video ORDER BY LENGTH(title), title";
        return jdbcTemplate.query(sql, videoRowMapper);
    }

    // ✅ 查單一 YouTube 連結
    public String getYoutubeUrl(String id) {
        String sql = "SELECT youtube_url FROM video WHERE id = ?";
        return jdbcTemplate.query(sql, (rs) -> rs.next() ? rs.getString("youtube_url") : null, id);
    }

    // ✅ 查最新影片
    public Video getLatestVideo() {
        String sql = "SELECT * FROM video ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.query(sql, videoRowMapper).stream().findFirst().orElse(null);
    }

    // ✅ 動態排序查詢
    public List<Video> getAllVideosSorted(String sortBy) {
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
