package tw.shawn.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.VideoDAO;
import tw.shawn.model.Video;

/**
 * GetVideoController：根據主鍵 ID 查詢指定影片資訊
 * 路徑：GET /api/getVideo?videoId=1
 * 回傳格式：{ "id": ..., "title": ..., "videoId": ... }
 */
@RestController
@RequestMapping("/api")
public class GetVideoController {

    @Autowired
    private VideoDAO videoDAO;

    @GetMapping("/getVideo")
    public String getVideoById(@RequestParam("videoId") int videoId) {
        try {
            Video video = videoDAO.getVideoById(String.valueOf(videoId)); // 注意型別一致

            if (video == null) {
                return "{\"error\": \"找不到影片\"}";
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
     * 擷取純 YouTube ID
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
