package util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides some useful methods for file operations.
 *
 * @version 0.1.5
 * @since 20.Oct.2023
 * @author ShuraBlack
 */
public class FileUtil {

    /**
     * The time format for the last modified date.
     */
    private static final Format TIME_FORMAT = new SimpleDateFormat("yyyy.MMMdd HH:mm:ss");

    /**
     * Private constructor to prevent instantiation.
     */
    private FileUtil() { }

    /**
     * Converts the given size in bytes to a human-readable string.
     * @param bytes The size in bytes.
     * @return The human-readable string.
     */
    public static String convert(long bytes) {
        if (bytes < 1024) {
            return bytes + " byte";
        } else if (bytes < 1024 * 1024) {
            return bytes / 1024 + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return bytes / (1024 * 1024) + " MB";
        } else {
            return bytes / (1024 * 1024 * 1024) + " GB";
        }
    }

    /**
     * Converts the given time in milliseconds to a human-readable string.
     * @param time The time in milliseconds.
     * @return The human-readable string.
     */
    public static String convertTime(long time){
        Date date = new Date(time);
        return TIME_FORMAT.format(date);
    }

}
