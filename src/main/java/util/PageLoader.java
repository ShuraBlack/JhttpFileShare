package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is used to load the html pages.
 *
 * @version 0.1.1
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class PageLoader {

    private static final Map<String, String> PAGES = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private PageLoader() { }

    /**
     * Returns the content of the given file.
     * @param filename The name of the file.
     * @return The content of the file.
     */
    private static String getHTML(String filename) {
        return new Scanner(PageLoader.class.getResourceAsStream("/html/" + filename + ".html")).useDelimiter("\\Z").next();
    }

    /**
     * Returns the page with the given filename.
     * @param pageName The name of the page.
     * @return The content of the page.
     */
    public static String getPage(String pageName) {
        if (pageName == null || pageName.isEmpty()) {
            return "";
        }


        if (PAGES.containsKey(pageName)) {
            return PAGES.get(pageName);
        }
        String page = getHTML(pageName);
        if (page == null || page.isEmpty()) {
            return "";
        }
        PAGES.put(pageName, page);
        return page;
    }
}
