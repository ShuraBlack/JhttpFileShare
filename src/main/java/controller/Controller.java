package controller;

import model.data.FileData;
import model.interpreter.ArgsInterpreter;
import model.server.Server;
import model.session.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main component for the JhttpFileShare application.<br><br>
 * This class is the entry point and will initialize the server and the command line interface.
 *
 * @version 0.1.0
 * @since 19.Oct.2023
 * @author ShuraBlack
 */
public class Controller {

    /**
     * Logger for the Controller class.
     */
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    /**
     * The server instance.
     */
    private final Server server;

    /**
     * List of all active sessions.
     */
    private static final List<UserSession> SESSIONS = new ArrayList<>();

    /**
     * Entry point of the application.
     *
     * @param args Command line arguments.
     */
    public Controller(String[] args) {
        Config.init();
        ArgsInterpreter.interpret(args);
        server = new Server();

        FileData.getDirectoryStructure(Config.getRootDirectory());

        new Thread(() -> {
            if (Config.isVerbose())
                LOGGER.info("Start Command Line Interface...");

            System.out.println(
                      "==================================================================================\n"
                    + "= JhttpFileShare - Version 0.1.2                                                 =\n"
                    + "= Code by ShuraBlack                                                             =\n"
                    + "==================================================================================\n"
                    + getProperties() + "\n"
                    + "==================================================================================\n"
                    + "= exit -> will close the server                                                  =\n"
                    + "==================================================================================\n");

            Scanner input = new Scanner(System.in);
            while (true) {
                String command = input.nextLine();
                if (command.equals("exit")) {
                    server.stop();
                    LOGGER.info("Server closed and CLI will be shutdowned...");
                    System.exit(0);
                    break;
                }
            }
        }, "CLI").start();
    }

    // =================================================================================================================
    // Getter
    // =================================================================================================================

    /**
     * Getter for session list.
     * @return List of all active sessions.
     */
    public static List<UserSession> getSessions() {
        return SESSIONS;
    }

    // =================================================================================================================
    // Private Methods
    // =================================================================================================================

    /**
     * Returns the properties of the server.
     * @return The properties
     */
    private static String getProperties() {
        String properties = String.format("= Verbose: %s, "
                        + "Root Restriction: %s, "
                        + "Port: %s, "
                        + "Thread Pool Size: %s",
                colorize(Config.isVerbose()),
                colorize(Config.isRootRestricted()),
                Config.getPort(),
                Config.getThreadPoolSize());
        properties += " ".repeat(102 - properties.length()) + " =";
        return properties;
    }

    /**
     * Colorizes the given boolean.
     * @param state The boolean to colorize.
     * @return The colorized boolean.
     */
    private static String colorize(boolean state) {
        if (state) {
            return "\033[0;32m" + state + "\033[0m";
        } else {
            return "\033[0;31m" + state + "\033[0m";
        }
    }
}
