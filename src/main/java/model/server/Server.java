package model.server;

import com.sun.net.httpserver.HttpServer;
import controller.Controller;
import model.pages.DirectoryPage;
import model.pages.DownloadPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * This class represents the server component of the JhttpFileShare application.<br><br>
 * It will create a HttpServer instance and register the needed contexts.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class Server {

    /**
     * Logger for the Server class.
     */
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    /**
     * Defines the size of the thread pool.
     */
    private static int THREAD_POOL_SIZE = 3;

    /**
     * Defines the port of the server.
     */
    public static int PORT = 80;

    /**
     * Defines the ip address of the server.
     */
    public static String IP_ADDRESS = "0.0.0.0";

    /**
     * The HttpServer instance.
     */
    private HttpServer httpServer;

    /**
     * Creates a new Server instance.
     */
    public Server() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(IP_ADDRESS, PORT), 0);

            httpServer.createContext("/directory", new DirectoryPage());
            if (Controller.isVerbose())
                LOGGER.info("Created context /directory");
            httpServer.createContext("/download", new DownloadPage());
            if (Controller.isVerbose())
                LOGGER.info("Created context /download");
            httpServer.setExecutor(Executors.newFixedThreadPool(THREAD_POOL_SIZE));
            httpServer.start();
        } catch (IOException e) {
            LOGGER.error("Couldnt start Server!", e);
        }

        LOGGER.info("Server started on http://{}:{}/directory", IP_ADDRESS, PORT);
    }

    /**
     * Stops the server.
     */
    public void stop() {
        httpServer.stop(0);
    }

    // =================================================================================================================
    // Getter
    // =================================================================================================================

    /**
     * Returns the current thread pool size.
     * @return The current thread pool size.
     */
    public static String getThreadPoolSize() {
        return String.valueOf(THREAD_POOL_SIZE);
    }

    // =================================================================================================================
    // Setter
    // =================================================================================================================

    /**
     * Sets the thread pool size.
     * @param size The new thread pool size.
     */
    public static void setThreadPoolSize(int size) {
        THREAD_POOL_SIZE = size;
    }
}
