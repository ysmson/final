package tw.shawn.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.VideoDAO;
import tw.shawn.model.Video;

/**
 * GetLatestVideoController：回傳資料庫中最新的影片資訊
 * 路徑：GET /api/getLatestVideo
 * 回傳 JSON：{ "id": 1, "title": "...", "videoId": "YouTube_ID" }
 */
@RestController
@RequestMapping("/api")
public class GetLatestVideoController {

    @Autowired
    private VideoDAO videoDAO;

    @GetMapping("/getLatestVideo")
    public String getLatestVideo() {
        try {
            Video video = videoDAO.getLatestVideo();

            if (video == null) {
                return "{\"error\":\"找不到影片\"}";
            }

            String cleanVideoId = extractYouTubeId(video.getVideoId());

            JSONObject json = new JSONObject();
            json.put("id", video.getId());
            json.put("title", video.getTitle());
            json.put("videoId", cleanVideoId);

            return json.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"後端錯誤：" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }

    /**
     * 萃取 YouTube 網址中的純 videoId
     */
    private String extractYouTubeId(String url) {
        if (url == null || url.isEmpty()) return "";

        if (url.contains("youtube.com/watch?v=")) {
            String temp = url.substring(url.indexOf("watch?v=") + 8);
            int ampIndex = temp.indexOf("&");
            return (ampIndex != -1) ? temp.substring(0, ampIndex) : temp;
        }

        if (url.contains("youtu.be/")) {
            String temp = url.substring(url.indexOf("youtu.be/") + 9);
            int paramIndex = temp.indexOf("?");
            return (paramIndex != -1) ? temp.substring(0, paramIndex) : temp;
        }

        if (url.contains("embed/")) {
            String temp = url.substring(url.indexOf("embed/") + 6);
            int paramIndex = temp.indexOf("?");
            return (paramIndex != -1) ? temp.substring(0, paramIndex) : temp;
        }

        if (url.contains("list=")) {
            return "INVALID_PLAYLIST_ID";
        }

        return url;
    }
}
