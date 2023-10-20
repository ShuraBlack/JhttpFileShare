package util;

import java.util.Scanner;

/**
 * This class is used to load the html pages.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class PageLoader {

    /**
     * Private constructor to prevent instantiation.
     */
    private PageLoader() { }

    /**
     * Returns the content of the given file.
     * @param filename The name of the file.
     * @return The content of the file.
     */
    public static String getContent(String filename) {
        return new Scanner(PageLoader.class.getResourceAsStream("/html/" + filename)).useDelimiter("\\Z").next();
    }
}
