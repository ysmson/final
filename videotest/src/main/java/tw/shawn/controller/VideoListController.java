package tw.shawn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.shawn.dao.VideoDAO;
import tw.shawn.model.Video;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoListController {

    @Autowired
    private VideoDAO videoDAO;

    /**
     * 回傳影片清單，可透過 ?sortBy=title/published/videoId 排序
     */
    @GetMapping("/videoList")
    public List<Video> getVideoList(@RequestParam(required = false) String sortBy) {
        if (sortBy != null && !sortBy.isBlank()) {
            return videoDAO.getAllVideosSorted(sortBy);
        } else {
            return videoDAO.getAllVideos();
        }
    }
}
