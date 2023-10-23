package util;

import java.util.Properties;

/**
 * This class is used to store the configuration of the server.
 * It also contains the default values.
 *
 * @version 0.1.4
 * @since 20.Oct.2023
 * @autor ShuraBlack
 */
public class Config {

    /**
     * Configuration of the server.
     */
    private static final Properties APP_CONFIG = new Properties();

    /**
     * Private constructor to prevent instantiation.
     */
    private Config() { }

    /**
     * Initializes the configuration.
     */
    public static void init() {
        APP_CONFIG.put("IP_ADDRESS", "0.0.0.0");
        APP_CONFIG.put("PORT", "80");
        APP_CONFIG.put("THREAD_POOL_SIZE", "3");
        APP_CONFIG.put("ROOT_RESTRICTED", "true");
        APP_CONFIG.put("VERBOSE", "false");
        APP_CONFIG.put("ROOT_DIRECTORY", System.getProperty("user.dir").replaceAll("\\\\", "/"));
        APP_CONFIG.put("ROOT_STRUCTURE", "");
        APP_CONFIG.put("UPLOAD_ALLOWED", "false");
    }

    // =================================================================================================================
    // Setter
    // =================================================================================================================

    /**
     * Sets the value for the given key.
     * @param key The key.
     * @param value The value.
     */
    public static void set(String key, String value) {
        if (APP_CONFIG.containsKey(key)) {
            APP_CONFIG.replace(key, value);
        } else {
            APP_CONFIG.put(key, value);
        }
    }

    // =================================================================================================================
    // Getter
    // =================================================================================================================

    /**
     * Get the IP address of the server.
     * @return The IP address.
     */
    public static String getIpAddress() {
        return APP_CONFIG.getProperty("IP_ADDRESS");
    }

    /**
     * Get the port of the server.
     * @return The port.
     */
    public static int getPort() {
        return Integer.parseInt(APP_CONFIG.getProperty("PORT"));
    }

    /**
     * Get the thread pool size of the server.
     * @return The thread pool size.
     */
    public static int getThreadPoolSize() {
        return Integer.parseInt(APP_CONFIG.getProperty("THREAD_POOL_SIZE"));
    }

    /**
     * Get the root restriction of the server.
     * @return The root restriction.
     */
    public static boolean isRootRestricted() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("ROOT_RESTRICTED"));
    }

    /**
     * Get the verbose mode of the server.
     * @return The verbose mode.
     */
    public static boolean isVerbose() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("VERBOSE"));
    }

    /**
     * Get the root directory of the server.
     * @return The root directory.
     */
    public static String getRootDirectory() {
        return APP_CONFIG.getProperty("ROOT_DIRECTORY");
    }

    /**
     * Get the root directory of the server.
     * @return The root directory.
     */
    public static String getRootStructure() {
        return APP_CONFIG.getProperty("ROOT_STRUCTURE");
    }

    /**
     * Get the upload mode of the server.
     * @return The upload mode.
     */
    public static boolean isUploadAllowed() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("UPLOAD_ALLOWED"));
    }
}
