package model.server;

import com.sun.net.httpserver.HttpServer;
import controller.Controller;
import model.pages.DirectoryPage;
import model.pages.DownloadPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;

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
     * The HttpServer instance.
     */
    private HttpServer httpServer;

    /**
     * Creates a new Server instance.
     */
    public Server() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(Config.getIpAddress(), Config.getPort()), 0);

            httpServer.createContext("/directory", new DirectoryPage());
            if (Config.isVerbose())
                LOGGER.info("Created context /directory");
            httpServer.createContext("/download", new DownloadPage());
            if (Config.isVerbose())
                LOGGER.info("Created context /download");
            httpServer.setExecutor(Executors.newFixedThreadPool(Config.getThreadPoolSize()));
            httpServer.start();
        } catch (IOException e) {
            LOGGER.error("Couldnt start Server!", e);
        }

        LOGGER.info("Server started on http://{}:{}/directory", Config.getIpAddress(), Config.getPort());
    }

    /**
     * Stops the server.
     */
    public void stop() {
        httpServer.stop(0);
    }
}
