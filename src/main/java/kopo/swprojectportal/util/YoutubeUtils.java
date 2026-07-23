package kopo.swprojectportal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUtils {

    private static final Pattern VIDEO_ID_PATTERN =
            Pattern.compile("(?:youtu\\.be/|v=)([a-zA-Z0-9_-]{11})");

    public static String extractVideoId(String youtubeUrl) {
        Matcher matcher = VIDEO_ID_PATTERN.matcher(youtubeUrl);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String extractThumbnailUrl(String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);
        return videoId != null ? "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg" : null;
    }

    public static String extractEmbedUrl(String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);
        return videoId != null ? "https://www.youtube.com/embed/" + videoId : null;
    }
}