package util;

/**
 * This class is used to convert file sizes.
 *
 * @version 0.1.1
 * @since 20.Oct.2023
 * @author ShuraBlack
 */
public class FileSize {

    /**
     * Private constructor to prevent instantiation.
     */
    private FileSize() { }

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

}
