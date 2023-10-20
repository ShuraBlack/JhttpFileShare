package model.data;

/**
 * Holds structure of the user dir.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class Structure {

    /**
     * Root tree of the user dir.
     */
    public static String ROOT_TREE;

    /**
     * Root dir of the application.
     * This is the {@link System#getProperty(String)} of {@code user.dir}.
     */
    public static String ROOT_DIR = System.getProperty("user.dir").replaceAll("\\\\", "/");

}
